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
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Preconditions;

import cofh.lib.util.helpers.InventoryHelper;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * An instance of this class is used to track recipes for the Thermal
 * Recycler.
 *
 */
public class RecipeData {
	
	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int DUPLICATE = 2;

	static final HashMap<ItemStack, RecipeData> recipes = new HashMap<ItemStack, RecipeData>();
	
	ItemStack inputStack;
	ItemStack[] outputStacks;
	boolean preserveOutput;
	boolean isBlacklisted;
	
	public RecipeData(ItemStack input, ItemStack... output) {
		this(input, false, false, output);
	}
	
	public RecipeData(ItemStack input, boolean preserveOutput, ItemStack... output) {
		this(input, preserveOutput, false, output);
	}

	public RecipeData(ItemStack input, boolean preserveOutput, boolean isBlacklisted, ItemStack... output) {
		Preconditions.checkNotNull(input);
		
		this.inputStack = input;
		this.preserveOutput = preserveOutput;
		this.isBlacklisted = isBlacklisted;
		this.outputStacks = output;
	}
	
	public ItemStack getInput() {
		return this.inputStack;
	}
	
	public int getMinimumInputStacksRequired() {
		return this.inputStack.stackSize;
	}

	public ItemStack[] getOutput() {
		return this.outputStacks;
	}
	
	public RecipeData setOutput(ItemStack... output) {
		this.outputStacks = output;
		return this;
	}
	
	public boolean getPreserveOutput() {
		return this.preserveOutput;
	}
	
	public RecipeData setPreserveOutput(boolean flag) {
		this.preserveOutput = flag;
		return this;
	}

	public boolean getIsBlacklisted() {
		return this.isBlacklisted;
	}
	
	public RecipeData setIsBlacklisted(boolean flag) {
		this.isBlacklisted = flag;
		return this;
	}

	public static RecipeData get(ItemStack input) {

		RecipeData match = null;
		RecipeData fuzzyMatch = null;

		for (Entry<ItemStack, RecipeData> e : recipes.entrySet()) {

			if (ItemHelper.itemsEqualWithMetadata(input, e.getKey())) {
				match = e.getValue();
				break;
			} else if (e.getKey().getItemDamage() == OreDictionary.WILDCARD_VALUE
					&& ItemHelper.itemsEqualWithoutMetadata(input, e.getKey())) {
				fuzzyMatch = e.getValue();
			}
		}
		
		if(match == null)
			match = fuzzyMatch;

		return match;
	}
	
	public static ItemStack[] getRecipe(ItemStack input) {
		RecipeData data = get(input);
		return data == null ? null : InventoryHelper.cloneInventory(data.getOutput());
	}
	
	public static int put(ItemStack input, ItemStack[] output) {

		int retCode = DUPLICATE;

		// See if we have an existing mapping
		RecipeData result = get(input);

		// If we don't, or the mapping that exists is a wild card and the
		// incoming
		// recipe is specific, we want to add to the dictionary. The dictionary
		// will prefer specific recipes over wild cards if possible.
		if (result == null
				|| (result.getInput().getItemDamage() == OreDictionary.WILDCARD_VALUE && input
						.getItemDamage() != OreDictionary.WILDCARD_VALUE)) {

			// Clone the inventory - don't want to work with the originals
			ItemStack[] workingSet = InventoryHelper.cloneInventory(output);

			// Traverse the list replacing WILDCARD stacks with concrete ones.
			// The logic prefers Thermal Foundation equivalents if found.
			for (int i = 0; i < workingSet.length; i++) {

				if (workingSet[i].getItemDamage() == OreDictionary.WILDCARD_VALUE) {
					String oreName = ItemHelper.getOreName(workingSet[i]);

					if (oreName != null) {
						workingSet[i] = ItemStackHelper.getItemStack(oreName,
								workingSet[i].stackSize);
					}
				}
			}

			// We do this to compact the output set. Callers may have
			// duplicate items in the recipe list because of how they
			// handle recipes.
			ItemStack cpy = input.copy();
			workingSet = ItemStackHelper.compact(workingSet);
			recipes.put(cpy, new RecipeData(cpy, ItemStackHelper.shrink(workingSet)));

			retCode = SUCCESS;
		}

		return retCode;
	}

	public static void blackList(int quantity,
			boolean wildcard, Block... blocks) {

		int subType = wildcard ? OreDictionary.WILDCARD_VALUE : 0; 
		for (Block b : blocks)
			blackList(new ItemStack(b, 1, subType));
	}

	public static void blackList(int quantity,
			boolean wildcard, Item... items) {

		int subType = wildcard ? OreDictionary.WILDCARD_VALUE : 0; 
		for (Item i : items)
			blackList(new ItemStack(i, 1, subType));
	}

	public static void blackList(ItemStack item) {
		RecipeData data = get(item);
		if(data == null) {
			data = new RecipeData(item, true, true, (ItemStack[])null);
			recipes.put(item.copy(), data);
		}
	}

	public static int getMinimumQuantityToRecycle(ItemStack item) {

		RecipeData recipe = get(item);
		return recipe == null ? 1 : recipe.getMinimumInputStacksRequired();
	}

	public static boolean isBlackList(ItemStack item) {
		RecipeData data = get(item);
		return data == null ? false : data.getIsBlacklisted();
	}
	
	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append(String.format("[%dx %s] => [",
				inputStack.stackSize, ItemStackHelper.resolveName(inputStack)));

		if(outputStacks == null || outputStacks.length == 0) {
			builder.append("none");
		} else {
			boolean sawOne = false;
			
			for (ItemStack stack : outputStacks) {
				if (sawOne)
					builder.append(", ");
				else
					sawOne = true;
				builder.append(String.format("%dx %s", stack.stackSize,
						ItemStackHelper.resolveName(stack)));
			}
		}
		builder.append("]");
		builder.append(String.format(" | preserve output %s | blacklist %s", Boolean.toString(preserveOutput), Boolean.toString(isBlacklisted)));

		return builder.toString();
	}
	
	public static void writeDiagnostic(Writer writer) throws Exception {
		
		writer.write("Item Info:\n");
		writer.write("=================================================================\n");
		for(RecipeData d: recipes.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}

}
