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

	public This setEnergy(final int energy) {

		assert energy > 0;

		this.energy = energy;
		return THIS;
	}

	public This append(final String... items) {
		AppendHelper.append(input, items);
		return THIS;
	}

	public This append(final String item, final int quantity) {
		AppendHelper.append(input, item, quantity);
		return THIS;
	}

	public This append(final Block... blocks) {
		AppendHelper.append(input, blocks);
		return THIS;
	}

	public This append(final Block block, final int quantity) {
		AppendHelper.append(input, block, quantity);
		return THIS;
	}

	public This append(final Item... items) {
		AppendHelper.append(input, items);
		return THIS;
	}

	public This append(final Item item, final int quantity) {
		AppendHelper.append(input, item, quantity);
		return THIS;
	}

	public This append(final List<ItemStack> stacks) {
		AppendHelper.append(input, stacks);
		return THIS;
	}

	public This append(final ItemStack... stacks) {
		AppendHelper.append(input, stacks);
		return THIS;
	}

	public This append(final ItemStack stack, final int quantity) {
		assert stack != null;
		assert quantity > 0;

		final ItemStack item = stack.copy();
		item.stackSize = quantity;
		AppendHelper.append(input, item);
		return THIS;
	}

	public This appendSubtype(final ItemStack stack, final int subtype) {
		AppendHelper.appendSubtype(input, stack, subtype);
		return THIS;
	}

	public This appendSubtype(final Item item, final int subtype) {
		AppendHelper.appendSubtype(input, item, subtype);
		return THIS;
	}

	public This appendSubtypeRange(final String item, final int start,
			final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(input, item, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(final String item, final int start,
			final int end) {
		AppendHelper.appendSubtypeRange(input, item, start, end);
		return THIS;
	}

	public This appendSubtypeRange(final Item item, final int start,
			final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(input, item, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(final Item item, final int start,
			final int end) {
		AppendHelper.appendSubtypeRange(input, item, start, end);
		return THIS;
	}

	public This appendSubtypeRange(final Block block, final int start,
			final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(input, block, start, end, quantity);
		return THIS;
	}

	public This appendSubtypeRange(final Block block, final int start,
			final int end) {
		AppendHelper.appendSubtypeRange(input, block, start, end);
		return THIS;
	}

	public This appendSubtypeRange(final ItemStack stack, final int start,
			final int end) {
		AppendHelper.appendSubtypeRange(input, stack, start, end);
		return THIS;
	}

	public This appendSubtypeRange(final ItemStack stack, final int start,
			final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(input, stack, start, end, quantity);
		return THIS;
	}

	public This output(final Block block) {
		
		assert block != null;

		return output(new ItemStack(block));
	}

	public This output(final Block block, final int quantity) {

		assert block != null;
		assert quantity > 0;

		return output(new ItemStack(block, quantity));
	}

	public This output(final Item item) {
		
		assert item != null;

		return output(new ItemStack(item));
	}

	public This output(final Item item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return output(new ItemStack(item, quantity));
	}

	public This output(final String item) {

		assert item != null;

		return output(item, 1);
	}

	public This output(final String item, final int quantity) {

		assert item != null;
		assert quantity > 0;

		return output(ItemStackHelper.getItemStack(item, quantity).get());
	}

	public This output(final ItemStack out, final int quantity) {

		assert out != null;
		assert quantity > 0;

		this.output = out;
		this.output.stackSize = quantity;
		return THIS;
	}

	public This output(final ItemStack out) {

		assert out != null;

		this.output = out;
		return THIS;
	}

	public void save() {

		assert input != null && !input.isEmpty();

		for (final ItemStack i : input) {
			final int result = saveImpl(i);
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
