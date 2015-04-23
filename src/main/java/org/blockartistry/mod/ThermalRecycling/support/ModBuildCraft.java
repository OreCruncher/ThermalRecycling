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
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.SawmillRecipeBuilder;

public class ModBuildCraft implements IModPlugin {

	SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();

	@Override
	public void apply(ModOptions options) {

		// Gears - metalic gears handled via Thermal Expansion
		sawmill.append("BuildCraft|Core:woodenGearItem").output("dustWood", 4)
				.save();
		pulverizer.append("BuildCraft|Core:stoneGearItem")
				.output(Blocks.cobblestone, 4).secondaryOutput("dustWood")
				.save();
		pulverizer.append("BuildCraft|Core:diamondGearItem")
				.output(Items.diamond, 4).secondaryOutput("ingotGold", 4)
				.save();

		// Misc block machines
		sawmill.append("BuildCraft|Builders:libraryBlock")
				.output(Blocks.planks, 6).secondaryOutput(Items.book, 3).save();

		sawmill.append("BuildCraft|Builders:builderBlock")
				.output(Blocks.planks, 12).secondaryOutput(Items.diamond, 8)
				.save();

		sawmill.append("BuildCraft|Builders:architectBlock")
				.output(Blocks.planks, 4).secondaryOutput(Items.diamond, 8)
				.save();

		sawmill.append("BuildCraft|Builders:fillerBlock")
				.output(Blocks.planks, 12).secondaryOutput("ingotGold", 8)
				.save();

		// Redstone Engine
		sawmill.append("BuildCraft|Energy:engineBlock")
				.output(Blocks.planks, 6).secondaryOutput("ingotIron").save();

		// Stirling Engine
		pulverizer.append("BuildCraft|Energy:engineBlock:1")
				.output(Blocks.cobblestone, 15).secondaryOutput("ingotIron")
				.save();

		// Combustion Engine
		furnace.append("BuildCraft|Energy:engineBlock:2")
				.output("ingotIron", 12).save();

		sawmill.append("BuildCraft|Factory:autoWorkbenchBlock")
				.output(Blocks.planks, 4).secondaryOutput("dustWood", 16)
				.save();

		furnace.append("BuildCraft|Factory:miningWellBlock",
				"BuildCraft|Factory:pumpBlock").output("ingotIron", 13).save();

		pulverizer.append("BuildCraft|Factory:blockHopper")
				.output("ingotIron", 5).secondaryOutput(Blocks.planks, 8)
				.save();

		// Quarry
		pulverizer.append("BuildCraft|Factory:machineBlock")
				.output(Items.diamond, 11).secondaryOutput("ingotGold", 8)
				.save();

		// Floodgate
		furnace.append("BuildCraft|Factory:floodGateBlock")
				.output("ingotIron", 9).save();

		pulverizer.append("BuildCraft|Factory:refineryBlock")
				.output(Items.diamond, 4).secondaryOutput("ingotGold", 4)
				.save();

		pulverizer.append("BuildCraft|Silicon:laserBlock")
				.output(Items.diamond, 2).secondaryOutput(Blocks.obsidian, 2)
				.save();
		pulverizer.append("BuildCraft|Silicon:laserTableBlock")
				.output(Items.diamond, 5).secondaryOutput(Blocks.obsidian, 6)
				.save();

		pulverizer.append("BuildCraft|Silicon:laserTableBlock:1")
				.output(Blocks.obsidian, 6).secondaryOutput(Blocks.planks, 12)
				.save();
		pulverizer.append("BuildCraft|Silicon:laserTableBlock:2")
				.output(Blocks.obsidian, 6).secondaryOutput(Items.diamond, 4)
				.save();
		pulverizer.append("BuildCraft|Silicon:laserTableBlock:3")
				.output(Blocks.obsidian, 6).secondaryOutput("ingotGold", 4)
				.save();
		pulverizer.append("BuildCraft|Silicon:laserTableBlock:4")
				.output(Items.emerald).secondaryOutput(Items.diamond, 4).save();

		pulverizer.append("BuildCraft|Silicon:requester")
				.output("ingotIron", 13).secondaryOutput(Blocks.planks, 11)
				.save();

		pulverizer.append("BuildCraft|Silicon:zonePlan")
				.output(Items.diamond, 4).secondaryOutput("ingotGold", 8)
				.save();

		pulverizer.append("BuildCraft|Transport:filteredBufferBlock")
				.output(Blocks.planks, 17).secondaryOutput("ingotIron").save();

		// Pipes! Fluid and Kinesis have crafting recipes that return them to
		// base types already.
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemswood",
						4).output(Blocks.planks).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsemerald",
						4).output(Items.emerald).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemscobblestone",
						4).output(Blocks.cobblestone).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsstone",
						4).output(Blocks.cobblestone).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsquartz",
						4).output(Items.quartz, 4).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsiron",
						4).output("ingotIron").save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsgold",
						4).output("ingotGold").save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsdiamond",
						4).output(Items.diamond).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsobsidian",
						4).output(Blocks.obsidian).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemslapis",
						4).output("blockLapis").save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsdaizuli",
						8).output(Items.diamond).secondaryOutput("blockLapis")
				.save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemssandstone",
						4).output(Blocks.sandstone).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsemzuli",
						8).output(Items.emerald).secondaryOutput("blockLapis")
				.save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsstripes",
						4).output("ingotGold", 4)
				.secondaryOutput("ingotIron", 4).save();
		pulverizer
				.setEnergy(1200)
				.append("BuildCraft|Transport:item.buildcraftPipe.pipeitemsclay",
						4).output(Items.clay_ball, 4).save();

		// Silicon
		pulverizer.setEnergy(600).append("BuildCraft|Silicon:redstoneChipset")
				.output(Items.redstone).save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:1")
				.output("ingotIron").save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:2")
				.output("ingotGold").save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:3")
				.output(Items.diamond).save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:4", 2)
				.output(Items.ender_pearl).save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:5")
				.output(Items.quartz).save();
		// pulverizer.setEnergy(600).append("BuildCraft|Silicon:redstoneChipset:6").output(Items.diamond).save();
		pulverizer.setEnergy(600)
				.append("BuildCraft|Silicon:redstoneChipset:7")
				.output(Items.emerald).save();

		// Pipe Wire
		furnace.setEnergy(600)
				.appendSubtypeRange("BuildCraft|Transport:pipeWire", 0, 3, 4)
				.output("nuggetIron", 4).save();
	}
}
