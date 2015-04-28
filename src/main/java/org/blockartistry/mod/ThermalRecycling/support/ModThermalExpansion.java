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

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

import org.blockartistry.mod.ThermalRecycling.support.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;

public class ModThermalExpansion extends ModPlugin {

	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();

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

		// Machine Frames
		recycler.input("ThermalExpansion:Frame").append("ingotIron", 4)
				.append(Blocks.glass, 4).append("gearTin").save();
		recycler.input("ThermalExpansion:Frame:1").append("ingotInvar", 4)
				.append("gearElectrum").append("ThermalExpansion:Frame").save();
		recycler.input("ThermalExpansion:Frame:2")
				.append("ThermalExpansion:Glass", 4).append("gearSignalum")
				.append("ThermalExpansion:Frame:1").save();
		recycler.input("ThermalExpansion:Frame:3").append("ingotSilver", 4)
				.append("gearElectrum").append("ThermalExpansion:Frame:2")
				.save();

		// Machines - Redstone Furnace
		recycler.input("ThermalExpansion:Machine").append("gearCopper", 2)
				.append(Blocks.brick_block, 2)
				.append("ThermalExpansion:material:1").append(Items.redstone)
				.append("ThermalExpansion:Frame").save();

		// Machines - Pulverizer
		recycler.input("ThermalExpansion:Machine:1").append("gearCopper", 2)
				.append(Items.flint, 2).append("ThermalExpansion:material:1")
				.append(Blocks.piston).append("ThermalExpansion:Frame").save();

		// Machines - Sawmill
		recycler.input("ThermalExpansion:Machine:2").append("gearCopper", 2)
				.append(Blocks.planks, 2).append("ThermalExpansion:material:1")
				.append(Items.iron_axe).append("ThermalExpansion:Frame").save();

		// Machines - Induction Smelter
		recycler.input("ThermalExpansion:Machine:3").append("gearInvar", 2)
				.append("ingotInvar", 2).append("ThermalExpansion:material:1")
				.append(Items.bucket).append("ThermalExpansion:Frame").save();

		// Machines - Magma Crucible
		recycler.input("ThermalExpansion:Machine:4").append("gearInvar", 2)
				.append(Blocks.nether_brick, 2)
				.append("ThermalExpansion:material:1")
				.append("ThermalExpansion:Frame:4")
				.append("ThermalExpansion:Frame").save();

		// Machines - Fluid Transposer
		recycler.input("ThermalExpansion:Machine:5").append("gearCopper", 2)
				.append(Blocks.glass, 2).append("ThermalExpansion:material:1")
				.append(Items.bucket).append("ThermalExpansion:Frame").save();

		// Machines - Glacial Precipitator
		recycler.input("ThermalExpansion:Machine:6").append("gearCopper", 2)
				.append("ingotInvar", 2).append("ThermalExpansion:material:1")
				.append(Blocks.piston).append("ThermalExpansion:Frame").save();

		// Machines - Igneous Extruder
		recycler.input("ThermalExpansion:Machine:7").append("gearCopper", 2)
				.append(Blocks.glass, 2).append("ThermalExpansion:material")
				.append(Blocks.piston).append("ThermalExpansion:Frame").save();

		// Machines - Aqueous Accumulator
		recycler.input("ThermalExpansion:Machine:8").append("gearCopper", 2)
				.append(Blocks.glass, 2).append("ThermalExpansion:material")
				.append(Items.bucket).append("ThermalExpansion:Frame").save();

		// Machines - Cyclic Assembler
		recycler.input("ThermalExpansion:Machine:9").append("gearCopper", 2)
				.append("gearTin", 2).append("ThermalExpansion:material:1")
				.append(Blocks.chest).append("ThermalExpansion:Frame").save();

		// Machines - Energetic Infuser
		recycler.input("ThermalExpansion:Machine:10").append("gearCopper", 2)
				.append("ThermalExpansion:material:2", 2)
				.append("ThermalExpansion:material:1")
				.append("ThermalExpansion:Frame:4")
				.append("ThermalExpansion:Frame").save();

		// Machines - Phytogenic Insolator
		recycler.input("ThermalExpansion:Machine:11").append("gearCopper", 2)
				.append(Blocks.dirt, 2).append("ThermalExpansion:material:1")
				.append("gearLumium").append("ThermalExpansion:Frame").save();

		// Machinist Workbench
		recycler.input("ThermalExpansion:Device").append(Blocks.crafting_table)
				.append("ingotCopper", 2).append("ingotTin", 4)
				.append(Items.paper).save();

		// Autonomous Activator
		recycler.input("ThermalExpansion:Device:2").append(Blocks.chest)
				.append(Blocks.piston).append("gearTin", 2)
				.append("ThermalExpansion:material:1").save();

		// Terrain Smasher
		recycler.input("ThermalExpansion:Device:3")
				.append("ThermalExpansion:material").append("gearTin", 2)
				.append(Blocks.piston)
				.append("ThermalFoundation:tool.pickaxeInvar").save();

