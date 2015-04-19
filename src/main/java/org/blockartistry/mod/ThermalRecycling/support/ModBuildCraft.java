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
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

import cpw.mods.fml.common.Loader;

public class ModBuildCraft extends ModTweaks {

	@Override
	public String getName() {
		return "BuildCraft";
	}

	@Override
	public boolean isModLoaded() {
		return Loader.isModLoaded("BuildCraft|Core");
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();

		// Gears - metalic gears handled via Thermal Expansion
		helper.setInput("BuildCraft|Core:woodenGearItem")
				.setOutput("dustWood", 4).addAsSawmillRecipe();
		helper.setInput("BuildCraft|Core:stoneGearItem")
				.setOutput(Blocks.cobblestone, 4).setSecondary("dustWood")
				.setSecondaryChance(100).addAsPulverizerRecipe();
		helper.setInput("BuildCraft|Core:diamondGearItem")
				.setOutput(Items.diamond, 4).setSecondary("ingotGold", 4)
				.addAsPulverizerRecipe();

		// Misc block machines
		helper.reset();
		helper.setInput("BuildCraft|Builders:libraryBlock")
				.setOutput(Blocks.planks, 6).setSecondary(Items.book, 3)
				.setSecondaryChance(100).addAsSawmillRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Builders:builderBlock")
				.setOutput(Blocks.planks, 12).setSecondary(Items.diamond, 8)
				.setSecondaryChance(100).addAsSawmillRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Builders:architectBlock")
				.setOutput(Blocks.planks, 4).setSecondary(Items.diamond, 8)
				.setSecondaryChance(100).addAsSawmillRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Builders:fillerBlock")
				.setOutput(Blocks.planks, 12).setSecondary("ingotGold", 8)
				.setSecondaryChance(100).addAsSawmillRecipe();

		helper.reset();

		// Redstone Engine
		helper.setInput("BuildCraft|Energy:engineBlock")
				.setOutput(Blocks.planks, 6).setSecondary("ingotIron")
				.setSecondaryChance(100).addAsSawmillRecipe();

		// Stirling Engine
		helper.setInputSubtype(1).setOutput(Blocks.cobblestone, 15)
				.addAsPulverizerRecipe();

		// Combustion Engine
		helper.setInputSubtype(2).setOutput("ingotIron", 12)
				.addAsFurnaceRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Factory:autoWorkbenchBlock")
				.setOutput(Blocks.planks, 4).setSecondary("dustWood", 16)
				.setSecondaryChance(100).addAsSawmillRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Factory:miningWellBlock")
				.setOutput("ingotIron", 13).addAsFurnaceRecipe();
		helper.setInput("BuildCraft|Factory:pumpBlock").addAsFurnaceRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Factory:blockHopper")
				.setOutput("ingotIron", 5).setSecondary(Blocks.planks, 8)
				.setSecondaryChance(100).addAsPulverizerRecipe();

		// Quarry
		helper.reset();
		helper.setInput("BuildCraft|Factory:machineBlock")
				.setOutput(Items.diamond, 11).setSecondary("ingotGold", 8)
				.setSecondaryChance(100).addAsPulverizerRecipe();

		// Floodgate
		helper.reset();
		helper.setInput("BuildCraft|Factory:floodGateBlock")
				.setOutput("ingotIron", 9).addAsFurnaceRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Factory:refineryBlock")
				.setOutput(Items.diamond, 4).setSecondary("ingotGold", 4)
				.setSecondaryChance(100).addAsPulverizerRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Silicon:laserBlock")
				.setOutput(Items.diamond, 2).setSecondary(Blocks.obsidian, 2)
				.setSecondaryChance(100).addAsPulverizerRecipe();
		helper.setInput("BuildCraft|Silicon:laserTableBlock")
				.setOutputQuantity(5).setSecondaryQuantity(6)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(1).setOutput(Blocks.obsidian, 6)
				.setSecondary(Blocks.planks, 12).addAsPulverizerRecipe();
		helper.setInputSubtype(2).setSecondary(Items.diamond, 4)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setSecondary("ingotGold", 4)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(4).setOutput(Items.emerald)
				.setSecondary(Items.diamond, 4).addAsPulverizerRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Silicon:requester")
				.setOutput("ingotIron", 13).setSecondary(Blocks.planks, 11)
				.setSecondaryChance(100).addAsPulverizerRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Silicon:zonePlan")
				.setOutput(Items.diamond, 4).setSecondary("ingotGold", 8)
				.setSecondaryChance(100).addAsPulverizerRecipe();

		helper.reset();
		helper.setInput("BuildCraft|Transport:filteredBufferBlock")
				.setOutput(Blocks.planks, 17).setSecondary("ingotIron")
				.setSecondaryChance(100).addAsPulverizerRecipe();
	}
}
