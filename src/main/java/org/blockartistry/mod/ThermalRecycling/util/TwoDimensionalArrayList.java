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

@SuppressWarnings("serial")
public class TwoDimensionalArrayList<T> extends ArrayList<ArrayList<T>> {
	
	public void setElement(int index1, int index2, T element) {
		while (index1 >= this.size()) {
			this.add(new ArrayList<T>());
		}

		ArrayList<T> inner = this.get(index1);
		while (index2 >= inner.size()) {
			inner.add(null);
		}

		inner.set(index2, element);
	}
	
	public T get(int index1, int index2) {
		return this.get(index1).get(index2);
	}
	
	public void setElementSegment(int index, ArrayList<T> newSegment) {
		while (index >= this.size()) {
			this.add(new ArrayList<T>());
		}
		this.set(index, newSegment);
	}
	
	public ArrayList<T> getElementSegment(int index) {
		while (index >= this.size()) {
			this.add(new ArrayList<T>());
		}
		return this.get(index);
	}
	
	public void appendElement(int index, T element) {
		while (index >= this.size()) {
			this.add(new ArrayList<T>());
		}
		this.get(index).add(element);
	}
}
