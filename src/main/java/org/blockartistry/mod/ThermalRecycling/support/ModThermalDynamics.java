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

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

public class ModThermalDynamics extends ModTweaks {

	@Override
	public String getName() {
		return "Thermal Dynamics";
	}

	@Override
	public boolean isModLoaded() {
		return isModLoaded("ThermalDynamics");
	}

	@Override
	public void apply(ModOptions options) {

		if (!isModLoaded())
			return;

		RecipeHelper helper = new RecipeHelper();

		// Servos
		helper.setEnergy(1200).setSecondaryChance(100);
		helper.setInput("ThermalDynamics:servo").setOutput("nuggetIron")
				.setSecondary("dustIron").addAsPulverizerRecipe();
		helper.setInputSubtype(1).setSecondary("dustInvar")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(2).setSecondary("dustElectrum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setSecondary("dustSignalum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(4).setSecondary("dustEnderium")
				.addAsPulverizerRecipe();

		// Filters
		helper.setEnergy(1200).setSecondaryChance(100);
		helper.setInput("ThermalDynamics:filter").setOutput("nuggetIron")
				.setSecondary("dustIron").addAsPulverizerRecipe();
		helper.setInputSubtype(1).setSecondary("dustInvar")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(2).setSecondary("dustElectrum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setSecondary("dustSignalum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(4).setSecondary("dustEnderium")
				.addAsPulverizerRecipe();

		// Retrievers
		helper.reset().setEnergy(1200).setSecondaryChance(100);
		helper.setInput("ThermalDynamics:retriever")
				.setOutput("nuggetEnderium").setSecondary("dustIron")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(1).setSecondary("dustInvar")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(2).setSecondary("dustElectrum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setSecondary("dustSignalum")
				.addAsPulverizerRecipe();
		helper.setInputSubtype(4).setSecondary("dustEnderium")
				.addAsPulverizerRecipe();

		// Structural Duct
		helper.reset();
		helper.setInput("ThermalDynamics:ThermalDynamics_48")
				.setOutput("nuggetLead").addAsPulverizerRecipe();

		helper.reset().setSecondaryChance(100);

		// Item Duct (Standard + Opaque)
		helper.setInput("ThermalDynamics:ThermalDynamics_32")
				.setOutput("nuggetTin", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(1).setSecondary("nuggetLead")
				.addAsPulverizerRecipe();

		// Impulse Ducts (Standard + Opaque) - drain and turn into a base type
		helper.setInputSubtype(2)
				.setOutput("ThermalDynamics:ThermalDynamics_32")
				.setFluid("glowstone", 200).addAsFluidTransposerRecipe();
		helper.setInputSubtype(3).setOutputSubtype(1)
				.addAsFluidTransposerRecipe();

		// Item Duct (Warp + Opaque)
		helper.reset().setInput("ThermalDynamics:ThermalDynamics_32")
				.setSecondaryChance(100);
		helper.setInputSubtype(5).setOutput("nuggetTin", 3)
				.setSecondary("nuggetEnderium").addAsPulverizerRecipe();
		helper.setInputSubtype(4).addAsPulverizerRecipe();

		// Fluxuating Duct (Standard + Opaque) - drain and turn into a base type
		// for further recycling
		helper.setInputSubtype(6)
				.setOutput("ThermalDynamics:ThermalDynamics_32")
				.setFluid("redstone", 50).addAsFluidTransposerRecipe();
		helper.setInputSubtype(7).setOutputSubtype(1)
				.addAsFluidTransposerRecipe();

		// Flux ducts - drain and smash the base type
		helper.reset().setInput("ThermalDynamics:ThermalDynamics_0")
				.setSecondaryChance(100);
		helper.setInputSubtype(3).setOutput("nuggetElectrum", 3)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(2)
				.setOutput("ThermalDynamics:ThermalDynamics_0")
				.setOutputSubtype(3).setFluid("redstone", 200)
				.addAsFluidTransposerRecipe();
		helper.setInputSubtype(4).setOutputSubtype(5)
				.addAsFluidTransposerRecipe();
		helper.setInputSubtype(5).setOutput("nuggetEnderium", 3)
				.setSecondary("nuggetElectrum", 3).addAsPulverizerRecipe();

		// Fluid ducts
		helper.reset().setInput("ThermalDynamics:ThermalDynamics_16")
				.setSecondaryChance(100);
		helper.setInputSubtype(2).setOutput("nuggetInvar", 3)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setSecondary("nuggetLead")
				.addAsPulverizerRecipe();

		helper.setInputSubtype(4).setOutput("nuggetElectrum", 3)
				.setSecondary("nuggetSignalum", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(5).addAsPulverizerRecipe();
	}
}
