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
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public final class ModThermalExpansion extends ModPlugin {

	public ModThermalExpansion() {
		super(SupportedMod.THERMAL_EXPANSION);
	}
	
	@Override
	public boolean initialize() {

		final ThermalExpansionScrapHandler handler = new ThermalExpansionScrapHandler();

		// Need to be able to see any special frames and security items
		// in realtime.
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:Machine:*"),
				handler);
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:Strongbox:*"),
				handler);
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:Device:*"),
				handler);
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:Cell:*"),
				handler);
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:Tesseract"),
				handler);
		ScrapHandler.registerHandler(
				ItemStackHelper.getItemStack("ThermalExpansion:satchel:*"),
				handler);

		// Sawdust and whatnot
		registerScrapValues(ScrapValue.NONE, "material:512", "material:513",
				"florb:*");
		registerScrubFromOutput("material:512", "material:513", "florb:*");
		
		registerCompostIngredient(CompostIngredient.BROWN, "^dustWood");
		
		// Register some stuff for the Pile of Rubble
		registerPileOfRubbleDrop(1, 1, 3, "satchel:1");

		return true;
	}
}
