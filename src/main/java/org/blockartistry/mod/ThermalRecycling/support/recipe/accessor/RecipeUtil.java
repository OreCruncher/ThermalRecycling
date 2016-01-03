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

package org.blockartistry.mod.ThermalRecycling.support.recipe.accessor;

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

public final class RecipeUtil {

	private static final List<String> classIgnoreList = new ImmutableList.Builder<String>().add(
			"forestry.lepidopterology.recipes.MatingRecipe", "cofh.thermaldynamics.util.crafting.RecipeCover",
			"mods.railcraft.common.carts.LocomotivePaintingRecipe",
			"mods.railcraft.common.emblems.EmblemPostColorRecipe", "codechicken.enderstorage.common.EnderStorageRecipe",
			"blusunrize.immersiveengineering.common.crafting.RecipePotionBullets",
			"mods.railcraft.common.util.crafting.RoutingTicketCopyRecipe",
			"mods.railcraft.common.emblems.LocomotiveEmblemRecipe",
			"mods.railcraft.common.emblems.EmblemPostEmblemRecipe").build();

	private static final List<ItemStack> recipeComponentBlacklist = ImmutableList
			.copyOf(ItemStackHelper.getItemStacks(ModOptions.getRecipeComponentBlacklist()));

	private RecipeUtil() {
	}

	/**
	 * Identifies and returns the ItemStack from the list that is considered the
	 * preferred stack. It is assumed that there is commonality between the
	 * stacks.
	 */
	public static ItemStack getPreferredFromList(final List<ItemStack> stacks) {
		if (stacks == null || stacks.isEmpty())
			return null;
		if (stacks.size() == 1)
			return ItemStackHelper.getPreferredStack(stacks.get(0)).get();

		// OK - do this the hard way. Have to scan each of the stacks looking
		// for the common ore dictionary grouping. If there is a single
		// name, great. If more than one the common factor has to be identified.
		final List<String> oreNames = OreDictionaryHelper.getOreNamesForStack(stacks.get(0));
		if (oreNames.size() > 1) {
			for (int i = 1; i < stacks.size(); i++) {
				oreNames.retainAll(OreDictionaryHelper.getOreNamesForStack(stacks.get(i)));
				if (oreNames.size() < 2)
					break;
			}
		}

		if (oreNames.size() == 0)
			return ItemStackHelper.getPreferredStack(stacks.get(0)).get();

		return ItemStackHelper.getPreferredStack(oreNames.get(0)).get();
	}

	/**
	 * Processes the input object list as if it were from a Forge recipe. Each
	 * entry is cracked and handled, resulting in an ItemStack in the return
	 * list.
	 */
	public static List<ItemStack> projectForgeRecipeList(final Object... list) {

		final List<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = 0; i < list.length; i++) {
			final Object o = list[i];

			if (o == null)
				continue;

			if (o instanceof ItemStack)
				result.add(((ItemStack) o).copy());
			else if (o instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				final ArrayList<ItemStack> t = (ArrayList<ItemStack>) o;
				if (!t.isEmpty())
					result.add(getPreferredFromList(t).copy());
			} else {
				ModLog.warn("Unknown class in Forge recipe list: " + o.getClass().getName());
			}
		}

		return result;
	}

	public static boolean matchClassName(final Object obj, final String name) {
		return obj.getClass().getName().compareTo(name) == 0;
	}

	public static boolean notConsumed(final ItemStack stack) {
		return stack.getItem().hasContainerItem(stack);
	}

	public static boolean isClassIgnored(final Object obj) {
		return classIgnoreList.contains(obj.getClass().getName());
	}

	public static boolean ignoreRecipe(final List<ItemStack> projection) {
		for (final ItemStack p : projection)
			for (final ItemStack s : recipeComponentBlacklist)
				if (ItemStackHelper.areEqual(p, s))
					return true;
		return false;
	}
}
