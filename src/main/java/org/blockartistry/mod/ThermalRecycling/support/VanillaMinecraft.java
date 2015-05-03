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

import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.common.config.Configuration;

public class VanillaMinecraft extends ModPlugin {

	static final String CONFIG_ENABLE_DIAMOND_RECIPIES = "Enable Diamond Recycling";
	static final String CONFIG_ENABLE_NETHER_STAR_RECIPIES = "Enable Nether Star Recycling";
	static final String CONFIG_QUANTITY_ROTTEN_FLESH_TO_LEATHER = "Quantity Rotten Flesh to Leather";

	static boolean enableDiamondRecycle = true;
	static boolean enableNetherStarRecycle = true;
	static int quantityRottenFleshToLeather = 2;

	public VanillaMinecraft() {
		super(SupportedMod.VANILLA);
	}

	@Override
	public void init(Configuration config) {

		enableDiamondRecycle = config.getBoolean(
				CONFIG_ENABLE_DIAMOND_RECIPIES, MOD_CONFIG_SECTION,
				enableDiamondRecycle,
				"Controls whether recycling items for diamonds is enabled");

		enableNetherStarRecycle = config.getBoolean(
				CONFIG_ENABLE_NETHER_STAR_RECIPIES, MOD_CONFIG_SECTION,
				enableNetherStarRecycle,
				"Controls whether recycling items for nether stars is enabled");

		quantityRottenFleshToLeather = config
				.getInt(CONFIG_QUANTITY_ROTTEN_FLESH_TO_LEATHER,
						MOD_CONFIG_SECTION, quantityRottenFleshToLeather, 0,
						64,
						"Amount of Rotten Flesh to use to create a piece of leather (0 to disable)");

	}

	@Override
	public void apply() {
		
		// Items
		ItemScrapData.put(Items.dye, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.arrow, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.blaze_powder, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.bone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.bowl, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.bread, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wheat, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.apple, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.reeds, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.egg, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.cake, ScrapValue.POOR, true, true);
		ItemScrapData.put(Items.carrot_on_a_stick, ScrapValue.POOR, true, false);
		ItemScrapData.put(Items.clay_ball, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.cookie, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.cooked_beef, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.cooked_porkchop, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.fish, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.cooked_fished, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.chicken, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.cooked_chicken, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.diamond, ScrapValue.SUPERIOR, true, false);
		ItemScrapData.put(Items.emerald, ScrapValue.SUPERIOR, true, false);
		ItemScrapData.put(Items.nether_star, ScrapValue.SUPERIOR, true, false);
		ItemScrapData.put(Items.ender_pearl, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.ender_eye, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.fermented_spider_eye, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.flint_and_steel, ScrapValue.NONE, false, false);
		ItemScrapData.put(Items.melon_seeds, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.pumpkin_seeds, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wheat_seeds, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.pumpkin_pie, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.sugar, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.gunpowder, ScrapValue.POOR, true, false);
		ItemScrapData.put(Items.string, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.chainmail_boots, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.chainmail_chestplate, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.chainmail_helmet, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.chainmail_leggings, ScrapValue.STANDARD, true, false);
		ItemScrapData.put(Items.map, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.coal, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.redstone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.lead, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.stone_axe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.stone_hoe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.stone_pickaxe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.stone_shovel, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.stone_sword, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.stick, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.wooden_axe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wooden_hoe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wooden_pickaxe, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wooden_shovel, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wooden_sword, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.wooden_door, ScrapValue.NONE, false, false);
		ItemScrapData.put(Items.lava_bucket, ScrapValue.STANDARD, true, true);
		ItemScrapData.put(Items.water_bucket, ScrapValue.STANDARD, true, true);
		ItemScrapData.put(Items.nether_wart, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.snowball, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.paper, ScrapValue.NONE, true, true);
		ItemScrapData.put(Items.painting, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.book, ScrapValue.NONE, false, false);
		ItemScrapData.put(Items.item_frame, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.rotten_flesh, ScrapValue.POOR, true, false);
		ItemScrapData.put(Items.leather, ScrapValue.NONE, true, false);
		ItemScrapData.put(Items.sign, ScrapValue.NONE, false, false);
		
		// Blocks
		ItemScrapData.put(Blocks.acacia_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.birch_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.spruce_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.jungle_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.brick_block, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.oak_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.brick_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.wooden_slab, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.double_wooden_slab, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.cobblestone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.cobblestone_wall, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stone_slab, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.double_stone_slab, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.dark_oak_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.dirt, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.mossy_cobblestone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.planks, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.fence, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.fence_gate, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.wooden_button, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stone_brick_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stone_button, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.stone_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stonebrick, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.tnt, ScrapValue.POOR, true, false);
		ItemScrapData.put(Blocks.hay_block, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.coal_block, ScrapValue.POOR, true, false);
		ItemScrapData.put(Blocks.carpet, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.hardened_clay, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stained_hardened_clay, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.glass, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.glass_pane, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.stained_glass, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.stained_glass_pane, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.furnace, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.sand, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.gravel, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.sandstone, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.sandstone_stairs, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.soul_sand, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.sapling, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.leaves, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.leaves2, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.lever, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.wool, ScrapValue.POOR, true, false);
		ItemScrapData.put(Blocks.mycelium, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.snow, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.snow_layer, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.ice, ScrapValue.NONE, true, true);
		ItemScrapData.put(Blocks.bookshelf, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.vine, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.log, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.log2, ScrapValue.NONE, true, false);
		ItemScrapData.put(Blocks.rail, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.ladder, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.redstone_torch, ScrapValue.NONE, false, false);
		ItemScrapData.put(Blocks.iron_bars, ScrapValue.NONE, false, false);

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

		pulverizer.setEnergy(1600).append(Blocks.clay)
				.output(Items.clay_ball, 4).save();

		if (enableDiamondRecycle) {
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

		if (enableNetherStarRecycle) {

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

		if (quantityRottenFleshToLeather > 0) {
			furnace.append(Items.rotten_flesh, quantityRottenFleshToLeather)
					.output(Items.leather).save();
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
