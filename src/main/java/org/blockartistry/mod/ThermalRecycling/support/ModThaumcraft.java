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

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

import cpw.mods.fml.common.Loader;

public class ModThaumcraft extends ModTweaks {

	@Override
	public String getName() {
		return "Thaumcraft";
	}

	@Override
	public boolean isModLoaded() {
		return Loader.isModLoaded("Thaumcraft");
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();

		// Basic Thaumcraft tools and stuff
		helper.setInput("Thaumcraft:ItemThaumometer").setOutput("ingotGold", 2)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:blockCustomPlant", 8)
				.setOutput(Blocks.dirt).addAsPulverizerRecipe();
		helper.setInputSubtype(1).addAsPulverizerRecipe();
		helper.setInput("Thaumcraft:ItemBaubleBlanks").setOutput("ingotGold")
				.addAsFurnaceRecipe();
		helper.setInputSubtype(2).addAsFurnaceRecipe();
		helper.setInputSubtype(1).setOutput("nuggetGold", 4)
				.addAsFurnaceRecipe();

		// Recycle Thaumium armor and tools
		helper.setOutput("ingotThaumium");
		helper.setInput("Thaumcraft:ItemHelmetThaumium").setOutputQuantity(5)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemChestplateThaumium")
				.setOutputQuantity(8).addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemLeggingsThaumium").setOutputQuantity(7)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemBootsThaumium").setOutputQuantity(4)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemShovelThaumium").setOutputQuantity(1)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemPickThaumium").setOutputQuantity(3)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemAxeThaumium").setOutputQuantity(3)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemSwordThaumium").setOutputQuantity(2)
				.addAsFurnaceRecipe();
		helper.setInput("Thaumcraft:ItemHoeThaumium").setOutputQuantity(2)
				.addAsFurnaceRecipe();

		// Recycle magic tools - require smelter because the magic bindings need
		// to be broken
		helper.setEnergy(4800).setSecondary(
				ModThermalFoundation.getPyrotheumDust(1));
		helper.setInput("Thaumcraft:ItemSwordElemental").setOutputQuantity(2)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemShovelElemental").setOutputQuantity(1)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemPickaxeElemental").setOutputQuantity(3)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemAxeElemental").setOutputQuantity(3)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemHoeElemental").setOutputQuantity(2)
				.addAsSmelterRecipe();

		// Recycle Void armor and tools; sand has a grounding effect when
		// smelting
		helper.setOutput("ingotVoid").setSecondary(Blocks.sand);
		helper.setInput("Thaumcraft:ItemHelmetVoid").setOutputQuantity(5)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemChestplateVoid").setOutputQuantity(8)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemLeggingsVoid").setOutputQuantity(7)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemBootsVoid").setOutputQuantity(4)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemShovelVoid").setOutputQuantity(1)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemPickVoid").setOutputQuantity(3)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemAxeVoid").setOutputQuantity(3)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemSwordVoid").setOutputQuantity(2)
				.addAsSmelterRecipe();
		helper.setInput("Thaumcraft:ItemHoeVoid").setOutputQuantity(2)
				.addAsSmelterRecipe();
	}
}
