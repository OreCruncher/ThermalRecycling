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

public class VanillaMinecraft extends ModTweaks {

	SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();

	@Override
	public String getName() {
		return "Vanilla Minecraft";
	}

	@Override
	public boolean isModLoaded() {
		return true;
	}

	@Override
	public void apply(ModOptions options) {

		// Recycle some parts!!
		sawmill.setEnergy(800).append(Blocks.ladder).output("dustWood", 2)
				.save();
		pulverizer.setEnergy(800).append(Blocks.pumpkin)
				.output(Items.pumpkin_seeds, 4).save();

		pulverizer.setEnergy(800).append(Items.melon).output(Items.melon_seeds)
				.save();
		pulverizer.setEnergy(800).append(Blocks.melon_block)
				.output(Items.melon_seeds, 8).save();
		pulverizer.setEnergy(800).append(Blocks.lit_pumpkin)
				.output(Blocks.torch).save();

		pulverizer.setEnergy(800).append(Items.leather_helmet, Items.saddle)
				.output(Items.leather, 5).save();
		pulverizer.setEnergy(800).append(Items.leather_chestplate)
				.output(Items.leather, 8).save();
		pulverizer.setEnergy(800).append(Items.leather_leggings)
				.output(Items.leather, 7).save();
		pulverizer.setEnergy(800).append(Items.leather_boots)
				.output(Items.leather, 4).save();

		pulverizer.append(Blocks.redstone_lamp).output("dustGlowstone", 4)
				.secondaryOutput("dustRedstone", 4).save();
		pulverizer.append(Items.brewing_stand).output(Blocks.cobblestone, 3)
				.secondaryOutput(Items.blaze_powder, 4).save();

		pulverizer.setEnergy(1600).append(Blocks.clay)
				.output(Items.clay_ball, 4).save();

		if (options.getEnableDiamondRecycle()) {
			pulverizer.append(Items.diamond_helmet).output(Items.diamond, 5)
					.save();
			pulverizer.append(Items.diamond_chestplate)
					.output(Items.diamond, 8).save();
			pulverizer.append(Items.diamond_leggings).output(Items.diamond, 7)
					.save();
			pulverizer.append(Items.diamond_boots).output(Items.diamond, 4)
					.save();
			pulverizer.append(Items.diamond_sword, Items.diamond_hoe)
					.output(Items.diamond, 2).secondaryOutput("dustWood", 2)
					.save();
			pulverizer.append(Items.diamond_axe, Items.diamond_pickaxe)
					.output(Items.diamond, 3).secondaryOutput("dustWood", 2)
					.save();
			pulverizer.append(Items.diamond_shovel).output(Items.diamond)
					.secondaryOutput("dustWood", 2).save();
			pulverizer.append(Blocks.enchanting_table).output(Items.diamond, 2)
					.secondaryOutput(Blocks.obsidian, 4).save();
			pulverizer.append(Items.diamond_horse_armor)
					.output(Items.diamond, 5).secondaryOutput(Items.leather)
					.chance(50).save();
		}

		if (options.getEnableNetherStarRecycle()) {

			pulverizer.setEnergy(21600).append(Blocks.beacon)
					.output(Items.nether_star)
					.secondaryOutput(Blocks.obsidian, 3).save();
		}

		furnace.append(Items.iron_door).output("ingotIron", 6).save();
		furnace.append(Items.shears).output("ingotIron", 2).save();
		furnace.append(Blocks.hopper).output("ingotIron", 5).save();
		furnace.append(Items.minecart).output("ingotIron", 5).save();
		furnace.append(Items.cauldron).output("ingotIron", 7).save();
		furnace.append(Items.compass).output("ingotIron", 4).save();

		furnace.append(Items.bucket, Items.milk_bucket, Items.water_bucket)
				.output("ingotIron", 3).save();

		furnace.append(Items.golden_apple, Items.golden_carrot,
				Items.speckled_melon).output("nuggetGold", 8).save();

		furnace.setEnergy(150).append(Blocks.iron_bars).output("nuggetIron", 3)
				.save();

		furnace.append(Items.clock).output("ingotGold", 4).save();

		int amount = options.getQuantityRottenFleshToLeather();
		if (amount > 0) {
			furnace.append(Items.rotten_flesh, amount).output(Items.leather)
					.save();
		}

		pulverizer.append(Items.chicken, 4).output(Items.rotten_flesh).save();
		pulverizer.append(Items.beef, 2).output(Items.rotten_flesh)
				.secondaryOutput(Items.bone).chance(25).save();
		pulverizer.append(Items.porkchop, 4).output(Items.rotten_flesh)
				.secondaryOutput(Items.bone).chance(10).save();

		furnace.append(Items.iron_helmet, Items.iron_horse_armor)
				.output("ingotIron", 5).save();
		furnace.append(Items.iron_chestplate).output("ingotIron", 8).save();
		furnace.append(Items.iron_leggings).output("ingotIron", 7).save();
		furnace.append(Items.iron_boots).output("ingotIron", 4).save();

		furnace.append(Items.iron_sword, Items.iron_hoe).output("ingotIron", 2)
				.save();
		furnace.append(Items.iron_axe, Items.iron_pickaxe)
				.output("ingotIron", 3).save();
		furnace.append(Items.iron_shovel).output("ingotIron").save();

		furnace.append(Items.golden_helmet, Items.golden_horse_armor)
				.output("ingotGold", 5).save();
		furnace.append(Items.golden_chestplate).output("ingotGold", 8).save();
		furnace.append(Items.golden_leggings).output("ingotGold", 7).save();
		furnace.append(Items.golden_boots).output("ingotGold", 4).save();

		furnace.append(Items.golden_sword, Items.golden_hoe)
				.output("ingotGold", 2).save();
		furnace.append(Items.golden_axe, Items.golden_pickaxe)
				.output("ingotGold", 3).save();
		furnace.append(Items.golden_shovel).output("ingotGold").save();

		furnace.append(Blocks.light_weighted_pressure_plate)
				.output("ingotGold", 2).save();
		furnace.append(Blocks.heavy_weighted_pressure_plate)
				.output("ingotIron", 2).save();

		pulverizer.setEnergy(1200).append(Items.apple, 16)
				.append(Items.carrot, 16).append(Items.potato, 16)
				.output(Blocks.dirt).save();

		sawmill.setEnergy(1200).append(Items.wooden_door)
				.output(Blocks.planks, 6).save();
		sawmill.append(Blocks.trapdoor).output(Blocks.planks, 3).save();

		pulverizer.setEnergy(1200).appendSubtypeRange(Blocks.sapling, 0, 5, 8)
				.output(Blocks.dirt).save();

		pulverizer.setEnergy(1200).appendSubtypeRange(Items.fish, 0, 4, 8)
				.output(Items.rotten_flesh).save();
	}
}
