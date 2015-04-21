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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class SecondaryInputRecipeBuilder extends RecipeBuilder {

	protected ItemStack secondaryInput;

	public SecondaryInputRecipeBuilder() {
		secondaryInput = null;
	}

	@Override
	public RecipeBuilder reset() {
		secondaryInput = null;
		return super.reset();
	}

	/*
	 * public RecipeBuilder resetSecondaryInput() { this.secondaryInput = null;
	 * return this; }
	 */

	@Override
	public RecipeBuilder secondaryInput(Block block) {
		return secondaryInput(block, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(Block block, int quantity) {
		return secondaryInput(new ItemStack(block, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(Item item) {
		return secondaryInput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(Item item, int quantity) {
		return secondaryInput(new ItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(String item) {
		return secondaryInput(item, 1);
	}

	@Override
	public RecipeBuilder secondaryInput(String item, int quantity) {
		return secondaryInput(RecipeHelper.getItemStack(item, quantity));
	}

	@Override
	public RecipeBuilder secondaryInput(ItemStack sec) {
		this.secondaryInput = sec;
		return this;
	}

	@Override
	public RecipeBuilder secondaryInputSubtype(int type) {
		if (this.secondaryInput != null)
			this.secondaryInput.setItemDamage(type);
		return this;
	}

	public RecipeBuilder setSecondaryInputQuantity(int quantity) {
		if (this.secondaryInput != null)
			this.secondaryInput.stackSize = quantity;
		return this;
	}
}
