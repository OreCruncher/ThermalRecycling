/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

import java.util.Comparator;

import net.minecraft.item.ItemStack;

public final class MyComparators {

	public static final Comparator<ItemStack> itemStackAscending = new Comparator<ItemStack>() {

		@Override
		public int compare(ItemStack o1, ItemStack o2) {

			int result = o1.getItem().hashCode() - o2.getItem().hashCode();
			if (result == 0)
				result = o1.getItemDamage() - o2.getItemDamage();
/*
			if (result == 0 && o2.stackTagCompound != o1.stackTagCompound) {
				if (o2.stackTagCompound == null && o1.stackTagCompound != null)
					result = -1;
				else if (o2.stackTagCompound != null
						&& o1.stackTagCompound == null)
					result = 1;
				else if (o2.stackTagCompound.equals(o1.stackTagCompound)) {
					result = 0;
				} else {
					// There not equal - not sure how to prioritize. In this
					// case, pick a direction
					// to be consistent
					result = -1;
				}
			}
*/
			return result;
		}

	};

	public static final Comparator<ItemStack> itemStackDescending = new Comparator<ItemStack>() {

		@Override
		public int compare(ItemStack o1, ItemStack o2) {

			int result = o2.getItem().hashCode() - o1.getItem().hashCode();
			if (result == 0)
				result = o2.getItemDamage() - o1.getItemDamage();
/*
			if (result == 0 && o2.stackTagCompound != o1.stackTagCompound) {
				if (o2.stackTagCompound == null && o1.stackTagCompound != null)
					result = 1;
				else if (o2.stackTagCompound != null
						&& o1.stackTagCompound == null)
					result = -1;
				else if (o2.stackTagCompound.equals(o1.stackTagCompound))
					result = 0;
				else {
					// There not equal - not sure how to prioritize. In this
					// case, pick a direction
					// to be consistent
					result = 1;
				}
			}
*/
			return result;
		}
	};
}
