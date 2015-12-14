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

package org.blockartistry.mod.ThermalRecycling.data;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler.ScrappingContext;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.LRUCache;

public final class ScrappingContextCache {
	
	private static class LRUCacheKey {
		
		public final ItemStack core;
		public final ItemStack stack;
		
		public LRUCacheKey(final ItemStack core, final ItemStack stack) {
			this.core = core;
			this.stack = stack;
		}
		
		@Override
		public int hashCode() {
			// Need to improve this if the size of the cache
			// gets significant.  This code essentially sorts
			// everything to a single bucket.
			return 0;
		}
		
		@Override
		public boolean equals(final Object o) {
			final LRUCacheKey key = (LRUCacheKey)o;
			return ItemStackHelper.areEqual(this.stack, key.stack) && ItemStackHelper.areEqual(this.core, key.core);
		}
	}
	
	private final LRUCache<LRUCacheKey, ScrappingContext> cache;
	
	public ScrappingContextCache(final int entries) {
		this.cache = new LRUCache<LRUCacheKey, ScrappingContext>(entries);
	}
	
	public ScrappingContext getContext(final ItemStack core, final ItemStack stack) {
		final LRUCacheKey key = new LRUCacheKey(core, stack);
		ScrappingContext ctx = cache.get(key);
		if(ctx == null) {
			ctx = new ScrappingContext(core, stack);
			cache.put(key, ctx);
		}
		
		return ctx;
	}
}
