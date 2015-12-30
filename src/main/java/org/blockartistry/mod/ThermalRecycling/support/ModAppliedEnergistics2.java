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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.AEShapedRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.AEShapelessRecipeAccessor;

public final class ModAppliedEnergistics2 extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "tile.BlockSkyStone:*", "tile.BlockCrank",
			"item.ItemMultiPart:120", "item.ItemFacade:*", "tile.BlockQuartz" };

	static final String[] scrapValuesNone = new String[] { "tile.BlockCrank", "item.ItemMultiPart:120",
			"item.ItemFacade:*" };

	static final String[] scrapValuesPoor = new String[] { "tile.BlockSkyStone:*", "tile.SkyStoneBrickStairBlock",
			"tile.SkyStoneStairBlock", "tile.SkyStoneSmallBrickStairBlock", "tile.SkyStoneBlockStairBlock",
			"item.ItemMultiPart:0", "item.ItemMultiPart:1", "item.ItemMultiPart:2", "item.ItemMultiPart:3",
			"item.ItemMultiPart:4", "item.ItemMultiPart:5", "item.ItemMultiPart:6", "item.ItemMultiPart:7",
			"item.ItemMultiPart:8", "item.ItemMultiPart:9", "item.ItemMultiPart:10", "item.ItemMultiPart:11",
			"item.ItemMultiPart:12", "item.ItemMultiPart:13", "item.ItemMultiPart:14", "item.ItemMultiPart:15",
			"item.ItemMultiPart:20", "item.ItemMultiPart:21", "item.ItemMultiPart:22", "item.ItemMultiPart:23",
			"item.ItemMultiPart:24", "item.ItemMultiPart:25", "item.ItemMultiPart:26", "item.ItemMultiPart:27",
			"item.ItemMultiPart:28", "item.ItemMultiPart:29", "item.ItemMultiPart:30", "item.ItemMultiPart:31",
			"item.ItemMultiPart:32", "item.ItemMultiPart:33", "item.ItemMultiPart:34", "item.ItemMultiPart:35",
			"item.ItemMultiPart:40", "item.ItemMultiPart:41", "item.ItemMultiPart:42", "item.ItemMultiPart:43",
			"item.ItemMultiPart:44", "item.ItemMultiPart:45", "item.ItemMultiPart:46", "item.ItemMultiPart:47",
			"item.ItemMultiPart:48", "item.ItemMultiPart:49", "item.ItemMultiPart:50", "item.ItemMultiPart:51",
			"item.ItemMultiPart:52", "item.ItemMultiPart:53", "item.ItemMultiPart:54", "item.ItemMultiPart:55",
			"item.ItemMultiPart:60", "item.ItemMultiPart:61", "item.ItemMultiPart:62", "item.ItemMultiPart:63",
			"item.ItemMultiPart:64", "item.ItemMultiPart:65", "item.ItemMultiPart:66", "item.ItemMultiPart:67",
			"item.ItemMultiPart:68", "item.ItemMultiPart:69", "item.ItemMultiPart:70", "item.ItemMultiPart:71",
			"item.ItemMultiPart:72", "item.ItemMultiPart:73", "item.ItemMultiPart:74", "item.ItemMultiPart:75",
			"item.ItemCrystalSeed:600", "item.ItemCrystalSeed:1200", "item.ItemCrystalSeed:0" };

	static final String[] scrapValuesStandard = new String[] { "tile.BlockWireless", "tile.BlockQuartz", };

	static final String[] scrapValuesSuperior = new String[] { "tile.BlockEnergyCell", "item.ItemBasicStorageCell.64k",
			"item.ItemBasicStorageCell.16k", "tile.BlockFluix", "tile.BlockInscriber",
			"item.ItemSpatialStorageCell.16Cubed", "item.ItemSpatialStorageCell.128Cubed",
			"item.ItemBasicStorageCell.1k", "item.ItemMultiPart:360", "item.ItemSpatialStorageCell.2Cubed",
			"item.ItemViewCell", "item.ToolColorApplicator", "tile.FluixStairBlock", "tile.BlockMolecularAssembler",
			"tile.BlockSpatialPylon", "tile.BlockEnergyAcceptor", "tile.BlockChest", "item.ToolNetworkTool",
			"tile.BlockSpatialIOPort", "tile.BlockQuartzGrowthAccelerator", "tile.BlockCraftingStorage:2",
			"item.ItemBasicStorageCell.4k", "tile.BlockVibrationChamber", "tile.BlockCraftingStorage:3",
			"tile.BlockQuantumRing", "tile.BlockDrive", "tile.BlockDenseEnergyCell", "tile.BlockIOPort",
			"tile.BlockQuantumLinkChamber", "tile.BlockController", "tile.BlockCraftingUnit:*", "tile.BlockSecurity",
			"item.ToolPortableCell", "item.ItemMultiPart:480", "item.ItemMultiPart:340", "item.ToolBiometricCard",
			"item.ItemMultiMaterial:29", "item.ItemMultiMaterial:31", "item.ItemMultiMaterial:30",
			"item.ItemMultiMaterial:33", "item.ItemMultiMaterial:34", "item.ItemMultiMaterial:37",
			"item.ItemMultiMaterial:32", "item.ItemMultiMaterial:38", "item.ItemMultiMaterial:24",
			"item.ItemMultiMaterial:17", "item.ToolMassCannon", "item.ToolEntropyManipulator",
			"item.ToolWirelessTerminal", "tile.BlockCraftingStorage:1", "tile.BlockCraftingStorage:0",
			"tile.BlockCraftingMonitor:0", "item.ItemMultiPart:220", "item.ItemMultiPart:420",
			"item.ItemMultiPart:460", };

	public ModAppliedEnergistics2() {
		super(SupportedMod.APPLIED_ENERGISTICS);

		RecipeDecomposition.registerAccessor("appeng.recipes.game.ShapedRecipe", new AEShapedRecipeAccessor());
		RecipeDecomposition.registerAccessor("appeng.recipes.game.ShapelessRecipe", new AEShapelessRecipeAccessor());
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		return true;
	}
}
