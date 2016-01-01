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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Wraps the Forge OreDictonary implementation.  Used as a insulator to
 * better support integration of Forge updates.
 */
public final class OreDictionaryHelper {
	
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;

	private OreDictionaryHelper() { }
	
	/**
	 * Retrieves a list of ItemStacks registered for the given oreName.
	 * 
	 * @param name
	 * @return
	 */
	public static List<ItemStack> getOres(final String name) {
		return OreDictionary.getOres(name);
	}
	
	/**
	 * Gets a list of all registered oreNames that can be found in the
	 * Forge OreDictionary.
	 * 
	 * @return
	 */
	public static String[] getOreNames() {
		return OreDictionary.getOreNames();
	}
	
	/**
	 * Gets a list of ore IDs that the specified ItemStack has been
	 * registered against.
	 * 
	 * @param stack
	 * @return
	 */
	public static int[] getOreIDs(final ItemStack stack) {
		return OreDictionary.getOreIDs(stack);
	}
	
	/**
	 * Get's a list of ore dictionary names that the ItemStack is
	 * associated with it.
	 */
	public static List<String> getOreNamesForStack(final ItemStack stack) {
		final List<String> result = new ArrayList<String>();
		final int[] oreIds = getOreIDs(stack);
		if(oreIds != null)
			for(int i = 0; i < oreIds.length; i++) {
				final String oreName = OreDictionaryHelper.getOreName(oreIds[i]);
				if(oreName != null && !oreName.isEmpty())
					result.add(oreName);
			}
		
		return result;
	}
	
	/**
	 * Determines if the ItemStack is considered a member of the oreName
	 * collection based on data in the OreDictionary.  Matching is based
	 * on crafting equivalence rather than strict.
	 * 
	 * @param stack
	 * @param oreName
	 * @return true if stack is a member of oreName; false otherwise
	 */
	public static boolean isOneOfThese(final ItemStack stack, final String oreName) {
		final List<ItemStack> possibles = getOres(oreName);
		if (possibles == null || possibles.isEmpty())
			return false;
		for (final ItemStack item : possibles) {
			if (ItemStackHelper.itemsEqualForCrafting(stack, item))
				return true;
		}

		return false;
	}
	
	/**
	 * Determines if the ItemStack in question is a generic ItemStack.
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean isGeneric(final ItemStack stack) {
		return ItemStackHelper.getItemDamage(stack) == OreDictionary.WILDCARD_VALUE;
	}
	
	/**
	 * Gets the Forge Ore Dictionary name for the ItemStack.
	 * 
	 * @param itemstack
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getOreName(final ItemStack itemstack) {
		return OreDictionary.getOreName(OreDictionary.getOreID(itemstack));
	}
	
	/**
	 * Gets the oreName of the provided ore ID.
	 * 
	 * @param id
	 * @return
	 */
	public static String getOreName(final int id) {
		return OreDictionary.getOreName(id);
	}
	
	public static ItemStack asGeneric(final Item item) {
		return new ItemStack(item, 1, item.getHasSubtypes() ? OreDictionaryHelper.WILDCARD_VALUE : 0);
	}
	
	public static ItemStack asGeneric(final ItemStack stack) {
		if (ItemStackHelper.canBeGeneric(stack)) {
			return new ItemStack(stack.getItem(), stack.stackSize, OreDictionary.WILDCARD_VALUE);
		}
		return stack;
	}
}
