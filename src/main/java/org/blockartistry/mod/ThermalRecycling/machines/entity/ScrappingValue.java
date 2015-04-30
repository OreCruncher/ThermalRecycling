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

package org.blockartistry.mod.ThermalRecycling.machines.entity;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Preconditions;

import cofh.lib.util.helpers.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Information point about items and their potential value as scrap.  Used
 * by the Thermal Recycler when deciding what to do when it decides to scrap
 * a part or item.
 * 
 * Operationally, any item that is not found in this is considered to have
 * POOR value.  A plugin can alter this by setting it's own value.
 * 
 */
public final class ScrappingValue {
	
	public static final int NONE = 0;
	public static final int POOR = 1;
	public static final int STANDARD = 2;
	public static final int SUPERIOR = 3;

	static HashMap<Item,Integer> scrapValues = null;
	
	static void initialize()
	{
		if(scrapValues != null)
			return;
		
		scrapValues = new HashMap<Item, Integer>();

		// Items
		add(Items.dye, NONE);
		add(Items.arrow, NONE);
		add(Items.blaze_powder, STANDARD);
		add(Items.bone, NONE);
		add(Items.bowl, NONE);
		add(Items.bread, NONE);
		add(Items.cake, POOR);
		add(Items.carrot_on_a_stick, POOR);
		add(Items.clay_ball, NONE);
		add(Items.cookie, NONE);
		add(Items.diamond, SUPERIOR);
		add(Items.emerald, SUPERIOR);
		add(Items.nether_star, SUPERIOR);
		add(Items.ender_eye, STANDARD);
		add(Items.fermented_spider_eye, NONE);
		add(Items.flint_and_steel, NONE);
		add(Items.melon_seeds, NONE);
		add(Items.pumpkin_seeds, NONE);
		add(Items.pumpkin_pie, NONE);
		add(Items.sugar, NONE);
		add(Items.gunpowder, POOR);
		add(Items.chainmail_boots, STANDARD);
		add(Items.chainmail_chestplate, STANDARD);
		add(Items.chainmail_helmet, STANDARD);
		add(Items.chainmail_leggings, STANDARD);
		add(Items.map, NONE);
		add(Items.coal, NONE);
		add(Items.redstone, NONE);
		add(Items.lead, NONE);
		add(Items.stone_axe, NONE);
		add(Items.stone_hoe, NONE);
		add(Items.stone_pickaxe, NONE);
		add(Items.stone_shovel, NONE);
		add(Items.stone_sword, NONE);
		add(Items.stick, NONE);
		add(Items.wooden_axe, NONE);
		add(Items.wooden_hoe, NONE);
		add(Items.wooden_pickaxe, NONE);
		add(Items.wooden_shovel, NONE);
		add(Items.wooden_sword, NONE);
		
		// Blocks
		add(Blocks.acacia_stairs, NONE);
		add(Blocks.birch_stairs, NONE);
		add(Blocks.spruce_stairs, NONE);
		add(Blocks.jungle_stairs, NONE);
		add(Blocks.brick_block, NONE);
		add(Blocks.oak_stairs, NONE);
		add(Blocks.brick_stairs, NONE);
		add(Blocks.wooden_slab, NONE);
		add(Blocks.cobblestone, NONE);
		add(Blocks.cobblestone_wall, NONE);
		add(Blocks.dark_oak_stairs, NONE);
		add(Blocks.dirt, NONE);
		add(Blocks.mossy_cobblestone, NONE);
		add(Blocks.planks, NONE);
		add(Blocks.fence, NONE);
		add(Blocks.fence_gate, NONE);
		add(Blocks.stone, NONE);
		add(Blocks.stone_brick_stairs, NONE);
		add(Blocks.stone_button, NONE);
		add(Blocks.stone_stairs, NONE);
		add(Blocks.stonebrick, NONE);
		add(Blocks.tnt, POOR);
		add(Blocks.hay_block, NONE);
		add(Blocks.coal_block, POOR);
		add(Blocks.carpet, NONE);
		add(Blocks.hardened_clay, NONE);
		add(Blocks.stained_hardened_clay, NONE);
		add(Blocks.glass, NONE);
		add(Blocks.glass_pane, NONE);
		add(Blocks.stained_glass, NONE);
		add(Blocks.stained_glass_pane, NONE);
	}

	public static int getValue(Item item) {
		initialize();
		Preconditions.checkNotNull(item);
		Integer result = scrapValues.get(item);
		return result == null ? POOR : result;
	}
	
	public static int getValue(Block block) {
		Preconditions.checkNotNull(block);
		return getValue(Item.getItemFromBlock(block));
	}
	
	public static int getValue(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return getValue(stack.getItem());
	}
	
	public static void add(Item item, int value) {
		initialize();
		Preconditions.checkNotNull(item);
		Preconditions.checkArgument(value >= NONE && value <= SUPERIOR);
		scrapValues.put(item,value);
	}
	
	public static void add(ItemStack stack, int value) {
		add(stack.getItem(), value);
	}
	
	public static void add(Block block, int value) {
		Preconditions.checkNotNull(block);
		add(Item.getItemFromBlock(block), value);
	}
}
