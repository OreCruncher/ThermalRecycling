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

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public abstract class RecipeBuilder<This extends RecipeBuilder<This>> {

	protected final int DEFAULT_ENERGY = 2400;

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

		Preconditions.checkArgument(items != null && items.length > 0,
				"Input ItemStacks cannot be null");

		for (String s : items)
			append(s, 1);

		return THIS;
	}

	public This append(String item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return append(RecipeHelper.getItemStack(item, quantity));
	}

	public This append(Block... blocks) {

		Preconditions.checkArgument(blocks != null && blocks.length > 0,
				"Input ItemStacks cannot be null");

		for (Block b : blocks)
			append(new ItemStack(b));

		return THIS;
	}

	public This append(Block block, int quantity) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return append(new ItemStack(block, quantity));
	}

	public This append(Item... items) {

		Preconditions.checkArgument(items != null && items.length > 0,
				"Input ItemStacks cannot be null");

		for (Item i : items)
			append(new ItemStack(i));

		return THIS;
	}

	public This append(Item item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return append(new ItemStack(item, quantity));
	}

	public This append(List<ItemStack> stacks) {

		Preconditions.checkArgument(stacks != null && !stacks.isEmpty(),
				"Input ItemStacks cannot be null");

		for (ItemStack stack : stacks) {
			this.input.add(stack);
		}

		return THIS;
	}

	public This append(ItemStack... stacks) {

		Preconditions.checkArgument(stacks != null && stacks.length > 0,
				"Input ItemStacks cannot be null");

		for (ItemStack stack : stacks) {
			this.input.add(stack);
		}

		return THIS;
	}

	public This appendSubtype(ItemStack stack, int subtype) {

		Preconditions.checkNotNull(stack, "Input ItemStack cannot be null");
		Preconditions.checkArgument(subtype >= 0,
				"Subtype has to be greater than or equal to 0");

		ItemStack s = stack.copy();
		s.setItemDamage(subtype);
		return append(s);
	}

	public This appendSubtype(Item item, int subtype) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(subtype >= 0,
				"Subtype has to be greater than or equal to 0");

		return append(new ItemStack(item, 1, subtype));
	}

	public This appendSubtypeRange(String item, int start, int end, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return append(RecipeHelper
				.getItemStackRange(item, start, end, quantity));
	}

	public This appendSubtypeRange(String item, int start, int end) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		return append(RecipeHelper.getItemStackRange(item, start, end, 1));
	}

	public This appendSubtypeRange(Item item, int start, int end, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		return append(RecipeHelper
				.getItemStackRange(item, start, end, quantity));
	}

	public This appendSubtypeRange(Item item, int start, int end) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		return append(RecipeHelper.getItemStackRange(item, start, end, 1));
	}

	public This appendSubtypeRange(Block block, int start, int end, int quantity) {
		return append(RecipeHelper.getItemStackRange(block, start, end,
				quantity));
	}

	public This appendSubtypeRange(Block block, int start, int end) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		return append(RecipeHelper.getItemStackRange(block, start, end, 1));
	}

	public This appendSubtypeRange(ItemStack stack, int start, int end) {

		Preconditions.checkNotNull(stack, "Input ItemStack cannot be null");
		Preconditions.checkArgument(end >= start && start > -1,
				"The subtype range is not valid");

		for (int i = start; i <= end; i++) {
			ItemStack s = stack.copy();
			s.setItemDamage(i);
			input.add(s);
		}

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

		return output(RecipeHelper.getItemStack(item, quantity));
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
			saveImpl(i);
			if (ModOptions.instance.getEnableRecipeLogging())
				ModLog.info(toString(i));
		}

		reset();
	}

	protected abstract String toString(ItemStack stack);

	protected abstract void saveImpl(ItemStack stack);
}
