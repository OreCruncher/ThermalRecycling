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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import net.minecraft.item.ItemStack;

public abstract class AEAccessorBase {

	private static Field ae2ItemNameAccessor = null;
	private static Field ae2NameSpaceAccessor = null;
	private static Field ae2MetaAccessor = null;
	private static Field ae2GroupIngredientInputAccessor = null;
	private static Field ae2IngredientSetInputAccessor = null;

	protected static Object getFromGroupIngredient(final Object input) throws Throwable {
		if (ae2GroupIngredientInputAccessor == null) {
			ae2GroupIngredientInputAccessor = input.getClass().getDeclaredField("ingredients");
			ae2GroupIngredientInputAccessor.setAccessible(true);
		}

		final Object result = ((List<?>) ae2GroupIngredientInputAccessor.get(input)).get(0);
		if (RecipeUtil.matchClassName(result, "appeng.recipes.IngredientSet"))
			return getFromIngredientSet(result);

		return result;
	}

	@SuppressWarnings("unchecked")
	protected static ItemStack getFromIngredientSet(final Object input) throws Throwable {
		if (ae2IngredientSetInputAccessor == null) {
			ae2IngredientSetInputAccessor = input.getClass().getDeclaredField("items");
			ae2IngredientSetInputAccessor.setAccessible(true);
		}

		final List<ItemStack> list = (List<ItemStack>) ae2IngredientSetInputAccessor.get(input);

		// Return the last in the list - it is more than likely the
		// Fluix version based on analysis.
		return list.get(list.size() - 1).copy();
	}

	@SuppressWarnings("unchecked")
	protected static List<ItemStack> projectAE2Recipe(final List<?> input) {
		final List<ItemStack> result = new ArrayList<ItemStack>();

		for (Object o : input) {
			if (o != null) {

				try {

					final ItemStack stack;
					if (RecipeUtil.matchClassName(o, "appeng.recipes.IngredientSet")) {
						stack = getFromIngredientSet(o);
					} else {

						if (RecipeUtil.matchClassName(o, "appeng.recipes.GroupIngredient")) {
							o = getFromGroupIngredient(o);
						}

						// Possible an ItemStack came back because of
						// IngredientSets
						if (o instanceof ItemStack) {
							stack = (ItemStack) o;
						} else {

							if (ae2ItemNameAccessor == null) {
								ae2ItemNameAccessor = o.getClass().getDeclaredField("itemName");
								ae2ItemNameAccessor.setAccessible(true);

								ae2NameSpaceAccessor = o.getClass().getDeclaredField("nameSpace");
								ae2NameSpaceAccessor.setAccessible(true);

								ae2MetaAccessor = o.getClass().getDeclaredField("meta");
								ae2MetaAccessor.setAccessible(true);
							}

							final String nameSpace = (String) ae2NameSpaceAccessor.get(o);
							final String itemName = (String) ae2ItemNameAccessor.get(o);
							final int meta = ae2MetaAccessor.getInt(o);

							final String theItem;
							if (nameSpace.equalsIgnoreCase("oredictionary")) {
								theItem = itemName;
							} else {
								if (nameSpace.equalsIgnoreCase("appliedenergistics2")) {

									final StringBuilder builder = new StringBuilder();
									builder.append(nameSpace).append(':');
									if (itemName.startsWith("Block"))
										builder.append("tile.");
									else
										builder.append("item.");
									builder.append(itemName).append(':').append(meta);
									theItem = builder.toString();

								} else {
									theItem = StringUtils.join(Arrays.asList(nameSpace, itemName, meta), ":");
								}
							}

							stack = ItemStackHelper.getItemStack(theItem).get();
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
}
