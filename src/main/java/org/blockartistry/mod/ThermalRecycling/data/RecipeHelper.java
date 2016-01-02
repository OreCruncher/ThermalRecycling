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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.data.registry.ExtractionData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.data.registry.RecipeData;
import org.blockartistry.mod.ThermalRecycling.util.InventoryHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;

import net.minecraft.item.ItemStack;

public final class RecipeHelper {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int DUPLICATE = 2;

	private RecipeHelper() {}

	/**
	 * Adds the given recipe to the tracking tables. It assumes control over
	 * output.
	 */
	public static int put(final ItemStack input, List<ItemStack> output) {
		assert input != null;
		assert output != null;

		int retCode = DUPLICATE;

		// See if we have an existing mapping
		final RecipeData result = ItemRegistry.getRecipe(input);

		// Use the incoming recipe if:
		// * It doesn't exist
		// * Existing entry is wildcard and the new one isn't
		// * The new entry has a quantity greater than the existing one
		if (result == RecipeData.EPHEMERAL || (result.isGeneric() && !OreDictionaryHelper.isGeneric(input))
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

					if (OreDictionaryHelper.isGeneric(working)) {
						final String oreName = OreDictionaryHelper.getOreName(working);

						if (oreName != null) {
							final Optional<ItemStack> t = ItemStackHelper.getItemStack(oreName, working.stackSize);
							if (t.isPresent())
								output.set(i, t.get());
						}
					}
				}

				output = ImmutableList.copyOf(InventoryHelper.coelece(output));
			}
			
			ItemRegistry.setRecipe(stack, new RecipeData(stack, output));

			retCode = SUCCESS;
		}

		return retCode;
	}
	
	public static void put(final ItemStack input, final ItemStackWeightTable table) {
		ItemRegistry.setExtractionData(input, new ExtractionData(input, table));
	}

}
