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
import org.blockartistry.mod.ThermalRecycling.machines.entity.TileEntityBase;
import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.machines.entity.renderers.VendingTileEntityRenderer;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class MachineVending extends MachineBase {

	public MachineVending() {
		super("MachineVending");

		// 1x1x2 blocks high
		setBlockBounds(0f, 0f, 0f, 1f, 2f, 1f);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int meta) {
		return new VendingTileEntity();
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
		
		// Clear out the top gag block
		final int gagY = y + 1;
		if(!world.isRemote && world.getBlock(x, gagY, z) == BlockManager.vendingTop)
			world.setBlockToAir(x, gagY, z);
	}

	@Override
	public void onBlockPlacedBy(final World world, final int x, final int y,
			final int z, final EntityLivingBase entity,
			final ItemStack p_149689_6_) {
		
		super.onBlockPlacedBy(world, x, y, z, entity, p_149689_6_);
		world.setBlock(x, y + 1, z, BlockManager.vendingTop);
	}

	@Override
	public boolean canPlaceBlockAt(final World world, final int x, final int y,
			final int z) {
		// Both target block and the one above need to be replaceable
		return world.getBlock(x, y, z).isReplaceable(world, x, y, z)
				&& world.getBlock(x, y + 1, z)
						.isReplaceable(world, x, y + 1, z);
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void register() {
		super.register();

		GameRegistry.registerTileEntity(VendingTileEntity.class,
				"vendingTileEntity");

		final ShapedOreRecipe recipe = new ShapedOreRecipe(
				BlockManager.vending, "PPP", "P P", "GSG", 'P', "plankWood",
				'G', "gearCopper", 'S',
				ItemStackHelper.getItemStack("ThermalExpansion:Strongbox:2"));

		GameRegistry.addRecipe(recipe);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerRenderer() {
		final VendingTileEntityRenderer renderer = new VendingTileEntityRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(VendingTileEntity.class,
				renderer);
		MinecraftForgeClient.registerItemRenderer(
				Item.getItemFromBlock(BlockManager.vending), renderer);
	}
}
