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

package org.blockartistry.mod.ThermalRecycling.util.function;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

public final class Apply {
	
	private Apply() {}
	
	public static <T> Iterable<T> forEach(final Iterable<T> iterable, final Predicate<T> pred) {
		for(final T e: iterable)
			pred.apply(e);
		return iterable;
	}
	
	public static <T,O> Iterable<T> forEach(final Iterable<T> iterable, final Function<T,O> func) {
		for(final T e: iterable)
			func.apply(e);
		return iterable;
	}
	
	public static <T> void forEach(final T[] list, final Predicate<T> pred) {
		for(final T e: list)
			pred.apply(e);
	}

	public static <T,O> void forEach(final T[] list, final Function<T,O> func) {
		for(final T e: list)
			func.apply(e);
	}
}
