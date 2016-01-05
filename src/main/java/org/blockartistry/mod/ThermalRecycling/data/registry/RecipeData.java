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

package org.blockartistry.mod.ThermalRecycling.data.registry;

import java.util.List;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;

/**
 * An instance of this class is used to track recipes for the Thermal Recycler.
 *
 */
public final class RecipeData {

	// Used as a generic "blank" for items that do not have
	// recipes registered.
	public static final RecipeData EPHEMERAL = new RecipeData();

	private final String name;
	private final int quantityRequired;
	private final boolean isGeneric;
	private List<ItemStack> outputStacks;

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
		this.isGeneric = OreDictionaryHelper.isGeneric(input);
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
				builder.append(ItemStackHelper.resolveName(stack));
				if(stack.stackSize > 1) {
					builder.append("(x");
					builder.append(stack.stackSize);
					builder.append(')');
				}
			}
		}
		builder.append(']');

		return builder.toString();
	}
}
