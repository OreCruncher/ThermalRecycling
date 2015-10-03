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
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;
import org.blockartistry.mod.ThermalRecycling.machines.entity.BatteryRackTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class MachineBatteryRack extends MachineBase {

	@SideOnly(Side.CLIENT)
	protected IIcon connectionPort;
	
	@SideOnly(Side.CLIENT)
	protected IIcon connectionBlank;

	@SideOnly(Side.CLIENT)
	protected int renderPass;
	
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

		connectionPort = iconRegister.registerIcon("thermalexpansion:config/Config_Open");
		connectionBlank = iconRegister.registerIcon("thermalexpansion:config/Config_None");
}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean canRenderInPass(final int pass) {
		renderPass = pass;
		return pass < 2;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public int getRenderBlockPass() {
		return 1;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isOpaqueCube() {
		return false;
	}
			
	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		
		if(renderPass == 0)
			return super.getIcon(world, x, y, z, side);

		if(side == 0 || side == 1)
			return connectionPort;
		
		final BatteryRackTileEntity te = (BatteryRackTileEntity)world.getTileEntity(x, y, z);
		if(te != null && ForgeDirection.OPPOSITES[te.getFacing()] == side)
			return connectionPort;
		
		return connectionBlank;
	}

	@Override
	public void register() {
		super.register();
		GameRegistry.registerTileEntity(BatteryRackTileEntity.class,
				"batteryRackTileEntity");

		for(int i = ItemLevel.BASIC.ordinal(); i <= ItemLevel.BASIC.ordinal(); i++) {
			final ItemStack stack = new ItemStack(BlockManager.batteryRack);
			ItemLevel.setLevel(stack, ItemLevel.values()[i]);
			final ShapedOreRecipe recipe = new ShapedOreRecipe(
					stack,
					" T ",
					"iMi",
					"rcr",
					'T',
					ItemStackHelper.getItemStack("ThermalExpansion:material:2"),
					'M', ItemStackHelper.getItemStack("ThermalExpansion:Frame:" + i),
					'c',
					"ingotCopper",
					'i', "ingotIron",
					'r', "dustRedstone");
	
			GameRegistry.addRecipe(recipe);
		}
	}
}
