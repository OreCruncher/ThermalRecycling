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

package org.blockartistry.mod.ThermalRecycling.machines;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ScrapAssessorTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MachineScrapAssessor extends MachineBase {

	public MachineScrapAssessor() {
		super("MachineScrapAssessor");
	}

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new ScrapAssessorTileEntity();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {

		super.registerBlockIcons(iconRegister);
		icons[BLOCK_TOP] = iconRegister
				.registerIcon("minecraft:crafting_table_top");
	}

	@Override
	public void register() {
		super.register();
		GameRegistry.registerTileEntity(ScrapAssessorTileEntity.class,
				"scrapAssessorTileEntity");

		ShapedOreRecipe recipe = new ShapedOreRecipe(new ItemStack(
				BlockManager.scrapAssessor, 1, 0), " m ", "cMc", "tst", 'c',
				new ItemStack(Blocks.crafting_table), 'm',
				ItemStackHelper.getItemStack("ThermalExpansion:meter"), 'M',
				ItemStackHelper.getItemStack("ThermalExpansion:Frame"), 't',
				"gearTin", 's',
				ItemStackHelper.getItemStack("ThermalExpansion:material"));

		GameRegistry.addRecipe(recipe);
	}
}
