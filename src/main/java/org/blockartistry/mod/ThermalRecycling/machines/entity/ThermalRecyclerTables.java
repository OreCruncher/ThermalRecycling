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

package org.blockartistry.mod.ThermalRecycling.machines.entity;

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

public final class ThermalRecyclerTables {

	public static final ItemStack keep = new ItemStack(Blocks.dirt);
	public static final ArrayList<WeightedRandomItemStack> unknownScrap = new ArrayList<WeightedRandomItemStack>();
	public static final ArrayList<WeightedRandomItemStack> componentScrap = new ArrayList<WeightedRandomItemStack>();

	static final ArrayList<WeightedRandomItemStack> loots = new ArrayList<WeightedRandomItemStack>();
	static final HashMap<ItemStack, ItemStack[]> thermalRecipes = new HashMap<ItemStack, ItemStack[]>();
	static final ArrayList<ItemStack> thermalWhitelist = new ArrayList<ItemStack>();
	static final Random rnd = new Random();
	static boolean inited = false;

	public static void initialize() {
		if (inited)
			return;

		inited = true;

		// This table is for when an unknown item gets scrapped. Null means they
		// get
		// nuttin.
		unknownScrap.add(new WeightedRandomItemStack(null, 35));
		unknownScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 1));
		unknownScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 25));
		unknownScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 60));

		// This table is for when a component of an item gets scrapped. Null
		// means they get nuttin.
		// A dirt block indicates they get back the original component.
		componentScrap.add(new WeightedRandomItemStack(keep, 207));
		componentScrap.add(new WeightedRandomItemStack(null, 20));
		componentScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR), 8));
		componentScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD), 17));
		componentScrap.add(new WeightedRandomItemStack(new ItemStack(
				ItemManager.recyclingScrap, 1, RecyclingScrap.POOR), 25));
	}

	public static ItemStack pickStackFrom(
			ArrayList<WeightedRandomItemStack> table) {
		WeightedRandom.Item item = WeightedRandom.getRandomItem(new Random(),
				table);
		return ((WeightedRandomItemStack) item).getStack();
	}

	public static void addThermalRecyclerRecipe(ItemStack input,
			ItemStack[] output) {

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
			ItemStack[] consolidated = new ItemStack[workingSet.length];

			for (ItemStack stack : workingSet) {
				boolean itAllFit = InventoryHelper.addItemStackToInventory(
						consolidated, stack);
				if (!itAllFit)
					ModLog.warn("Unable to fit the entire stack into inventory!");
			}

			// Consolidated has already been cloned
			addThermalRecyclerWhitelist(input.copy());
			thermalRecipes.put(input.copy(), shrink(consolidated));
		}
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

	public static void addThermalRecyclerWhitelist(int quantity,
			boolean wildcard, Block... blocks) {

		for (Block b : blocks)
			addThermalRecyclerWhitelist(new ItemStack(b, quantity,
					wildcard ? OreDictionary.WILDCARD_VALUE : 0));
	}

	public static void addThermalRecyclerWhitelist(int quantity,
			boolean wildcard, Item... items) {

		for (Item i : items)
			addThermalRecyclerWhitelist(new ItemStack(i, quantity,
					wildcard ? OreDictionary.WILDCARD_VALUE : 0));
	}

	public static void addThermalRecyclerWhitelist(ItemStack item) {

		if (!isThermalRecyclerWhitelisted(item))
			thermalWhitelist.add(item.copy());
	}

	protected static ItemStack findWhitelistEntry(ItemStack item) {

		for (ItemStack stack : thermalWhitelist) {
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

		int result = -1;
		ItemStack stack = findWhitelistEntry(item);

		if (stack != null)
			result = stack.stackSize;

		return result;
	}

	public static boolean isThermalRecyclerWhitelisted(ItemStack item) {
		initialize();
		return findWhitelistEntry(item) != null;
	}

	protected static ItemStack[] shrink(ItemStack[] list) {

		// Quick check - if there is no null at the end there is nothing
		// to shrink.
		if (list[list.length - 1] != null)
			return list;

		// Find the first null in the array - that our termination. This
		// shouldn't be infinite given the initial check in the method
		int x;
		for (x = 0; list[x] != null; x++)
			;

		// At this point x should be indexed at a null.
		ItemStack[] result = new ItemStack[x];

		// Copy them over
		for (int i = 0; i < x; i++)
			result[i] = list[i];

		// Done!
		return result;
	}
}