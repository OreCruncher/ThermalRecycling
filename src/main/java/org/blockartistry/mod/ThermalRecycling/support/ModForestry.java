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
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.support.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SawmillRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModForestry extends ModPlugin {

	SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();

	public ModForestry() {
		super(SupportedMod.FORESTRY);
	}

	@Override
	public void apply() {

		// Machine casings
		sawmill.append("Forestry:impregnatedCasing").output(Blocks.planks, 32)
				.save();
		pulverizer.append("Forestry:sturdyMachine").output("ingotBronze", 8)
				.save();
		pulverizer.append("Forestry:hardenedMachine").output("ingotBronze", 8)
				.secondaryOutput(Items.diamond, 4).save();

		// Engines
		pulverizer.append("Forestry:engine:1").secondaryOutput("ingotIron")
				.output("ingotCopper", 11).save();
		pulverizer.append("Forestry:engine:2").output("ingotBronze", 11).save();
		pulverizer.append("Forestry:engine:3").output("ingotCopper", 4)
				.secondaryOutput("ingotGold", 4).save();

		// Tubes
		ItemStack is = ItemStackHelper.getItemStack("Forestry:thermionicTubes",
				1);
		pulverizer.append(is).output("nuggetCopper", 11)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 1).output("nuggetTin", 11)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 2).output("nuggetBronze", 11)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 3).output("nuggetIron", 11)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 4).output("nuggetGold", 11)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 5).output(Items.diamond)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 6).output(Blocks.obsidian)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 7).output(Items.blaze_powder)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 8).output("itemRubber")
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 9).output(Items.emerald)
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 10).output("Forestry:apatite")
				.secondaryOutput(Items.redstone).chance(20).save();
		pulverizer.appendSubtype(is, 11).output("minecraft:dye:4")
				.secondaryOutput(Items.redstone).chance(20).save();

		pulverizer.appendSubtype(is, 12).output(Blocks.end_stone)
				.secondaryOutput(Items.ender_eye).chance(20).save();

		// Survivalist tools
		furnace.append("Forestry:bronzePickaxe", "Forestry:kitPickaxe")
				.output("ingotBronze", 3).save();
		furnace.append("Forestry:bronzeShovel", "Forestry:kitShovel")
				.output("ingotBronze").save();

		// Circuit boards
		pulverizer.append("Forestry:chipsets").output("ingotTin")
				.secondaryOutput(Items.redstone, 4).save();
		pulverizer.append("Forestry:chipsets:1").output("ingotBronze", 3)
				.secondaryOutput(Items.redstone, 4).save();
		pulverizer.append("Forestry:chipsets:2").output("ingotIron", 3)
				.secondaryOutput(Items.redstone, 4).save();
		pulverizer.append("Forestry:chipsets:3").output("ingotGold", 3)
				.secondaryOutput(Items.redstone, 4).save();

		// Various analyzers
		pulverizer.append("Forestry:beealyzer").output("ingotTin", 4)
				.secondaryOutput(Items.diamond).save();
		pulverizer.append("Forestry:treealyzer").output("ingotCopper", 4)
				.secondaryOutput(Items.diamond).save();
		pulverizer.append("Forestry:flutterlyzer").output("ingotBronze", 4)
				.secondaryOutput(Items.diamond).save();

		// Misc
		pulverizer.append("Forestry:solderingIron").output("ingotIron", 3)
				.secondaryOutput("ingotBronze").save();

		pulverizer.setEnergy(200).append("Forestry:canEmpty")
				.output("nuggetTin", 2).secondaryOutput("nuggetTin").chance(10)
				.save();

		pulverizer.append("Forestry:grafter").output("ingotBronze")
				.secondaryOutput("dustWood", 2).save();

		pulverizer.append("Forestry:habitatLocator").output("ingotBronze", 4)
				.save();

		pulverizer.append("Forestry:infuser").output("ingotBronze", 2)
				.secondaryOutput("ingotIron").save();
	}
}