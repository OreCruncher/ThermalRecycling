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
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.blocks.PileOfRubble;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.items.Material;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.support.handlers.ThermalRecyclingScrapHandler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;

public final class ModThermalRecycling extends ModPlugin {

	public ModThermalRecycling() {
		super(SupportedMod.THERMAL_RECYCLING);
	}

	@Override
	public boolean initialize() {

		// Register special scrap handlers
		final ThermalRecyclingScrapHandler handler = new ThermalRecyclingScrapHandler();
		// Need to be able to see any special frames in real time.
		ScrapHandler.registerHandler(new ItemStack(ItemManager.processingCore,
				1, OreDictionary.WILDCARD_VALUE), handler);

		ItemData.setRecipeIgnored(ItemManager.recyclingScrapBox, true);
		ItemData.setRecipeIgnored(ItemManager.debris, true);
		ItemData.setRecipeIgnored(BlockManager.scrapBlock, true);
		ItemData.setRecipeIgnored(ItemManager.material, true);
		ItemData.setRecipeIgnored(ItemManager.paperLogMaker, true);

		ItemData.setValue(new ItemStack(ItemManager.debris), ScrapValue.NONE);
		ItemData.setValue(new ItemStack(BlockManager.scrapBlock),
				ScrapValue.NONE);
		ItemData.setValue(new ItemStack(ItemManager.paperLogMaker),
				ScrapValue.NONE);
		ItemData.setValue(new ItemStack(ItemManager.material, 1,
				Material.LITTER_BAG), ScrapValue.NONE);

		ItemData.setValue(new ItemStack(ItemManager.material, 1,
				Material.PAPER_LOG), ScrapValue.POOR);

		ItemData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.POOR), ScrapValue.POOR);
		ItemData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.STANDARD), ScrapValue.STANDARD);
		ItemData.setValue(new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.SUPERIOR), ScrapValue.SUPERIOR);
		ItemData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.POOR), ScrapValue.POOR);
		ItemData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.STANDARD), ScrapValue.STANDARD);
		ItemData.setValue(new ItemStack(ItemManager.recyclingScrapBox, 1,
				RecyclingScrap.SUPERIOR), ScrapValue.SUPERIOR);

		// ////////////////////
		//
		// Add recipe blacklist items first
		// before processing!
		//
		// ////////////////////

		// Apply the blacklist from the configuration. We need to fix up
		// each entry with a ^ so the underlying routine just does what it
		// needs to do.
		for (final String s : ModOptions.getRecyclerBlacklist()) {
			registerItemBlockedFromScrapping(true, "^" + s);
		}

		return true;
	}

	@Override
	public boolean postInit() {

		// ////////////////////
		//
		// Process the recipes
		//
		// ////////////////////

		final String modIds = ":"
				+ StringUtils.join(SupportedMod.getEffectiveModIdList(), ":")
				+ ":";

		// Process all registered recipes
		for (final Object o : CraftingManager.getInstance().getRecipeList()) {

			final IRecipe recipe = (IRecipe) o;
			final ItemStack stack = recipe.getRecipeOutput();

			// Check to see if this item should have a recipe in
			// the list. This does not mean that something later
			// on can't add one - just means by default it will
			// not be included.
			if (stack != null) {
				if (!ItemData.isRecipeIgnored(stack)) {

					// If the name is prefixed with any of the mods
					// we know about then we can create the recipe.
					final String name = Item.itemRegistry
							.getNameForObject(stack.getItem());

					if (modIds.contains(":"
							+ StringUtils.substringBefore(name, ":") + ":")) {
						try {
							recycler.useRecipe(recipe).save();
						} catch (Throwable t) {
							ModLog.catching(t);
						}
					}
				}
			}
		}

		// Lock our tables
		ItemData.freeze();
		RecipeData.freeze();
		ScrapHandler.freeze();

		// Register scrap items for Pile of Rubble
		PileOfRubble.addRubbleDrop(ScrappingTables.poorScrap, 1, 2, 5);
		PileOfRubble.addRubbleDrop(ScrappingTables.poorScrapBox, 1, 1, 2);
		PileOfRubble.addRubbleDrop(ScrappingTables.standardScrap, 1, 2, 4);
		PileOfRubble.addRubbleDrop(ScrappingTables.standardScrapBox, 1, 1, 1);

		PileOfRubble.addRubbleDrop(new ItemStack(ItemManager.material, 1,
				Material.LITTER_BAG), 1, 2, 4);

		PileOfRubble.addRubbleDrop(new ItemStack(ItemManager.soylentGreen), 1,
				2, 6);

		return true;
	}
}
