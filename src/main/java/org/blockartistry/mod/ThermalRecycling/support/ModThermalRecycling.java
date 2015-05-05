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

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.common.config.Configuration;

public class ModThermalRecycling extends ModPlugin {

	public ModThermalRecycling() {
		super(SupportedMod.THERMAL_RECYCLING);
	}

	String[] whiteList;

	@Override
	public void init(Configuration config) {

		String[] modList = SupportedMod.getModIdList();
		String[] configList = ModOptions.getModWhitelist();

		if (configList == null || configList.length == 0)
			whiteList = modList;
		else
			whiteList = MyUtils.concat(modList, configList);
	}

	@Override
	public void apply() {

		ItemScrapData.setRecipeIgnored(ItemManager.recyclingScrapBox, true);
		ItemScrapData.setRecipeIgnored(ItemManager.debris, true);
		ItemScrapData.setRecipeIgnored(BlockManager.scrapBlock, true);
		
		ItemScrapData.setValue(new ItemStack(ItemManager.debris), ScrapValue.NONE);
		ItemScrapData.setValue(new ItemStack(BlockManager.scrapBlock), ScrapValue.NONE);
		
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.POOR), ScrapValue.POOR);
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.STANDARD), ScrapValue.STANDARD);
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.SUPERIOR), ScrapValue.SUPERIOR);
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.POOR), ScrapValue.POOR);
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.STANDARD), ScrapValue.STANDARD);
		ItemScrapData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.SUPERIOR), ScrapValue.SUPERIOR);

		// ////////////////////
		//
		// Add recipe blacklist items first
		// before processing!
		//
		// ////////////////////

		// ////////////////////
		//
		// Process the recipes
		//
		// ////////////////////

		String modIds = ":" + String.join(":", whiteList) + ":";

		// Process all registered recipes
		for (Object o : CraftingManager.getInstance().getRecipeList()) {

			IRecipe recipe = (IRecipe) o;
			ItemStack stack = recipe.getRecipeOutput();

			// Check to see if this item should have a recipe in
			// the list. This does not mean that something later
			// on can't add one - just means by default it will
			// not be included.
			if (stack != null) {
				if (!ItemScrapData.isRecipeIgnored(stack)) {

					// If the name is prefixed with any of the mods
					// we know about then we can create the recipe.
					String name = Item.itemRegistry.getNameForObject(stack
							.getItem());
					if (modIds.contains(":"
							+ StringUtils.substringBefore(name, ":") + ":")) {
						recycler.useRecipe(stack).save();
					}
				}
			}
		}
	}
}
