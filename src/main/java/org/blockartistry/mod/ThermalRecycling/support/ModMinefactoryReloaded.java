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

public class ModMinefactoryReloaded extends ModTweaks {

	@Override
	public String getName() {
		return "MineFactory Reloaded";
	}

	@Override
	public boolean isModLoaded() {
		return Loader.isModLoaded("MineFactoryReloaded");
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();
		
		// Add the rubber saplings for recycling.  We don't want the big daddy.
		helper.setInput("MineFactoryReloaded:rubberwood.sapling", 8).setOutput(Blocks.dirt).addAsPulverizerRecipe();
		helper.setInputSubtype(1).addAsPulverizerRecipe();
		helper.setInputSubtype(2).setInputQuantity(2).addAsPulverizerRecipe();
		
		// Range upgrades
		helper.reset().setSecondary("nuggetGold").setSecondaryChance(100);
		helper.setInput("MineFactoryReloaded:upgrade.radius").setOutput(Items.dye, 3).setOutputSubtype(4).addAsPulverizerRecipe();
		helper.setInputSubtype(1).setOutput("ingotTin", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(2).setOutput("ingotIron", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(3).setOutput("ingotCopper", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(4).setOutput("ingotBronze", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(5).setOutput("ingotSilver", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(6).setOutput("ingotGold", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(7).setOutput(Items.quartz, 3).addAsPulverizerRecipe();
		helper.setInputSubtype(8).setOutput(Items.diamond, 3).addAsPulverizerRecipe();
		helper.setInputSubtype(9).setOutput("ingotPlatinum", 3).addAsPulverizerRecipe();
		helper.setInputSubtype(10).setOutput(Items.emerald, 3).addAsPulverizerRecipe();
		helper.setInputSubtype(11).setOutput(Blocks.cobblestone, 3).addAsPulverizerRecipe();
		
		//	Laser Focii
		helper.reset().setInput("MineFactoryReloaded:laserfocus").setOutput(Items.emerald, 4).setSecondary("nuggetGold", 4).setSecondaryChance(100);
		for(int i = 0; i < 16; i++) {
			helper.setInputSubtype(i).addAsPulverizerRecipe();
		}
		
		// Syringe
		helper.reset().setInput("MineFactoryReloaded:syringe.empty").setOutput("ingotIron").addAsFurnaceRecipe();
		helper.setInput("MineFactoryReloaded:syringe.zombie").addAsFurnaceRecipe();
		helper.setInput("MineFactoryReloaded:syringe.cure").addAsFurnaceRecipe();
		helper.setInput("MineFactoryReloaded:syringe.growth").addAsFurnaceRecipe();
		helper.setInput("MineFactoryReloaded:syringe.slime").addAsFurnaceRecipe();
		helper.setInput("MineFactoryReloaded:syringe.health").addAsFurnaceRecipe();
		
		// Plastic
		helper.reset().setInput("MineFactoryReloaded:plastic.sheet").setOutput("MineFactoryReloaded:plastic.raw").addAsPulverizerRecipe();
		helper.setInput("MineFactoryReloaded:xpextractor").setOutputQuantity(5).addAsPulverizerRecipe();
		helper.setInput("MineFactoryReloaded:straw").setOutputQuantity(4).addAsPulverizerRecipe();
		helper.setInput("MineFactoryReloaded:plastic.boots").addAsPulverizerRecipe();
		helper.setInput("MineFactoryReloaded:record.blank").setOutputQuantity(8).addAsPulverizerRecipe();
		
		// Misc
		helper.setInput("MineFactoryReloaded:needlegun.ammo.empty").setOutputQuantity(1).setSecondary("nuggetIron", 2).setSecondaryChance(100).addAsPulverizerRecipe();
		helper.setInput("MineFactoryReloaded:spyglass").setOutput("ingotBronze", 2).setSecondary("MineFactoryReloaded:plastic.raw", 2).addAsPulverizerRecipe();
		
	}
}
