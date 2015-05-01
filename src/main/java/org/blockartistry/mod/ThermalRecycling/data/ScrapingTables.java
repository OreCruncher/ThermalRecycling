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
import java.util.HashMap;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cofh.lib.util.WeightedRandomItemStack;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.WeightedRandom;

public final class ScrapingTables {

	static final ItemStack keep = new ItemStack(Blocks.dirt);
	static final ItemStack dust = new ItemStack(Blocks.cobblestone);

	static final ArrayList<ArrayList<WeightedRandomItemStack>> componentScrap = new ArrayList<ArrayList<WeightedRandomItemStack>>();

	static final ArrayList<WeightedRandomItemStack> loots = new ArrayList<WeightedRandomItemStack>();
	static final HashMap<ItemStack, ItemStack[]> thermalRecipes = new HashMap<ItemStack, ItemStack[]>();
	static final ArrayList<ItemStack> thermalBlacklist = new ArrayList<ItemStack>();
	static final Random rnd = new Random();
	static boolean inited = false;

	protected static void dumpList(Writer writer, String name,
			ArrayList<WeightedRandomItemStack> list) throws Exception {

		int weight = 0;
		for (WeightedRandomItemStack e : list)
			weight += e.itemWeight;

		writer.write("\n");
		writer.write(String.format("Loot table [%s] (total weight %d):\n", name, weight));
		for (WeightedRandomItemStack e : list)
			writer.write(String.format("%s (%d) - %f%%\n", ItemStackHelper.resolveName(e.getStack()), e.itemWeight, ((float) e.itemWeight * 100) / (weight)));
		writer.write("\n");
	}

	static boolean keepIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(keep);
	}

	static boolean dustIt(ItemStack stack) {
		return stack != null && stack.isItemEqual(dust);
	}

	public static void initialize() {
		if (inited)
			return;

		inited = true;

		// The "NONE" scrap value. This is used when breaking a recipe down and
		// this item is one of the components.
		ArrayList<WeightedRandomItemStack> t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 480));
		t.add(new WeightedRandomItemStack(keep, 80));
		t.add(new WeightedRandomItemStack(dust, 80));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 144));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 16));
		componentScrap.add(t);

		// The "NONE" must scrap table. No scrap to be had.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 1));
		componentScrap.add(t);

		// The "POOR" scrap table.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 480));
		t.add(new WeightedRandomItemStack(keep, 80));
		t.add(new WeightedRandomItemStack(dust, 80));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 120));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 40));
		componentScrap.add(t);

		// The "POOR" scrap MUST table.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 480));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 198));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 120));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 2));
		componentScrap.add(t);

		// The "STANDARD" scrap table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 220));
		t.add(new WeightedRandomItemStack(keep, 185));
		t.add(new WeightedRandomItemStack(dust, 185));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 105));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 85));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 20));
		componentScrap.add(t);

		// The "STANDARD" scrap MUST table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 200));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 75));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 200));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 75));
		componentScrap.add(t);

		// The "SUPERIOR" scrap table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(keep, 185));
		t.add(new WeightedRandomItemStack(dust, 185));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 75));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 115));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 140));
		componentScrap.add(t);

		// The "SUPERIOR" scrap MUST table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 15));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 75));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 200));
		componentScrap.add(t);
	}

	protected static ArrayList<WeightedRandomItemStack> getTable(ItemStack stack, boolean mustScrap) {
		
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
		ArrayList<WeightedRandomItemStack> t = getTable(stack, true);
		
		int totalWeight = 0;
		for(WeightedRandomItemStack w: t)
			totalWeight += w.itemWeight;
		
		for(WeightedRandomItemStack w: t) {
			ItemStack temp = w.getStack();
			if(temp != null) {
				double percent = (double)(w.itemWeight * 100F) / (double)totalWeight;
				
				NBTTagCompound nbt = temp.getTagCompound();
				if (nbt == null)
					nbt = new NBTTagCompound();

				NBTTagList lore = new NBTTagList();
				lore.appendTag(new NBTTagString(String.format("%-1.2f%% chance", percent)));
				NBTTagCompound display = new NBTTagCompound();
				display.setTag("Lore", lore);

				nbt.setTag("display", display);
				temp.setTagCompound(nbt);

				result.add(temp);
			}
		}
		
		return result.toArray(new ItemStack[result.size()]);
	}
	
	public static ItemStack scrapItem(ItemStack stack) {
		return scrapItem(stack, false);
	}

	public static ItemStack scrapItem(ItemStack stack, boolean mustScrap) {

		if (stack == null)
			return null;

		ArrayList<WeightedRandomItemStack> t = getTable(stack, mustScrap);
		ItemStack cupieDoll = pickStackFrom(t);

		// Post process and return
		if (keepIt(cupieDoll))
			return stack;

		if (dustIt(cupieDoll))
			return ItemStackHelper.convertToDustIfPossible(stack);

		return cupieDoll;
	}

	public static boolean canBeScrapped(ItemStack stack) {

		if (ItemHelper.isBlock(stack) || ItemHelper.isDust(stack)
				|| ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack)
				|| ItemHelper.isOre(stack))
			return false;

		return !RecipeData.isBlackList(stack);
	}

	protected static ItemStack pickStackFrom(
			ArrayList<WeightedRandomItemStack> table) {
		WeightedRandom.Item item = WeightedRandom.getRandomItem(new Random(),
				table);
		return ((WeightedRandomItemStack) item).getStack();
	}

	public static void writeDiagnostic(Writer writer) throws Exception {
		
		writer.write("Scrapping Tables:\n");
		writer.write("=================================================================\n");
		dumpList(writer, "NONE", componentScrap.get(0));
		dumpList(writer, "NONE Must", componentScrap.get(1));
		dumpList(writer, "POOR", componentScrap.get(2));
		dumpList(writer, "POOR Must", componentScrap.get(3));
		dumpList(writer, "STANDARD", componentScrap.get(4));
		dumpList(writer, "STANDARD Must", componentScrap.get(5));
		dumpList(writer, "SUPERIOR", componentScrap.get(4));
		dumpList(writer, "SUPERIOR Must", componentScrap.get(5));
		writer.write("=================================================================\n");
	}
}