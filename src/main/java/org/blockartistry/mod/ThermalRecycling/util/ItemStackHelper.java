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

package org.blockartistry.mod.ThermalRecycling.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;

import com.google.common.base.Optional;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

public final class ItemStackHelper {

	private ItemStackHelper() {
	}

	protected static final Random rand = XorShiftRandom.shared;

	public static Optional<ItemStack> convertToDustIfPossible(final ItemStack stack) {

		String oreName = OreDictionaryHelper.getOreName(stack);

		if (oreName == null)
			return Optional.of(stack);

		if (oreName.startsWith("ingot"))
			oreName = StringUtils.replaceOnce(oreName, "ingot", "dust");
		else if (oreName.startsWith("plank"))
			oreName = StringUtils.replaceOnce(oreName, "plank", "dust");
		else
			return Optional.of(stack);

		return getItemStack(oreName);
	}

	public static Optional<ItemStack> getPreferredStack(final String oreName) {
		return getItemStack(oreName);
	}

	public static Optional<ItemStack> getPreferredStack(final ItemStack stack) {
		final String oreName = OreDictionaryHelper.getOreName(stack);
		if (oreName == null || oreName.isEmpty() || "Unknown".compareToIgnoreCase(oreName) == 0)
			return Optional.of(stack);

		Optional<ItemStack> newStack = getItemStack(oreName);
		if (newStack.isPresent()) {
			final ItemStack result = newStack.get();
			result.stackSize = stack.stackSize;
			newStack = Optional.of(result);
		}

		return newStack;
	}

	public static List<ItemStack> getItemStacks(final String... items) {

		final List<ItemStack> result = new ArrayList<ItemStack>();

		for (final String s : items) {
			final Optional<ItemStack> stack = getItemStack(s);
			if (stack.isPresent())
				result.add(stack.get());
		}

		return result;
	}

	public static Optional<ItemStack> getItemStack(final String name) {
		return getItemStack(name, 1);
	}

	public static Optional<ItemStack> getItemStack(final String name, final int quantity) {

		if (name == null || name.isEmpty())
			return Optional.absent();

		// Check our preferred list first. If we have a hit, use it.
		ItemStack result = PreferredItemStacks.instance.get(name);

		if (result != null) {

			result = result.copy();
			result.stackSize = quantity;

		} else {

			String workingName = name;
			int subType = -1;
			int q = quantity;
			
			// See if there is a quantity token in the
			// name string.
			if(workingName.contains("#")) {
				final String num = StringUtils.substringAfterLast(workingName, "#");
				if(!num.isEmpty()) {
					q = Integer.parseInt(num);
				}
				workingName = StringUtils.substringBeforeLast(workingName, "#");
			}
			
			// Parse out the possible subtype from the end of the string
			if (StringUtils.countMatches(name, ":") == 2) {
				workingName = StringUtils.substringBeforeLast(name, ":");
				final String num = StringUtils.substringAfterLast(name, ":");

				if (num != null && !num.isEmpty()) {

					if ("*".compareTo(num) == 0)
						subType = OreDictionaryHelper.WILDCARD_VALUE;
					else {
						try {
							subType = Integer.parseInt(num);
						} catch (Exception e) {
							// It appears malformed - assume the incoming name
							// is
							// the real name and continue.
							;
						}
					}
				}
			}

			// Check the OreDictionary first for any alias matches. Otherwise
			// go to the game registry to find a match.
			final List<ItemStack> ores = OreDictionaryHelper.getOres(workingName);
			if (!ores.isEmpty()) {
				result = ores.get(0).copy();
				result.stackSize = q;
			} else {
				final Item i = GameData.getItemRegistry().getObject(workingName);
				if (i != null) {
					result = new ItemStack(i, q);
				}
			}

			// If we did have a hit on a base item, set the sub-type
			// as needed.
			if (result != null && subType != -1) {
				if (subType == OreDictionaryHelper.WILDCARD_VALUE && !ItemStackHelper.canBeGeneric(result)
						&& !ItemStackHelper.canBeDamaged(result)) {
					ModLog.warn("[%s] GENERIC requested but Item does not support sub-types", name);
				} else {
					result.setItemDamage(subType);
				}
			}
		}

		return Optional.fromNullable(result);
	}

	/**
	 * Attempts to get the name of an ItemStack if possible.
	 * 
	 * @param stack
	 * @return
	 */
	public static String resolveName(final ItemStack stack) {
		String result = null;

		if (stack != null) {

			try {
				result = stack.getDisplayName();
			} catch (Throwable e) {
				;
			}

			if (result == null || result.isEmpty()) {
				try {
					result = stack.getUnlocalizedName();
				} catch (Throwable e) {
					;
				}
			}
		}

		return result == null || result.isEmpty() ? "UNKNOWN" : result;
	}

	/**
	 * Determines if the ItemStack can be matched using a generic.
	 */
	public static boolean canBeGeneric(final ItemStack stack) {
		return stack.getHasSubtypes() && !OreDictionaryHelper.isGeneric(stack);
	}

