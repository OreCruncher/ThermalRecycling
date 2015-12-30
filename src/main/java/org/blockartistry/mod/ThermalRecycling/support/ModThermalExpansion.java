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

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.handlers.ThermalExpansionScrapHandler;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.TERecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public final class ModThermalExpansion extends ModPlugin {

	private static final String[] scrapValuesNone = new String[] { "Sponge:1", "diagram:0", "diagram:1", };

	private static final String[] scrapValuesPoor = new String[] { "material:512", "material:513", "florb:*",
			"capacitor:1", "augment:32", };

	private static final String[] scrapValuesStandard = new String[] {};

	private static final String[] scrapValuesSuperior = new String[] { "Cell:1", "Cell:2", "Cell:4", "capacitor:4",
			"capacitor:5", "Frame:1", "Frame:2", "Frame:3", "Frame:5", "Frame:6", "Frame:8", "Frame:10",
			"tool.battleWrenchInvar", "Strongbox:2", "Strongbox:3", "Strongbox:4", "Cache:2", "Cache:3", "Cache:4",
			"Plate:0", "satchel:3", "satchel:4", "Machine:*", "Tank:3", "Tank:4", "Tesseract:0", "Device:3", };

	public ModThermalExpansion() {
		super(SupportedMod.THERMAL_EXPANSION);

		RecipeDecomposition.registerAccessor("cofh.thermalexpansion.plugins.nei.handlers.NEIRecipeWrapper",
				new TERecipeAccessor());
	}

	@Override
	public boolean initialize() {

		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		final ThermalExpansionScrapHandler handler = new ThermalExpansionScrapHandler();

		// Need to be able to see any special frames and security items
		// in realtime.
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:Machine:*").get(), handler);
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:Strongbox:*").get(), handler);
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:Device:*").get(), handler);
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:Cell:*").get(), handler);
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:Tesseract").get(), handler);
		ScrapHandler.registerHandler(ItemStackHelper.getItemStack("ThermalExpansion:satchel:*").get(), handler);

		// Sawdust and whatnot
		registerScrubFromOutput("material:512", "material:513", "florb:*");

		registerCompostIngredient(CompostIngredient.BROWN, "^dustWood");

		// Register some stuff for the Pile of Rubble
		registerPileOfRubbleDrop(1, 1, 3, "satchel:1");

		return true;
	}
}
