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

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.UpgradeRecipe;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public final class ProcessingCore extends ItemBase {

	private static final String[] types = new String[] { "decomposition", "extraction" };

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

		if (ItemStackHelper.equals(stack.getItem(), ItemManager.processingCore)) {
			final int level = ItemLevel.getLevel(stack).ordinal();
			if (level > 0) {
				builder.append('_');
				builder.append(level);
			}
		}

		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(final Item par1, final CreativeTabs par2CreativeTabs,
			@SuppressWarnings("rawtypes") final List par3List) {

		par3List.add(new ItemStack(this, 1, CoreType.EXTRACTION.ordinal()));
		par3List.add(ItemLevel.setLevel(new ItemStack(this, 1, CoreType.DECOMPOSITION.ordinal()), ItemLevel.BASIC));
		par3List.add(ItemLevel.setLevel(new ItemStack(this, 1, CoreType.DECOMPOSITION.ordinal()), ItemLevel.HARDENED));
		par3List.add(
				ItemLevel.setLevel(new ItemStack(this, 1, CoreType.DECOMPOSITION.ordinal()), ItemLevel.REINFORCED));
		par3List.add(ItemLevel.setLevel(new ItemStack(this, 1, CoreType.DECOMPOSITION.ordinal()), ItemLevel.RESONANT));

		final ItemStack stack = new ItemStack(this, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(stack, ItemLevel.ETHEREAL);
		ItemStackHelper.makeGlow(stack);
		par3List.add(stack);
	}

	@Override
	public void register() {
		super.register();

		// Basic
		final ItemStack decompCore = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore, ItemLevel.BASIC);
		ShapedOreRecipe recipe = new ShapedOreRecipe(decompCore, " h ", "mMm", "tst", 'h',
				ItemStackHelper.getItemStack("ThermalExpansion:meter").get(), 'm', "ingotIron", 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame").get(), 't', "gearTin", 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material").get());

		GameRegistry.addRecipe(recipe);

		// Hardened
		final ItemStack decompCore1 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore1, ItemLevel.HARDENED);
		recipe = new ShapedOreRecipe(decompCore1, " h ", "mMm", "tst", 'h',
				ItemStackHelper.getItemStack("ThermalExpansion:meter").get(), 'm', "ingotIron", 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame:1").get(), 't', "gearTin", 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material").get());

		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(ItemLevel.HARDENED, decompCore1, "igi", " c ", "i i", 'i', "ingotInvar", 'g',
				"gearElectrum", 'c', decompCore);

		GameRegistry.addRecipe(recipe);

		// Reinforced
		final ItemStack decompCore2 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore2, ItemLevel.REINFORCED);
		recipe = new ShapedOreRecipe(decompCore2, " h ", "mMm", "tst", 'h',
				ItemStackHelper.getItemStack("ThermalExpansion:meter").get(), 'm', "ingotIron", 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame:2").get(), 't', "gearTin", 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material").get());

		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(ItemLevel.REINFORCED, decompCore2, "igi", " c ", "i i", 'i', "blockGlassHardened",
				'g', "gearSignalum", 'c', decompCore1);

		GameRegistry.addRecipe(recipe);

		// Resonant
		final ItemStack decompCore3 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore3, ItemLevel.RESONANT);
		recipe = new ShapedOreRecipe(decompCore3, " h ", "mMm", "tst", 'h',
				ItemStackHelper.getItemStack("ThermalExpansion:meter").get(), 'm', "ingotIron", 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame:3").get(), 't', "gearTin", 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material").get());

		GameRegistry.addRecipe(recipe);

		recipe = new UpgradeRecipe(ItemLevel.RESONANT, decompCore3, "igi", " c ", "i i", 'i', "ingotSilver", 'g',
				"gearEnderium", 'c', decompCore2);

		GameRegistry.addRecipe(recipe);

		// Ethereal - only as an upgraded - can't be directly crafted
		final ItemStack decompCore4 = new ItemStack(ItemManager.processingCore, 1, CoreType.DECOMPOSITION.ordinal());
		ItemLevel.setLevel(decompCore4, ItemLevel.ETHEREAL);
		ItemStackHelper.makeGlow(decompCore4);
		recipe = new UpgradeRecipe(ItemLevel.ETHEREAL, decompCore4, "igi", " c ", "i i", 'i', "ingotPlatinum", 'g',
				Items.nether_star, 'c', decompCore3);

		GameRegistry.addRecipe(recipe);

		recipe = new ShapedOreRecipe(new ItemStack(ItemManager.processingCore, 1, CoreType.EXTRACTION.ordinal()), "cfc",
				"gMg", "csc", 'c', ItemStackHelper.getItemStack("ThermalExpansion:material:3").get(), 'f',
				ItemStackHelper.getItemStack("ThermalExpansion:igniter").get(), 'g', "gearInvar", 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame").get(), 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material").get());

		GameRegistry.addRecipe(recipe);
	}
}
