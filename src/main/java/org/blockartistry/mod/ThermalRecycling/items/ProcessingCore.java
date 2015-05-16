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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;

public class ProcessingCore extends ItemBase {

	public static final int DECOMPOSITION = 0;
	public static final int EXTRACTION = 1;

	public static final int LEVEL_BASIC = 0;
	public static final int LEVEL_HARDENED = 1;
	public static final int LEVEL_REINFORCED = 2;
	public static final int LEVEL_RESONANT = 3;
	
	private static final String[] types = new String[] { "decomposition",
			"extraction" };

	public ProcessingCore() {
		super(types);

		setUnlocalizedName("ProcessingCore");
		setMaxStackSize(1);
	}
	
	protected ItemStack setCoreLevel(ItemStack stack, int level) {
		NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Level", level);
		stack.setTagCompound(nbt);
		return stack;
	}
	
	@Override
	public IIcon getIconFromDamage(int subType) {
		return icons[subType];
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		String result = super.getUnlocalizedName(stack);
		
		if(stack.getItem() == ItemManager.processingCore) {
			int level = ProcessingCorePolicy.getItemLevel(stack);
			if(level > 0)
				result += "_" + level;
		}
		
		return result;
	}
	
	@Override
	public void register() {
		super.register();
		
		// Basic
		ItemStack decompCore = new ItemStack(ItemManager.processingCore, 1, DECOMPOSITION);
		setCoreLevel(decompCore, 0);
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
		ItemStack decompCore1 = new ItemStack(ItemManager.processingCore, 1, DECOMPOSITION);
		setCoreLevel(decompCore1, 1);
		recipe = new ShapedOreRecipe(
				decompCore1,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:1"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(
				decompCore1,
				"igi", " c ", "i i",
				'i', "ingotInvar",
				'g', "gearElectrum",
				'c', decompCore);
		
		GameRegistry.addRecipe(recipe);

		// Reinforced
		ItemStack decompCore2 = new ItemStack(ItemManager.processingCore, 1, DECOMPOSITION);
		setCoreLevel(decompCore2, 2);
		recipe = new ShapedOreRecipe(
				decompCore2,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:2"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(
				decompCore2,
				"igi", " c ", "i i",
				'i', "blockGlassHardened",
				'g', "gearSignalum",
				'c', decompCore1);
		
		GameRegistry.addRecipe(recipe);

		// Resonant
		ItemStack decompCore3 = new ItemStack(ItemManager.processingCore, 1, DECOMPOSITION);
		setCoreLevel(decompCore3, 3);
		recipe = new ShapedOreRecipe(
				decompCore3,
				" h ", "mMm", "tst", 'h',
				Blocks.hopper,
				'm', ItemStackHelper.getItemStack("ThermalExpansion:meter"),
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:3"),
				't', "gearTin",
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));
		
		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(
				decompCore3,
				"igi", " c ", "i i",
				'i', "ingotSilver",
				'g', "gearEnderium",
				'c', decompCore2);
		
		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(
				new ItemStack(ItemManager.processingCore, 1, EXTRACTION),
				"cfc", "gMg", "csc",
				'c', ItemStackHelper.getItemStack("ThermalExpansion:material:3"),
				'f', ItemStackHelper.getItemStack("ThermalExpansion:igniter"),
				'g', "gearInvar",
				'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame"),
				's', ItemStackHelper.getItemStack("ThermalExpansion:material"));

		GameRegistry.addRecipe(recipe);
	}
}
