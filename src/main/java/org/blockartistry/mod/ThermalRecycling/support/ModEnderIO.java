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

import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModEnderIO extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
			"blockFusedQuartz:*", "itemFusedQuartzFrame", "itemConduitFacade",
			"itemPowderIngot:7" };

	static final String[] scrapValuesNone = new String[] {
			"blockFusedQuartz:*", "itemFusedQuartzFrame", "itemMaterial:3",
			"itemMaterial:4", "itemConduitFacade", "itemPowderIngot:7" };

	static final String[] scrapValuesPoor = new String[] {
			"itemLiquidConduit:0", "itemLiquidConduit:1" };

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] {
			"itemMaterial:5", "itemMaterial:6", "itemMaterial:8",
			"itemMaterial:9", "blockEnchanter", "blockAttractor",
			"blockPoweredSpawner", "blockTravelAnchor", "blockTransceiver",
			"blockVacuumChest", "blockFarmStation", "blockSolarPanel:1" };

	public ModEnderIO() {
		super(SupportedMod.ENDERIO);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		ItemScrapData.put(
				ItemStackHelper.getItemStack("EnderIO:itemMachinePart:1"),
				ScrapValue.NONE, false, false);
		ItemScrapData.put(ItemStackHelper.getItemStack("EnderIO:itemMaterial"),
				ScrapValue.NONE, false, false);
		ItemScrapData.put(
				ItemStackHelper.getItemStack("EnderIO:itemMaterial:1"),
				ScrapValue.NONE, false, false);
		ItemScrapData.put(
				ItemStackHelper.getItemStack("EnderIO:itemMaterial:2"),
				ScrapValue.NONE, false, false);

		// Armor and tools
		smelter.append("EnderIO:item.darkSteel_helmet")
				.secondaryInput("dustCoal", 5).output("EnderIO:itemAlloy:6", 5)
				.save();
		smelter.append("EnderIO:item.darkSteel_chestplate")
				.secondaryInput("dustCoal", 8).output("EnderIO:itemAlloy:6", 8)
				.save();
		smelter.append("EnderIO:item.darkSteel_leggings")
				.secondaryInput("dustCoal", 7).output("EnderIO:itemAlloy:6", 7)
				.save();
		smelter.append("EnderIO:item.darkSteel_boots")
				.secondaryInput("dustCoal", 4).output("EnderIO:itemAlloy:6", 4)
				.save();
		smelter.append("EnderIO:item.darkSteel_sword",
				"EnderIO:itemTravelStaff:16").secondaryInput("dustCoal", 2)
				.output("EnderIO:itemAlloy:6", 2).save();
		smelter.append("EnderIO:item.darkSteel_axe",
				"EnderIO:item.darkSteel_pickaxe").secondaryInput("dustCoal", 3)
				.output("EnderIO:itemAlloy:6", 3).save();
		smelter.append("EnderIO:itemYetaWrench", "EnderIO:itemModItemFilter")
				.secondaryInput("dustCoal", 3).output("EnderIO:itemAlloy", 3)
				.save();

		pulverizer.appendSubtypeRange("EnderIO:itemExtractSpeedUpgrade", 0, 1)
				.output("EnderIO:itemAlloy", 4).secondaryOutput("ingotIron", 4)
				.save();

		pulverizer.append("EnderIO:itemBasicCapacitor").output("ingotCopper")
				.secondaryOutput("nuggetGold", 4).save();
		pulverizer.append("EnderIO:itemBasicCapacitor:1")
				.output("EnderIO:itemAlloy:1", 2)
				.secondaryOutput("ingotCopper", 2).save();
		pulverizer.append("EnderIO:itemBasicCapacitor:2")
				.output("EnderIO:itemAlloy:2", 2)
				.secondaryOutput("EnderIO:itemAlloy:1", 4).save();

		pulverizer.append("EnderIO:itemBasicFilterUpgrade")
				.output("ingotIron", 5).save();
		pulverizer.append("EnderIO:itemBasicFilterUpgrade:1")
				.output("EnderIO:itemAlloy:7", 2)
				.secondaryOutput("dustRedstone", 5).save();
		pulverizer.append("EnderIO:itemExistingItemFilter")
				.output("EnderIO:itemAlloy:7", 2)
				.secondaryOutput("dustRedstone", 8).save();

		pulverizer.append("EnderIO:itemXpTransfer")
				.output("EnderIO:itemAlloy:7", 2)
				.secondaryOutput("EnderIO:itemAlloy:1").save();

		pulverizer.append("EnderIO:itemSoulVessel")
				.output("EnderIO:itemAlloy:7").save();

		pulverizer.append("EnderIO:itemGliderWing")
				.output("EnderIO:itemAlloy:6", 3).save();
		pulverizer.append("EnderIO:itemGliderWing:1")
				.output("EnderIO:itemAlloy:6", 8).save();

		pulverizer.appendSubtypeRange("EnderIO:blockElectricLight", 0, 1)
				.output("ingotCopper").secondaryOutput("dustGlowstone").save();
		pulverizer.appendSubtypeRange("EnderIO:blockElectricLight", 2, 3)
				.output("ingotIron", 2).secondaryOutput("dustGlowstone", 4)
				.save();
		pulverizer.appendSubtypeRange("EnderIO:blockElectricLight", 4, 5)
				.output("EnderIO:itemAlloy:2")
				.secondaryOutput("EnderIO:itemAlloy:7", 2).save();

		/*
		 * // Thermal Recycler
		 * recycler.useRecipe("EnderIO:blockTransceiver").save();
		 * recycler.useRecipe("EnderIO:blockEnchanter").save();
		 * recycler.useRecipe("EnderIO:blockCombustionGenerator").save();
		 * recycler.useRecipe("EnderIO:blockKillerJoe").save();
		 * recycler.useRecipe("EnderIO:blockSagMill").save();
		 * recycler.useRecipe("EnderIO:blockSoulBinder").save();
		 * recycler.useRecipe("EnderIO:blockCrafter").save();
		 * recycler.useRecipe("EnderIO:blockZombieGenerator").save();
		 * recycler.useRecipe("EnderIO:blockWirelessCharger").save();
		 * recycler.useRecipe("EnderIO:blockFarmStation").save();
		 * recycler.useRecipe("EnderIO:blockAlloySmelter").save();
		 * recycler.useRecipe("EnderIO:blockVacuumChest").save();
		 * recycler.useRecipe("EnderIO:blockPainter").save();
		 * recycler.useRecipe("EnderIO:blockEnderIo").save();
		 * recycler.useRecipe("EnderIO:blockAttractor").save();
		 * recycler.useRecipe("EnderIO:blockReinforcedObsidian").save();
		 * recycler.useRecipe("EnderIO:blockVat").save();
		 * recycler.useRecipe("EnderIO:blockExperienceObelisk").save();
		 * recycler.useRecipe("EnderIO:blockSliceAndSplice").save();
		 * recycler.useRecipe("EnderIO:blockSpawnGuard").save();
		 * recycler.useRecipe("EnderIO:blockTravelAnchor").save();
		 * recycler.useRecipe("EnderIO:blockPowerMonitor").save();
		 * recycler.useRecipe("EnderIO:itemMachinePart").save();
		 * recycler.useRecipe("EnderIO:itemMagnet:16").save();
		 * recycler.useRecipe("EnderIO:itemConduitProbe").save();
		 * 
		 * ThermalRecyclerRecipeBuilder .applyRecipeRange("EnderIO:blockTank",
		 * 0, 1);
		 * 
		 * ThermalRecyclerRecipeBuilder.applyRecipeRange(
		 * "EnderIO:blockSolarPanel", 0, 1);
		 * 
		 * ThermalRecyclerRecipeBuilder.applyRecipeRange("EnderIO:blockBuffer",
		 * 0, 2);
		 * 
		 * ThermalRecyclerRecipeBuilder.applyRecipeRange("EnderIO:blockCapBank",
		 * 1, 3);
		 * 
		 * ThermalRecyclerRecipeBuilder.applyRecipeRange(
		 * "EnderIO:itemConduitFacade", 0, 1);
		 * 
		 * ThermalRecyclerRecipeBuilder.applyRecipeRange(
		 * "EnderIO:itemExtractSpeedUpgrade", 0, 1);
		 */
	}

}
