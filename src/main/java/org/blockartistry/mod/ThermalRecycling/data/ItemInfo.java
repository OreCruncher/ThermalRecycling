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

package org.blockartistry.mod.ThermalRecycling.data;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;

import cofh.lib.util.helpers.ItemHelper;

import com.google.common.base.Preconditions;

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
 * ScrapValue.POOR value.  A plugin can alter this by setting it's own value.
 * 
 */
public final class ItemInfo {
	
	static HashMap<Item, Data> itemData = null;
	
	static void initialize()
	{
		if(itemData != null)
			return;
		
		itemData = new HashMap<Item, Data>();

		// Items
		put(Items.dye, ScrapValue.NONE, true, false);
		put(Items.arrow, ScrapValue.NONE, true, false);
		put(Items.blaze_powder, ScrapValue.STANDARD, true, false);
		put(Items.bone, ScrapValue.NONE, true, false);
		put(Items.bowl, ScrapValue.NONE, true, false);
		put(Items.bread, ScrapValue.NONE, true, true);
		put(Items.wheat, ScrapValue.NONE, true, false);
		put(Items.apple, ScrapValue.NONE, true, true);
		put(Items.egg, ScrapValue.NONE, true, true);
		put(Items.cake, ScrapValue.POOR, true, true);
		put(Items.carrot_on_a_stick, ScrapValue.POOR, true, false);
		put(Items.clay_ball, ScrapValue.NONE, true, false);
		put(Items.cookie, ScrapValue.NONE, true, true);
		put(Items.diamond, ScrapValue.SUPERIOR, true, false);
		put(Items.emerald, ScrapValue.SUPERIOR, true, false);
		put(Items.nether_star, ScrapValue.SUPERIOR, true, false);
		put(Items.ender_eye, ScrapValue.STANDARD, true, false);
		put(Items.fermented_spider_eye, ScrapValue.NONE, true, false);
		put(Items.flint_and_steel, ScrapValue.NONE, true, false);
		put(Items.melon_seeds, ScrapValue.NONE, true, true);
		put(Items.pumpkin_seeds, ScrapValue.NONE, true, true);
		put(Items.pumpkin_pie, ScrapValue.NONE, true, true);
		put(Items.sugar, ScrapValue.NONE, true, false);
		put(Items.gunpowder, ScrapValue.POOR, true, false);
		put(Items.string, ScrapValue.NONE, true, false);
		put(Items.chainmail_boots, ScrapValue.STANDARD, true, false);
		put(Items.chainmail_chestplate, ScrapValue.STANDARD, true, false);
		put(Items.chainmail_helmet, ScrapValue.STANDARD, true, false);
		put(Items.chainmail_leggings, ScrapValue.STANDARD, true, false);
		put(Items.map, ScrapValue.NONE, true, true);
		put(Items.coal, ScrapValue.NONE, true, false);
		put(Items.redstone, ScrapValue.NONE, true, false);
		put(Items.lead, ScrapValue.NONE, true, false);
		put(Items.stone_axe, ScrapValue.NONE, true, true);
		put(Items.stone_hoe, ScrapValue.NONE, true, true);
		put(Items.stone_pickaxe, ScrapValue.NONE, true, true);
		put(Items.stone_shovel, ScrapValue.NONE, true, true);
		put(Items.stone_sword, ScrapValue.NONE, true, true);
		put(Items.stick, ScrapValue.NONE, true, false);
		put(Items.wooden_axe, ScrapValue.NONE, true, true);
		put(Items.wooden_hoe, ScrapValue.NONE, true, true);
		put(Items.wooden_pickaxe, ScrapValue.NONE, true, true);
		put(Items.wooden_shovel, ScrapValue.NONE, true, true);
		put(Items.wooden_sword, ScrapValue.NONE, true, true);
		put(Items.lava_bucket, ScrapValue.STANDARD, true, true);
		put(Items.water_bucket, ScrapValue.STANDARD, true, true);
		
		// Blocks
		put(Blocks.acacia_stairs, ScrapValue.NONE, true, false);
		put(Blocks.birch_stairs, ScrapValue.NONE, true, false);
		put(Blocks.spruce_stairs, ScrapValue.NONE, true, false);
		put(Blocks.jungle_stairs, ScrapValue.NONE, true, false);
		put(Blocks.brick_block, ScrapValue.NONE, true, false);
		put(Blocks.oak_stairs, ScrapValue.NONE, true, false);
		put(Blocks.brick_stairs, ScrapValue.NONE, true, false);
		put(Blocks.wooden_slab, ScrapValue.NONE, true, false);
		put(Blocks.cobblestone, ScrapValue.NONE, true, false);
		put(Blocks.cobblestone_wall, ScrapValue.NONE, true, false);
		put(Blocks.dark_oak_stairs, ScrapValue.NONE, true, false);
		put(Blocks.dirt, ScrapValue.NONE, true, false);
		put(Blocks.mossy_cobblestone, ScrapValue.NONE, true, false);
		put(Blocks.planks, ScrapValue.NONE, true, false);
		put(Blocks.fence, ScrapValue.NONE, true, false);
		put(Blocks.fence_gate, ScrapValue.NONE, true, false);
		put(Blocks.stone, ScrapValue.NONE, true, false);
		put(Blocks.stone_brick_stairs, ScrapValue.NONE, true, false);
		put(Blocks.stone_button, ScrapValue.NONE, true, true);
		put(Blocks.stone_stairs, ScrapValue.NONE, true, false);
		put(Blocks.stonebrick, ScrapValue.NONE, true, false);
		put(Blocks.tnt, ScrapValue.POOR, true, false);
		put(Blocks.hay_block, ScrapValue.NONE, true, false);
		put(Blocks.coal_block, ScrapValue.POOR, true, false);
		put(Blocks.carpet, ScrapValue.NONE, true, false);
		put(Blocks.hardened_clay, ScrapValue.NONE, true, false);
		put(Blocks.stained_hardened_clay, ScrapValue.NONE, true, false);
		put(Blocks.glass, ScrapValue.NONE, true, false);
		put(Blocks.glass_pane, ScrapValue.NONE, true, true);
		put(Blocks.stained_glass, ScrapValue.NONE, true, false);
		put(Blocks.stained_glass_pane, ScrapValue.NONE, true, true);
		put(Blocks.furnace, ScrapValue.NONE, true, false);
		put(Blocks.sand, ScrapValue.NONE, true, false);
		put(Blocks.gravel, ScrapValue.NONE, true, false);
		put(Blocks.sandstone, ScrapValue.NONE, true, false);
		put(Blocks.sapling, ScrapValue.NONE, true, true);
		put(Blocks.leaves, ScrapValue.NONE, true, true);
		put(Blocks.leaves2, ScrapValue.NONE, true, true);
		put(Blocks.lever, ScrapValue.NONE, true, false);
		put(Blocks.wool, ScrapValue.POOR, true, false);
	}

