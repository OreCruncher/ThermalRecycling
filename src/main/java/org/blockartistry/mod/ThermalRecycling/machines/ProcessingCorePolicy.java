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

package org.blockartistry.mod.ThermalRecycling.machines;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.items.ProcessingCore;

import net.minecraft.item.ItemStack;

public final class ProcessingCorePolicy {

	public static final int CORE_NONE = -1;
	public static final int CORE_DECOMPOSITION = ProcessingCore.DECOMPOSITION;
	public static final int CORE_EXTRACTION = ProcessingCore.EXTRACTION;

	/**
	 * Indicates whether the given core can process the ItemStack in question.
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean canCoreProcess(int core, ItemStack stack) {

		if (core == CORE_NONE || core == CORE_DECOMPOSITION)
			return ScrappingTables.canBeScrapped(stack);

		if (core == CORE_EXTRACTION)
			return stack.getItem() == ItemManager.recyclingScrap
					|| stack.getItem() == ItemManager.recyclingScrapBox;

		return false;
	}

	/**
	 * Indicates whether the given core can process the ItemStack in question.
	 * If the core is not a processing core, the routine returns false.
	 * 
	 * @param core
	 * @param stack
	 * @return
	 */
	public static boolean canCoreProcess(ItemStack core, ItemStack stack) {
		if (core != null && !isProcessingCore(core))
			return false;

		return canCoreProcess(core == null ? CORE_NONE : core.getItemDamage(),
				stack);
	}

	/**
	 * Indicates whether the ItemStack represents a processing core.
	 * 
	 * @param core
	 * @return
	 */
	public static boolean isProcessingCore(ItemStack core) {
		return core != null && core.getItem() == ItemManager.processingCore;
	}

	public static boolean isDecompositionCore(ItemStack core) {
		return isProcessingCore(core)
				&& core.getItemDamage() == CORE_DECOMPOSITION;
	}

	public static boolean isExtractionCore(ItemStack core) {
		return isProcessingCore(core)
				&& core.getItemDamage() == CORE_EXTRACTION;
	}
	
	/**
	 * Obtains the item level information stored in NBT.
	 * 
	 * @param item
	 * @return
	 */
	public static int getItemLevel(ItemStack item) {
		
		// For now only decomp cores have levels
		if(!isDecompositionCore(item))
			return 0;
		
		int level = 0;
		if(item.hasTagCompound()) {
			level = item.getTagCompound().getInteger("Level");
		}
		
		if(level < 0) {
			ModLog.warn("Processing core with invalid level set: %d", level);
			level = 0;
		}
		
		if(level > 3) {
			ModLog.warn("Processing core with invalid level set: %d", level);
			level = 3;
		}
		
		return level;
	}
}
