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

import org.blockartistry.mod.ThermalRecycling.support.recipe.IRecipeAccessor;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class AEShapedRecipeAccessor extends AEAccessorBase implements IRecipeAccessor {

	private static Field ae2ShapedRecipeAccessor = null;

	@Override
	public ItemStack getInput(final Object recipe) {
		return ((IRecipe) recipe).getRecipeOutput().copy();
	}

	@Override
	public List<ItemStack> getOutput(final Object recipe) {
		try {

			if (ae2ShapedRecipeAccessor == null) {
				ae2ShapedRecipeAccessor = recipe.getClass().getDeclaredField("input");
				ae2ShapedRecipeAccessor.setAccessible(true);
			}

			return projectAE2Recipe(Arrays.asList((Object[]) ae2ShapedRecipeAccessor.get(recipe)));

		} catch (Throwable t) {
			;
		}

		return new ArrayList<ItemStack>();
	}
}
