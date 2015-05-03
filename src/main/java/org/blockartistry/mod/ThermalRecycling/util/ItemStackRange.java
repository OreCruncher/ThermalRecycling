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

import java.util.Iterator;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemStackRange implements Iterable<ItemStack> {

	public class MyIterator<T> implements Iterator<T> {

		ItemStack[] list;
		int index;

		public MyIterator(ItemStack[] list) {
			this.list = list;
			this.index = 0;
		}

		@Override
		public boolean hasNext() {
			return list != null && index < list.length;
		}

		@Override
		@SuppressWarnings("unchecked")
		public T next() {
			return (T) list[index++];
		}

		@Override
		public void remove() {
			// implement... if supported.
		}
	}

	ItemStack[] projection;

	public ItemStackRange(String item, int begin, int end) {
		this(ItemStackHelper.getItemStackRange(item, begin, end, 1));
	}

	public ItemStackRange(String item, int begin, int end, int quantity) {
		this(ItemStackHelper.getItemStackRange(item, begin, end, quantity));
	}

	public ItemStackRange(ItemStack stack, int begin, int end) {
		this(stack, begin, end, 1);
	}

	public ItemStackRange(ItemStack stack, int begin, int end, int quantity) {
		projection = new ItemStack[end - begin];
		int index = 0;
		for (int i = begin; i <= end; i++) {
			ItemStack t = stack.copy();
			t.stackSize = quantity;
			t.setItemDamage(i);
			projection[index++] = t;
		}
	}

	public ItemStackRange(Item item, int begin, int end) {
		this(ItemStackHelper.getItemStackRange(item, begin, end, 1));
	}

	public ItemStackRange(Item item, int begin, int end, int quantity) {
		this(ItemStackHelper.getItemStackRange(item, begin, end, quantity));
	}

	public ItemStackRange(Block block, int begin, int end) {
		this(ItemStackHelper.getItemStackRange(block, begin, end, 1));
	}

	public ItemStackRange(Block block, int begin, int end, int quantity) {
		this(ItemStackHelper.getItemStackRange(block, begin, end, quantity));
	}

	public ItemStackRange(ItemStack... stack) {
		projection = stack;
	}

	public ItemStackRange(List<ItemStack> list) {
		projection = list.toArray(new ItemStack[list.size()]);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return new MyIterator<ItemStack>(projection);
	}
}