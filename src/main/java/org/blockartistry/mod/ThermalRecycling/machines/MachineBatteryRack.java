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

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.entity.BatteryRackTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class MachineBatteryRack extends MachineBase {

	@SideOnly(Side.CLIENT)
	private IIcon backPort;
	
	public MachineBatteryRack() {
		super("MachineBatteryRack");
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_,
			final int p_149915_2_) {
		return new BatteryRackTileEntity();
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
		final TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof BatteryRackTileEntity)
			((BatteryRackTileEntity)te).onNeighborBlockChange(world, x, y, z, block);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {

		super.registerBlockIcons(iconRegister);
		icons[BLOCK_FRONT] = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":BatteryRack_Front");
		icons[BLOCK_ACTIVE] = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":BatteryRack_Front");
	}

	@Override
	public void register() {
		super.register();
		GameRegistry.registerTileEntity(BatteryRackTileEntity.class,
				"batteryRackTileEntity");

		final ShapedOreRecipe recipe = new ShapedOreRecipe(
				BlockManager.batteryRack,
				" T ",
				"bcb",
				"iri",
				'T',
				ItemStackHelper.getItemStack("ThermalExpansion:material:2"),
				'b',
				Blocks.iron_bars,
				'c',
				"ingotCopper",
				'i', "ingotIron",
				'r', "dustRedstone");

		GameRegistry.addRecipe(recipe);
	}
}
