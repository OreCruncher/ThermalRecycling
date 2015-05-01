/*
 * This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.ThermalRecycling.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;

public class ItemScrapData {
	
	Item item;
	ScrapValue value;
	boolean ignoreRecipe;
	boolean scrubFromOutput;
	boolean isFood;

	public ItemScrapData(Item item) {
		this(item, ScrapValue.STANDARD, false, false);
	}
	
	public ItemScrapData(Item item, ScrapValue value, boolean ignoreRecipe, boolean scrubFromOutput) {
		this.item = item;
		this.value = value;
		this.ignoreRecipe = ignoreRecipe;
		this.scrubFromOutput = scrubFromOutput;
		this.isFood = item instanceof ItemFood;
	}
	
	public ItemScrapData setValue(ScrapValue value) {
		this.value = value;
		return this;
	}
	
	public ScrapValue getScrapValue() {
		return value;
	}
	
	public ItemScrapData setIgnoreRecipe(boolean flag) {
		this.ignoreRecipe = flag;
		return this;
	}
	
	public boolean getIgnoreRecipe() {
		return ignoreRecipe;
	}
	
	public ItemScrapData setScrubFromOutput(boolean flag) {
		this.scrubFromOutput = flag;
		return this;
	}
	
	public boolean isScrubbedFromOutput() {
		return scrubFromOutput;
	}
	
	public boolean isFood() {
		return isFood;
	}
	
	@Override
	public String toString() {
		
		return String.format("%s [scrap value: %s; ignoreRecipe: %s; scrubFromOutput: %s; isFood: %s]",
				item.toString(), value.name(), Boolean.toString(ignoreRecipe), Boolean.toString(scrubFromOutput), Boolean.toString(isFood));
	}
}