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
import org.blockartistry.mod.ThermalRecycling.ItemManager;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnergeticRedstoneOre extends BlockRedstoneOre {

	public EnergeticRedstoneOre() {
		// Always give the nice sparklies
		super(true);
		
		setBlockName("EnergeticRedstoneOre");
		setLightLevel(0.625F);
		setHardness(3F);
		setResistance(5F);
		setStepSound(soundTypePiston);
		setCreativeTab(CreativeTabManager.tab);
	}
	
	// Override some of the redstone block routines to make things a bit more efficient
	@Override
	public void onBlockClicked(World p_onBlockClicked_1_, int p_onBlockClicked_2_, int p_onBlockClicked_3_,
			int p_onBlockClicked_4_, EntityPlayer p_onBlockClicked_5_) {
	}

	@Override
	public void onEntityWalking(World p_onEntityWalking_1_, int p_onEntityWalking_2_, int p_onEntityWalking_3_,
			int p_onEntityWalking_4_, Entity p_onEntityWalking_5_) {
	}

	@Override
	public boolean onBlockActivated(World p_onBlockActivated_1_, int p_onBlockActivated_2_, int p_onBlockActivated_3_,
			int p_onBlockActivated_4_, EntityPlayer p_onBlockActivated_5_, int p_onBlockActivated_6_,
			float p_onBlockActivated_7_, float p_onBlockActivated_8_, float p_onBlockActivated_9_) {
		return false;
	}

	@Override
	public void updateTick(World p_updateTick_1_, int p_updateTick_2_, int p_updateTick_3_, int p_updateTick_4_,
			Random p_updateTick_5_) {
	}
	
	@Override
	protected ItemStack createStackedBlock(int p_createStackedBlock_1_) {
		return new ItemStack(BlockManager.energeticRedstone);
	}

	@Override
	public Item getItemDropped(int p_getItemDropped_1_, Random p_getItemDropped_2_, int p_getItemDropped_3_) {
		return ItemManager.energeticRedstoneDust;
	}

	public void registerBlockIcons(IIconRegister p_registerBlockIcons_1_) {
		blockIcon = p_registerBlockIcons_1_.registerIcon("minecraft:redstone_ore");
	}
	
    public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
}
