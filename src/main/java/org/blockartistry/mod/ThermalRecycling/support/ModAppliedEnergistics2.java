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

public final class ModAppliedEnergistics2 extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "tile.BlockCrank",
			"item.ItemMultiPart:120", "item.ItemFacade:*" };

	static final String[] scrapValuesNone = new String[] { "tile.BlockCrank",
			"item.ItemMultiPart:120", "item.ItemFacade:*" };

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] {

	};

	static final String[] scrapValuesSuperior = new String[] {
			"tile.BlockCraftingStorage:2", "tile.BlockCraftingStorage:3",
			"tile.BlockQuantumRing", "tile.BlockDrive",
			"tile.BlockDenseEnergyCell", "tile.BlockWireless",
			"tile.BlockIOPort", "tile.BlockQuantumLinkChamber",
			"tile.BlockController", "tile.BlockCraftingUnit:*",
			"tile.BlockSpacialIOPort", "tile.BlockSecurity",
			"item.ItemBasicStorageCell.64K", "item.ToolPortableCell",
			"item.ItemSpacialStorageCell.16Cubed",
			"item.ItemSpacialStorageCell.128Cubed",
			"item.ItemBasicStorageCell.16K", "item.ItemMultiPart:480",
			"item.ItemMultiPart:340", "item.ToolBiometricCard",
			"item.ItemMultiMatrial:28", "item.ItemMultiMaterial:29",
			"item.ItemMultiMaterial:31", "item.ItemMultiMaterial:30",
			"item.ItemMultiMaterial:33", "item.ItemMultiMaterial:34",
			"item.ItemMultiMaterial:37", "item.ItemMultiMaterial:32",
			"item.ItemMultiMaterial:38", "item.ItemMultiMaterial:24",
			"item.ItemMultiMaterial:17", "item.ToolMassCannon",
			"item.ItemSpacialStorageCell.2Cubed",
			"item.ToolEntropyManipulator", "item.ToolWirelessTerminal", };

	public ModAppliedEnergistics2() {
		super(SupportedMod.APPLIED_ENERGISTICS);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

	}
}
