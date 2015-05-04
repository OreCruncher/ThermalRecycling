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
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import cofh.lib.util.helpers.ItemHelper;

import com.google.common.base.Preconditions;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public final class ItemStackHelper {

	protected static final Random rand = new Random();
	protected static final HashMap<String, ItemStack> preferred = new HashMap<String, ItemStack>();

	static Item materialBase = GameData.getItemRegistry().getObject(
			"ThermalFoundation:material");
	static Item storageBase = GameData.getItemRegistry().getObject(
			"ThermalFoundation:Storage");
	static Item materialBaseTE = GameData.getItemRegistry().getObject(
			"ThermalExpansion:material");

	public static final ItemStack dustIron = new ItemStack(materialBase, 1, 0);
	public static final ItemStack dustGold = new ItemStack(materialBase, 1, 1);
	public static final ItemStack dustCopper = new ItemStack(materialBase, 1,
			32);
	public static final ItemStack dustTin = new ItemStack(materialBase, 1, 33);
	public static final ItemStack dustSilver = new ItemStack(materialBase, 1,
			34);
	public static final ItemStack dustLead = new ItemStack(materialBase, 1, 35);
	public static final ItemStack dustNickel = new ItemStack(materialBase, 1,
			36);
	public static final ItemStack dustPlatinum = new ItemStack(materialBase, 1,
			37);
	public static final ItemStack dustManaInfused = new ItemStack(materialBase,
			1, 38);
	public static final ItemStack dustElectrum = new ItemStack(materialBase, 1,
			39);
	public static final ItemStack dustInvar = new ItemStack(materialBase, 1, 40);
	public static final ItemStack dustBronze = new ItemStack(materialBase, 1,
			41);
	public static final ItemStack dustSignalum = new ItemStack(materialBase, 1,
			42);
	public static final ItemStack dustLumium = new ItemStack(materialBase, 1,
			43);
	public static final ItemStack dustEnderium = new ItemStack(materialBase, 1,
			44);

	public static final ItemStack dustWood = new ItemStack(materialBaseTE, 1,
			512);
	public static final ItemStack boneMeal = new ItemStack(Items.dye, 1, 15);

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
	}

	public static ItemStack convertToDustIfPossible(ItemStack stack) {

		boolean isBlock = false;

		String oreName = ItemHelper.getOreName(stack);

		if (oreName == null)
			return stack;

		if (oreName.startsWith("ingot"))
			oreName = StringUtils.replaceOnce(oreName, "ingot", "dust");
		else if (oreName.startsWith("plank"))
			oreName = StringUtils.replaceOnce(oreName, "plank", "dust");
		else if ((isBlock = oreName.startsWith("block")))
			oreName = StringUtils.replaceOnce(oreName, "block", "dust");
		else
			return stack;

		ItemStack newStack = getItemStack(oreName);
		if (newStack == null)
			return stack;

		newStack.stackSize = Math.min(newStack.getMaxStackSize(),
				stack.stackSize * (isBlock ? 9 : 1));
		return newStack;
	}

	public static ItemStack getPreferredStack(ItemStack stack) {
		String oreName = ItemHelper.getOreName(stack);
		if (oreName == null || oreName.isEmpty()
				|| "Unknown".compareToIgnoreCase(oreName) == 0)
			return stack;

		ItemStack newStack = getItemStack(oreName);
		if (newStack == null)
			return stack;

		newStack.stackSize = stack.stackSize;
		return newStack;
	}

	public static ItemStack getItemStack(String name) {
		return getItemStack(name, 1);
	}

	public static ItemStack getItemStack(String name, int quantity) {

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
				String num = StringUtils.substringAfterLast(name, ":");

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
			ArrayList<ItemStack> ores = OreDictionary.getOres(workingName);
			if (!ores.isEmpty()) {
				result = ores.get(0).copy();
				result.stackSize = quantity;
			} else {
				Item i = GameData.getItemRegistry().getObject(workingName);
				if (i != null) {
					result = new ItemStack(i, quantity);
				}
			}

			// If we did have a hit on a base item, set the subtype
			// as needed.
			if (result != null && subType != -1) {
				result.setItemDamage(subType);
			}
		}

		// Log if we didn't find an item - it's possible that the recipe has a
		// type
		// or the mod has changed where the item no longer exists.
		// if (result == null)
		// ModLog.info("Unable to locate item: " + name);

		return result;
	}

	public static List<ItemStack> getItemStackRange(String name,
			int startSubtype, int endSubtype, int quantity) {

		return getItemStackRange(getItemStack(name).getItem(), startSubtype,
				endSubtype, quantity);
	}

	public static List<ItemStack> getItemStackRange(Item item, int start,
			int end, int quantity) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = start; i <= end; i++) {
			result.add(new ItemStack(item, quantity, i));
		}

		return result;
	}

	public static List<ItemStack> getItemStackRange(Block block, int start,
			int end, int quantity) {

		ArrayList<ItemStack> result = new ArrayList<ItemStack>();

		for (int i = start; i <= end; i++) {
			result.add(new ItemStack(block, quantity, i));
		}

		return result;
	}

	public static FluidStack getFluidStack(String name, int quantity) {
		return FluidRegistry.getFluidStack(name, quantity);
	}

	public static String resolveName(ItemStack stack) {
		String result = null;

		if (stack != null) {

			try {
				result = stack.getDisplayName();
			} catch (Exception e) {
				;
			}

			if (result == null) {
				try {
					result = stack.getUnlocalizedName();
				} catch (Exception e) {
					;
				}
			}
		}

		return result == null || result.isEmpty() ? "UNKNOWN" : result;
	}

	public void dumpSubItems(Logger log, String itemId) {
		ItemStack stack = getItemStack(itemId, 1);
		if (stack != null) {

			try {
				for (int i = 0; i < 1024; i++) {
					stack.setItemDamage(i);
					String name = resolveName(stack);
					if (name != null && !name.isEmpty()
							&& !name.contains("(Destroy)"))
						log.info(String.format("%s:%d = %s", itemId, i, name));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				;
			}
		}
	}

	public void dumpItemStack(Logger log, String title, ItemStack... items) {

		log.info("");
		log.info(title);
		log.info(StringUtils.repeat('-', 32));

		if (items == null || items.length == 0) {
			log.info("No items in list");
			return;
		}

		for (ItemStack stack : items) {
			log.info(String.format("%s (%s)", resolveName(stack),
					stack.toString()));
		}
		log.info(StringUtils.repeat('-', 32));
		log.info(String.format("Total: %d item stacks", items.length));
	}

	public static void dumpFluidRegistry(Logger log) {

		log.info("Fluid Registry:");

		for (Entry<String, Fluid> e : FluidRegistry.getRegisteredFluids()
				.entrySet()) {
			log.info(String.format("%s: %s", e.getKey(), e.getValue().getName()));
		}
	}

	public static List<ItemStack> clone(ItemStack... stacks) {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (int i = 0; i < stacks.length; i++)
			if (stacks[i] != null)
				result.add(stacks[i].copy());
		return result;
	}

	public static List<ItemStack> clone(List<ItemStack> stacks) {
		ArrayList<ItemStack> result = new ArrayList<ItemStack>();
		for (ItemStack stack : stacks)
			if (stack != null)
				result.add(stack.copy());
		return result;
	}

	public static void append(List<ItemStack> list, ItemStack stack) {
		list.add(stack);
	}

	public static void append(List<ItemStack> list, String... items) {

		Preconditions.checkArgument(items != null && items.length > 0,
				"Input ItemStacks cannot be null");

		for (String s : items)
			list.add(ItemStackHelper.getItemStack(s));
	}

	public static void append(List<ItemStack> list, String item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		list.add(ItemStackHelper.getItemStack(item, quantity));
	}

	public static void append(List<ItemStack> list, Block... blocks) {

		Preconditions.checkArgument(blocks != null && blocks.length > 0,
				"Input ItemStacks cannot be null");

		for (Block b : blocks)
			list.add(new ItemStack(b));
	}

	public static void append(List<ItemStack> list, Block block, int quantity) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		list.add(new ItemStack(block, quantity));
	}

	public static void append(List<ItemStack> list, Item... items) {

		Preconditions.checkArgument(items != null && items.length > 0,
				"Input ItemStacks cannot be null");

		for (Item i : items)
			list.add(new ItemStack(i));
	}

	public static void append(List<ItemStack> list, Item item, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		list.add(new ItemStack(item, quantity));
	}

	public static void append(List<ItemStack> list, List<ItemStack> stacks) {

		Preconditions.checkArgument(stacks != null && !stacks.isEmpty(),
				"Input ItemStacks cannot be null");

		for (ItemStack stack : stacks) {
			list.add(stack);
		}
	}

	public static void append(List<ItemStack> list, ItemStack... stacks) {

		Preconditions.checkArgument(stacks != null && stacks.length > 0,
				"Input ItemStacks cannot be null");

		for (ItemStack stack : stacks) {
			list.add(stack);
		}
	}

	public static void appendSubtype(List<ItemStack> list, ItemStack stack,
			int subtype) {

		Preconditions.checkNotNull(stack, "Input ItemStack cannot be null");
		Preconditions.checkArgument(subtype >= 0,
				"Subtype has to be greater than or equal to 0");

		ItemStack s = stack.copy();
		s.setItemDamage(subtype);
		list.add(s);
	}

	public static void appendSubtype(List<ItemStack> list, Item item,
			int subtype) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(subtype >= 0,
				"Subtype has to be greater than or equal to 0");

		list.add(new ItemStack(item, 1, subtype));
	}

	public static void appendSubtypeRange(List<ItemStack> list, String item,
			int start, int end, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(item, start,
				end, quantity))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list, String item,
			int start, int end) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(item, start,
				end, 1))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list, Item item,
			int start, int end, int quantity) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");
		Preconditions.checkArgument(quantity > 0,
				"Quantity has to be greater than 0");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(item, start,
				end, quantity))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list, Item item,
			int start, int end) {

		Preconditions.checkNotNull(item, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(item, start,
				end, 1))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list, Block block,
			int start, int end, int quantity) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(block, start,
				end, quantity))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list, Block block,
			int start, int end) {

		Preconditions.checkNotNull(block, "Input ItemStack cannot be null");
		Preconditions.checkArgument(start >= 0 && end >= start,
				"The subtype range is invalid");

		for (ItemStack stack : ItemStackHelper.getItemStackRange(block, start,
				end, 1))
			list.add(stack);
	}

	public static void appendSubtypeRange(List<ItemStack> list,
			ItemStack stack, int start, int end) {

		Preconditions.checkNotNull(stack, "Input ItemStack cannot be null");
		Preconditions.checkArgument(end >= start && start > -1,
				"The subtype range is not valid");

		for (int i = start; i <= end; i++) {
			ItemStack s = stack.copy();
			s.setItemDamage(i);
			list.add(s);
		}
	}

	public static void spawnIntoWorld(World world, ItemStack stack, int x,
			int y, int z) {

		if (stack == null)
			return;

		float f = rand.nextFloat() * 0.8F + 0.1F;
		float f1 = rand.nextFloat() * 0.8F + 0.1F;
		float f2 = rand.nextFloat() * 0.8F + 0.1F;

		while (stack.stackSize > 0) {
			int j = rand.nextInt(21) + 10;

			if (j > stack.stackSize) {
				j = stack.stackSize;
			}

			stack.stackSize -= j;

			EntityItem item = new EntityItem(world, x + f, y + f1, z + f2,
					new ItemStack(stack.getItem(), j, stack.getItemDamage()));

			if (stack.hasTagCompound()) {
				item.getEntityItem().setTagCompound(
						(NBTTagCompound) stack.getTagCompound().copy());
			}

			world.spawnEntityInWorld(item);
		}
	}

	/**
	 * Compresses the inventory array by consolidating stacks toward the
	 * beginning of the array. This operation occurs in place meaning the return
	 * array is the original one passed in.
	 * 
	 * @param inv
	 * @return
	 */
	public static ItemStack[] coelece(ItemStack[] inv) {

		if (inv == null || inv.length == 1) {
			return inv;
		}

		for (int i = 1; i < inv.length; i++) {

			ItemStack stack = inv[i];
			if (stack != null) {

				for (int j = 0; j < i; j++) {

					ItemStack target = inv[j];
					if (target == null) {
						inv[j] = stack;
						inv[i] = null;
						break;
					} else if (ItemHelper.itemsEqualForCrafting(stack, target)) {

						int hold = target.getMaxStackSize() - target.stackSize;

						if (hold >= stack.stackSize) {
							target.stackSize += stack.stackSize;
							inv[i] = null;
							break;
						} else if (hold != 0) {
							stack.stackSize -= hold;
							target.stackSize += hold;
						}
					}
				}
			}
		}

		return inv;
	}

	public static List<ItemStack> coelece(List<ItemStack> inv) {

		if (inv == null || inv.size() == 1) {
			return inv;
		}

		for (int i = 1; i < inv.size(); i++) {

			ItemStack stack = inv.get(i);
			if (stack != null) {

				for (int j = 0; j < i; j++) {

					ItemStack target = inv.get(j);
					if (target == null) {
						inv.set(j, stack);
						inv.set(i, null);
						break;
					} else if (ItemHelper.itemsEqualForCrafting(stack, target)) {

						int hold = target.getMaxStackSize() - target.stackSize;

						if (hold >= stack.stackSize) {
							target.stackSize += stack.stackSize;
							inv.set(i, null);
							break;
						} else if (hold != 0) {
							stack.stackSize -= hold;
							target.stackSize += hold;
						}
					}
				}
			}
		}

		return inv;
	}

	public static void setItemLore(ItemStack stack, String lore) {

		if (stack == null)
			return;

		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();

		NBTTagList l = new NBTTagList();
		l.appendTag(new NBTTagString(lore));
		NBTTagCompound display = new NBTTagCompound();
		display.setTag("Lore", l);

		nbt.setTag("display", display);
		stack.setTagCompound(nbt);

	}

	public static ItemStack asGeneric(ItemStack stack) {
		return asGeneric(stack.getItem());
	}

	public static ItemStack asGeneric(Item item) {
		return new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE);
	}
}
