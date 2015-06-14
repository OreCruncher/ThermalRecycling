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

package org.blockartistry.mod.ThermalRecycling.util;

import net.minecraft.item.ItemStack;

public final class InventoryHelper {
	
	private InventoryHelper() { }

	/**
	 * Checks the provided inventory and slot range as to whether there is enough material equivalent
	 * to the two sample ItemStacks.
	 * 
	 * @param inv
	 * @param startSlot
	 * @param endSlot
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public static boolean doesInventoryContain(final ItemStack[] inv, final int startSlot, final int endSlot, final ItemStack stack1, final ItemStack stack2) {
		
		if (stack1 == null && stack2 == null)
			return true;

		if (ItemStackHelper.areEqual(stack1, stack2)) {
			int count = stack1.stackSize + stack2.stackSize;
			for (int i = startSlot; i <= endSlot && count > 0; i++) {
				final ItemStack item = inv[i];
				if (ItemStackHelper.areEqual(stack1, item)) {
					count -= item.stackSize;
				}
			}

			return count < 1;

		} else {

			// Two different types of stacks. Have to handle nulls.
			int count1 = stack1 != null ? stack1.stackSize : 0;
			int count2 = stack2 != null ? stack2.stackSize : 0;
			for (int i = startSlot; i <= endSlot && (count1 > 0 || count2 > 0); i++) {
				final ItemStack item = inv[i];
				if (item == null)
					continue;

				if (ItemStackHelper.areEqual(item, stack1)) {
					count1 -= item.stackSize;
				} else if (ItemStackHelper.areEqual(item, stack2)) {
					count2 -= item.stackSize;
				}
			}

			return count1 < 1 && count2 < 1;
		}

	}
	
	/**
	 * Checks whether the specified inventory has enough space to accept the two sample
	 * ItemStacks.
	 * 
	 * @param inv
	 * @param slotStart
	 * @param slotEnd
	 * @param stack1
	 * @param stack2
	 * @return
	 */
	public static boolean canInventoryAccept(final ItemStack[] inv, final int slotStart, final int slotEnd, final ItemStack stack1, final ItemStack stack2) {

		if (stack1 == null && stack2 == null)
			return true;

		if (ItemStackHelper.areEqual(stack1, stack2)) {
			int count = stack1.stackSize + stack2.stackSize;
			for (int i = slotStart; i <= slotEnd && count > 0; i++) {
				final ItemStack item = inv[i];
				if (item == null) {
					count -= stack1.getMaxStackSize();
				} else if (ItemStackHelper.areEqual(stack1, item)) {
					count -= item.getMaxStackSize() - item.stackSize;
				}
			}

			return count < 1;

		} else {

			// Two different types of stacks. Have to handle nulls.
			int count1 = stack1 != null ? stack1.stackSize : 0;
			int count2 = stack2 != null ? stack2.stackSize : 0;
			for (int i = slotStart; i <= slotEnd && (count1 > 0 || count2 > 0); i++) {
				final ItemStack item = inv[i];
				if (item == null) {
					if (count1 > 0) {
						count1 -= stack1.getMaxStackSize();
					} else if (count2 > 0) {
						count2 -= stack2.getMaxStackSize();
					}
				} else if (ItemStackHelper.areEqual(item, stack1)) {
					count1 -= item.getMaxStackSize() - item.stackSize;
				} else if (ItemStackHelper.areEqual(item, stack2)) {
					count2 -= item.getMaxStackSize() - item.stackSize;
				}
			}

			return count1 < 1 && count2 < 1;
		}
	}
	
}
