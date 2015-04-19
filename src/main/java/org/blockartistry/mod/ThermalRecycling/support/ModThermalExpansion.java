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

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

public class ModThermalExpansion extends ModTweaks {

	@Override
	public String getName() {
		return "Thermal Expansion";
	}

	@Override
	public boolean isModLoaded() {
		return isModLoaded("ThermalExpansion");
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();

		ItemStack is = RecipeHelper
				.getItemStack("ThermalExpansion:material", 1);

		// Misc parts
		helper.setInput(is).setOutput("ingotIron", 2).addAsFurnaceRecipe();
		helper.setInputSubtype(1).setOutput("ingotGold").addAsFurnaceRecipe();
		helper.setInputSubtype(2).setOutput("ingotSilver").addAsFurnaceRecipe();
		helper.setInputSubtype(3).setOutput("ingotElectrum")
				.addAsFurnaceRecipe();
		helper.setInputSubtype(16).setOutput("nuggetSignalum", 6)
				.addAsFurnaceRecipe();

		// Dynamos
		is = RecipeHelper.getItemStack("ThermalExpansion:Dynamo", 1);
		helper.reset().setInput(is).setSecondary("ingotSilver")
				.setSecondaryChance(100);
		helper.setOutput("ingotCopper", 11).addAsPulverizerRecipe();
		helper.setInputSubtype(1).setOutput("ingotInvar", 11)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(2).setOutput("ingotTin", 11)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(3).setOutput("ingotBronze", 11)
				.addAsPulverizerRecipe();
		helper.setInputSubtype(4).setOutput("ingotElectrum", 11)
				.addAsPulverizerRecipe();

	}
}
