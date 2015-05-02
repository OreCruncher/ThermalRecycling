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

import java.util.Arrays;
import java.util.List;

public final class MyUtils {

	/**
	 * Concatinates two arrays together returning the result in a new
	 * array.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**
	 * Compresses the array by removing intervening nulls by arranging
	 * array elements.  It then truncates the array returning a new
	 * array with just those elements.
	 * 
	 * @param list
	 * @return
	 */
	public static <T> T[] compress(T[] list) {
		
		if(list.length < 2)
			return list;
		
		// Null slot is a trailing index that points
		// to a null slot.  Once i finds a null slot
		// there will always be a gap so it just
		// a matter of continuing to increment
		// nullSlot.
		int nullSlot = -1;
		for(int i = 0; i < list.length; i++) {
			if(nullSlot == -1 && list[i] == null) {
				nullSlot = i;
			}
			else if(nullSlot != -1 & list[i] != null) {
				list[nullSlot++] = list[i];
			}
		}
		
		// If nullSlot is still -1 it means there are
		// no nulls.
		if(nullSlot == -1)
			return list;
		
		// If it is not -1 then it contains the index of
		// the last null slot, or the length of the array
		// up to the null.
		return Arrays.copyOf(list, nullSlot);
	}
	
	/**
	 * Appends a standard array to a List of the given type.
	 * 
	 * @param list
	 * @param array
	 */
	public static <T> void concat(List<T> list, T[] array) {
		for(T t : array)
			list.add(t);
	}
	
	public static <T> T[] cast(Object[] list, Class<? extends T[]> clazz) {
		return Arrays.copyOf(list, list.length, clazz);
	}
}
