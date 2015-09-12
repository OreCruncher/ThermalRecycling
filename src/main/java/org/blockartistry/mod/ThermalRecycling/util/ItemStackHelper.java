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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModLog;

import com.google.common.collect.ImmutableMap;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public final class ItemStackHelper {

	protected static final Random rand = XorShiftRandom.shared;
	protected static Map<String, ItemStack> preferred = new HashMap<String, ItemStack>();

	private static final Item materialBase = GameData.getItemRegistry().getObject("ThermalFoundation:material");
	private static final Item storageBase = GameData.getItemRegistry().getObject("ThermalFoundation:Storage");
	private static final Item materialBaseTE = GameData.getItemRegistry().getObject("ThermalExpansion:material");

	public static final ItemStack dustIron = new ItemStack(materialBase, 1, 0);
	public static final ItemStack dustGold = new ItemStack(materialBase, 1, 1);
	public static final ItemStack dustCopper = new ItemStack(materialBase, 1, 32);
	public static final ItemStack dustTin = new ItemStack(materialBase, 1, 33);
	public static final ItemStack dustSilver = new ItemStack(materialBase, 1, 34);
	public static final ItemStack dustLead = new ItemStack(materialBase, 1, 35);
	public static final ItemStack dustNickel = new ItemStack(materialBase, 1, 36);
	public static final ItemStack dustPlatinum = new ItemStack(materialBase, 1, 37);
	public static final ItemStack dustManaInfused = new ItemStack(materialBase, 1, 38);
	public static final ItemStack dustElectrum = new ItemStack(materialBase, 1, 39);
	public static final ItemStack dustInvar = new ItemStack(materialBase, 1, 40);
	public static final ItemStack dustBronze = new ItemStack(materialBase, 1, 41);
	public static final ItemStack dustSignalum = new ItemStack(materialBase, 1, 42);
	public static final ItemStack dustLumium = new ItemStack(materialBase, 1, 43);
	public static final ItemStack dustEnderium = new ItemStack(materialBase, 1, 44);

	public static final ItemStack dustWood = new ItemStack(materialBaseTE, 1, 512);
	public static final ItemStack boneMeal = new ItemStack(Items.dye, 1, 15);

	public static final ItemStack dustCoal = new ItemStack(materialBase, 1, 2);
	public static final ItemStack dustCharcoal = new ItemStack(materialBase, 1, 3);
	public static final ItemStack sulfer = new ItemStack(materialBase, 1, 16);
	public static final ItemStack niter = new ItemStack(materialBase, 1, 17);

	public static final ItemStack dustObsidian = new ItemStack(materialBase, 1, 4);

	static {

		// Cache builtins
		preferred.put("ingotIron", new ItemStack(Items.iron_ingot));
		preferred.put("ingotGold", new ItemStack(Items.gold_ingot));
		preferred.put("nuggetGold", new ItemStack(Items.gold_nugget));
		preferred.put("blockGold", new ItemStack(Blocks.gold_block));
		preferred.put("blockIron", new ItemStack(Blocks.iron_block));
		preferred.put("gemDiamond", new ItemStack(Items.diamond));
		preferred.put("gemEmerald", new ItemStack(Items.emerald));

		preferred.put("ingotCopper", new ItemStack(materialBase, 1, 64));
		preferred.put("ingotTin", new ItemStack(materialBase, 1, 65));
		preferred.put("ingotSilver", new ItemStack(materialBase, 1, 66));
		preferred.put("ingotLead", new ItemStack(materialBase, 1, 67));
		preferred.put("ingotNickel", new ItemStack(materialBase, 1, 68));
		preferred.put("ingotPlatinum", new ItemStack(materialBase, 1, 69));
		preferred.put("ingotManaInfused", new ItemStack(materialBase, 1, 70));
		preferred.put("ingotElectrum", new ItemStack(materialBase, 1, 71));
		preferred.put("ingotInvar", new ItemStack(materialBase, 1, 72));
		preferred.put("ingotBronze", new ItemStack(materialBase, 1, 73));
		preferred.put("ingotSignalum", new ItemStack(materialBase, 1, 74));
		preferred.put("ingotLumium", new ItemStack(materialBase, 1, 75));
		preferred.put("ingotEnderium", new ItemStack(materialBase, 1, 76));

		preferred.put("nuggetIron", new ItemStack(materialBase, 1, 8));
		preferred.put("nuggetCopper", new ItemStack(materialBase, 1, 96));
		preferred.put("nuggetTin", new ItemStack(materialBase, 1, 97));
		preferred.put("nuggetSilver", new ItemStack(materialBase, 1, 98));
		preferred.put("nuggetLead", new ItemStack(materialBase, 1, 99));
		preferred.put("nuggetNickel", new ItemStack(materialBase, 1, 100));
		preferred.put("nuggetPlatinum", new ItemStack(materialBase, 1, 101));
		preferred.put("nuggetManaInfused", new ItemStack(materialBase, 1, 102));
		preferred.put("nuggetElectrum", new ItemStack(materialBase, 1, 103));
		preferred.put("nuggetInvar", new ItemStack(materialBase, 1, 104));
		preferred.put("nuggetBronze", new ItemStack(materialBase, 1, 105));
		preferred.put("nuggetSignalum", new ItemStack(materialBase, 1, 106));
		preferred.put("nuggetLumium", new ItemStack(materialBase, 1, 107));
		preferred.put("nuggetEnderium", new ItemStack(materialBase, 1, 108));

		preferred.put("dustIron", dustIron);
		preferred.put("dustGold", dustGold);
		preferred.put("dustCopper", dustCopper);
		preferred.put("dustTin", dustTin);
		preferred.put("dustSilver", dustSilver);
		preferred.put("dustLead", dustLead);
		preferred.put("dustNickel", dustNickel);
		preferred.put("dustPlatinum", dustPlatinum);
		preferred.put("dustManaInfused", dustManaInfused);
		preferred.put("dustElectrum", dustElectrum);
		preferred.put("dustInvar", dustInvar);
		preferred.put("dustBronze", dustBronze);
		preferred.put("dustSignalum", dustSignalum);
		preferred.put("dustLumium", dustLumium);
		preferred.put("dustEnderium", dustEnderium);

		preferred.put("dustCoal", dustCoal);
		preferred.put("dustCharcoal", dustCharcoal);
		preferred.put("dustSulfer", sulfer);
		preferred.put("dustObsidian", dustObsidian);

		preferred.put("blockCopper", new ItemStack(storageBase, 1, 0));
		preferred.put("blockTin", new ItemStack(storageBase, 1, 1));
		preferred.put("blockSilver", new ItemStack(storageBase, 1, 2));
		preferred.put("blockLead", new ItemStack(storageBase, 1, 3));
		preferred.put("blockFerrous", new ItemStack(storageBase, 1, 4));
		preferred.put("blockPlatinum", new ItemStack(storageBase, 1, 5));
		preferred.put("blockManaInfused", new ItemStack(storageBase, 1, 6));
		preferred.put("blockElectrum", new ItemStack(storageBase, 1, 7));
		preferred.put("blockInvar", new ItemStack(storageBase, 1, 8));
		preferred.put("blockBronze", new ItemStack(storageBase, 1, 9));
		preferred.put("blockSignalum", new ItemStack(storageBase, 1, 10));
		preferred.put("blockLumium", new ItemStack(storageBase, 1, 11));
		preferred.put("blockEnderium", new ItemStack(storageBase, 1, 12));

		preferred.put("gearIron", new ItemStack(materialBase, 1, 12));
		preferred.put("gearGold", new ItemStack(materialBase, 1, 13));
		preferred.put("gearCopper", new ItemStack(materialBase, 1, 128));
		preferred.put("gearTin", new ItemStack(materialBase, 1, 129));
		preferred.put("gearSilver", new ItemStack(materialBase, 1, 130));
		preferred.put("gearLead", new ItemStack(materialBase, 1, 131));
		preferred.put("gearNickel", new ItemStack(materialBase, 1, 132));
		preferred.put("gearPlatinum", new ItemStack(materialBase, 1, 133));
		preferred.put("gearManaInfused", new ItemStack(materialBase, 1, 134));
		preferred.put("gearElectrum", new ItemStack(materialBase, 1, 135));
		preferred.put("gearInvar", new ItemStack(materialBase, 1, 136));
		preferred.put("gearBronze", new ItemStack(materialBase, 1, 137));
		preferred.put("gearSignalum", new ItemStack(materialBase, 1, 138));
		preferred.put("gearLumium", new ItemStack(materialBase, 1, 139));
		preferred.put("gearEnderium", new ItemStack(materialBase, 1, 140));

		preferred = ImmutableMap.copyOf(preferred);

	}

	public static ItemStack convertToDustIfPossible(final ItemStack stack) {

		String oreName = getOreName(stack);

		if (oreName == null)
			return stack;

		if (oreName.startsWith("ingot"))
			oreName = StringUtils.replaceOnce(oreName, "ingot", "dust");
		else if (oreName.startsWith("plank"))
			oreName = StringUtils.replaceOnce(oreName, "plank", "dust");
		else
			return stack;

		return getItemStack(oreName);
	}

	public static ItemStack getPreferredStack(final ItemStack stack) {
		final String oreName = getOreName(stack);
		if (oreName == null || oreName.isEmpty() || "Unknown".compareToIgnoreCase(oreName) == 0)
			return stack;

		final ItemStack newStack = getItemStack(oreName);
		if (newStack == null)
			return stack;

		newStack.stackSize = stack.stackSize;
		return newStack;
	}

	public static List<ItemStack> getItemStacks(final String... items) {

		final List<ItemStack> result = new ArrayList<ItemStack>();

		for (final String s : items) {
			final ItemStack stack = getItemStack(s);
			if (stack != null)
				result.add(stack);
		}

		return result;
	}

	public static ItemStack getItemStack(final String name) {
		return getItemStack(name, 1);
	}

	public static ItemStack getItemStack(final String name, final int quantity) {

		if (name == null || name.isEmpty())
			return null;

		// Check our preferred list first. If we have a hit, use it.
		ItemStack result = preferred.get(name);

		if (result != null) {

			result = result.copy();
			result.stackSize = quantity;

		} else {

			// Parse out the possible subtype from the end of the string
			String workingName = name;
			int subType = -1;

			if (StringUtils.countMatches(name, ":") == 2) {
				workingName = StringUtils.substringBeforeLast(name, ":");
				final String num = StringUtils.substringAfterLast(name, ":");

				if (num != null && !num.isEmpty()) {

					if ("*".compareTo(num) == 0)
						subType = OreDictionary.WILDCARD_VALUE;
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
			final ArrayList<ItemStack> ores = OreDictionary.getOres(workingName);
			if (!ores.isEmpty()) {
				result = ores.get(0).copy();
				result.stackSize = quantity;
			} else {
				final Item i = GameData.getItemRegistry().getObject(workingName);
				if (i != null) {
					result = new ItemStack(i, quantity);
				}
			}

			// If we did have a hit on a base item, set the sub-type
			// as needed.
			if (result != null && subType != -1) {
				if (subType == OreDictionary.WILDCARD_VALUE && !result.getHasSubtypes()) {
					ModLog.warn("[%s] GENERIC requested but Item does not support sub-types", name);
				} else {
					result.setItemDamage(subType);
				}
			}
		}

		return result;
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
	 * Spawns the ItemStack into the world.  If it is a large stack it is broken down
	 * into smaller stacks and spun out at different velocities.
	 * 
	 * @param world
	 * @param stack
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void spawnIntoWorld(final World world, final ItemStack stack, final double x, final double y,
			final double z) {

		if (stack == null)
			return;

		final float f = rand.nextFloat() * 0.8F + 0.1F;
		final float f1 = rand.nextFloat() * 0.8F + 0.1F;
		final float f2 = rand.nextFloat() * 0.8F + 0.1F;

		while (stack.stackSize > 0) {
			int j = rand.nextInt(21) + 10;

			if (j > stack.stackSize) {
				j = stack.stackSize;
			}

			stack.stackSize -= j;

			final EntityItem item = new EntityItem(world, x + f, y + f1, z + f2,
					new ItemStack(stack.getItem(), j, getItemDamage(stack)));

			if (stack.hasTagCompound()) {
				item.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
			}

			world.spawnEntityInWorld(item);
		}
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
		if(stack != null) {
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

		return stack1.isItemEqual(stack2) && areTagsEqual(stack1.stackTagCompound, stack2.stackTagCompound);
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
		return isVanilla(stack.getItem());
	}

	public static boolean isVanilla(final Item item) {
		final String name = Item.itemRegistry.getNameForObject(item);
		return name != null && name.startsWith("minecraft");
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
	 * Determines if the ItemStack in question is a generic stack
	 * 
	 * @param stack
	 * @return
	 */
	public static boolean isWildcard(final ItemStack stack) {
		return getItemDamage(stack) == OreDictionary.WILDCARD_VALUE;
	}
	
	/**
	 * Gets the Forge Ore Dictionary name for the ItemStack.
	 * 
	 * @param itemstack
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getOreName(ItemStack itemstack) {
		return OreDictionary.getOreName(OreDictionary.getOreID(itemstack));
	}
}