	public static boolean canBeDamaged(final ItemStack stack) {
		return stack.isItemStackDamageable();
	}

	public static boolean hasDamage(final ItemStack stack) {
		return stack.isItemDamaged();
	}

	public static String resolveInternalName(final ItemStack stack) {
		final StringBuilder builder = new StringBuilder();

		// Generate a new we can display to make things real easy
		final String name = Item.itemRegistry.getNameForObject(stack.getItem());

		if (name == null)
			builder.append("UNKNOWN");
		else
			builder.append(name);

		if (canBeGeneric(stack)) {
			builder.append(':');
			builder.append(getItemDamage(stack));
		}

		return builder.toString();
	}

	/**
	 * Sets the display name of the ItemStack.
	 * 
	 * @param stack
	 * @param name
	 */
	public static void setItemName(final ItemStack stack, final String name) {
		if (stack != null)
			stack.setStackDisplayName(name);
	}

	/**
	 * Sets the lore of the ItemStack.
	 * 
	 * @param stack
	 * @param lore
	 */
	public static void setItemLore(final ItemStack stack, final List<String> lore) {

		if (stack == null)
			return;

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();

		final NBTTagList l = new NBTTagList();
		for (final String s : lore)
			l.appendTag(new NBTTagString(s));
		NBTTagCompound display = nbt.getCompoundTag("display");
		if (display == null)
			display = new NBTTagCompound();
		display.setTag("Lore", l);

		nbt.setTag("display", display);
		stack.setTagCompound(nbt);
	}

	/**
	 * Sets the enchantment tag in the ItemStack NBT to make it glow.
	 * 
	 * @param stack
	 * @return
	 */
	public static void makeGlow(final ItemStack stack) {
		if (stack != null) {
			final NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			nbt.setTag("ench", new NBTTagList());
			stack.setTagCompound(nbt);
		}
	}

	/**
	 * Determines if the two specified tags are equal.
	 * 
	 * @param nbt1
	 *            First tag compound to compare
	 * @param nbt2
	 *            Second tag compound to compare
	 * @return true if they are equal; false otherwise
	 */
	public static boolean areTagsEqual(final NBTTagCompound nbt1, final NBTTagCompound nbt2) {
		return nbt1 == null && nbt2 != null ? false : nbt1 == null || nbt1.equals(nbt2);
	}

	/**
	 * Determines if the two ItemStacks are equal. Does not include quantity in
	 * the determination, but does include tags.
	 * 
	 * @param stack1
	 *            First stack to compare
	 * @param stack2
	 *            Second stack to compare
	 * @return true if they are equal; false otherwise
	 */
	public static boolean areEqual(final ItemStack stack1, final ItemStack stack2) {
		if (stack1 == stack2)
			return true;

		if (stack1 == null || stack2 == null)
			return false;

		return areEqualNoNBT(stack1, stack2) && areTagsEqual(stack1.stackTagCompound, stack2.stackTagCompound);
	}

	public static boolean areEqualNoNBT(final ItemStack stack1, final ItemStack stack2) {
		if (stack1 == stack2)
			return true;

		if (stack1 == null || stack2 == null)
			return false;

		return stack1.isItemEqual(stack2);
	}

	public static <T> boolean equals(final T i1, final T i2) {
		if (i1 == i2)
			return true;

		if (i1 == null || i2 == null)
			return false;

		return i1.equals(i2);

	}

	public static boolean areEqualNoMeta(final ItemStack stack1, final ItemStack stack2) {
		if (stack1 == null || stack2 == null)
			return false;
		return equals(stack1.getItem(), stack2.getItem());
	}

	public static boolean itemsEqualForCrafting(final ItemStack stack1, final ItemStack stack2) {
		return areEqualNoMeta(stack1, stack2) && (!stack1.getHasSubtypes() || OreDictionaryHelper.isGeneric(stack1)
				|| OreDictionaryHelper.isGeneric(stack2) || getItemDamage(stack2) == getItemDamage(stack1));
	}

	/**
	 * Determines if an ItemStacks represents a Vanilla item
	 * 
	 * @param output
	 * @return
	 */
	public static boolean isVanilla(final List<ItemStack> output) {
		assert output != null;
		assert output.size() > 0;
		for (final ItemStack item : output)
			if (!isVanilla(item))
				return false;
		return true;
	}

	public static boolean isVanilla(final ItemStack stack) {
		assert stack != null;
		return SupportedMod.VANILLA.belongsTo(stack.getItem());
	}

	/**
	 * Gets the damage value of the ItemStack. Sometimes the Item attached to
	 * the ItemStack is missing or strange so alternate means are used to obtain
	 * the value.
	 * 
	 * @param stack
	 * @return
	 */
	public static int getItemDamage(final ItemStack stack) {
		assert stack != null;
		return Items.diamond.getDamage(stack);
	}

	/**
	 * Determines if the ItemStack is repairable.
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean isRepairable(final ItemStack stack) {
		final Item item = stack.getItem();
		return item != null ? item.isRepairable() : false;
	}
}
