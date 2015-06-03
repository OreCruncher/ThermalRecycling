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

import org.blockartistry.mod.ThermalRecycling.CreativeTabManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public class PileOfRubble extends Block {

	private static final Random random = new XorShiftRandom();
	private static final String CHEST_PILE_OF_RUBBLE = "pileOfRubble";
	private static final ChestGenHooks rubbleContent = ChestGenHooks
			.getInfo(CHEST_PILE_OF_RUBBLE);

	static {

		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.cobblestone), 0, 1, 4, 12));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.stone), 0, 1, 2, 9));
		rubbleContent.addItem(new WeightedRandomChestContent(Items.coal, 0, 1,
				3, 8));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.gravel), 0, 1, 2, 9));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.sand), 0, 1, 2, 8));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.dirt), 0, 1, 3, 10));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.clay), 0, 1, 1, 7));

		rubbleContent.addItem(new WeightedRandomChestContent(Items.bread, 0, 1,
				3, 8));
		rubbleContent.addItem(new WeightedRandomChestContent(Items.cooked_beef,
				0, 1, 3, 6));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.torch), 0, 1, 8, 8));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.iron_ore), 0, 1, 3, 5));
		rubbleContent.addItem(new WeightedRandomChestContent(ItemStackHelper
				.getItemStack("ThermalFoundation:Ore:0"), 1, 3, 5));
		rubbleContent.addItem(new WeightedRandomChestContent(ItemStackHelper
				.getItemStack("ThermalFoundation:Ore:1"), 1, 3, 5));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.gold_ore), 0, 1, 2, 3));
		rubbleContent.addItem(new WeightedRandomChestContent(Items.redstone, 0,
				1, 2, 3));
		rubbleContent.addItem(new WeightedRandomChestContent(Items.diamond, 0,
				1, 1, 1));
		rubbleContent.addItem(new WeightedRandomChestContent(Item
				.getItemFromBlock(Blocks.tnt), 0, 1, 1, 4));
		rubbleContent.addItem(new WeightedRandomChestContent(
				Items.iron_pickaxe, 0, 1, 1, 4));
		rubbleContent.addItem(new WeightedRandomChestContent(Items.iron_helmet,
				0, 1, 1, 3));

		rubbleContent.addItem(new WeightedRandomChestContent(
				ItemManager.recyclingScrap, 0, 1, 2, 5));
		rubbleContent.addItem(new WeightedRandomChestContent(
				ItemManager.recyclingScrapBox, 0, 1, 1, 2));
		rubbleContent.addItem(new WeightedRandomChestContent(
				ItemManager.recyclingScrap, 1, 1, 2, 4));
		rubbleContent.addItem(new WeightedRandomChestContent(
				ItemManager.recyclingScrapBox, 1, 1, 1, 1));
	}

	@SideOnly(Side.CLIENT)
	protected IIcon icon;

	public PileOfRubble() {
		super(Material.rock);

		setBlockName("PileOfRubble");
		setCreativeTab(CreativeTabManager.tab);

		setHardness(5.0F);
		setResistance(10.0F);
		setStepSound(soundTypeStone);
		setHarvestLevel("pickaxe", 3);

		setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.375F, 0.9375F);

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
		// http://www.minecraftforum.net/forums/archive/tutorials/928513-creating-mods-mcp-getrendertype
		return 6; // Crops
	}

	@Override
	protected boolean canSilkHarvest() {
		// No silk touch - gotta break it
		return false;
	}

	@Override
	public int quantityDropped(Random random) {
		// Prevent the Pile of Scrap from dropping
		return 0;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block,
			int meta) {

		if (!world.isRemote) {
			final int dropCount = ModOptions.getRubblePileDropCount();
			for (int i = 0; i < dropCount; i++) {
				final ItemStack stack = rubbleContent.getOneItem(random);
				if (stack != null) {
					ItemStackHelper.spawnIntoWorld(world, stack, x, y, z);
				}

			}
		}
		super.breakBlock(world, x, y, z, block, meta);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		icon = iconRegister.registerIcon(ThermalRecycling.MOD_ID
				+ ":pileOfRubble");
	}

	// http://www.minecraftforge.net/forum/index.php/topic,13626.0.html

	@SideOnly(Side.CLIENT)
	@Override
	public IIcon getIcon(final int side, int metadata) {
		return icon;
	}

	public boolean canBlockStay(World world, int x, int y, int z) {
		// Make sure the block underneath is solid
		return world.getBlock(x, y - 1, z).getMaterial().isSolid();
	}

	public void register() {
		GameRegistry.registerBlock(this, getUnlocalizedName());
	}
}
