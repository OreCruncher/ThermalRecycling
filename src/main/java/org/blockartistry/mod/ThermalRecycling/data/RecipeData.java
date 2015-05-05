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

package org.blockartistry.mod.ThermalRecycling.data;

import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.MyComparators;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import com.google.common.base.Preconditions;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * An instance of this class is used to track recipes for the Thermal Recycler.
 *
 */
public class RecipeData {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int DUPLICATE = 2;

	static final TreeMap<ItemStack, RecipeData> recipes = new TreeMap<ItemStack, RecipeData>(
			MyComparators.itemStackAscending);

	ItemStack inputStack;
	List<ItemStack> outputStacks;
	boolean preserveOutput;

	public RecipeData(ItemStack input, List<ItemStack> output) {
		this(input, false, output);
	}

	public RecipeData(ItemStack input, boolean preserveOutput,
			List<ItemStack> output) {
		Preconditions.checkNotNull(input);

		this.inputStack = input;
		this.preserveOutput = preserveOutput;
		this.outputStacks = output;
	}

	public ItemStack getInput() {
		return this.inputStack;
	}

	public int getMinimumInputStacksRequired() {
		return this.inputStack.stackSize;
	}

	public List<ItemStack> getOutput() {
		return Collections.unmodifiableList(this.outputStacks);
	}

	public boolean getPreserveOutput() {
		return this.preserveOutput;
	}

	public RecipeData setPreserveOutput(boolean flag) {
		this.preserveOutput = flag;
		return this;
	}

	public static RecipeData get(ItemStack input) {

		RecipeData match = recipes.get(input);

		if (match == null
				&& input.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
			ItemStack t = input.copy();
			t.setItemDamage(OreDictionary.WILDCARD_VALUE);
			match = recipes.get(t);
		}

		return match;
	}

	public static List<ItemStack> getRecipe(ItemStack input) {
		List<ItemStack> result = null;
		RecipeData data = get(input);
		if (data != null) {
			result = data.getOutput();
			if (result != null)
				result = ItemStackHelper.clone(result);
		}
		return result;
	}

	public static int put(ItemStack input, ItemStack[] output) {

		int retCode = DUPLICATE;

		// See if we have an existing mapping
		RecipeData result = get(input);

		// If we don't, or the mapping that exists is a wild card and the
		// incoming
		// recipe is specific, we want to add to the dictionary. The dictionary
		// will prefer specific recipes over wild cards if possible.
		if (result == null
				|| (result.getInput().getItemDamage() == OreDictionary.WILDCARD_VALUE && input
						.getItemDamage() != OreDictionary.WILDCARD_VALUE)) {

			List<ItemStack> workingSet = null;
			ItemStack stack = input.copy();

			if (output != null && output.length > 0) {

				// Clone the inventory - don't want to work with the originals
				workingSet = ItemStackHelper.clone(output);

				// Traverse the list replacing WILDCARD stacks with concrete
				// ones.
				// The logic prefers Thermal Foundation equivalents if found.
				for (int i = 0; i < workingSet.size(); i++) {

					ItemStack working = workingSet.get(i);

					if (working.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
						String oreName = ItemHelper.getOreName(working);

						if (oreName != null) {
							working = ItemStackHelper.getItemStack(oreName,
									working.stackSize);
							if (working != null)
								workingSet.set(i, working);
						}
					}
				}

				// We do this to compact the output set. Callers may have
				// duplicate items in the recipe list because of how they
				// handle recipes.
				workingSet = MyUtils.compress(ItemStackHelper
						.coelece(workingSet));
			}

			recipes.put(stack, new RecipeData(stack, workingSet));

			retCode = SUCCESS;
		}

		return retCode;
	}

	public static int getMinimumQuantityToRecycle(ItemStack item) {

		RecipeData recipe = get(item);
		return recipe == null ? 1 : recipe.getMinimumInputStacksRequired();
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("[%dx %s] => [", inputStack.stackSize,
				ItemStackHelper.resolveName(inputStack)));

		if (outputStacks == null || outputStacks.size() == 0) {
			builder.append("none");
		} else {
			boolean sawOne = false;

			for (ItemStack stack : outputStacks) {
				if (sawOne)
					builder.append(", ");
				else
					sawOne = true;
				builder.append(String.format("%dx %s", stack.stackSize,
						ItemStackHelper.resolveName(stack)));
			}
		}
		builder.append("]");
		builder.append(String.format(" | preserve output %s",
				Boolean.toString(preserveOutput)));

		return builder.toString();
	}

	public static void writeDiagnostic(Writer writer) throws Exception {

		writer.write("\nKnown Thermal Recycler Recipes:\n");
		writer.write("=================================================================\n");
		for (RecipeData d : recipes.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}

}
