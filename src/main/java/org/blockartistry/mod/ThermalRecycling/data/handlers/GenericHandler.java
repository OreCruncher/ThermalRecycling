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
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;

public class GenericHandler extends ScrapHandler {

	@Override
	public List<ItemStack> scrapItems(final ItemStack core, final ItemStack stack) {

		CoreType coreType = CoreType.getType(core);
		
		final List<ItemStack> result = new ArrayList<ItemStack>();
		List<ItemStack> processingList = null;

		// If a decomp core is installed, get a list of outputs from the
		// recipes. If an extraction core and the item is a scrap box,
		// convert to scrap with a bonus.
		if (coreType == CoreType.DECOMPOSITION) {
			processingList = getRecipeOutput(stack);

			// If there isn't a recipe, process the item as if it were being
			// scrapped without any cores.
			if (processingList.isEmpty()) {
				processingList = Collections.singletonList(stack);
				coreType = CoreType.NONE;
			}
			
		} else if (coreType == CoreType.EXTRACTION && stack.getItem() == ItemManager.recyclingScrapBox) {
			processingList = Collections.singletonList(new ItemStack(ItemManager.recyclingScrap,
					8 + ModOptions.getScrapBoxBonus(), stack.getItemDamage()));
		} else {
			processingList = Collections.singletonList(stack);
		}

		final ItemLevel coreLevel = coreType != CoreType.NONE ? ItemLevel.getLevel(core) : ItemLevel.BASIC;
		
		for (final ItemStack item : processingList) {

			final ScrapValue sv = ItemData.get(item).getScrapValue();
			final ItemStackWeightTable t = ScrappingTables.getTable(coreType, coreLevel, sv).get();
			
			for (int count = 0; count < item.stackSize; count++) {

				ItemStack cupieDoll = t.nextStack();

				if (cupieDoll != null) {

					if (ScrappingTables.keepIt(cupieDoll)) {
						cupieDoll = item.copy();
						cupieDoll.stackSize = 1;
					} else if (ScrappingTables.dustIt(cupieDoll)) {
						cupieDoll = item.copy();
						cupieDoll.stackSize = 1;
						cupieDoll = ItemStackHelper.convertToDustIfPossible(cupieDoll);
					}
					
					// Maybe be null in the destroy case
					if(cupieDoll != null) {
						result.add(cupieDoll);
					}
				}
			}
		}

		return result;
	}
}
