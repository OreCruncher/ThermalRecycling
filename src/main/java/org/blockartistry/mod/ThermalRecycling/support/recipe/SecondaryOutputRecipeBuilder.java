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

public abstract class SecondaryOutputRecipeBuilder<This extends SecondaryOutputRecipeBuilder<This>>
		extends RecipeBuilder<This> {

	protected ItemStack secondaryOutput;
	protected int secondaryChance;

	public SecondaryOutputRecipeBuilder() {
		secondaryOutput = null;
		secondaryChance = 100;
	}

	@Override
	public This reset() {
		secondaryOutput = null;
		secondaryChance = 100;
		return super.reset();
	}

	public This secondaryOutput(final Block block) {

		assert block != null;

		return secondaryOutput(new ItemStack(block));
	}

	public This secondaryOutput(final Block block, final int quantity) {

		assert block != null;
		assert quantity > 0;

		return secondaryOutput(new ItemStack(block, quantity));
	}

	public This secondaryOutput(final Item item) {

		assert item != null;

		return secondaryOutput(new ItemStack(item));
	}

	public This secondaryOutput(final Item item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return secondaryOutput(new ItemStack(item, quantity));
	}

	public This secondaryOutput(final String item) {

		assert item != null;

		return secondaryOutput(item, 1);
	}

	public This secondaryOutput(final String item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return secondaryOutput(ItemStackHelper.getItemStack(item, quantity));
	}

	public This secondaryOutput(final ItemStack sec) {

		assert sec != null;

		this.secondaryOutput = sec;
		return THIS;
	}

	public This chance(final int chance) {

		assert chance > 0 && chance <= 100;

		this.secondaryChance = chance;
		return THIS;
	}
}
