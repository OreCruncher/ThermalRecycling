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

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class InventoryHelper {
	
	private final static Set<Object> NULL_SET = Collections.singleton(null);
	
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
	
	/**
	 * Compresses the inventory list by consolidating stacks toward the
	 * beginning of the array. This operation occurs in place meaning the return
	 * array is the original one passed in.
	 * 
	 * @param inv
	 * @return
	 */
	public static void coelece(final ItemStack[] inv, final int startSlot, final int endSlot) {

		assert inv != null;
		assert startSlot >= 0 && endSlot >= startSlot;
		assert startSlot < inv.length;

		for (int i = startSlot + 1; i <= endSlot; i++) {

			final ItemStack stack = inv[i];
			if (stack != null) {

				for (int j = startSlot; j < i; j++) {

					final ItemStack target = inv[j];
					if (target == null) {
						inv[j] = stack;
						inv[i] = null;
						break;
					} else if (ItemStackHelper.areEqual(stack, target)) {

						final int hold = target.getMaxStackSize() - target.stackSize;

						if (hold >= stack.stackSize) {
							target.stackSize += stack.stackSize;
							inv[i] = null;
							break;
						} else if (hold != 0) {
							stack.stackSize -= hold;
							target.stackSize += hold;
						}
					}
				}
			}
		}
	}

	/**
	 * Compresses the inventory list by consolidating stacks toward the
	 * beginning of the array. This operation occurs in place meaning the return
	 * array is the original one passed in.
	 * 
	 * @param inv
	 * @return
	 */
	public static List<ItemStack> coelece(final List<ItemStack> inv) {

		if (inv == null) {
			return inv;
		}

		for (int i = 1; i < inv.size(); i++) {

			final ItemStack stack = inv.get(i);
			if (stack != null) {

				for (int j = 0; j < i; j++) {

					final ItemStack target = inv.get(j);
					if (target == null) {
						inv.set(j, stack);
						inv.set(i, null);
						break;
					} else if (ItemStackHelper.areEqual(stack, target)) {

						final int hold = target.getMaxStackSize() - target.stackSize;

						if (hold >= stack.stackSize) {
							target.stackSize += stack.stackSize;
							inv.set(i, null);
							break;
						} else if (hold != 0) {
							stack.stackSize -= hold;
							target.stackSize += hold;
						}
					}
				}
			}
		}

		// Purge all null entries
		inv.removeAll(NULL_SET);

		return inv;
	}

	public static boolean addItemStackToInventory(final ItemStack inv[], final ItemStack stack, final int startSlot,
			final int endSlot) {

		if (stack == null || stack.stackSize == 0)
			return true;

		for (int slot = startSlot; slot <= endSlot; slot++) {
			ItemStack invStack = inv[slot];

			// Quick and easy - if the slot is empty its the target
			if (invStack == null) {
				inv[slot] = stack;
				return true;
			}

			// If the stack can fit into this slot do the merge
			final int remainingSpace = invStack.getMaxStackSize() - invStack.stackSize;
			if (remainingSpace > 0 && ItemStackHelper.areEqual(stack, invStack)) {

				if (remainingSpace >= stack.stackSize) {
					invStack.stackSize += stack.stackSize;
					return true;
				}

				stack.stackSize -= remainingSpace;
				invStack.stackSize += remainingSpace;
			}
		}

		return false;
	}

	public static boolean removeItemStackFromInventory(final ItemStack inv[], final ItemStack stack,
			final int startSlot, final int endSlot) {
		if (stack == null || stack.stackSize == 0)
			return true;

		for (int slot = startSlot; slot <= endSlot && stack.stackSize > 0; slot++) {
			final ItemStack invStack = inv[slot];
			if (invStack != null && ItemStackHelper.areEqual(invStack, stack)) {
				if (invStack.stackSize > stack.stackSize) {
					invStack.stackSize -= stack.stackSize;
					stack.stackSize = 0;
				} else {
					stack.stackSize -= invStack.stackSize;
					inv[slot] = null;
				}
			}
		}

		return stack.stackSize == 0;
	}

	/**
	 * Clears the target inventory of ItemStacks matching the provided Item and
	 * metadata values. Passing in WILDCARD_VALUE for metadata will match all
	 * subtypes for the provided Item.
	 * 
	 * @param inv
	 * @param item
	 * @param meta
	 * @return
	 */
	public static boolean clearInventory(final ItemStack[] inv, final Item item, final int meta) {

		assert inv != null;

		int cleared = 0;

		for (int i = 0; i < inv.length; i++) {

			final ItemStack stack = inv[i];

			if (stack != null && (item == null || stack.getItem() == item)
					&& (meta == OreDictionaryHelper.WILDCARD_VALUE || ItemStackHelper.getItemDamage(stack) == meta)) {
				cleared += stack.stackSize;
				inv[i] = null;
			}
		}

		return cleared != 0;
	}

	public static boolean clearInventory(final ItemStack[] inv, final ItemStack stack) {
		assert inv != null;
		assert stack != null;
		return clearInventory(inv, stack.getItem(), ItemStackHelper.getItemDamage(stack));
	}
}
