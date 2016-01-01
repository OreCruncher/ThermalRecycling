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
import java.util.Arrays;
import java.util.List;

import buildcraft.api.recipes.IFlexibleRecipeViewable;
import net.minecraft.item.ItemStack;

public class BEFlexibleRecipeAccessor extends RecipeAccessorBase {

	private static List<ItemStack> recurseArray(final List<?> list, final int level) {

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

	private static List<ItemStack> projectBuildcraftRecipeList(final Object... list) {
		return recurseArray(Arrays.asList(list), 1);
	}

	@Override
	public ItemStack getInput(final Object recipe) {
		final IFlexibleRecipeViewable t = (IFlexibleRecipeViewable)recipe;
		return ((ItemStack)t.getOutput()).copy();
	}

	@Override
	public List<ItemStack> getOutput(final Object recipe) {
		final IFlexibleRecipeViewable t = (IFlexibleRecipeViewable)recipe;
		return projectBuildcraftRecipeList(t.getInputs());
	}
}
