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
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.ItemInfo;
import org.blockartistry.mod.ThermalRecycling.data.ScrapingTables;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackRange;

import com.google.common.base.Preconditions;

public class ThermalRecyclerRecipeBuilder {

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

	public ThermalRecyclerRecipeBuilder append(String... items) {
		ItemStackHelper.append(output, items);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(String item, int quantity) {
		ItemStackHelper.append(output, item, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(Block... blocks) {
		ItemStackHelper.append(output, blocks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(Block block, int quantity) {
		ItemStackHelper.append(output, block, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(Item... items) {
		ItemStackHelper.append(output, items);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(Item item, int quantity) {
		ItemStackHelper.append(output, item, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(List<ItemStack> stacks) {
		ItemStackHelper.append(output, stacks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder append(ItemStack... stacks) {
		ItemStackHelper.append(output, stacks);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtype(ItemStack stack,
			int subtype) {
		ItemStackHelper.appendSubtype(output, stack, subtype);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtype(Item item, int subtype) {
		ItemStackHelper.appendSubtype(output, item, subtype);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(String item,
			int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(output, item, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(String item,
			int start, int end) {
		ItemStackHelper.appendSubtypeRange(output, item, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(Item item,
			int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(output, item, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(Item item,
			int start, int end) {
		ItemStackHelper.appendSubtypeRange(output, item, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(Block block,
			int start, int end, int quantity) {
		ItemStackHelper.appendSubtypeRange(output, block, start, end, quantity);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(Block block,
			int start, int end) {
		ItemStackHelper.appendSubtypeRange(output, block, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder appendSubtypeRange(ItemStack stack,
			int start, int end) {
		ItemStackHelper.appendSubtypeRange(output, stack, start, end);
		return this;
	}

	public ThermalRecyclerRecipeBuilder input(Block block) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");

		return input(new ItemStack(block));
	}

	public ThermalRecyclerRecipeBuilder input(Item item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return input(new ItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder input(String item) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");

		return input(ItemStackHelper.getItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder input(ItemStack in) {

		Preconditions.checkNotNull(in, "Input ItemStack cannot be null");

		this.input = in;
		return this;
	}

	public ThermalRecyclerRecipeBuilder useRecipe() {
		useRecipe(this.input);
		return this;
	}

	public ThermalRecyclerRecipeBuilder useRecipe(Block block) {
		return useRecipe(new ItemStack(block));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(Block block, int quantity) {
		return useRecipe(new ItemStack(block, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(Item item) {
		return useRecipe(new ItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(Item item, int quantity) {
		return useRecipe(new ItemStack(item, 1, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(String item) {
		return useRecipe(ItemStackHelper.getItemStack(item));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(String item, int quantity) {
		return useRecipe(ItemStackHelper.getItemStack(item, quantity));
	}

	public ThermalRecyclerRecipeBuilder useRecipe(ItemStack stack) {
		this.input(stack);

		try {
			IRecipe recipe = RecipeDecomposition.findRecipe(stack);
			if (recipe != null)
				for (ItemStack item : new RecipeDecomposition(recipe))
					if(!ItemInfo.isScrubbedFromOutput(item))
						output.add(item);
		}
		catch(Exception e) {
			;
		}
		
		return this;
	}

	public void save() {

		if (output == null || output.isEmpty()|| input == null)
		{
			ModLog.warn("Parameters not correct for [%s]", ItemStackHelper.resolveName(input));
			return;
		}

		Preconditions.checkState(input != null,
				"Input ItemStack needs to be specified");

		try {

			int result = ScrapingTables.addThermalRecyclerRecipe(input,
					output.toArray(new ItemStack[output.size()]));
			
			if (result == ScrapingTables.SUCCESS && ModOptions.getEnableRecipeLogging())
				ModLog.info(toString());
			
		} catch (Exception e) {
			;
		}

		reset();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("Thermal Recycler [%dx %s] => [",
				input.stackSize, ItemStackHelper.resolveName(input)));
		boolean sawOne = false;
		for (ItemStack stack : output) {
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

	public static void applyRecipeRange(String item, int begin, int end) {
		ThermalRecyclerRecipeBuilder builder = new ThermalRecyclerRecipeBuilder();
		for (ItemStack stack : new ItemStackRange(item, begin, end))
			builder.useRecipe(stack).save();
	}

}