		// Nullifier
		recycler.input("ThermalExpansion:Device:5")
				.append("ThermalExpansion:material").append("gearTin", 2)
				.append(Items.lava_bucket).append("ingotInvar").save();

		// Dynamo - steam
		recycler.input("ThermalExpansion:Dynamo").append("gearCopper", 2)
				.append("ingotCopper", 3).append(Items.redstone)
				.append("ThermalExpansion:material:2").save();

		// Dynamo - magmatic
		recycler.input("ThermalExpansion:Dynamo:1").append("gearInvar", 2)
				.append("ingotInvar", 3).append(Items.redstone)
				.append("ThermalExpansion:material:2").save();

		// Dynamo - compression
		recycler.input("ThermalExpansion:Dynamo:2").append("gearTin", 2)
				.append("ingotTin", 3).append(Items.redstone)
				.append("ThermalExpansion:material:2").save();

		// Dynamo - reactant
		recycler.input("ThermalExpansion:Dynamo:3").append("gearBronze", 2)
				.append("ingotBronze", 3).append(Items.redstone)
				.append("ThermalExpansion:material:2").save();

		// Dynamo - envervation
		recycler.input("ThermalExpansion:Dynamo:4").append("gearElectrum", 2)
				.append("ingotElectrum", 3).append(Items.redstone)
				.append("ThermalExpansion:material:2").save();

		// Leadstone Energy Cell
		recycler.input("ThermalExpansion:Cell:1").append("ingotCopper", 3)
				.append("ThermalExpansion:material:3")
				.append("ThermalExpansion:Frame:4").save();

		// Hardened Energy Cell
		recycler.input("ThermalExpansion:Cell:2").append("ingotCopper", 3)
				.append("ingotInvar", 4).append("ThermalExpansion:material:3")
				.append("ThermalExpansion:Frame:4").save();

		// Redstone Energy Cell
		recycler.input("ThermalExpansion:Cell:3").append("ingotElectrum", 3)
				.append("ingotLead", 2).append("ThermalExpansion:material:3")
				.append("ThermalExpansion:Frame:7").save();

		// Resonant Energy Cell
		recycler.input("ThermalExpansion:Cell:4").append("ingotElectrum", 3)
				.append("ingotLead", 2).append("ThermalExpansion:material:3")
				.append("ThermalExpansion:Frame:9").save();

		// Portable Tank
		recycler.input("ThermalExpansion:Tank:1").append("ingotCopper")
				.append(Blocks.glass, 4).save();
		recycler.input("ThermalExpansion:Tank:2").append("ingotCopper")
				.append(Blocks.glass, 4).append("ingotInvar", 4).save();
		recycler.input("ThermalExpansion:Tank:3")
				.append("ThermalExpansion:Tank:2")
				.append("ThermalExpansion:Glass", 4).save();
		recycler.input("ThermalExpansion:Tank:4")
				.append("ThermalExpansion:Tank:3").append("ingotEnderium", 4)
				.save();

		// Strongbox
		recycler.input("ThermalExpansion:Strongbox:1").append("ingotTin", 4)
				.append(Blocks.chest).save();
		recycler.input("ThermalExpansion:Strongbox:2").append("ingotTin", 4)
				.append("ingotInvar", 4).append(Blocks.chest).save();
		recycler.input("ThermalExpansion:Strongbox:3")
				.append("ThermalExpansion:Strongbox:2")
				.append("ThermalExpansion:Glass", 4).save();
		recycler.input("ThermalExpansion:Strongbox:4")
				.append("ThermalExpansion:Strongbox:3")
				.append("ingotEnderium", 4).save();

		// Cache
		recycler.input("ThermalExpansion:Cache:1").append("ingotTin", 4)
				.append(Blocks.log).save();
		recycler.input("ThermalExpansion:Cache:2").append("ingotTin", 4)
				.append("ingotInvar", 4).append(Blocks.log).save();
		recycler.input("ThermalExpansion:Cache:3")
				.append("ThermalExpansion:Cache:2")
				.append("ThermalExpansion:Glass", 4).save();
		recycler.input("ThermalExpansion:Cache:4")
				.append("ThermalExpansion:Cache:3").append("ingotEnderium", 4)
				.save();

		// Tesseract
		recycler.input("ThermalExpansion:Tesseract").append("ingotBronze", 4)
				.append("ingotSilver", 4).append("ThermalExpansion:Frame:11")
				.save();

		// Leadstone, Hardened Frame
		recycler.input("ThermalExpansion:Frame:4")
				.append(Blocks.redstone_block).append(Blocks.glass, 4)
				.append("ingotLead", 4).save();
		recycler.input("ThermalExpansion:Frame:5")
				.append("ThermalExpansion:Frame:4").append("ingotInvar", 4)
				.save();

		// Redstone Frame (Empty)
		recycler.input("ThermalExpansion:Frame:6").append(Items.diamond)
				.append("ThermalExpansion:Glass", 4).append("ingotElectrum", 4)
				.save();

		// Resonant Frame (Empty)
		recycler.input("ThermalExpansion:Frame:8")
				.append("ThermalExpansion:Frame:6").append("ingotEnderium", 4)
				.save();

	}
}
