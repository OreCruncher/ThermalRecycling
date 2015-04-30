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
		projection = new ItemStack[list.size()];
		for (int i = 0; i < projection.length; i++)
			projection[i] = list.get(i);
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return new MyIterator<ItemStack>(projection);
	}
}