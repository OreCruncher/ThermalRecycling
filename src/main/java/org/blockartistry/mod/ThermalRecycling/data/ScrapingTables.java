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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.Tuple;

import cofh.lib.util.WeightedRandomItemStack;
import cofh.lib.util.helpers.InventoryHelper;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.oredict.OreDictionary;

public final class ScrapingTables {

	public static final int SUCCESS = 0;
	public static final int FAILURE = 1;
	public static final int DUPLICATE = 2;

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
		t.add(new WeightedRandomItemStack(null, 50));
		t.add(new WeightedRandomItemStack(keep, 40));
		t.add(new WeightedRandomItemStack(dust, 40));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 5));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 20));
		componentScrap.add(t);

		// The "NONE" must scrap table. No scrap to be had.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 1));
		componentScrap.add(t);

		// The "POOR" scrap table.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(keep, 175));
		t.add(new WeightedRandomItemStack(dust, 175));
		t.add(new WeightedRandomItemStack(null, 250));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 1));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 25));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 60));
		componentScrap.add(t);

		// The "POOR" scrap MUST table.
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 250));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 10));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 30));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 60));
		componentScrap.add(t);

		// The "STANDARD" scrap table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(keep, 175));
		t.add(new WeightedRandomItemStack(dust, 175));
		t.add(new WeightedRandomItemStack(null, 75));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 2));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 38));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 89));
		componentScrap.add(t);

		// The "STANDARD" scrap MUST table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(null, 75));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 20));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 60));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 20));
		componentScrap.add(t);

		// The "SUPERIOR" scrap table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(keep, 175));
		t.add(new WeightedRandomItemStack(dust, 175));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 2));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 60));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 89));
		componentScrap.add(t);

		// The "SUPERIOR" scrap MUST table
		t = new ArrayList<WeightedRandomItemStack>();
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 60));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 30));
		t.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 10));
		componentScrap.add(t);
	}

	public static ItemStack scrapItem(ItemStack stack) {
		return scrapItem(stack, false);
	}

	public static ItemStack scrapItem(ItemStack stack, boolean mustScrap) {

		if (stack == null)
			return null;

		// What's this item worth. We adjust based on whether the
		// item needs to be turned into scrap so we get the right
		// table.
		int scrappingValue = ItemInfo.get(stack).getScrapValue().ordinal() * 2;
		if (mustScrap)
			scrappingValue++;

		ArrayList<WeightedRandomItemStack> t = componentScrap
				.get(scrappingValue);

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

		return !isThermalRecyclerBlacklisted(stack);
	}

	protected static ItemStack pickStackFrom(
			ArrayList<WeightedRandomItemStack> table) {
		WeightedRandom.Item item = WeightedRandom.getRandomItem(new Random(),
				table);
		return ((WeightedRandomItemStack) item).getStack();
	}

	public static int addThermalRecyclerRecipe(ItemStack input,
			ItemStack[] output) {

		int retCode = DUPLICATE;

		// See if we have an existing mapping
		Tuple<ItemStack, ItemStack[]> result = _getResultStacks(input);

		// If we don't, or the mapping that exists is a wild card and the
		// incoming
		// recipe is specific, we want to add to the dictionary. The dictionary
		// will prefer specific recipes over wild cards if possible.
		if (result == null
				|| (result.x.getItemDamage() == OreDictionary.WILDCARD_VALUE && input
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
			workingSet = ItemStackHelper.compact(workingSet);

			thermalRecipes
					.put(input.copy(), ItemStackHelper.shrink(workingSet));

			retCode = SUCCESS;
		}

		return retCode;
	}

	public static ItemStack[] getResultStacks(ItemStack stack) {
		Tuple<ItemStack, ItemStack[]> result = _getResultStacks(stack);
		return result == null || result.y == null ? null : InventoryHelper
				.cloneInventory(result.y);
	}

	public static Tuple<ItemStack, ItemStack[]> _getResultStacks(ItemStack stack) {

		Tuple<ItemStack, ItemStack[]> fuzzyMatch = null;

		for (Entry<ItemStack, ItemStack[]> e : thermalRecipes.entrySet()) {

			if (ItemHelper.itemsEqualWithMetadata(stack, e.getKey())) {
				return new Tuple<ItemStack, ItemStack[]>(e);
			} else if (e.getKey().getItemDamage() == OreDictionary.WILDCARD_VALUE
					&& ItemHelper.itemsEqualWithoutMetadata(stack, e.getKey())) {
				fuzzyMatch = new Tuple<ItemStack, ItemStack[]>(e);
			}
		}

		return fuzzyMatch;
	}

	public static void addThermalRecyclerBlacklist(int quantity,
			boolean wildcard, Block... blocks) {

		for (Block b : blocks)
			addThermalRecyclerBlacklist(new ItemStack(b, quantity,
					wildcard ? OreDictionary.WILDCARD_VALUE : 0));
	}

	public static void addThermalRecyclerBlacklist(int quantity,
			boolean wildcard, Item... items) {

		for (Item i : items)
			addThermalRecyclerBlacklist(new ItemStack(i, quantity,
					wildcard ? OreDictionary.WILDCARD_VALUE : 0));
	}

	public static void addThermalRecyclerBlacklist(ItemStack item) {

		if (!isThermalRecyclerBlacklisted(item))
			thermalBlacklist.add(item.copy());
	}

	protected static ItemStack findBlacklistEntry(ItemStack item) {

		for (ItemStack stack : thermalBlacklist) {
			// Highly specific match?
			if (ItemHelper.itemsEqualWithMetadata(item, stack))
				return stack;
			// Wildcard match?
			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE
					&& ItemHelper.itemsEqualWithoutMetadata(item, stack))
				return stack;
		}

		return null;
	}

	public static int getMinimumQuantityToRecycle(ItemStack item) {

		initialize();

		// If we don't have a recipe assume 1
		int result = 1;

		for (ItemStack stack : thermalRecipes.keySet()) {
			if (ItemHelper.itemsEqualWithMetadata(item, stack)
					|| (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && ItemHelper
							.itemsEqualWithoutMetadata(item, stack))) {
				result = stack.stackSize;
				break;
			}
		}

		return result;
	}

	public static boolean isThermalRecyclerBlacklisted(ItemStack item) {
		initialize();
		return findBlacklistEntry(item) != null;
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