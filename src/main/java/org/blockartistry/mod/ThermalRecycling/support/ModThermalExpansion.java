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

import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;

public class ModThermalExpansion extends ModPlugin {

	public ModThermalExpansion() {
		super(SupportedMod.THERMAL_EXPANSION);
	}

	@Override
	public void apply() {

		// Misc parts
		furnace.append("ThermalExpansion:material").output("ingotIron", 2)
				.save();
		furnace.append("ThermalExpansion:material:1").output("ingotGold")
				.save();
		furnace.append("ThermalExpansion:material:2").output("ingotSilver")
				.save();
		furnace.append("ThermalExpansion:material:3").output("ingotElectrum")
				.save();
		furnace.append("ThermalExpansion:material:16")
				.output("nuggetSignalum", 6).save();

		// Machine Frames/Energy Frames
		ThermalRecyclerRecipeBuilder.applyRecipeRange("ThermalExpansion:Frame",
				0, 6);

		// Resonant Frame (Empty)
		recycler.useRecipe("ThermalExpansion:Frame:8").save();

		// Machines
		ThermalRecyclerRecipeBuilder.applyRecipeRange(
				"ThermalExpansion:Machine", 0, 11);

		// Devices
		recycler.useRecipe("ThermalExpansion:Device").save();
		recycler.useRecipe("ThermalExpansion:Device:2").save();
		recycler.useRecipe("ThermalExpansion:Device:3").save();
		recycler.useRecipe("ThermalExpansion:Device:5").save();

		// Dynamo
		ThermalRecyclerRecipeBuilder.applyRecipeRange(
				"ThermalExpansion:Dynamo", 0, 4);

		// Cells
		ThermalRecyclerRecipeBuilder.applyRecipeRange("ThermalExpansion:Cell",
				1, 4);

		// Portable Tanks
		ThermalRecyclerRecipeBuilder.applyRecipeRange("ThermalExpansion:Tank",
				1, 4);

		// Strongbox
		ThermalRecyclerRecipeBuilder.applyRecipeRange(
				"ThermalExpansion:Strongbox", 1, 4);

		// Cache
		ThermalRecyclerRecipeBuilder.applyRecipeRange("ThermalExpansion:Cache",
				1, 4);

		// Tesseract
		recycler.useRecipe("ThermalExpansion:Tesseract").save();
	}
}
