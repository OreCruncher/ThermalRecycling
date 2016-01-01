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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Special key object for using ItemStacks as keys in various collection key
 * sets.
 *
 */
public final class ItemStackKey {
	
	private final static int TERM = 3079;
	
	// The cached key is used by the various framework routines where a temporary
	// key is generated just to index an internal table.  It's thread local so
	// there should be no collision.  They key should not be cached or used in
	// an index - unpredictable results will occur.
	private static final ThreadLocal<ItemStackKey> cachedKey = new ThreadLocal<ItemStackKey>() {
        @Override
		protected ItemStackKey initialValue() {
            return new ItemStackKey();
        }
	};
	
	public static ItemStackKey getCachedKey(final ItemStack stack) {
		final ItemStackKey key = cachedKey.get();
		key.item = stack.getItem();
		key.meta = ItemStackHelper.getItemDamage(stack);//stack.getHasSubtypes() ? ItemStackHelper.getItemDamage(stack) : 0;
		key.hash = calculateHash(key.item.hashCode(), key.meta);
		return key;
	}

	public static ItemStackKey getCachedKey(final Item item) {
		final ItemStackKey key = cachedKey.get();
		key.item = item;
		key.meta = item.getHasSubtypes() ? OreDictionaryHelper.WILDCARD_VALUE : 0;
		key.hash = calculateHash(item.hashCode(), key.meta);
		return key;
	}

	private Item item;
	private int meta;
	private int hash;

	// Modified Bernstein
	// http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
	private static int calculateHash(int id, int meta) {
		return id ^ (meta * TERM);
		/*
		int working = meta << 24 | id;
		int hash = 0;
		for (int i = 0; i < 4; i++) {
			hash = (33 * hash) ^ (working & 0xFF);
			working >>>= 8;
		}
		return hash;
		*/
	}

	private ItemStackKey() {
		this.item = null;
		this.meta = 0;
		this.hash = 0;
	}
	
	public ItemStackKey(final Item item, final int meta) {
		this.item = item;
		this.meta = meta;
		this.hash = calculateHash(item.hashCode(), meta);
	}

	public ItemStackKey(final ItemStack stack) {
		this(stack.getItem(), ItemStackHelper.getItemDamage(stack));
	}

	@Override
	public int hashCode() {
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		final ItemStackKey key = (ItemStackKey) obj;
		return this.item == key.item && this.meta == key.meta;
	}

	@Override
	public String toString() {
		return String.format("%s:%d", item.toString(), meta);
	}
}
