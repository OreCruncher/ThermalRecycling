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

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ExtractionData {
	
	private static final ExtractionData ephemeral = new ExtractionData();

	private static Map<ItemStackKey, ExtractionData> recipes = new HashMap<ItemStackKey, ExtractionData>();

	public static void freeze() {
		recipes = ImmutableMap.copyOf(recipes);
	}

	protected ExtractionData() {
		this.name = "<Ephemeral>";
		this.quantityRequired = 1;
		this.isGeneric = true;
		this.extraction = new ItemStackWeightTable();
		this.isDefault = true;
	}
	
	protected ExtractionData(final ItemStack input, final ItemStackWeightTable table) {
		assert input != null;
		assert table != null;

		this.name = ItemStackHelper.resolveName(input);
		this.quantityRequired = input.stackSize;
		this.isGeneric = input.getItemDamage() == OreDictionary.WILDCARD_VALUE;
		this.extraction = table;
		this.isDefault = false;
	}

	private final String name;
	private final int quantityRequired;
	private final boolean isGeneric;
	private ItemStackWeightTable extraction;
	private final boolean isDefault;
	
	public boolean isDefault() {
		return isDefault;
	}
	
	public boolean isGeneric() {
		return isGeneric;
	}
	
	public boolean hasOutput() {
		return !(extraction == null || extraction.getTotalWeight() == 0);
	}

	public int getMinimumInputQuantityRequired() {
		return quantityRequired;
	}

	public ItemStackWeightTable getOutput() {
		return this.extraction;
	}

	public static ExtractionData get(final ItemStack input) {
		ExtractionData match = recipes.get(ItemStackKey.getCachedKey(input));

		if (match == null && input.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
			match = recipes.get(ItemStackKey.getCachedKey(input.getItem()));
		}

		return match == null ? ephemeral : match;
	}
	
	public static void remove(final ItemStack stack) {
		recipes.remove(ItemStackKey.getCachedKey(stack));
	}
	
	public static void put(final ItemStack input, final ItemStackWeightTable table) {
		ExtractionData data = get(input);
		if(data == ephemeral) {
			data = new ExtractionData(input, table);
			recipes.put(new ItemStackKey(input), data);
		} else {
			data.extraction = table;
		}
	}
	
	@Override
	public String toString() {

		final StringBuilder builder = new StringBuilder(128);
		builder.append(String.format("[%dx %s] => [", quantityRequired, name));

		if (!hasOutput()) {
			builder.append("none");
		} else {
			
			boolean sawOne = false;

			for (final ItemStackItem stack : extraction.getEntries()) {
				if (sawOne)
					builder.append(", ");
				else
					sawOne = true;
				builder.append(String.format("%d %s",stack.itemWeight, stack.toString()));
			}
		}
		builder.append(']');

		return builder.toString();
	}

	public static void writeDiagnostic(final Writer writer) throws Exception {

		writer.write("\nKnown Thermal Recycler Extraction Recipes:\n");
		writer.write("=================================================================\n");
		for (final ExtractionData d : recipes.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}
}
