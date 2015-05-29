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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class WeightTable<T extends WeightTable.Item> {

	static final Random random = new Random();

	protected final List<T> items = new ArrayList<T>();
	protected final Random rand;
	protected int totalWeight = 0;

	public abstract static class Item implements Cloneable {

		public final int itemWeight;
		protected Random rnd;

		public Item(final int weight) {
			this.itemWeight = weight;
		}
		
		@Override
		public Object clone() throws CloneNotSupportedException {
			return super.clone();
		}
	}

	public WeightTable() {
		this(null);
	}

	public WeightTable(final Random rand) {
		if (rand != null)
			this.rand = rand;
		else
			this.rand = random;
	}

	public void add(final T entry) {
		totalWeight += entry.itemWeight;
		entry.rnd = rand;
		items.add(entry);
	}

	public void remove(final T entry) {
		if(items.remove(entry))
			totalWeight -= entry.itemWeight;
	}

	public T next() {

		int targetWeight = rand.nextInt(totalWeight);

		int i = 0;
		for (i = items.size(); (targetWeight -= items.get(i - 1).itemWeight) > 0; i--)
			;

		return items.get(i - 1);
	}

	public List<T> getEntries() {
		return Collections.unmodifiableList(items);
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public void diagnostic(final String title, final Writer writer) throws IOException {

		writer.write(String.format("\nWeight table [%s] (total weight %d):\n",
				title, totalWeight));
		writer.write("==========================================================\n");
		for (final Item i : items)
			writer.write(String.format("%5.2f%% (%4d) %s\n",
					(double) i.itemWeight * 100F / totalWeight, i.itemWeight,
					i.toString()));
	}
}
