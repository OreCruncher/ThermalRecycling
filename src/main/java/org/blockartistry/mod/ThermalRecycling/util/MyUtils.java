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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

public final class MyUtils {

	private MyUtils() {
	}

	public static boolean contains(final int[] list, final int entity) {
		for (final int e : list)
			if (e == entity)
				return true;
		return false;
	}

	public static int[] split(String split, String list) throws Exception {

		String[] tokens = list.split(split);
		if (tokens == null || tokens.length == 0)
			return new int[] {};

		int[] result = new int[tokens.length];
		for (int i = 0; i < tokens.length; i++) {
			Integer v = Integer.parseInt(tokens[i]);
			result[i] = v;
		}

		return result;
	}

	public static List<ItemStack> clone(final ItemStack... stacks) {
		final ArrayList<ItemStack> result = new ArrayList<ItemStack>(stacks.length);
		for (final ItemStack stack : stacks)
			if (stack != null)
				result.add(stack.copy());
		return result;
	}

	public static List<ItemStack> clone(final List<ItemStack> stacks) {
		final ArrayList<ItemStack> result = new ArrayList<ItemStack>(stacks.size());
		for (final ItemStack stack : stacks)
			if (stack != null)
				result.add(stack.copy());
		return result;
	}

}
