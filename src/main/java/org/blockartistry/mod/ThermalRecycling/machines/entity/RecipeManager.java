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

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.Tuple;

import cofh.lib.util.helpers.InventoryHelper;
import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public final class RecipeManager {

	static HashMap<ItemStack, ItemStack[]> thermalRecipes = new HashMap<ItemStack, ItemStack[]>();
	static ArrayList<ItemStack> thermalBlackList;
	
	public static void initialize() {
		if(thermalBlackList != null)
			return;
		
		thermalBlackList = new ArrayList<ItemStack>();
		addThermalRecyclerBlacklist(new ItemStack(Blocks.cobblestone, 1, OreDictionary.WILDCARD_VALUE));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.stone, 1, OreDictionary.WILDCARD_VALUE));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.netherrack));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.nether_brick));
		addThermalRecyclerBlacklist(new ItemStack(Items.netherbrick, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.stone_slab, 1, OreDictionary.WILDCARD_VALUE));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.wooden_slab, 1, OreDictionary.WILDCARD_VALUE));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.acacia_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.oak_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.spruce_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.jungle_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.birch_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.dark_oak_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.quartz_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.stone_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.brick_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.stone_brick_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.nether_brick_stairs, 1));
		addThermalRecyclerBlacklist(new ItemStack(Blocks.sandstone_stairs, 1));
	}
	
	public static void addThermalRecyclerRecipe(ItemStack input,
			ItemStack[] output) {

		// See if we have an existing mapping
		Tuple<ItemStack, ItemStack[]> result = _getResultStacks(input);

		// If we don't, or the mapping that exists is a wild card and the incoming
		// recipe is specific, we want to add to the dictionary.  The dictionary
		// will prefer specific recipes over wild cards if possible.
		if (result == null
				|| (result.x.getItemDamage() == OreDictionary.WILDCARD_VALUE && input
						.getItemDamage() != OreDictionary.WILDCARD_VALUE)) {

			// Clone the inventory - don't want to work with the originals
			ItemStack[] workingSet = InventoryHelper.cloneInventory(output);

			// Traverse the list replacing WILDCARD stacks with concrete ones.
			// The logic
			// prefers Thermal Foundation equivalents if found.
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
			thermalRecipes.put(input.copy(), shrink(consolidated));
		}
	}
	
	public static ItemStack[] getResultStacks(ItemStack stack) {
		Tuple<ItemStack, ItemStack[]> result = _getResultStacks(stack);
		return result == null ? null : InventoryHelper.cloneInventory(result.y);
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

	public static void addThermalRecyclerBlacklist(ItemStack item) {

		if (!isThermalRecyclerBlacklisted(item))
			thermalBlackList.add(item.copy());
	}

	public static boolean isThermalRecyclerBlacklisted(ItemStack item) {

		initialize();
		
		for (ItemStack stack : thermalBlackList) {
			// Highly specific match?
			if(ItemHelper.itemsEqualWithMetadata(item, stack))
				return true;
			// Wildcard match?
			if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE && ItemHelper.itemsEqualWithoutMetadata(item, stack))
				return true;
		}

		return false;
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