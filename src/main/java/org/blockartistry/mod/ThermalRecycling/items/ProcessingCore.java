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

package org.blockartistry.mod.ThermalRecycling.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.UpgradeRecipe;

import cpw.mods.fml.common.registry.GameRegistry;

public final class ProcessingCore extends ItemBase {
	
	private static final String[] types = new String[] { "decomposition",
			"extraction" };

	public ProcessingCore() {
		super(types);

		setUnlocalizedName("ProcessingCore");
		setMaxStackSize(1);
	}

	@Override
	public IIcon getIconFromDamage(final int subType) {
		return icons[subType];
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		final StringBuilder builder = new StringBuilder(32);
		builder.append(super.getUnlocalizedName(stack));
		
		if(stack.getItem() == ItemManager.processingCore) {
			final int level = ItemLevel.getLevel(stack).ordinal();
			if(level > 0) {
				builder.append('_');
				builder.append(level);
			}
		}
		
		return builder.toString();
	}
	
	@Override
	public void register() {
		super.register();
		
		// Basic
		final ItemStack decompCore = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore, ItemLevel.BASIC);
		ShapedOreRecipe recipe = new ShapedOreRecipe(
				decompCore,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		// Hardened
		final ItemStack decompCore1 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore1, ItemLevel.HARDENED);
		recipe = new ShapedOreRecipe(
				decompCore1,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:1"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(
				ItemLevel.HARDENED,
				decompCore1,
				"igi", " c ", "i i",
				'i', "ingotInvar",
				'g', "gearElectrum",
				'c', decompCore);
		
		GameRegistry.addRecipe(recipe);

		// Reinforced
		final ItemStack decompCore2 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore2, ItemLevel.REINFORCED);
		recipe = new ShapedOreRecipe(
				decompCore2,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:2"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(
				ItemLevel.REINFORCED,
				decompCore2,
				"igi", " c ", "i i",
				'i', "blockGlassHardened",
				'g', "gearSignalum",
				'c', decompCore1);
		
		GameRegistry.addRecipe(recipe);

		// Resonant
		final ItemStack decompCore3 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore3, ItemLevel.RESONANT);
		recipe = new ShapedOreRecipe(
				decompCore3,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:3"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(
				ItemLevel.RESONANT,
				decompCore3,
				"igi", " c ", "i i",
				'i', "ingotSilver",
				'g', "gearEnderium",
				'c', decompCore2);

		GameRegistry.addRecipe(recipe);

		// Ethereal - only as an upgraded - can't be directly crafted
		final ItemStack decompCore4 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore4, ItemLevel.ETHEREAL);
		ItemStackHelper.makeGlow(decompCore4);
		recipe = new UpgradeRecipe(
				ItemLevel.ETHEREAL,
				decompCore4,
				"igi", " c ", "i i",
				'i', "ingotPlatinum",
				'g', Items.nether_star,
				'c', decompCore3);

		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(
				new ItemStack(ItemManager.processingCore, 1, CoreType.EXTRACTION.ordinal()),
				"cfc", "gMg", "csc",
				'c', ItemStackHelper.getItemStack("ThermalExpansion:material:3"),
				'f', ItemStackHelper.getItemStack("ThermalExpansion:igniter"),
				'g', "gearInvar",
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame"),
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));

		GameRegistry.addRecipe(recipe);
	}
}
