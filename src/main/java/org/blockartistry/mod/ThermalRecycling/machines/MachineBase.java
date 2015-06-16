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
import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.util.FluidStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cofh.api.item.IToolHammer;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class MachineBase extends BlockContainer {

	private static ItemStack lockTool = ItemStackHelper
			.getItemStack("ThermalExpansion:material:16");
	
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

	public MachineBase(final String name) {
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

	protected static boolean holdingRotateTool(final EntityPlayer player) {
		final ItemStack item = player.getCurrentEquippedItem();
		if (item == null)
			return false;

		return item.getItem() instanceof IToolHammer;
	}

	protected static boolean holdingLockTool(final EntityPlayer player) {
		final ItemStack item = player.getCurrentEquippedItem();
		if (item == null)
			return false;

		return ItemStackHelper.areEqual(item, lockTool);
	}
	
	protected static boolean holdingDyeTool(final EntityPlayer player) {
		final ItemStack item = player.getCurrentEquippedItem();
		return item != null && DyeHelper.isDye(item);
	}

	/*
	 * Forward the block activated request to the corresponding TileEntity
	 */
	@Override
	public boolean onBlockActivated(final World world, final int x,
			final int y, final int z, final EntityPlayer player,
			final int side, final float a, final float b, final float c) {

		if (!world.isRemote) {
			final TileEntity te = world.getTileEntity(x, y, z);

			if (!(te instanceof TileEntityBase))
				// Returns false so it doesn't update anything
				return false;

			if (te instanceof IMachineFluidHandler)
				if (FluidStackHelper.applyPlayerContainerInteraction(world, te,
						player))
					return true;

			final TileEntityBase entity = (TileEntityBase) te;

			// If the player is holding a tool rotate
			if (holdingRotateTool(player)) {
				return entity.rotateBlock();
			} else if (entity.isLockable(player) && holdingLockTool(player)) {
				return entity.toggleLock();
			} else if(entity.isNameColorable(player) && holdingDyeTool(player)) {
				final int color = DyeHelper.getDyeColor(player.getCurrentEquippedItem());
				if(entity.getNameColor() == color)
					entity.setNameBackgroundColor(color);
				else
					entity.setNameColor(color);
			} else {
				return entity.onBlockActivated(world, x, y, z, player, side, a,
						b, c);
			}
		}

		return true;
	}

	@Override
	public void breakBlock(final World world, final int x, final int y,
			final int z, final Block oldBlock, final int oldMeta) {
		if (!world.isRemote) {
			final TileEntity te = world.getTileEntity(x, y, z);
			if (te instanceof TileEntityBase) {
				((TileEntityBase) te).dropInventory(world, x, y, z);
			}
		}

		super.breakBlock(world, x, y, z, oldBlock, oldMeta);
	}

	@Override
	public int getLightValue(final IBlockAccess world, final int x,
			final int y, final int z) {
		int meta = world.getBlockMetadata(x, y, z);
		meta = meta & META_ACTIVE_INDICATOR;

		return meta != 0 ? 8 : 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(final World world, final int x, final int y,
			final int z, final Random rand) {

		final TileEntity te = world.getTileEntity(x, y, z);

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
	public void registerBlockIcons(final IIconRegister iconRegister) {

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

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final int side, final int metadata) {

		// BLOCK_BOTTOM and BLOCK_TOP
		if (side == 0 || side == 1)
			return icons[side];

		// If metadata is 0 it means we are rendering in
		// inventory.
		if (metadata == 0 && side == 3)
			return icons[BLOCK_FRONT];

		final int meta = metadata & ~META_ACTIVE_INDICATOR;

		if (side != meta)
			return icons[BLOCK_SIDE];

		final boolean isActive = (metadata & META_ACTIVE_INDICATOR) != 0;
		return icons[isActive ? BLOCK_ACTIVE : BLOCK_FRONT];
	}

	// Map the MathHelper result into metadata
	private static final int[] dirMap = new int[] { 2, 5, 3, 4 };

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y,
			final int z, final EntityLivingBase entity,
			final ItemStack p_149689_6_) {

		// From the furnace code
		final int i = MathHelper
				.floor_double((double) ((entity.rotationYaw * 4F) / 360F) + 0.5D) & 3;
		world.setBlockMetadataWithNotify(x, y, z, dirMap[i], 2);
	}

	public void register() {
		GameRegistry.registerBlock(this, myUnlocalizedName);
	}

	@SideOnly(Side.CLIENT)
	public void registerRenderer() {
	}
}