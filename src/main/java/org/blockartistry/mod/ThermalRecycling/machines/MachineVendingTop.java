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

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class MachineVendingTop extends Block {

	public MachineVendingTop() {
		super(Material.iron);

		setBlockName("MachineVendingTop");
		// setCreativeTab(CreativeTabManager.tab);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
	}

	@Override
	public int quantityDropped(final Random random) {
		// Prevent the vending machine top from dropping
		return 0;
	}

	@Override
	public void breakBlock(final World world, final int x, final int y, final int z, final Block oldBlock,
			final int oldMeta) {

		// Reflect to the vending machine base block
		final int baseY = y - 1;
		final Block vending = world.getBlock(x, baseY, z);
		if (vending == BlockManager.vending) {
			vending.dropBlockAsItem(world, x, baseY, z, world.getBlockMetadata(x, baseY, z), 0);
			world.setBlockToAir(x, baseY, z);
		}

		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player,
			final int side, final float a, final float b, final float c) {

		// Reflect to the base vending machine block - it has all the smarts
		return world.getBlock(x, y - 1, z).onBlockActivated(world, x, y - 1, z, player, side, a, b, c);
	}

	// This makes our gag invisible.
	@Override
	public boolean shouldSideBeRendered(IBlockAccess iblockaccess, int i, int j, int k, int l) {
		return false;
	}

	// This tells minecraft to render surrounding blocks.
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final int side, final int metadata) {
		return BlockManager.vending.getIcon(side, metadata);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		return BlockManager.vending.getIcon(world, x, y, z, side);
	}

	public void register() {
		GameRegistry.registerBlock(this, "MachineVendingTop");
	}
}
