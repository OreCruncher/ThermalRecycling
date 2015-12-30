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
			"tile.SkyStoneStairBlock", "tile.SkyStoneSmallBrickStairBlock", "tile.SkyStoneBlockStairBlock" };

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
			"item.ToolWirelessTerminal", };

	public ModAppliedEnergistics2() {
		super(SupportedMod.APPLIED_ENERGISTICS);

		RecipeDecomposition.registerAccessor("appeng.recipes.game.ShapedRecipe",
				new AEShapedRecipeAccessor());
		RecipeDecomposition.registerAccessor("appeng.recipes.game.ShapelessRecipe",
				new AEShapelessRecipeAccessor());
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
