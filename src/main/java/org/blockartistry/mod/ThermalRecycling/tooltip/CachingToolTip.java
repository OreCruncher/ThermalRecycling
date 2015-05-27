package org.blockartistry.mod.ThermalRecycling.tooltip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.util.function.MultiFunction;

public abstract class CachingToolTip implements MultiFunction<List<String>, ItemStack, Void> {

	private Item lastItem;
	private int lastMeta;
	private List<String> cachedLore = Collections.emptyList();

	public abstract void addToToolTip(final List<String> output, final ItemStack stack);
	
	@Override
	public Void apply(final List<String> output, final ItemStack stack) {
		
		final Item item = stack.getItem();
		final int meta = stack.getItemDamage();
		if(lastItem == item && lastMeta == meta) {
			output.addAll(cachedLore);
			return null;
		}
		
		lastItem = item;
		lastMeta = meta;
		cachedLore = new ArrayList<String>();
		
		addToToolTip(cachedLore, stack);
		output.addAll(cachedLore);
		
		return null;
	}

}
