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
import java.util.ArrayList;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import cofh.lib.util.WeightedRandomItemStack;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public final class ScrapingTables {

	static final ItemStack keep = new ItemStack(Blocks.dirt);
	static final ItemStack dust = new ItemStack(Blocks.cobblestone);
	static final ItemStack poorScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.POOR);
	static final ItemStack standardScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD);
	static final ItemStack superiorScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR);

	static final ArrayList<ItemStackWeightTable> componentScrap = new ArrayList<ItemStackWeightTable>();

	static boolean keepIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(keep);
	}

	static boolean dustIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(dust);
	}

	static int totalWeight(ArrayList<WeightedRandomItemStack> t) {
		int totalWeight = 0;
		for(WeightedRandomItemStack w: t)
			totalWeight += w.itemWeight;
		return totalWeight;
	}
	
	static {
		
		// The "NONE" scrap value. This is used when breaking a recipe down and
		// this item is one of the components.
		ItemStackWeightTable t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 480));
		t.add(t.new ItemStackItem(keep, 80));
		t.add(t.new ItemStackItem(dust, 80));
		t.add(t.new ItemStackItem(poorScrap, 144));
		t.add(t.new ItemStackItem(standardScrap, 16));
		componentScrap.add(t);

		// The "NONE" must scrap table. No scrap to be had.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 1));
		componentScrap.add(t);

		// The "POOR" scrap table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 480));
		t.add(t.new ItemStackItem(keep, 80));
		t.add(t.new ItemStackItem(dust, 80));
		t.add(t.new ItemStackItem(poorScrap, 120));
		t.add(t.new ItemStackItem(standardScrap, 40));
		componentScrap.add(t);

		// The "POOR" scrap MUST table.
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 480));
		t.add(t.new ItemStackItem(poorScrap, 198));
		t.add(t.new ItemStackItem(standardScrap, 120));
		t.add(t.new ItemStackItem(superiorScrap, 2));
		componentScrap.add(t);

		// The "STANDARD" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 220));
		t.add(t.new ItemStackItem(keep, 185));
		t.add(t.new ItemStackItem(dust, 185));
		t.add(t.new ItemStackItem(poorScrap, 105));
		t.add(t.new ItemStackItem(standardScrap, 85));
		t.add(t.new ItemStackItem(superiorScrap, 20));
		componentScrap.add(t);

		// The "STANDARD" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(null, 200));
		t.add(t.new ItemStackItem(poorScrap, 75));
		t.add(t.new ItemStackItem(standardScrap, 200));
		t.add(t.new ItemStackItem(superiorScrap, 75));
		componentScrap.add(t);

		// The "SUPERIOR" scrap table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(keep, 185));
		t.add(t.new ItemStackItem(dust, 185));
		t.add(t.new ItemStackItem(poorScrap, 75));
		t.add(t.new ItemStackItem(standardScrap, 115));
		t.add(t.new ItemStackItem(superiorScrap, 140));
		componentScrap.add(t);

		// The "SUPERIOR" scrap MUST table
		t = new ItemStackWeightTable();
		t.add(t.new ItemStackItem(poorScrap, 15));
		t.add(t.new ItemStackItem(standardScrap, 75));
		t.add(t.new ItemStackItem(superiorScrap, 200));
		componentScrap.add(t);
	}

	protected static ItemStackWeightTable getTable(ItemStack stack, boolean mustScrap) {
		
		// What's this item worth. We adjust based on whether the
		// item needs to be turned into scrap so we get the right
		// table.
		int scrappingValue = ItemInfo.get(stack).getScrapValue().ordinal() * 2;
		if (mustScrap)
			scrappingValue++;

		return componentScrap.get(scrappingValue);
	}
	
	public static ItemStack[] getScrapPossibilities(ItemStack stack) {
		
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		ItemStackWeightTable t = getTable(stack, true);

		for(ItemStackItem w: t.getEntries()) {
			
			ItemStack temp = w.getStack();
			if(temp != null) {
				
				double percent = w.itemWeight * 100F / t.getTotalWeight();
				ItemStackHelper.setItemLore(temp, String.format("%-1.2f%% chance", percent));
				result.add(temp);
			}
		}
		
		return result.toArray(new ItemStack[result.size()]);
	}
	
	public static ItemStack[] scrapItems(ItemStack stack, boolean mustScrap) {
		if(stack == null || stack.stackSize < 1)
			return null;
		
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		ItemStackWeightTable t = getTable(stack, mustScrap);
		
		for(int i = 0; i < stack.stackSize; i++) {
			ItemStack cupieDoll = t.nextStack();
			
			if(cupieDoll != null) {
				ItemStack item = stack.copy();
				item.stackSize = 1;
				
				// Post process and return
				if (keepIt(cupieDoll)) {
					result.add(item);
				} else if(dustIt(cupieDoll)) {
					result.add(ItemStackHelper.convertToDustIfPossible(item));
				} else {
					result.add(cupieDoll);
				}
			}
		}

		return result.toArray(new ItemStack[result.size()]);
	}

	public static boolean canBeScrapped(ItemStack stack) {

		return !(ItemHelper.isBlock(stack) || ItemHelper.isDust(stack)
				|| ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack)
				|| ItemHelper.isOre(stack));
	}

	public static void writeDiagnostic(Writer writer) throws Exception {
		
		writer.write("Scrapping Tables:\n");
		writer.write("=================================================================\n");

		componentScrap.get(0).diagnostic("NONE", writer);
		componentScrap.get(1).diagnostic("NONE Must", writer);
		componentScrap.get(2).diagnostic("POOR", writer);
		componentScrap.get(3).diagnostic("POOR Must", writer);
		componentScrap.get(4).diagnostic("STANDARD", writer);
		componentScrap.get(5).diagnostic("STANDARD Must", writer);
		componentScrap.get(6).diagnostic("SUPERIOR", writer);
		componentScrap.get(7).diagnostic("SUPERIOR Must", writer);

		writer.write("=================================================================\n");
	}
}