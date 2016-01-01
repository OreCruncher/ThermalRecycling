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

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Optional;

import ic2.api.recipe.RecipeInputFluidContainer;
import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.RecipeInputOreDict;
import net.minecraft.item.ItemStack;

public abstract class IC2AccessorBase extends RecipeAccessorBase {
	
	private static final ItemStack emptyCell = ItemStackHelper.getItemStack("IC2:itemCellEmpty:0").get();

	protected static List<ItemStack> project(final Object[] ingredients) {
		final List<ItemStack> result = new ArrayList<ItemStack>();
		for(int i = 0; i < ingredients.length; i++) {
			ItemStack scratch = null;
			final Object o = ingredients[i];
			if(o instanceof RecipeInputItemStack) {
				final RecipeInputItemStack r = (RecipeInputItemStack) o;
				scratch = r.input.copy();
				scratch.stackSize = r.amount;
			} else if(o instanceof RecipeInputOreDict) {
				final RecipeInputOreDict r = (RecipeInputOreDict) o;
				final Optional<ItemStack> opt = ItemStackHelper.getItemStack(r.input, r.amount);
				if(opt.isPresent())
					scratch = opt.get();
			} else if(o instanceof RecipeInputFluidContainer) {
				final RecipeInputFluidContainer c = (RecipeInputFluidContainer)o;
				final int quantity = c.amount / 1000;
				if(quantity > 0) {
					scratch = emptyCell.copy();
					scratch.stackSize = quantity;
				}
			}
			if(scratch != null)
				result.add(scratch);
		}
		return result;
	}
}