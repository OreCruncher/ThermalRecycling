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

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;

public class GenericHandler extends ScrapHandler {
	
	@Override
	public List<ItemStack> scrapItems(final ScrappingContext ctx) {
		
		// Initialize the context as needed
		initializeContext(ctx);
		
		final List<ItemStack> result = new ArrayList<ItemStack>();

		final int dataLength = ctx.recipeOutput.size();
		for (int i = 0; i < dataLength; i++) {

			final ItemStack target = ctx.recipeOutput.get(i);
			final ItemStackWeightTable t = ctx.tables[i];
			
			for (int count = 0; count < target.stackSize; count++) {

				ItemStack cupieDoll = t.nextStack();

				if (cupieDoll != null) {

					if (ScrappingTables.keepIt(cupieDoll)) {
						cupieDoll = target.copy();
						cupieDoll.stackSize = 1;
					} else if (ScrappingTables.dustIt(cupieDoll)) {
						cupieDoll = target.copy();
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
