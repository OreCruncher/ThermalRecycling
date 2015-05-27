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

package org.blockartistry.mod.ThermalRecycling.items;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;

public enum CoreType {
	
	// Order is important!  Should match up with the alignment
	// in the ProcessingCore item.
	DECOMPOSITION,
	EXTRACTION,
	
	// This should always be last
	NONE;
	
	
	/**
	 * Indicates whether the given core can process the ItemStack in question.
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean canCoreProcess(final CoreType core, final ItemStack stack) {

		if (core == NONE || core == DECOMPOSITION)
			return ScrappingTables.canBeScrapped(stack);

		if (core == EXTRACTION)
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
	public static boolean canCoreProcess(final ItemStack core, final ItemStack stack) {
		
		if (!isProcessingCore(core)) {
			return false;
		}

		return canCoreProcess(core == null ? NONE : values()[core.getItemDamage()],
				stack);
	}

	/**
	 * Indicates whether the ItemStack represents a processing core.
	 * 
	 * @param core
	 * @return
	 */
	public static boolean isProcessingCore(final ItemStack core) {
		return core != null && core.getItem() == ItemManager.processingCore;
	}

	public static CoreType getType(final ItemStack core) {
		if(core == null || core.getItem() != ItemManager.processingCore) {
			return NONE;
		}
		return core.getItemDamage() == 0 ? DECOMPOSITION : EXTRACTION;
	}
}
