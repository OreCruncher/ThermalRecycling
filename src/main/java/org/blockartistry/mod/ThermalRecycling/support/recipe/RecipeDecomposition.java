/*
 * This file is part of ThermalRecycling, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.blockartistry.mod.ThermalRecycling.support.recipe;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.RecipeAccessorBase;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.RecipeUtil;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ShapedOreRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ShapedRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ShapelessOreRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ShapelessRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.util.InventoryHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeDecomposition {

	private RecipeDecomposition() {
	}

	private static Map<Class<?>, RecipeAccessorBase> accessors = new IdentityHashMap<Class<?>, RecipeAccessorBase>();

	static {
		// Register Vanilla and Forge recipe handlers
		accessors.put(ShapedRecipes.class, new ShapedRecipeAccessor());
		accessors.put(ShapelessRecipes.class, new ShapelessRecipeAccessor());
		accessors.put(ShapedOreRecipe.class, new ShapedOreRecipeAccessor());
		accessors.put(ShapelessOreRecipe.class, new ShapelessOreRecipeAccessor());
	}

	public static void registerAccessor(final Class<?> clazz, final RecipeAccessorBase accessor) {
		accessors.put(clazz, accessor);
	}

	@SuppressWarnings("unchecked")
	public static void registerAccessor(final String className, final RecipeAccessorBase accessor) {
		try {
			final Class<?> clazz = Class.forName(className);
			registerAccessor((Class<? extends IRecipe>) clazz, accessor);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	private static RecipeAccessorBase find(final Object recipe) {
		for (final Entry<Class<?>, RecipeAccessorBase> entry : accessors.entrySet()) {
			if (entry.getKey().isAssignableFrom(recipe.getClass()))
				return entry.getValue();
		}

		return null;
	}

	public static ItemStack getInput(final Object recipe) {
		final RecipeAccessorBase accessor = find(recipe);
		if (accessor != null)
			return accessor.getInput(recipe);
		return null;
	}

	public static List<ItemStack> decompose(final Object recipe) {

		List<ItemStack> projection = null;

		final RecipeAccessorBase accessor = find(recipe);
		if (accessor != null) {
			if (accessor.isHidden(recipe)) {
				projection = new ArrayList<ItemStack>();
			} else {
				projection = accessor.getOutput(recipe);
			}
		} else if (!RecipeUtil.isClassIgnored(recipe)) {
			ModLog.info("Unknown recipe class: %s", recipe.getClass().getName());
		}

		if (projection != null)
			projection = scrubProjection(accessor.getInput(recipe), projection);

		return projection;
	}

	@Deprecated
	public static List<ItemStack> decomposeForestry(final ItemStack input, final Object... output) {
		List<ItemStack> projection = RecipeUtil.projectForgeRecipeList(output);
		if (projection != null)
			projection = scrubProjection(input, projection);
		return projection;
	}

	private static List<ItemStack> scrubProjection(final ItemStack inputStack, List<ItemStack> projection) {

		if (projection == null || RecipeUtil.ignoreRecipe(projection))
			return null;

		// Scan for the list looking for wildcards as well
		// as those items that match the input. Sometimes
		// an author will make a cyclic recipe. Scrubbing
		// items that are marked scrubbed is handled
		// during freeze.
		for (int i = 0; i < projection.size(); i++) {
			final ItemStack stack = projection.get(i);
			if (stack != null)
				if (ItemStackHelper.areEqualNoNBT(stack, inputStack) || RecipeUtil.notConsumed(stack)
						|| ItemRegistry.isScrubbedFromOutput(stack))
					projection.set(i, null);
				else if (OreDictionaryHelper.isGeneric(stack))
					stack.setItemDamage(0);
		}

		// Do final scrub on the list
		return ImmutableList.copyOf(InventoryHelper.coelece(projection));
	}

	public static IRecipe findRecipe(final ItemStack stack) {

		for (final Object o : CraftingManager.getInstance().getRecipeList()) {
			final IRecipe r = (IRecipe) o;
			// craftingEquivalent
			if (ItemStackHelper.areEqual(stack, r.getRecipeOutput()))
				return r;
		}

		return null;
	}
}
