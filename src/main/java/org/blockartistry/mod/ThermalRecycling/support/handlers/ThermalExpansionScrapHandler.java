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

package org.blockartistry.mod.ThermalRecycling.support.handlers;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ThermalExpansionScrapHandler extends GenericHandler {

	protected static final ItemStack frame = ItemStackHelper
			.getItemStack("ThermalExpansion:Frame");
	protected static final ItemStack secure = ItemStackHelper
			.getItemStack("ThermalExpansion:material:16");
	protected static final ItemStack signalumNuggets = ItemStackHelper
			.getItemStack("nuggetSignalum", 3);

	@Override
	protected List<ItemStack> getRecipeOutput(ItemStack stack) {
		List<ItemStack> result = super.getRecipeOutput(stack);

		if (result != null && stack.hasTagCompound()) {

			// Crack the machine NBT to find out it's level
			NBTTagCompound nbt = stack.getTagCompound();
			int level = nbt.getInteger("Level");

			// Search the recipe and replace the machine frame
			// with an appropriate one for the level
			if (level > 0) {
				for (int i = 0; i < result.size(); i++) {
					ItemStack item = result.get(i);
					if (item.isItemEqual(frame)) {
						item.setItemDamage(level);
						break;
					}
				}
			}

			if (nbt.getInteger("Secure") > 0) {
				result.add(secure.copy());
				result.add(signalumNuggets.copy());
			}
		}

		return result;
	}
}