	public static ScrapValue getValue(Item item) {
		initialize();
		Preconditions.checkNotNull(item);
		return get(item).value;
	}
	
	public static ScrapValue getValue(Block block) {
		Preconditions.checkNotNull(block);
		return getValue(Item.getItemFromBlock(block));
	}
	
	public static ScrapValue getValue(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return getValue(stack.getItem());
	}
	
	public static void setValue(Item item, ScrapValue value) {
		Preconditions.checkNotNull(item);
		put(get(item).setValue(value));
	}
	
	public static void setValue(Block block, ScrapValue value) {
		Preconditions.checkNotNull(block);
		setValue(Item.getItemFromBlock(block), value);
	}
	
	public static void setValue(ItemStack stack, ScrapValue value) {
		Preconditions.checkNotNull(stack);
		setValue(stack.getItem(), value);
	}
	
	public static Data get(Item item) {
		initialize();
		Preconditions.checkNotNull(item);
		Data result = itemData.get(item);
		if(result == null)
			result = new Data(item);
		return result;
	}
	
	public static Data get(Block block) {
		Preconditions.checkNotNull(block);
		return get(Item.getItemFromBlock(block));
	}
	
	public static Data get(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return get(stack.getItem());
	}
	
	public static void put(Data data) {
		initialize();
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(data.item);
		itemData.put(data.item, data);
	}

	public static Data put(Item item, ScrapValue value, boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(item);
		Data data = get(item);
		data.setValue(value).setIgnoreRecipe(ignoreRecipe).setScrubFromOutput(scrubFromOutput);
		put(data);
		return data;
	}

	public static Data put(Block block, ScrapValue value, boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(block);
		return put(Item.getItemFromBlock(block), value, ignoreRecipe, scrubFromOutput);
	}

	public static Data put(ItemStack stack, ScrapValue value, boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(stack);
		return put(stack.getItem(), value, ignoreRecipe, scrubFromOutput);
	}
	
	protected static boolean isOreDictionaryType(ItemStack stack) {
		return ItemHelper.isBlock(stack) || ItemHelper.isDust(stack) || ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack);
	}
	
	public static boolean isRecipeIgnored(Item item) {
		initialize();
		Data data = get(item);
		return data.getIgnoreRecipe() || data.isFood();
	}
	
	public static boolean isRecipeIgnored(Block block) {
		return isRecipeIgnored(Item.getItemFromBlock(block));
	}
	
	public static boolean isRecipeIgnored(ItemStack stack) {
		return isOreDictionaryType(stack) || isRecipeIgnored(stack.getItem());
	}
	
	public static void setRecipeIgnored(Item item, boolean flag) {
		Preconditions.checkNotNull(item);
		put(get(item).setIgnoreRecipe(flag));
	}
	
	public static void setRecipeIgnored(Block block, boolean flag) {
		Preconditions.checkNotNull(block);
		setRecipeIgnored(Item.getItemFromBlock(block), flag);
	}
	
	public static void setRecipeIgnored(ItemStack stack, boolean flag) {
		Preconditions.checkNotNull(stack);
		setRecipeIgnored(stack.getItem(), flag);
	}
	
	public static boolean isScrubbedFromOutput(Item item) {
		initialize();
		Preconditions.checkNotNull(item);
		Data data = get(item);
		return data.isScrubbedFromOutput() || data.isFood();
	}
	
	public static boolean isScrubbedFromOutput(Block block) {
		Preconditions.checkNotNull(block);
		return isScrubbedFromOutput(Item.getItemFromBlock(block));
	}
	
	public static boolean isScrubbedFromOutput(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return isScrubbedFromOutput(stack.getItem());
	}
	
	public static void setScrubbedFromOutput(Item item, boolean flag) {
		Preconditions.checkNotNull(item);
		put(get(item).setScrubFromOutput(flag));
	}
	
	public static void setScrubbedFromOutput(Block block, boolean flag) {
		Preconditions.checkNotNull(block);
		setScrubbedFromOutput(Item.getItemFromBlock(block), flag);
	}
	
	public static void setScrubbedFromOutput(ItemStack stack, boolean flag) {
		Preconditions.checkNotNull(stack);
		setScrubbedFromOutput(stack.getItem(), flag);
	}
	
	public static void writeDiagnostic(Writer writer) throws Exception {
		
		writer.write("Item Info:\n");
		writer.write("=================================================================\n");
		for(Data d: itemData.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}
}
