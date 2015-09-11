/*
 * ThermalRecyclerRecipeBuilder file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Preconditions;

public final class ThermalRecyclerRecipeBuilder {

	protected List<ItemStack> output;
	protected ItemStack input;

	public ThermalRecyclerRecipeBuilder() {
		reset();
	}

	public ThermalRecyclerRecipeBuilder reset() {
		this.output = new ArrayList<ItemStack>();
		this.input = null;
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final String... items) {
		AppendHelper.append(output, items);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final String item, final int quantity) {
		AppendHelper.append(output, item, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final Block... blocks) {
		AppendHelper.append(output, blocks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final Block block, final int quantity) {
		AppendHelper.append(output, block, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final Item... items) {
		AppendHelper.append(output, items);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final Item item, final int quantity) {
		AppendHelper.append(output, item, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final List<ItemStack> stacks) {
		AppendHelper.append(output, stacks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final ItemStack... stacks) {
		AppendHelper.append(output, stacks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(final ItemStack stack, final int quantity) {
		Preconditions.checkNotNull(stack);
		final ItemStack t = stack.copy();
		t.stackSize = quantity;
		return append(t);
	}

	public ThermalRecyclerRecipeBuilder appendSubtype(final ItemStack stack,
			final int subtype) {
		AppendHelper.appendSubtype(output, stack, subtype);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtype(final Item item, final int subtype) {
		AppendHelper.appendSubtype(output, item, subtype);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final String item,
			final int start, final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(output, item, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final String item,
			final int start, final int end) {
		AppendHelper.appendSubtypeRange(output, item, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final Item item,
			final int start, final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(output, item, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final Item item,
			final int start, final int end) {
		AppendHelper.appendSubtypeRange(output, item, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final Block block,
			final int start, final int end, final int quantity) {
		AppendHelper.appendSubtypeRange(output, block, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final Block block,
			final int start, final int end) {
		AppendHelper.appendSubtypeRange(output, block, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(final ItemStack stack,
			final int start, final int end) {
		AppendHelper.appendSubtypeRange(output, stack, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder input(final Block block) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");

		return input(new ItemStack(block));
	}

	public ThermalRecyclerRecipeBuilder input(final Item item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return input(new ItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder input(final String item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return input(ItemStackHelper.getItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder input(final String name, final int quantity) {
		Preconditions.checkNotNull(name, "Input ItemStack cannot be null");

		return input(ItemStackHelper.getItemStack(name, quantity));
	}

	public ThermalRecyclerRecipeBuilder input(final ItemStack stack, final int quantity) {
		Preconditions.checkNotNull(stack, "Input ItemStack cannot be null");
		final ItemStack item = stack.copy();
		item.stackSize = quantity;
		return input(item);
	}

	public ThermalRecyclerRecipeBuilder input(final ItemStack in) {

		Preconditions.checkNotNull(in, "Input ItemStack cannot be null");

		this.input = in;
		return this;
	}

	public ThermalRecyclerRecipeBuilder useRecipe() {
		useRecipe(this.input);
		return this;
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final Block block) {
		return useRecipe(new ItemStack(block));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final Block block, final int quantity) {
		return useRecipe(new ItemStack(block, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final Item item) {
		return useRecipe(new ItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final Item item, final int quantity) {
		return useRecipe(new ItemStack(item, 1, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final String item) {
		return useRecipe(ItemStackHelper.getItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final String item, final int quantity) {
		return useRecipe(ItemStackHelper.getItemStack(item, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final ItemStack stack) {

		try {
			final IRecipe recipe = RecipeDecomposition.findRecipe(stack);
			if(recipe != null) {
				input = recipe.getRecipeOutput();
				useRecipe(RecipeDecomposition.decompose(recipe));
			}
		} catch (Exception e) {
			;
		}

		return this;
	}
	
	public ThermalRecyclerRecipeBuilder useRecipe(final IRecipe recipe) {
		Preconditions.checkNotNull(recipe);
		input = recipe.getRecipeOutput();
		useRecipe(RecipeDecomposition.decompose(recipe));
		return this;
	}
	
	public ThermalRecyclerRecipeBuilder scrubOutput(final String item) {
		
		final ItemStack stack = ItemStackHelper.getItemStack(item);
		
		if(stack != null) {
			final List<ItemStack> newOutput = new ArrayList<ItemStack>();
			for(ItemStack i: output) {
				if(stack.isItemEqual(i)) {
					i.stackSize--;
					if(i.stackSize > 0)
						newOutput.add(i);
				} else {
					newOutput.add(i);
				}
			}
			
			output = newOutput;
		}
		return this;
	}

	public ThermalRecyclerRecipeBuilder useRecipe(final List<ItemStack> recipe) {
		if(recipe != null)
			output.addAll(recipe);
		return this;
	}

	public void save() {

		if (output == null || output.isEmpty()) {
			ModLog.debug(
					"No output parameters for [%s] - could have been scrubbed",
					ItemStackHelper.resolveName(input));
			return;
		}

		Preconditions.checkState(input != null,
				"Input ItemStack needs to be specified");

		try {

			final int result = RecipeData.put(input, output);
			if (result == RecipeData.FAILURE)
				ModLog.warn("Unable to save recipe [%s]", toString());
			else if (result == RecipeData.DUPLICATE)
				ModLog.debug("Duplicate recipe [%s]", toString());

		} catch (Exception e) {
			;
		}

		reset();
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder();
		builder.append(String.format("Thermal Recycler [%dx %s] => [",
				input.stackSize, ItemStackHelper.resolveName(input)));
		boolean sawOne = false;
		for (final ItemStack stack : output) {
			if (sawOne)
				builder.append(", ");
			else
				sawOne = true;
			builder.append(String.format("%dx %s", stack.stackSize,
					ItemStackHelper.resolveName(stack)));
		}
		builder.append("]");

		return builder.toString();
	}
}
