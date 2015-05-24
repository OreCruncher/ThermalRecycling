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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class RecipeDecomposition {

	static final String[] classIgnoreList = new String[] {
			"forestry.lepidopterology.MatingRecipe",
			"cofh.thermaldynamics.util.crafting.RecipeCover",
			"mods.railcraft.common.carts.LocomotivePaintingRecipe",
			"mods.railcraft.common.emblems.EmblemPostColorRecipe",
			"codechicken.enderstorage.common.EnderStorageRecipe" };

	static Field teRecipeAccessor = null;
	static Field forestryRecipeAccessor = null;

	public static List<ItemStack> decompose(IRecipe recipe) {
		
		List<ItemStack> projection = null;

		if (recipe instanceof ShapedRecipes) {
			projection = project((ShapedRecipes) recipe);
		} else if (recipe instanceof ShapelessRecipes) {
			projection = project((ShapelessRecipes) recipe);
		} else if (recipe instanceof ShapedOreRecipe) {
			projection = project((ShapedOreRecipe) recipe);
		} else if (recipe instanceof ShapelessOreRecipe) {
			projection = project((ShapelessOreRecipe) recipe);
		} else if (matchClassName(recipe,
				"cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper")) {
			projection = projectTERecipe(recipe);
		} else if (matchClassName(recipe,
				"forestry.core.utils.ShapedRecipeCustom")) {
			projection = projectForestryRecipe(recipe);
		} else if (!isClassIgnored(recipe)) {
			ModLog.info("Unknown recipe class: %s", recipe.getClass()
					.getName());
		}

		if(projection != null)
			scrubProjection(recipe.getRecipeOutput(), projection);

		return projection;
	}

	public static List<ItemStack> decomposeBuildCraft(ItemStack input, Object... output) {
		List<ItemStack> projection = projectBuildcraftRecipeList(output);
		if(projection != null)
			scrubProjection(input, projection);
		return projection;
	}
	
	public static List<ItemStack> decomposeForestry(ItemStack input, Object... output) {
		List<ItemStack> projection = projectForgeRecipeList(output);
		if(projection != null)
			scrubProjection(input, projection);
		return projection;
	}
	
	protected static boolean matchClassName(Object obj, String name) {
		return obj.getClass().getName().compareTo(name) == 0;
	}

	protected static boolean isClassIgnored(Object obj) {
		for (String s : classIgnoreList)
			if (matchClassName(obj, s))
				return true;
		return false;
	}

	protected static void scrubProjection(ItemStack inputStack, List<ItemStack> projection) {

		if (projection == null)
			return;

		// Scan for the list looking for wildcards as well
		// as those items that match the input. Sometimes
		// an author will make a cyclic recipe.
		for (int i = 0; i < projection.size(); i++) {
			ItemStack stack = projection.get(i);
			if (stack != null)
				if (ItemHelper.itemsEqualWithMetadata(stack, inputStack) || ItemScrapData.isScrubbedFromOutput(stack))
					projection.set(i, null);
				else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					stack.setItemDamage(0);
		}

		// Do final scrub on the list
		projection = MyUtils.compress(ItemStackHelper.coelece(projection));
	}

	protected static List<ItemStack> project(ShapedRecipes recipe) {
		return ItemStackHelper.clone(recipe.recipeItems);
	}

	protected static List<ItemStack> project(ShapelessRecipes recipe) {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (Object stack : recipe.recipeItems)
			result.add(((ItemStack) (stack)).copy());
		return result;
	}

	static List<ItemStack> recurseArray(List<?> list, int level) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		if (level == 3)
			result.add((ItemStack) list.get(0));
		else
			for (Object o : list) {
				if (o instanceof ItemStack)
					result.add(((ItemStack) o).copy());
				else if (o instanceof ArrayList<?>) {
					result.addAll(recurseArray((ArrayList<?>) o, level + 1));
				}
			}

		return result;
	}

	protected static List<ItemStack> projectBuildcraftRecipeList(Object[] list) {
		return recurseArray(Arrays.asList(list), 1);
	}

	protected static List<ItemStack> projectForgeRecipeList(Object[] list) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = 0; i < list.length; i++) {
			Object o = list[i];

			if (o instanceof ItemStack)
				result.add(((ItemStack) o).copy());
			else if (o instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<ItemStack> t = (ArrayList<ItemStack>) o;
				result.add(ItemStackHelper.getPreferredStack(t.get(0)));
			}
		}

		return result;
	}

	protected static List<ItemStack> project(ShapedOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput());
	}

	protected static List<ItemStack> project(ShapelessOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput().toArray());
	}

	protected static List<ItemStack> projectTERecipe(IRecipe recipe) {

		List<ItemStack> result = null;

		try {

			if (teRecipeAccessor == null) {
				Field temp = recipe.getClass().getDeclaredField("recipe");
				temp.setAccessible(true);
				teRecipeAccessor = temp;
			}

			try {
				ShapedOreRecipe shaped = (ShapedOreRecipe) teRecipeAccessor
						.get(recipe);
				result = project(shaped);
			} catch (Exception e) {
			}

		} catch (NoSuchFieldException e) {
			;
		} catch (SecurityException e) {
			;
		} catch (IllegalArgumentException e) {
			;
		}

		return result;
	}

	protected static List<ItemStack> projectForestryRecipe(IRecipe recipe) {

		List<ItemStack> result = null;

		try {

			if (forestryRecipeAccessor == null) {
				Field temp = recipe.getClass().getDeclaredField("ingredients");
				temp.setAccessible(true);
				forestryRecipeAccessor = temp;
			}

			try {
				Object[] shaped = (Object[]) forestryRecipeAccessor.get(recipe);
				result = projectForgeRecipeList(shaped);
			} catch (Exception e) {
			}

		} catch (NoSuchFieldException e) {
			;
		} catch (SecurityException e) {
			;
		} catch (IllegalArgumentException e) {
			;
		}

		return result;
	}

	public static IRecipe findRecipe(ItemStack stack) {

		for (Object o : CraftingManager.getInstance().getRecipeList()) {
			IRecipe r = (IRecipe) o;
			// craftingEquivalent
			if (ItemHelper.itemsEqualForCrafting(stack, r.getRecipeOutput()))
				return r;
		}

		return null;
	}
}
