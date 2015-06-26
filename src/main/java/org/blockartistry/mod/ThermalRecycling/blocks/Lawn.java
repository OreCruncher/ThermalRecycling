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

package org.blockartistry.mod.ThermalRecycling.blocks;

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.CreativeTabManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.ForgeDirection;

public final class Lawn extends BlockGrass {

	private static final int NORTH_SOUTH = 0;
	private static final int EAST_WEST = 1;
	private static final int TOP = 1;
	
	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;
	
	public Lawn() {
		super();

		setBlockName("Lawn");
		setBlockTextureName("lawn");
		setHardness(0.6F);
		setStepSound(soundTypeGrass);
		setCreativeTab(CreativeTabManager.tab);
	}

	@Override
    public boolean canSustainPlant(IBlockAccess world, int x, int y, int z, ForgeDirection direction, IPlantable plantable) {
    	return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister register) {
    	icons = new IIcon[2];
    	icons[NORTH_SOUTH] = register.registerIcon(ThermalRecycling.MOD_ID + ":lawn_northsouth");
    	icons[EAST_WEST] = register.registerIcon(ThermalRecycling.MOD_ID + ":lawn_eastwest");
    }

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int meta) {
		
		if(side == TOP)
			return icons[meta];
		
		return Blocks.grass.getIcon(side, meta);
	}
	
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    	
    	if(side == TOP) {
    		return icons[world.getBlockMetadata(x, y, z)];
    	}
    	
    	return Blocks.grass.getIcon(world, x, y, z, side);
    }
    
    public void setLawn(final World world, final int x, final int y, final int z, final EntityLivingBase entity) {
    	final Block block = world.getBlock(x, y, z);
    	if(block instanceof BlockGrass || block instanceof Lawn) {
    		final int i = MathHelper
    				.floor_double((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
    		world.setBlock(x, y, z, BlockManager.lawn, dirMap[i], 2);
    		world.markBlockForUpdate(x, y, z);
    	}
    }

	// Map the MathHelper result into metadata
	private static final int[] dirMap = new int[] { NORTH_SOUTH, EAST_WEST, NORTH_SOUTH, EAST_WEST };

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y,
			final int z, final EntityLivingBase entity,
			final ItemStack p_149689_6_) {

		// From the furnace code
		final int i = MathHelper
				.floor_double((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, dirMap[i], 2);
	}

	// No bone meal
	@Override
    public boolean func_149851_a(World world, int x, int y, int z, boolean isRemote) {
    	return false;
    }

	// No bone meal
	@Override
    public boolean func_149852_a(World world, Random random, int x, int y, int z) {
    	return false;
    }

    public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
}
