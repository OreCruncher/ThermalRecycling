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
import java.util.List;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.AutoDetect;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.items.Material;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;

import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;
import org.blockartistry.mod.ThermalRecycling.util.PreferredItemStacks;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class ModThermalRecycling extends ModPlugin {

	private final static List<String> oreNamesToIgnore = ImmutableList.<String> builder()
			.add("blockHopper", "blockCloth", "blockWool").build();

	private static class EnergeticRedstoneRecipes {

		protected final String ore;
		protected final int input;
		protected final int output;

		public EnergeticRedstoneRecipes(final String ore, final int input, final int output) {
			this.ore = ore;
			this.input = input;
			this.output = output;
		}

		public boolean areOresAvailable() {
			return !OreDictionary.getOres(this.ore).isEmpty();
		}

		public void register() {
			if (!areOresAvailable())
				return;

			final List<Object> ingredients = new ArrayList<Object>();
			ingredients.add(this.ore);
			for (int i = 0; i < input; i++)
				ingredients.add(Items.redstone);
			final ShapelessOreRecipe recipe = new ShapelessOreRecipe(
					new ItemStack(ItemManager.energeticRedstoneDust, output), ingredients.toArray());
			GameRegistry.addRecipe(recipe);
		}
	}

	private static final EnergeticRedstoneRecipes[] energeticUraniumRecipes = new EnergeticRedstoneRecipes[] {
			new EnergeticRedstoneRecipes("dustUranium", 2, 3), new EnergeticRedstoneRecipes("crushedUranium", 2, 3),
			new EnergeticRedstoneRecipes("crushedPurifiedUranium", 4, 6) };

	public ModThermalRecycling() {
		super(SupportedMod.THERMAL_RECYCLING);
	}

	@Override
	public boolean initialize() {

		// Use the Forge dictionary to find equivalent ore to set the
		// appropriate scrap value.
		registerScrapValuesForge(ScrapValue.NONE, "dustSulfur", "dustCoal", "dustWood");

		registerScrapValuesForge(ScrapValue.STANDARD, "ingotIron", "ingotGold", "ingotCopper", "ingotTin",
				"ingotSilver", "ingotLead", "ingotNickle", "ingotPlatinum", "ingotManaInfused", "ingotElectrum",
				"ingotInvar", "ingotBronze", "ingotSignalum", "ingotEnderium", "ingotSteel");

		registerScrapValuesForge(ScrapValue.STANDARD, "dustIron", "dustGold", "dustCopper", "dustTin", "dustSilver",
				"dustLead", "dustNickle", "dustPlatinum", "dustManaInfused", "dustElectrum", "dustInvar", "dustBronze",
				"dustSignalum", "dustEnderium", "dustSteel");

		registerScrapValuesForge(ScrapValue.SUPERIOR, "blockIron", "blockGold", "blockCopper", "blockTin",
				"blockSilver", "blockLead", "blockNickle", "blockPlatinum", "blockManaInfused", "blockElectrum",
				"blockInvar", "blockBronze", "blockSignalum", "blockEnderium", "blockSteel");

		registerScrapValuesForge(ScrapValue.STANDARD, "oreIron", "oreGold", "oreCopper", "oreTin", "oreSilver",
				"oreLead", "oreNickle", "orePlatinum", "oreManaInfused", "oreElectrum", "oreInvar", "oreBronze",
				"oreSignalum", "oreEnderium");

		registerScrapValuesForge(ScrapValue.POOR, "nuggetIron", "nuggetGold", "nuggetCopper", "nuggetTin",
				"nuggetSilver", "nuggetLead", "nuggetNickle", "nuggetPlatinum", "nuggetManaInfused", "nuggetElectrum",
				"nuggetInvar", "nuggetBronze", "nuggetSignalum", "nuggetEnderium", "nuggetSteel", "dustObsidian");

		registerScrapValuesForge(ScrapValue.SUPERIOR, "gemDiamond", "gemEmerald", "oreDiamond", "oreEmerald",
				"blockDiamond", "blockEmerald");
		registerScrapValuesForge(ScrapValue.STANDARD, "nuggetDiamond", "nuggetEmerald");

		// Tiny Piles from IC2
		if (SupportedMod.INDUSTRIAL_CRAFT.isLoaded()) {
			registerScrapValuesForge(ScrapValue.NONE, "dustTinySulfur", "dustTinyLapis", "dustTinyObsidian");
			registerScrapValuesForge(ScrapValue.POOR, "dustTinyIron", "dustTinyCopper", "dustTinyGold", "dustTinyTin",
					"dustTinySilver", "dustTinyLead", "dustTinyBronze", "dustTinyLithium");
		}

		// Scan the OreDictionary looking for blocks/items that we want
		// to prevent from being scrapped.
		for (final String oreName : OreDictionaryHelper.getOreNames()) {
			if (oreName.startsWith("block") || oreName.startsWith("dust") || oreName.startsWith("ingot")
					|| oreName.startsWith("nugget")) {

				if(oreNamesToIgnore.contains(oreName))
					continue;

				for (final ItemStack stack : OreDictionaryHelper.getOres(oreName)) {
					ItemRegistry.setBlockedFromScrapping(stack, true);
				}
			}
		}

		registerRecycleToWoodDustForge(1, "logWood");
		registerRecycleToWoodDustForge(2, "plankWood");
		registerRecycleToWoodDustForge(8, "treeSapling");

		registerRecipesToIgnoreForge("logWood", "plankWood", "treeSapling");

		// Configure extraction recipes
		registerExtractionRecipe(ScrappingTables.poorScrap, new ItemStackItem(null, 120),
				new ItemStackItem(ScrappingTables.standardScrap, 60),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:dye:15").get(), 10),
				new ItemStackItem(PreferredItemStacks.instance.dustCoal, 10),
				new ItemStackItem(PreferredItemStacks.instance.dustCharcoal, 10),
				new ItemStackItem(PreferredItemStacks.instance.sulfer, 10),
				new ItemStackItem(PreferredItemStacks.instance.dustIron, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustTin, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustCopper, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustNickel, 20));

		registerExtractionRecipe(ScrappingTables.standardScrap, new ItemStackItem(null, 78),
				new ItemStackItem(ScrappingTables.superiorScrap, 52),
				new ItemStackItem(PreferredItemStacks.instance.dustCoal, 10),
				new ItemStackItem(ItemStackHelper.getItemStack("ThermalFoundation:material:17").get(), 10),
				new ItemStackItem(PreferredItemStacks.instance.dustIron, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustTin, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustCopper, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustSilver, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustLead, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustGold, 10));

		registerExtractionRecipe(ScrappingTables.superiorScrap,
				new ItemStackItem(PreferredItemStacks.instance.dustGold, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustPlatinum, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustElectrum, 20),
				new ItemStackItem(PreferredItemStacks.instance.dustSignalum, 10),
				new ItemStackItem(PreferredItemStacks.instance.dustLumium, 10),
				new ItemStackItem(PreferredItemStacks.instance.dustEnderium, 10));

		registerExtractionRecipe(new ItemStack(ItemManager.recyclingScrapBox, 1, OreDictionaryHelper.WILDCARD_VALUE),
				new ItemStackItem(null, 1));

		// Soylent Red and Yellow
		registerExtractionRecipe(new ItemStack(Blocks.pumpkin, 6),
				new ItemStackItem(new ItemStack(ItemManager.soylentYellow), 1));
		registerExtractionRecipe(new ItemStack(Items.carrot, 12),
				new ItemStackItem(new ItemStack(ItemManager.soylentYellow), 1));
		registerExtractionRecipe(new ItemStack(Items.potato, 16),
				new ItemStackItem(new ItemStack(ItemManager.soylentYellow), 1));
		registerExtractionRecipe(new ItemStack(Items.apple, 12),
				new ItemStackItem(new ItemStack(ItemManager.soylentYellow), 1));

		registerExtractionRecipe(new ItemStack(Items.beef, 6),
				new ItemStackItem(new ItemStack(ItemManager.soylentRed), 1));
		registerExtractionRecipe(new ItemStack(Items.porkchop, 8),
				new ItemStackItem(new ItemStack(ItemManager.soylentRed), 1));
		registerExtractionRecipe(new ItemStack(Items.fish, 12),
				new ItemStackItem(new ItemStack(ItemManager.soylentRed), 1));
		registerExtractionRecipe(new ItemStack(Items.chicken, 8),
				new ItemStackItem(new ItemStack(ItemManager.soylentRed), 1));

		registerExtractionRecipe(new ItemStack(Items.rotten_flesh, 16),
				new ItemStackItem(new ItemStack(ItemManager.soylentGreen), 1));

		// RTG - Extract an RTG Energy Cell to a Housing - loses anything
		// energy, etc.
		registerExtractionRecipe(new ItemStack(ItemManager.energyCell, 1, OreDictionaryHelper.WILDCARD_VALUE),
				new ItemStackItem(new ItemStack(ItemManager.material, 1, Material.RTG_HOUSING), 1));

		// RTG - Extract a Depleted RTG Energy Cell to a Housing
		registerExtractionRecipe(new ItemStack(ItemManager.material, 1, Material.RTG_DEPLETED),
				new ItemStackItem(new ItemStack(ItemManager.material, 1, Material.RTG_HOUSING), 1));

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

		// If there is uranium dust in the ore dictionary create a crafting
		// recipe for Energetic Redstone Dust.
		if (ModOptions.getEnergeticRedstoneUraniumCrafting()) {
			for (final EnergeticRedstoneRecipes r : energeticUraniumRecipes)
				r.register();
		}

		return true;
	}

	private void processRecipeList(final List<Object> recipes, final boolean vanillaOnly) {

		// Process all registered recipes
		for (final Object o : recipes) {

			final IRecipe recipe = (IRecipe) o;
			final ItemStack stack = recipe.getRecipeOutput();

			try {

				// Check to see if this item should have a recipe in
				// the list. This does not mean that something later
				// on can't add one - just means by default it will
				// not be included.
				if (stack != null && (!vanillaOnly || ItemStackHelper.isVanilla(stack))) {
					if (!ItemRegistry.isRecipeIgnored(stack)) {

						// If the name is prefixed with any of the mods
						// we know about then we can create the recipe.
						final String name = Item.itemRegistry.getNameForObject(stack.getItem());

						if (SupportedMod.isModWhitelisted(name)) {
							final List<ItemStack> output = RecipeDecomposition.decompose(recipe);
							if (output != null && !output.isEmpty()) {
								if (vanillaOnly && !ItemStackHelper.isVanilla(output))
									continue;
								recycler.useRecipe(recipe).save();
							}
						}
					}
				}
			} catch (Throwable t) {
				ModLog.warn("processRecipeList: Unable to register recipe for [%s]",
						ItemStackHelper.resolveName(stack));
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean postInit() {

		// ////////////////////
		//
		// Process the recipes
		//
		// ////////////////////
		final List<Object> recipes = CraftingManager.getInstance().getRecipeList();
		processRecipeList(recipes, true);
		processRecipeList(recipes, false);

		// Lock our tables
		ScrapHandler.freeze();

		// AutoDetect scrap values
		AutoDetect.detect();

		return true;
	}
}
