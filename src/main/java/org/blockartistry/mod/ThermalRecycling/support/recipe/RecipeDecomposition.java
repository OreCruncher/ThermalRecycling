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

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.collect.ImmutableList;

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

	private RecipeDecomposition() {
	}

	private static final List<String> classIgnoreList = new ImmutableList.Builder<String>()
			.add("forestry.lepidopterology.MatingRecipe",
					"cofh.thermaldynamics.util.crafting.RecipeCover",
					"mods.railcraft.common.carts.LocomotivePaintingRecipe",
					"mods.railcraft.common.emblems.EmblemPostColorRecipe",
					"codechicken.enderstorage.common.EnderStorageRecipe")
			.build();
	
	private static final List<ItemStack> recipeComponentBlacklist = ImmutableList
			.copyOf(ItemStackHelper.getItemStacks(ModOptions
					.getRecipeComponentBlacklist()));

	private static Field teRecipeAccessor = null;
	private static Field forestryRecipeAccessor = null;

	private static Field ae2ShapedRecipeAccessor = null;
	private static Field ae2ShapelessRecipeAccessor = null;
	private static Field ae2ItemNameAccessor = null;
	private static Field ae2NameSpaceAccessor = null;
	private static Field ae2MetaAccessor = null;
	private static Field ae2GroupIngredientInputAccessor = null;
	private static Field ae2IngredientSetInputAccessor = null;

	public static List<ItemStack> decompose(final IRecipe recipe) {

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
		} else if (matchClassName(recipe, "appeng.recipes.game.ShapedRecipe")) {
			projection = projectAE2ShapedRecipe(recipe);
		} else if (matchClassName(recipe, "appeng.recipes.game.ShapelessRecipe")) {
			projection = projectAE2ShapelessRecipe(recipe);
		} else if (!isClassIgnored(recipe)) {
			ModLog.info("Unknown recipe class: %s", recipe.getClass().getName());
		}

		if (projection != null)
			projection = scrubProjection(recipe.getRecipeOutput(), projection);

		return projection;
	}

	public static List<ItemStack> decomposeBuildCraft(final ItemStack input,
			final Object... output) {
		List<ItemStack> projection = projectBuildcraftRecipeList(output);
		if (projection != null)
			projection = scrubProjection(input, projection);
		return projection;
	}

	public static List<ItemStack> decomposeForestry(final ItemStack input,
			final Object... output) {
		List<ItemStack> projection = projectForgeRecipeList(output);
		if (projection != null)
			projection = scrubProjection(input, projection);
		return projection;
	}

	private static boolean matchClassName(final Object obj, final String name) {
		return obj.getClass().getName().compareTo(name) == 0;
	}

	private static boolean isClassIgnored(final Object obj) {
		return classIgnoreList.contains(obj.getClass().getName());
	}

	private static boolean notConsumed(final ItemStack stack) {
		return stack.getItem().hasContainerItem(stack);
	}
	
	private static boolean ignoreRecipe(final List<ItemStack> projection) {
		
		for(final ItemStack p: projection)
			for(final ItemStack s: recipeComponentBlacklist)
				if(ItemStackHelper.areEqual(p, s))
					return true;
		
		return false;
	}
	
	private static List<ItemStack> scrubProjection(final ItemStack inputStack,
			List<ItemStack> projection) {

		if (projection == null || ignoreRecipe(projection))
			return null;

		// Scan for the list looking for wildcards as well
		// as those items that match the input. Sometimes
		// an author will make a cyclic recipe.
		for (int i = 0; i < projection.size(); i++) {
			final ItemStack stack = projection.get(i);
			if (stack != null)
				if (ItemHelper.itemsEqualWithMetadata(stack, inputStack)
						|| ItemData.isScrubbedFromOutput(stack) || notConsumed(stack))
					projection.set(i, null);
				else if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
					stack.setItemDamage(0);
		}

		// Do final scrub on the list
		return ImmutableList.copyOf(ItemStackHelper.coelece(projection));
	}

	private static List<ItemStack> project(final ShapedRecipes recipe) {
		return ItemStackHelper.clone(recipe.recipeItems);
	}

	private static List<ItemStack> project(final ShapelessRecipes recipe) {
		final ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (final Object stack : recipe.recipeItems)
			result.add(((ItemStack) (stack)).copy());
		return result;
	}

	private static List<ItemStack> recurseArray(final List<?> list,
			final int level) {

		final ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		if (level == 3)
			result.add(((ItemStack) list.get(0)).copy());
		else
			for (final Object o : list) {
				if (o instanceof ItemStack)
					result.add(((ItemStack) o).copy());
				else if (o instanceof ArrayList<?>) {
					result.addAll(recurseArray((ArrayList<?>) o, level + 1));
				}
			}

		return result;
	}

	private static List<ItemStack> projectBuildcraftRecipeList(
			final Object... list) {
		return recurseArray(Arrays.asList(list), 1);
	}

	private static List<ItemStack> projectForgeRecipeList(final Object... list) {

		final ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = 0; i < list.length; i++) {
			final Object o = list[i];

			if (o instanceof ItemStack)
				result.add(((ItemStack) o).copy());
			else if (o instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				final ArrayList<ItemStack> t = (ArrayList<ItemStack>) o;
				if (!t.isEmpty())
					result.add(ItemStackHelper.getPreferredStack(t.get(0).copy()));
			}
		}

		return result;
	}

	private static List<ItemStack> project(final ShapedOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput());
	}

	private static List<ItemStack> project(final ShapelessOreRecipe recipe) {
		return projectForgeRecipeList(recipe.getInput().toArray());
	}

	private static List<ItemStack> projectTERecipe(final IRecipe recipe) {

		List<ItemStack> result = null;

		try {

			if (teRecipeAccessor == null) {
				final Field temp = recipe.getClass().getDeclaredField("recipe");
				temp.setAccessible(true);
				teRecipeAccessor = temp;
			}

			try {
				final ShapedOreRecipe shaped = (ShapedOreRecipe) teRecipeAccessor
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

	private static List<ItemStack> projectForestryRecipe(final IRecipe recipe) {

		List<ItemStack> result = null;

		try {

			if (forestryRecipeAccessor == null) {
				final Field temp = recipe.getClass().getDeclaredField(
						"ingredients");
				temp.setAccessible(true);
				forestryRecipeAccessor = temp;
			}

			try {
				final Object[] shaped = (Object[]) forestryRecipeAccessor
						.get(recipe);
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

	private static Object getFromGroupIngredient(final Object input)
			throws Throwable {
		if (ae2GroupIngredientInputAccessor == null) {
			ae2GroupIngredientInputAccessor = input.getClass()
					.getDeclaredField("ingredients");
			ae2GroupIngredientInputAccessor.setAccessible(true);
		}

		final Object result = ((List<?>) ae2GroupIngredientInputAccessor
				.get(input)).get(0);
		if (matchClassName(result, "appeng.recipes.IngredientSet"))
			return getFromIngredientSet(result);

		return result;
	}

	@SuppressWarnings("unchecked")
	private static ItemStack getFromIngredientSet(final Object input)
			throws Throwable {
		if (ae2IngredientSetInputAccessor == null) {
			ae2IngredientSetInputAccessor = input.getClass().getDeclaredField(
					"items");
			ae2IngredientSetInputAccessor.setAccessible(true);
		}

		final List<ItemStack> list = (List<ItemStack>) ae2IngredientSetInputAccessor
				.get(input);

		// Return the last in the list - it is more than likely the
		// Fluix version based on analysis.
		return list.get(list.size() - 1).copy();
	}

	@SuppressWarnings("unchecked")
	private static List<ItemStack> projectAE2Recipe(final List<?> input) {
		final List<ItemStack> result = new ArrayList<ItemStack>();

		for (Object o : input) {
			if (o != null) {

				try {

					final ItemStack stack;
					if (matchClassName(o, "appeng.recipes.IngredientSet")) {
						stack = getFromIngredientSet(o);
					} else {

						if (matchClassName(o, "appeng.recipes.GroupIngredient")) {
							o = getFromGroupIngredient(o);
						}

						// Possible an ItemStack came back because of
						// IngredientSets
						if (o instanceof ItemStack) {
							stack = (ItemStack) o;
						} else {

							if (ae2ItemNameAccessor == null) {
								ae2ItemNameAccessor = o.getClass()
										.getDeclaredField("itemName");
								ae2ItemNameAccessor.setAccessible(true);

								ae2NameSpaceAccessor = o.getClass()
										.getDeclaredField("nameSpace");
								ae2NameSpaceAccessor.setAccessible(true);

								ae2MetaAccessor = o.getClass()
										.getDeclaredField("meta");
								ae2MetaAccessor.setAccessible(true);
							}

							final String nameSpace = (String) ae2NameSpaceAccessor
									.get(o);
							final String itemName = (String) ae2ItemNameAccessor
									.get(o);
							final int meta = ae2MetaAccessor.getInt(o);

							final String theItem;
							if (nameSpace.equalsIgnoreCase("oredictionary")) {
								theItem = itemName;
							} else {
								if (nameSpace
										.equalsIgnoreCase("appliedenergistics2")) {

									final StringBuilder builder = new StringBuilder();
									builder.append(nameSpace)
										.append(':');
									if (itemName.startsWith("Block"))
										builder.append("tile.");
									else
										builder.append("item.");
									builder.append(itemName)
										.append(':')
										.append(meta);
									theItem = builder.toString();

								} else {
									theItem = StringUtils.join(Arrays.asList(
											nameSpace, itemName, meta), ":");
								}
							}

							stack = ItemStackHelper.getItemStack(theItem);
						}
					}

					if (stack != null) {
						result.add(stack);
					}

				} catch (Throwable t) {
					;
				}
			}
		}

		return result;
	}

	private static List<ItemStack> projectAE2ShapelessRecipe(
			final IRecipe recipe) {

		try {

			if (ae2ShapelessRecipeAccessor == null) {
				ae2ShapelessRecipeAccessor = recipe.getClass()
						.getDeclaredField("input");
				ae2ShapelessRecipeAccessor.setAccessible(true);
			}

			return projectAE2Recipe((List<?>) ae2ShapelessRecipeAccessor
					.get(recipe));

		} catch (Throwable t) {
			;
		}

		return null;
	}

	private static List<ItemStack> projectAE2ShapedRecipe(final IRecipe recipe) {

		try {

			if (ae2ShapedRecipeAccessor == null) {
				ae2ShapedRecipeAccessor = recipe.getClass().getDeclaredField(
						"input");
				ae2ShapedRecipeAccessor.setAccessible(true);
			}

			return projectAE2Recipe(Arrays
					.asList((Object[]) ae2ShapedRecipeAccessor.get(recipe)));

		} catch (Throwable t) {
			;
		}

		return new ArrayList<ItemStack>();
	}

	public static IRecipe findRecipe(final ItemStack stack) {

		for (final Object o : CraftingManager.getInstance().getRecipeList()) {
			final IRecipe r = (IRecipe) o;
			// craftingEquivalent
			if(ItemStackHelper.areEqual(stack, r.getRecipeOutput()))
				return r;
			//if (ItemHelper.itemsEqualForCrafting(stack, r.getRecipeOutput()))
			//	return r;
		}

		return null;
	}
}
