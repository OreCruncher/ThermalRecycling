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

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeBuilder<This extends RecipeBuilder<This>> {

	protected static final int DEFAULT_ENERGY = 2400;

	@SuppressWarnings("unchecked")
	protected final This THIS = (This) this;

	protected int energy;
	protected List<ItemStack> input;
	protected ItemStack output;

	public RecipeBuilder() {

		reset();
	}

	public This reset() {
		this.energy = DEFAULT_ENERGY;
		this.input = new ArrayList<ItemStack>();
		this.output = null;
		return THIS;
	}

	public This setEnergy(int energy) {

		Preconditions
				.checkArgument(energy > 0, "Energy must be greater than 0");

		this.energy = energy;
		return THIS;
	}

	public This append(String... items) {
		ItemStackHelper.append(input, items);
		return THIS;
	}

	public This append(String item, int quantity) {
		ItemStackHelper.append(input, item, quantity);
		return THIS;
	}

	public This append(Block... blocks) {
		ItemStackHelper.append(input, blocks);
		return THIS;
	}

	public This append(Block block, int quantity) {
		ItemStackHelper.append(input, block, quantity);
		return THIS;
	}

	public This append(Item... items) {
		ItemStackHelper.append(input, items);
		return THIS;
	}

	public This append(Item item, int quantity) {
		ItemStackHelper.append(input, item, quantity);
		return THIS;
	}

	public This append(List<ItemStack> stacks) {
		ItemStackHelper.append(input, stacks);
		return THIS;
	}

	public This append(ItemStack... stacks) {
		ItemStackHelper.append(input, stacks);
		return THIS;
	}

	public This append(ItemStack stack, int quantity) {
		Preconditions.checkNotNull(stack);
		ItemStack item = stack.copy();
		item.stackSize = quantity;
		ItemStackHelper.append(input, item);
		return THIS;
	}

	public This appendSubtype(ItemStack stack, int subtype) {
		ItemStackHelper.appendSubtype(input, stack, subtype);
		return THIS;
	}

	public This appendSubtype(Item item, int subtype) {
		ItemStackHelper.appendSubtype(input, item, subtype);
		return THIS;
	}

	public This appendSubtypeRange(String item, int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(input, item, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(String item, int start, int end) {
		ItemStackHelper.appendSubtypeRange(input, item, start, end);
		return THIS;
	}

	public This appendSubtypeRange(Item item, int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(input, item, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(Item item, int start, int end) {
		ItemStackHelper.appendSubtypeRange(input, item, start, end);
		return THIS;
	}

	public This appendSubtypeRange(Block block, int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(input, block, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(Block block, int start, int end) {
		ItemStackHelper.appendSubtypeRange(input, block, start, end);
		return THIS;
	}

	public This appendSubtypeRange(ItemStack stack, int start, int end) {
		ItemStackHelper.appendSubtypeRange(input, stack, start, end);
		return THIS;
	}

	public This appendSubtypeRange(ItemStack stack, int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(input, stack, start, end, quantity);
		return THIS;
	}

	public This output(Block block) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");

		return output(new ItemStack(block));
	}

	public This output(Block block, int quantity) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return output(new ItemStack(block, quantity));
	}

	public This output(Item item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return output(new ItemStack(item));
	}

	public This output(Item item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return output(new ItemStack(item, quantity));
	}

	public This output(String item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return output(item, 1);
	}

	public This output(String item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return output(ItemStackHelper.getItemStack(item, quantity));
	}

	public This output(ItemStack out, int quantity) {

		Preconditions.checkNotNull(out, "Output ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		this.output = out;
		this.output.stackSize = quantity;
		return THIS;
	}

	public This output(ItemStack out) {

		Preconditions.checkNotNull(out, "Output ItemStack cannot be null");

		this.output = out;
		return THIS;
	}

	public void save() {

		Preconditions.checkState(!input.isEmpty(),
				"No input ItemStacks were specified");

		for (ItemStack i : input) {
			int result = saveImpl(i);
			if (result == RecipeData.FAILURE)
				ModLog.warn("Unable to save recipe [%s]", toString());
			else if (result == RecipeData.DUPLICATE)
				ModLog.debug("Duplicate recipe [%s]", toString());
		}

		reset();
	}

	protected abstract String toString(ItemStack stack);

	protected abstract int saveImpl(ItemStack stack);
}
