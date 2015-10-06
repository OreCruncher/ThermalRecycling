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

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

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

	public This secondaryInput(final Block block) {
		
		assert block != null;

		return secondaryInput(new ItemStack(block));
	}

	public This secondaryInput(final Block block, final int quantity) {

		assert block != null;
		assert quantity > 0;

		return secondaryInput(new ItemStack(block, quantity));
	}

	public This secondaryInput(final Item item) {

		assert item != null;

		return secondaryInput(new ItemStack(item));
	}

	public This secondaryInput(final Item item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return secondaryInput(new ItemStack(item, quantity));
	}

	public This secondaryInput(final String item) {

		assert item != null;

		return secondaryInput(item, 1);
	}

	public This secondaryInput(final String item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return secondaryInput(ItemStackHelper.getItemStack(item, quantity).get());
	}

	public This secondaryInput(final ItemStack sec) {

		assert sec != null;

		this.secondaryInput = sec;
		return THIS;
	}
}
