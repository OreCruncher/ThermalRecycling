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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;

/**
 * An instance of this class is used to track recipes for the Thermal Recycler.
 *
 */
public final class RecipeData {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int DUPLICATE = 2;

	// Used as a generic "blank" for items that do not have
	// recipes registered.
	private static final RecipeData ephemeral = new RecipeData();

	private static Map<ItemStackKey, RecipeData> recipes = new HashMap<ItemStackKey, RecipeData>(1024);

	public static void freeze() {
		recipes = ImmutableMap.copyOf(recipes);
	}

	private final String name;
	private final int quantityRequired;
	private final boolean isGeneric;
	private final List<ItemStack> outputStacks;

	/**
	 * Special CTOR for creating recipes for items that do not have any recipes
	 * cached.
	 */
	protected RecipeData() {
		this.name = "<Ephemeral>";
		this.quantityRequired = 1;
		this.isGeneric = true;
		this.outputStacks = ImmutableList.of();
	}

	public RecipeData(final ItemStack input, final List<ItemStack> output) {
		assert input != null;
		assert output != null;

		this.name = ItemStackHelper.resolveName(input);
		this.quantityRequired = input.stackSize;
		this.isGeneric = ItemStackHelper.isWildcard(input);
		this.outputStacks = (output instanceof ImmutableList) ? output : ImmutableList.copyOf(output);
	}

	public boolean isGeneric() {
		return isGeneric;
	}

	public boolean hasOutput() {
		return !this.outputStacks.isEmpty();
	}

	public int getMinimumInputQuantityRequired() {
		return quantityRequired;
	}

	public List<ItemStack> getOutput() {
		return this.outputStacks;
	}

	public boolean isVanillaOutput() {

		if (hasOutput()) {

			for (final ItemStack stack : getOutput()) {
				if (!ItemStackHelper.isVanilla(stack))
					return false;
			}

			return true;
		}

		return false;
	}

	public static RecipeData get(final ItemStack input) {

		RecipeData match = recipes.get(ItemStackKey.getCachedKey(input));

		if (match == null && !ItemStackHelper.isWildcard(input)) {
			match = recipes.get(ItemStackKey.getCachedKey(input.getItem()));
		}

		return match == null ? ephemeral : match;
	}

	public static void remove(final ItemStack stack) {
		recipes.remove(ItemStackKey.getCachedKey(stack));
	}

	/**
	 * Adds the given recipe to the tracking tables. It assumes control over
	 * output.
	 */
	public static int put(final ItemStack input, List<ItemStack> output) {
		assert input != null;
		assert output != null;

		int retCode = DUPLICATE;

		// See if we have an existing mapping
		final RecipeData result = get(input);

		// Use the incoming recipe if:
		// * It doesn't exist
		// * Existing entry is wildcard and the new one isn't
		// * The new entry has a quantity greater than the existing one
		if (result == ephemeral || (result.isGeneric() && !ItemStackHelper.isWildcard(input))
				|| (input.stackSize > result.getMinimumInputQuantityRequired())) {

			final ItemStack stack = input.copy();

			// An immutable list has already been processed by
			// something like RecipeDecomposition
			if (!(output instanceof ImmutableList)) {
				// Traverse the list replacing WILDCARD stacks with concrete
				// ones. The logic prefers Thermal Foundation equivalents
				// if found.
				for (int i = 0; i < output.size(); i++) {

					ItemStack working = output.get(i);

					if (ItemStackHelper.isWildcard(working)) {
						final String oreName = ItemStackHelper.getOreName(working);

						if (oreName != null) {
							working = ItemStackHelper.getItemStack(oreName, working.stackSize);
							if (working != null)
								output.set(i, working);
						}
					}
				}

				output = ImmutableList.copyOf(MyUtils.coelece(output));
			}

			recipes.put(new ItemStackKey(stack), new RecipeData(stack, output));

			retCode = SUCCESS;
		}

		return retCode;
	}

	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder(128);
		builder.append(String.format("[%dx %s] => [", quantityRequired, name));

		if (!hasOutput()) {
			builder.append("none");
		} else {
			boolean sawOne = false;

			for (final ItemStack stack : outputStacks) {
				if (sawOne)
					builder.append(", ");
				else
					sawOne = true;
				builder.append(String.format("%dx %s", stack.stackSize, ItemStackHelper.resolveName(stack)));
			}
		}
		builder.append(']');

		return builder.toString();
	}

	public static void writeDiagnostic(final Writer writer) throws Exception {

		writer.write("\nKnown Thermal Recycler Decomp Recipes:\n");
		writer.write("=================================================================\n");
		for (final RecipeData d : recipes.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}
}
