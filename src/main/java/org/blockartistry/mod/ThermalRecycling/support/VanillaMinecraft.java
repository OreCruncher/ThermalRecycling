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
import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.RecipeHelper;

public class VanillaMinecraft extends ModTweaks {

	protected void recycleSaplings() {

		RecipeHelper helper = new RecipeHelper();
		ItemStack sapling = new ItemStack(Blocks.sapling, 8);

		helper.setEnergy(1200).setInput(sapling).setOutput(Blocks.dirt);

		for (int i = 0; i < 6; i++) {
			helper.setInputSubtype(i).addAsPulverizerRecipe();
		}
	}

	@Override
	public void apply(ModOptions options) {

		RecipeHelper helper = new RecipeHelper();

		// Recycle some parts!!
		helper.reset().setEnergy(800);
		helper.setInput(Blocks.ladder).setOutput("dustWood", 2)
				.addAsSawmillRecipe();
		helper.setInput(Blocks.pumpkin).setOutput(Items.pumpkin_seeds, 4)
				.addAsPulverizerRecipe();
		helper.setInput(Items.melon).setOutput(Items.melon_seeds)
				.addAsPulverizerRecipe();
		helper.setInput(Blocks.melon_block).setOutput(Items.melon_seeds, 8)
				.addAsPulverizerRecipe();
		helper.setInput(Blocks.lit_pumpkin).setOutput(Blocks.torch)
				.addAsPulverizerRecipe();
		helper.setInput(Items.leather_helmet).setOutput(Items.leather, 5)
				.addAsPulverizerRecipe();
		helper.setInput(Items.leather_chestplate).setOutputQuantity(8)
				.addAsPulverizerRecipe();
		helper.setInput(Items.leather_leggings).setOutputQuantity(7)
				.addAsPulverizerRecipe();
		helper.setInput(Items.leather_boots).setOutputQuantity(4)
				.addAsPulverizerRecipe();

		helper.reset();
		helper.setInput(Blocks.redstone_lamp).setOutput("dustGlowstone", 4)
				.setSecondary("dustRedstone", 4).setSecondaryChance(100)
				.addAsPulverizerRecipe();
		helper.setInput(Blocks.brewing_stand).setOutput(Blocks.cobblestone, 3)
				.setSecondary(Items.blaze_powder, 4).setSecondaryChance(100)
				.addAsPulverizerRecipe();

		helper.reset().setEnergy(1600);
		helper.setInput(Blocks.clay).setOutput(Items.clay_ball, 4)
				.addAsPulverizerRecipe();

		if (options.getEnableDiamondRecycle()) {

			helper.reset();
			helper.setOutput(Items.diamond);

			helper.setInput(Items.diamond_helmet).setOutputQuantity(5)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_chestplate).setOutputQuantity(8)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_leggings).setOutputQuantity(7)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_boots).setOutputQuantity(4)
					.addAsPulverizerRecipe();

			helper.setInput(Items.diamond_sword).setOutputQuantity(2)
					.setSecondary("dustWood", 2).setSecondaryChance(100)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_hoe).addAsPulverizerRecipe();
			helper.setInput(Items.diamond_axe).setOutputQuantity(3)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_pickaxe).addAsPulverizerRecipe();
			helper.setInput(Items.diamond_shovel).setOutputQuantity(1)
					.addAsPulverizerRecipe();

			helper.setInput(Blocks.enchanting_table).setOutputQuantity(2)
					.setSecondary(Blocks.obsidian, 4).setSecondaryChance(100)
					.addAsPulverizerRecipe();
			helper.setInput(Items.diamond_horse_armor).setOutputQuantity(5)
					.setSecondary(Items.leather).setSecondaryChance(50)
					.addAsPulverizerRecipe();
		}

		if (options.getEnableNetherStarRecycle()) {

			helper.reset().setEnergy(21600);
			helper.setInput(Blocks.beacon).setOutput(Items.nether_star)
					.setSecondary(Blocks.obsidian, 3).setSecondaryChance(100)
					.addAsPulverizerRecipe();
		}

		helper.reset().setOutput(Items.iron_ingot);
		helper.setInput(Blocks.iron_door).setOutputQuantity(6)
				.addAsFurnaceRecipe();
		helper.setInput(Items.shears).setOutputQuantity(2).addAsFurnaceRecipe();
		helper.setInput(Items.bucket).setOutputQuantity(3).addAsFurnaceRecipe();
		helper.setInput(Items.milk_bucket).setOutputQuantity(3)
				.addAsFurnaceRecipe();
		helper.setInput(Blocks.hopper).setOutputQuantity(5)
				.addAsFurnaceRecipe();
		helper.setInput(Items.minecart).setOutputQuantity(5)
				.addAsFurnaceRecipe();
		helper.setInput(Blocks.cauldron).setOutputQuantity(7)
				.addAsFurnaceRecipe();
		helper.setInput(Items.compass).setOutputQuantity(4)
				.addAsFurnaceRecipe();

		helper.reset().setOutput("nuggetGold", 8);
		helper.setInput(Items.golden_apple).addAsFurnaceRecipe();
		helper.setInput(Items.golden_carrot).addAsFurnaceRecipe();
		helper.setInput(Items.speckled_melon).addAsFurnaceRecipe();

		helper.reset().setEnergy(150);
		helper.setInput(Blocks.iron_bars).setOutput("nuggetIron", 3)
				.addAsFurnaceRecipe();

		helper.reset();
		helper.setInput(Items.clock).setOutput("ingotGold", 4)
				.addAsFurnaceRecipe();

		helper.reset();
		helper.setInput(Items.rotten_flesh, 2).setOutput(Items.leather)
				.addAsFurnaceRecipe();
		helper.setInput(Items.chicken, 4).setOutput(Items.rotten_flesh)
				.addAsPulverizerRecipe();
		helper.setInput(Items.fish, 8).addAsPulverizerRecipe();
		helper.setInput(Items.beef, 2).setSecondary(Items.bone)
				.setSecondaryChance(25).addAsPulverizerRecipe();
		helper.setInput(Items.porkchop, 4).setSecondaryChance(10)
				.addAsPulverizerRecipe();

		helper.reset();
		helper.setOutput(Items.iron_ingot);
		helper.setInput(Items.iron_helmet).setOutputQuantity(5)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_chestplate).setOutputQuantity(8)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_leggings).setOutputQuantity(7)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_boots).setOutputQuantity(4)
				.addAsFurnaceRecipe();

		helper.setInput(Items.iron_sword).setOutputQuantity(2)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_hoe).addAsFurnaceRecipe();
		helper.setInput(Items.iron_axe).setOutputQuantity(3)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_pickaxe).addAsFurnaceRecipe();
		helper.setInput(Items.iron_shovel).setOutputQuantity(1)
				.addAsFurnaceRecipe();
		helper.setInput(Items.iron_horse_armor).setOutputQuantity(5)
				.addAsFurnaceRecipe();

		helper.reset();
		helper.setOutput(Items.gold_ingot);
		helper.setInput(Items.golden_helmet).setOutputQuantity(5)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_chestplate).setOutputQuantity(8)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_leggings).setOutputQuantity(7)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_boots).setOutputQuantity(4)
				.addAsFurnaceRecipe();

		helper.setInput(Items.golden_sword).setOutputQuantity(2)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_hoe).addAsFurnaceRecipe();
		helper.setInput(Items.golden_axe).setOutputQuantity(3)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_pickaxe).addAsFurnaceRecipe();
		helper.setInput(Items.golden_shovel).setOutputQuantity(1)
				.addAsFurnaceRecipe();
		helper.setInput(Items.golden_horse_armor).setOutputQuantity(5)
				.addAsFurnaceRecipe();

		helper.reset().setEnergy(1200).setOutput(Blocks.dirt);
		helper.setInput(Items.apple, 16).addAsPulverizerRecipe();
		helper.setInput(Items.carrot, 16).addAsPulverizerRecipe();
		helper.setInput(Items.potato, 16).addAsPulverizerRecipe();

		recycleSaplings();
	}

}
