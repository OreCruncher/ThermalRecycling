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

package org.blockartistry.mod.ThermalRecycling.support;

import java.util.ArrayList;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Used by the startup routine to determine which recipes to ignore
 * right off the bat.  This does not mean those items can't be
 * recycled, just means that by default the recipes are not included.
 */
public final class RecipeBlacklist {

	static ArrayList<Item> blacklist = null;
	
	static void initialize()
	{
		if(blacklist != null)
			return;
		
		blacklist = new ArrayList<Item>();
		
		// Items
		add(Items.dye);
		add(Items.arrow);
		add(Items.blaze_powder);
		add(Items.bone);
		add(Items.bowl);
		add(Items.bread);
		add(Items.cake);
		add(Items.carrot_on_a_stick);
		add(Items.clay_ball);
		add(Items.cookie);
		add(Items.diamond);
		add(Items.emerald);
		add(Items.ender_eye);
		add(Items.fermented_spider_eye);
		add(Items.flint_and_steel);
		add(Items.melon_seeds);
		add(Items.pumpkin_seeds);
		add(Items.pumpkin_pie);
		add(Items.sugar);
		add(Items.gunpowder);
		add(Items.chainmail_boots);
		add(Items.chainmail_chestplate);
		add(Items.chainmail_helmet);
		add(Items.chainmail_leggings);
		add(Items.map);
		add(Items.coal);
		add(Items.redstone);
		add(Items.lead);
		add(Items.stone_axe);
		add(Items.stone_hoe);
		add(Items.stone_pickaxe);
		add(Items.stone_shovel);
		add(Items.stone_sword);
		add(Items.stick);
		add(Items.wooden_axe);
		add(Items.wooden_hoe);
		add(Items.wooden_pickaxe);
		add(Items.wooden_shovel);
		add(Items.wooden_sword);
		
		// Blocks
		add(Blocks.acacia_stairs);
		add(Blocks.birch_stairs);
		add(Blocks.spruce_stairs);
		add(Blocks.jungle_stairs);
		add(Blocks.brick_block);
		add(Blocks.oak_stairs);
		add(Blocks.wooden_slab);
		add(Blocks.brick_stairs);
		add(Blocks.cobblestone);
		add(Blocks.cobblestone_wall);
		add(Blocks.dark_oak_stairs);
		add(Blocks.dirt);
		add(Blocks.mossy_cobblestone);
		add(Blocks.planks);
		add(Blocks.fence);
		add(Blocks.fence_gate);
		add(Blocks.stone);
		add(Blocks.stone_brick_stairs);
		add(Blocks.stone_button);
		add(Blocks.stone_stairs);
		add(Blocks.stonebrick);
		add(Blocks.tnt);
		add(Blocks.hay_block);
		add(Blocks.coal_block);
		add(Blocks.carpet);
		add(Blocks.hardened_clay);
		add(Blocks.stained_hardened_clay);
		add(Blocks.glass);
		add(Blocks.glass_pane);
		add(Blocks.stained_glass);
		add(Blocks.stained_glass_pane);
	}

	protected static boolean isOreDictionaryType(ItemStack stack) {
		return ItemHelper.isBlock(stack) || ItemHelper.isDust(stack) || ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack);
	}
	
	public static boolean isListed(Item item) {
		initialize();
		return blacklist.contains(item);
	}
	
	public static boolean isListed(Block block) {
		initialize();
		return blacklist.contains(Item.getItemFromBlock(block));
	}
	
	public static boolean isListed(ItemStack stack) {
		return isOreDictionaryType(stack) || isListed(stack.getItem());
	}
	
	public static void add(Item item) {
		initialize();
		blacklist.add(item);
	}
	
	public static void add(ItemStack stack) {
		initialize();
		blacklist.add(stack.getItem());
	}
	
	public static void add(Block block) {
		initialize();
		blacklist.add(Item.getItemFromBlock(block));
	}
}
