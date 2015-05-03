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

public class WeightTable<T extends WeightTable<T>.Item> {
	
	static final Random random = new Random();
	
	private ArrayList<T> items = new ArrayList<T>();
	private Random rand = random;
	private int totalWeight = 0;
	
	public abstract class Item {
		
		public final int itemWeight;
		protected final Random rnd;
		
		public Item(int weight) {
			this.itemWeight = weight;
			this.rnd = rand;
		}
	}
	
	public WeightTable() {
		this(null);
	}
	
	public WeightTable(Random rand) {
		if(rand != null)
			this.rand = rand;
	}

	public void add(T entry) {
		totalWeight += entry.itemWeight;
		items.add(entry);
	}
	
	public T next() {
		
		int targetWeight = rand.nextInt(totalWeight);

		int i = 0;
		for(i = items.size() ; (targetWeight -= items.get(i-1).itemWeight) > 0; i--);

		return items.get(i-1);
	}
	
	public List<T> getEntries() {
		return Collections.unmodifiableList(items);
	}
	
	public int getTotalWeight() {
		return totalWeight;
	}
	
	public void diagnostic(String title, Writer writer) throws IOException {
		
		writer.write(String.format("Weight table [%s] (total weight %d):\n", title, totalWeight));
		writer.write("==========================================================\n");
		for(Item i: items)
			writer.write(String.format("%s - %-1.2f%%\n", i.toString(), (double)i.itemWeight * 100F / totalWeight));
		writer.write("==========================================================\n");
	}
}
