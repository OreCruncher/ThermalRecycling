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

import org.blockartistry.mod.ThermalRecycling.CreativeTabManager;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.entity.IMachineFluidHandler;
import org.blockartistry.mod.ThermalRecycling.machines.entity.TileEntityBase;
import org.blockartistry.mod.ThermalRecycling.util.FluidStackHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class MachineBase extends BlockContainer {

	public static int BLOCK_BOTTOM = 0;
	public static int BLOCK_TOP = 1;
	public static int BLOCK_SIDE = 2;
	public static int BLOCK_FRONT = 3;
	public static int BLOCK_ACTIVE = 4;

	public static int META_ACTIVE_INDICATOR = 8;

	@SideOnly(Side.CLIENT)
	protected IIcon[] icons;

	public final String[] names;
	public final String myUnlocalizedName;

	protected int facing;

	public MachineBase(String name) {
		super(Material.iron);

		myUnlocalizedName = name;
		names = new String[1];
		names[0] = name;

		setBlockName(name);
		setCreativeTab(CreativeTabManager.tab);
		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeMetal);
		setHarvestLevel("pickaxe", 3);
	}

	/*
	 * Forward the block activated request to the corresponding TileEntity
	 */
	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float a, float b, float c) {

		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(x, y, z);

			if (!(te instanceof TileEntityBase) || player.isSneaking())
				// Returns false so it doesn't update anything
				return false;
			
			if(te instanceof IMachineFluidHandler)
				if(FluidStackHelper.applyPlayerContainerInteraction(world, te, player))
					return true;

			return ((TileEntityBase) te).onBlockActivated(world, x, y, z,
					player, side, a, b, c);
		}

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block oldBlock,
			int oldMeta) {
		if (!world.isRemote) {
			TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileEntityBase) {
				((TileEntityBase) te).dropInventory(world, x, y, z);
			}
		}

		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	@Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		meta = meta & META_ACTIVE_INDICATOR;

		return meta != 0 ? 8 : 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {

		TileEntity te = world.getTileEntity(x, y, z);

		if (!(te instanceof TileEntityBase))
			return;

		((TileEntityBase) te).randomDisplayTick(world, x, y, z, rand);
	}

	/**
	 * Derived class should override this method to ensure that BLOCK_FRONT is
	 * set to an appropriate icon. Otherwise the thermalRecycler will have a
	 * side texture for the front.
	 * 
	 * @param iconRegister
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {

		icons = new IIcon[5];
		icons[BLOCK_BOTTOM] = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":Machine_bottom");
		icons[BLOCK_TOP] = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":Machine_top");
		icons[BLOCK_SIDE] = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":Machine_side");

		if (icons[BLOCK_FRONT] == null)
			icons[BLOCK_FRONT] = icons[BLOCK_SIDE];
		if (icons[BLOCK_ACTIVE] == null)
			icons[BLOCK_ACTIVE] = icons[BLOCK_FRONT];
	}

	// http://www.minecraftforge.net/forum/index.php/topic,13626.0.html

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(int side, int metadata) {
		// Take off bit 4 since that indicates active
		int frontId = BLOCK_FRONT;

		if ((metadata & META_ACTIVE_INDICATOR) != 0)
			frontId = BLOCK_ACTIVE;

		metadata &= ~META_ACTIVE_INDICATOR;

		if (side == 0 || side == 1)
			return icons[side];

		if (metadata == 2 && side == 2)
			return icons[frontId];
		else if (metadata == 3 && side == 5)
			return icons[frontId];
		else if (metadata == 0 && side == 3)
			return icons[frontId];
		else if (metadata == 1 && side == 4)
			return icons[frontId];

		return icons[BLOCK_SIDE];
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z,
			EntityLivingBase entity, ItemStack p_149689_6_) {
		int whichDirectionFacing = MathHelper
				.floor_double(entity.rotationYaw * 4.0F / 360.0F + 2.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, whichDirectionFacing, 2);
	}

	public void register() {
		GameRegistry.registerBlock(this, myUnlocalizedName);
	}
}