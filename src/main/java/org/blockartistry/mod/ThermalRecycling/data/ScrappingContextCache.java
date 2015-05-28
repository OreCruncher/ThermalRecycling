package org.blockartistry.mod.ThermalRecycling.data;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler.ScrappingContext;
import org.blockartistry.mod.ThermalRecycling.util.LRUCache;

public final class ScrappingContextCache {
	
	private class LRUCacheKey {
		
		public final ItemStack core;
		public final ItemStack stack;
		
		public LRUCacheKey(final ItemStack core, final ItemStack stack) {
			this.core = core;
			this.stack = stack;
		}
		
		@Override
		public int hashCode() {
			return 0;
		}
		
		@Override
		public boolean equals(Object o) {
			final LRUCacheKey key = (LRUCacheKey)o;
			return ItemStack.areItemStacksEqual(this.stack, key.stack) && ItemStack.areItemStacksEqual(this.core, key.core);
		}
	}
	
	private final LRUCache<LRUCacheKey, ScrappingContext> cache;
	
	public ScrappingContextCache(int entries) {
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
