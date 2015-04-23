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
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.recipe.BlastRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.recipe.SmelterRecipeBuilder;

public class ModThaumcraft implements IModPlugin {

	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	BlastRecipeBuilder blast = new BlastRecipeBuilder();
	SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();

	@Override
	public void apply(ModOptions options) {

		// Saplings
		pulverizer.setEnergy(1200)
				.appendSubtypeRange("Thaumcraft:blockCustomPlant", 0, 1, 8)
				.output(Blocks.dirt).save();

		// Basic Thaumcraft tools and stuff
		furnace.append("Thaumcraft:ItemThaumometer").output("ingotGold", 2)
				.save();
		furnace.append("Thaumcraft:ItemBaubleBlanks",
				"Thaumcraft:ItemBaubleBlanks:2").output("ingotGold").save();
		furnace.append("Thaumcraft:ItemBaubleBlanks:1").output("nuggetGold", 4)
				.save();

		// Recycle Thaumium armor and tools
		furnace.append("Thaumcraft:ItemHelmetThaumium")
				.output("ingotThaumium", 5).save();
		furnace.append("Thaumcraft:ItemChestplateThaumium")
				.output("ingotThaumium", 8).save();
		furnace.append("Thaumcraft:ItemLeggingsThaumium")
				.output("ingotThaumium", 7).save();
		furnace.append("Thaumcraft:ItemBootsThaumium")
				.output("ingotThaumium", 5).save();
		furnace.append("Thaumcraft:ItemShovelThaumium").output("ingotThaumium")
				.save();
		furnace.append("Thaumcraft:ItemPickThaumium",
				"Thaumcraft:ItemAxeThaumium").output("ingotThaumium", 3).save();
		furnace.append("Thaumcraft:ItemSwordThaumium",
				"Thaumcraft:ItemHoeThaumium").output("ingotThaumium", 2).save();

		// Recycle magic tools - require smelter because the magic bindings need
		// to be broken
		blast.setEnergy(4800)
				.append("Thaumcraft:ItemSwordElemental",
						"Thaumcraft:ItemHoeElemental")
				.output("ingotThaumium", 2).save();
		blast.append("Thaumcraft:ItemShovelElemental").output("ingotThaumium")
				.save();
		blast.append("Thaumcraft:ItemPickaxeElemental",
				"Thaumcraft:ItemAxeElemental").output("ingotThaumium", 3)
				.save();

		// Recycle Void armor and tools; sand has a grounding effect when
		// smelting
		smelter.append("Thaumcraft:ItemHelmetVoid").secondaryInput(Blocks.sand)
				.output("ingotVoid", 5).save();
		smelter.append("Thaumcraft:ItemChestplateVoid")
				.secondaryInput(Blocks.sand).output("ingotVoid", 8).save();
		smelter.append("Thaumcraft:ItemLeggingsVoid")
				.secondaryInput(Blocks.sand).output("ingotVoid", 7).save();
		smelter.append("Thaumcraft:ItemBootsVoid").secondaryInput(Blocks.sand)
				.output("ingotVoid", 4).save();

		smelter.append("Thaumcraft:ItemPickVoid", "Thaumcraft:ItemAxeVoid")
				.secondaryInput(Blocks.sand).output("ingotVoid", 3).save();

		smelter.append("Thaumcraft:ItemSwordVoid", "Thaumcraft:ItemHoeVoid")
				.secondaryInput(Blocks.sand).output("ingotVoid", 2).save();

		smelter.append("Thaumcraft:ItemShovelVoid").secondaryInput(Blocks.sand)
				.output("ingotVoid").save();
	}
}
