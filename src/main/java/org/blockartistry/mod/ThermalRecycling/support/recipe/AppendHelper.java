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

package org.blockartistry.mod.ThermalRecycling.support.recipe;

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Optional;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class AppendHelper {

	private AppendHelper() {
	}

	private static List<ItemStack> getItemStackRange(final String name, final int startSubtype, final int endSubtype,
			final int quantity) {

		return getItemStackRange(ItemStackHelper.getItemStack(name).get().getItem(), startSubtype, endSubtype, quantity);
	}

	private static List<ItemStack> getItemStackRange(final Item item, final int start, final int end,
			final int quantity) {

		final ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = start; i <= end; i++) {
			result.add(new ItemStack(item, quantity, i));
		}

		return result;
	}

	private static List<ItemStack> getItemStackRange(final Block block, final int start, final int end,
			final int quantity) {

		final ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = start; i <= end; i++) {
			result.add(new ItemStack(block, quantity, i));
		}

		return result;
	}

	public static void append(final List<ItemStack> list, final ItemStack stack) {

		assert list != null;
		assert stack != null;

		list.add(stack);
	}

	public static void append(final List<ItemStack> list, final String... items) {

		assert list != null;
		assert items != null && items.length > 0;

		for (final String s : items) {
			final Optional<ItemStack> stack = ItemStackHelper.getItemStack(s);
			if(stack.isPresent())
				list.add(stack.get());
		}
	}

	public static void append(final List<ItemStack> list, final String item, final int quantity) {

		assert list != null;
		assert item != null;
		assert quantity > 0;

		final Optional<ItemStack> stack = ItemStackHelper.getItemStack(item, quantity);
		if(stack.isPresent())
			list.add(stack.get());
	}

	public static void append(final List<ItemStack> list, final Block... blocks) {

		assert list != null;
		assert blocks != null && blocks.length > 0;

		for (final Block b : blocks)
			list.add(new ItemStack(b));
	}

	public static void append(final List<ItemStack> list, final Block block, final int quantity) {

		assert list != null;
		assert block != null;
		assert quantity > 0;

		list.add(new ItemStack(block, quantity));
	}

	public static void append(final List<ItemStack> list, final Item... items) {

		assert list != null;
		assert items != null && items.length > 0;

		for (final Item i : items)
			list.add(new ItemStack(i));
	}

	public static void append(final List<ItemStack> list, final Item item, final int quantity) {

		assert list != null;
		assert item != null;
		assert quantity > 0;

		list.add(new ItemStack(item, quantity));
	}

	public static void append(final List<ItemStack> list, final List<ItemStack> stacks) {

		assert list != null;
		assert stacks != null && !stacks.isEmpty();

		list.addAll(stacks);
	}

	public static void append(final List<ItemStack> list, final ItemStack... stacks) {

		assert list != null;
		assert stacks != null && stacks.length > 0;

		for (final ItemStack stack : stacks) {
			list.add(stack);
		}
	}

	public static void appendSubtype(final List<ItemStack> list, final ItemStack stack, final int subtype) {

		assert list != null;
		assert stack != null;
		assert subtype >= 0;

		final ItemStack s = stack.copy();
		s.setItemDamage(subtype);
		list.add(s);
	}

	public static void appendSubtype(final List<ItemStack> list, final Item item, final int subtype) {

		assert list != null;
		assert item != null;
		assert subtype >= 0;

		list.add(new ItemStack(item, 1, subtype));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final String item, final int start, final int end,
			final int quantity) {

		assert list != null;
		assert item != null;
		assert start >= 0 && end >= start;
		assert quantity > 0;

		list.addAll(getItemStackRange(item, start, end, quantity));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final String item, final int start,
			final int end) {

		assert list != null;
		assert item != null;
		assert start >= 0 && end >= start;

		list.addAll(getItemStackRange(item, start, end, 1));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final Item item, final int start, final int end,
			final int quantity) {

		assert list != null;
		assert item != null;
		assert start >= 0 && end >= start;
		assert quantity > 0;

		list.addAll(getItemStackRange(item, start, end, quantity));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final Item item, final int start, final int end) {

		assert list != null;
		assert item != null;
		assert start >= 0 && end >= start;

		list.addAll(getItemStackRange(item, start, end, 1));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final Block block, final int start, final int end,
			final int quantity) {

		assert list != null;
		assert block != null;
		assert start >= 0 && end >= start;
		assert quantity > 0;

		list.addAll(getItemStackRange(block, start, end, quantity));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final Block block, final int start,
			final int end) {

		assert list != null;
		assert block != null;
		assert start >= 0 && end >= start;

		list.addAll(getItemStackRange(block, start, end, 1));
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final ItemStack stack, final int start,
			final int end) {
		appendSubtypeRange(list, stack, start, end, 1);
	}

	public static void appendSubtypeRange(final List<ItemStack> list, final ItemStack stack, final int start,
			final int end, final int quantity) {

		assert list != null;
		assert stack != null;
		assert start >= 0 && end >= start;
		assert quantity > 0;

		for (int i = start; i <= end; i++) {
			final ItemStack s = stack.copy();
			s.setItemDamage(i);
			s.stackSize = quantity;
			list.add(s);
		}
	}
}
