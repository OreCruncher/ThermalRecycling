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

package org.blockartistry.mod.ThermalRecycling.recipe;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class SecondaryInputRecipeBuilder<This extends SecondaryInputRecipeBuilder<This>>
		extends RecipeBuilder<This> {

	protected ItemStack secondaryInput;

	public SecondaryInputRecipeBuilder() {
		secondaryInput = null;
	}

	@Override
	public This reset() {
		secondaryInput = null;
		return super.reset();
	}

	public This secondaryInput(Block block) {

		Preconditions.checkNotNull(block,
				"Secondary input ItemStack cannot be null");

		return secondaryInput(new ItemStack(block));
	}

	public This secondaryInput(Block block, int quantity) {

		Preconditions.checkNotNull(block,
				"Secondary input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity must be greather than 0");

		return secondaryInput(new ItemStack(block, quantity));
	}

	public This secondaryInput(Item item) {

		Preconditions.checkNotNull(item,
				"Secondary input ItemStack cannot be null");

		return secondaryInput(new ItemStack(item));
	}

	public This secondaryInput(Item item, int quantity) {

		Preconditions.checkNotNull(item,
				"Secondary input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity must be greather than 0");

		return secondaryInput(new ItemStack(item, quantity));
	}

	public This secondaryInput(String item) {

		Preconditions.checkNotNull(item,
				"Secondary input ItemStack cannot be null");

		return secondaryInput(item, 1);
	}

	public This secondaryInput(String item, int quantity) {

		Preconditions.checkNotNull(item,
				"Secondary input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity must be greather than 0");

		return secondaryInput(RecipeHelper.getItemStack(item, quantity));
	}

	public This secondaryInput(ItemStack sec) {

		Preconditions.checkNotNull(sec,
				"Secondary input ItemStack cannot be null");

		this.secondaryInput = sec;
		return THIS;
	}
}
