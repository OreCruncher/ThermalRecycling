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

package org.blockartistry.mod.ThermalRecycling.data.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;

public class GenericHandler extends ScrapHandler {

	@Override
	public List<ItemStack> scrapItems(ItemStack core, ItemStack stack) {

		List<ItemStack> result = new ArrayList<ItemStack>();
		List<ItemStack> processingList = null;

		// If a decomp core is installed, get a list of outputs from the
		// recipes.
		// If an extraction core and the item is a scrap box, convert to scrap
		// with a bonus - 10 pieces total.
		if (ProcessingCorePolicy.isDecompositionCore(core)) {
			processingList = getRecipeOutput(stack);

			// If there isn't a recipe, process the item as if it were being
			// scrapped without any cores.
			if (processingList == null)
				core = null;
		} else if (ProcessingCorePolicy.isExtractionCore(core)
				&& stack.getItem() == ItemManager.recyclingScrapBox) {
			stack = new ItemStack(ItemManager.recyclingScrap, 10,
					stack.getItemDamage());
		}

		// If we get here just scrap the input
		if (processingList == null) {
			processingList = new ArrayList<ItemStack>();
			processingList.add(stack);
		}

		for (ItemStack item : processingList) {

			ItemStackWeightTable t = ScrappingTables.getTable(core, item);

			for (int count = 0; count < item.stackSize; count++) {

				ItemStack cupieDoll = t.nextStack();

				if (cupieDoll != null) {
					ItemStack temp = item.copy();
					temp.stackSize = 1;

					// Post process and return
					if (ScrappingTables.keepIt(cupieDoll)) {
						result.add(temp);
					} else if (ScrappingTables.dustIt(cupieDoll)) {
						result.add(ItemStackHelper
								.convertToDustIfPossible(temp));
					} else {
						result.add(cupieDoll);
					}
				}
			}
		}

		return result;
	}
}
