/*
 * This file is part of ModpackInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 * Copyright (c) contributors
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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.recipe.PulverizerRecipeBuilder;
import cpw.mods.fml.common.Loader;

public class ModAdvancedGenerators extends ModTweaks {

	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();

	@Override
	public String getName() {
		return "Advanced Generators";
	}

	@Override
	public boolean isModLoaded() {
		return Loader.isModLoaded("advgenerators");
	}

	@Override
	public void apply(ModOptions options) {

		// Parts
		pulverizer.setEnergy(400)
				.append("advgenerators:IronTubing", "advgenerators:IronWiring")
				.output("nuggetIron").save();
		pulverizer.append("advgenerators:IronFrame").output("ingotIron", 2)
				.save();
		pulverizer.append("advgenerators:TurbineRotor").output("ingotIron", 11)
				.save();

		pulverizer.append("advgenerators:TurbineBlade").output("ingotIron")
				.secondaryOutput("nuggetIron", 2).save();
		pulverizer.append("advgenerators:PressureValve").output("ingotIron", 2)
				.secondaryOutput("nuggetIron", 3).save();
		pulverizer.append("advgenerators:Controller").output("ingotIron", 4)
				.secondaryOutput(Items.quartz).save();
		pulverizer.append("advgenerators:PowerIO").output("ingotIron", 8)
				.secondaryOutput("dustRedstone", 3).save();

		// Machines
		pulverizer.append("advgenerators:ItemInput").output("ingotIron", 11)
				.save();
		pulverizer.append("advgenerators:SteamTurbineController")
				.output("ingotIron", 12).secondaryOutput("nuggetIron", 4)
				.save();
		pulverizer.append("advgenerators:SyngasController")
				.output("ingotIron", 14).secondaryOutput("nuggetIron", 6)
				.save();
		pulverizer.append("advgenerators:FluidInput").output("ingotIron", 7)
				.secondaryOutput("nuggetIron").save();
		pulverizer.append("advgenerators:FuelTank").output("ingotIron", 8)
				.secondaryOutput("nuggetIron").save();
		pulverizer.append("advgenerators:HeatingChamber")
				.output("ingotIron", 10).secondaryOutput("nuggetIron", 5)
				.save();
		pulverizer.append("advgenerators:HeatExchanger").output("ingotIron", 8)
				.secondaryOutput("nuggetIron", 5).save();
		pulverizer.append("advgenerators:FluidOutputSelect")
				.output("ingotIron", 6).secondaryOutput("nuggetIron", 2).save();
		pulverizer.append("advgenerators:Turbine").output("ingotIron", 19)
				.secondaryOutput("nuggetIron", 4).save();
		pulverizer.append("advgenerators:MixingChamber")
				.output("ingotIron", 12).secondaryOutput("nuggetIron", 8)
				.save();
		pulverizer
				.append("advgenerators:TurbineController",
						"advgenerators:HeatExchangerController")
				.output("ingotIron", 12).secondaryOutput("nuggetIron", 4)
				.save();

		pulverizer.append("advgenerators:Sensor").output("ingotIron", 8)
				.secondaryOutput(Items.quartz).save();
		pulverizer.append("advgenerators:ItemOutput").output("ingotIron", 13)
				.secondaryOutput(Blocks.planks, 8).save();
		pulverizer.append("advgenerators:PowerCapacitor")
				.output("ingotIron", 8).secondaryOutput("dustRedstone", 4)
				.save();
		pulverizer.append("advgenerators:RFOutput").output("ingotIron", 14)
				.secondaryOutput("ingotGold").save();
	}
}
