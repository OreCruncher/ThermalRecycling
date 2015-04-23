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

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.PulverizerRecipeBuilder;

public class ModThermalExpansion implements IModPlugin {

	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();

	@Override
	public void apply(ModOptions options) {

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

		// Dynamos
		pulverizer.append("ThermalExpansion:Dynamo").output("ingotCopper", 11)
				.secondaryOutput("ingotSilver").save();
		pulverizer.append("ThermalExpansion:Dynamo:1").output("ingotInvar", 11)
				.secondaryOutput("ingotSilver").save();
		pulverizer.append("ThermalExpansion:Dynamo:2").output("ingotTin", 11)
				.secondaryOutput("ingotSilver").save();
		pulverizer.append("ThermalExpansion:Dynamo:3")
				.output("ingotBronze", 11).secondaryOutput("ingotSilver")
				.save();
		pulverizer.append("ThermalExpansion:Dynamo:4")
				.output("ingotElectrum", 11).secondaryOutput("ingotSilver")
				.save();

	}
}
