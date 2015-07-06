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

import org.blockartistry.mod.ThermalRecycling.blocks.PileOfRubble;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public final class VanillaMinecraft extends ModPlugin {

	private static final int ITEM_DAMAGE = 20;

	private static final String CONFIG_ENABLE_DIAMOND_RECIPIES = "Enable Diamond Recycling";
	private static final String CONFIG_ENABLE_NETHER_STAR_RECIPIES = "Enable Nether Star Recycling";
	private static final String CONFIG_QUANTITY_ROTTEN_FLESH_TO_LEATHER = "Quantity Rotten Flesh to Leather";

	private static boolean enableDiamondRecycle = true;
	private static boolean enableNetherStarRecycle = true;
	private static int quantityRottenFleshToLeather = 2;

	private static final String[] recipeIgnoreList = new String[] {
			"chainmail_helmet", "chainmail_leggings", "chainmail_boots",
			"chainmail_chestplate", "dye:*", "coal", "ender_pearl",
			"blaze_powder", "diamond", "emerald", "planks:*", "oak_stairs",
			"stone_stairs", "brick_stairs", "stone_brick_stairs",
			"nether_brick_stairs", "sandstone_stairs", "spruce_stairs",
			"birch_stairs", "jungle_stairs", "quartz_stairs", "acacia_stairs",
			"dark_oak_stairs", "wooden_slab:*", "stone_slab:*", "torch",
			"lit_pumpkin", "wooden_pressure_plate", "stone_pressure_plate",
			"wooden_button", "stone_button", "fence", "stick",
			"crafting_table", "chest", "ladder", "trapdoor", "fence_gate",
			"glass", "glass_pane", "wooden_shovel", "wooden_sword",
			"wooden_hoe", "wooden_pickaxe", "wooden_axe", "stone_shovel",
			"stone_sword", "stone_hoe", "stone_pickaxe", "stone_axe", "bowl",
			"string", "bow", "sign", "boat", "wooden_door", "fishing_rod",
			"paper", "hay_block", "slime_ball", "clay_ball", "sandstone:*",
			"brick_block", "mossy_cobblestone", "furnace", "lever",
			"glowstone", "nether_brick", "nether_brick_fence",
			"cobblestone_wall:*", "stained_hardened_clay:*",
			"stained_glass_pane:*", "carpet:*", "wool:*", "clay", "dispenser",
			"dropper", "painting", "cake", "lead", "snow", "glass_bottle",
			"item_frame", "sugar", "flower_pot",

	};

	private static final String[] scrapValuesPoor = new String[] { "cake",
			"gunpowder", "rotten_flesh", "tnt", "coal_block", "gold_nugget",
			"leather_helmet", "leather_chestplate", "leather_leggings",
			"leather_boots", "brewing_stand", "experience_bottle" };

	private static final String[] scrapValuesStandard = new String[] {
			"blaze_powder", "blaze_rod", "ender_eye", "ender_pearl",
			"chainmail_boots", "chainmail_chestplate", "chainmail_leggings",
			"chainmail_helmet", "lava_bucket", "water_bucket", "map",
			"filled_map", "iron_bars", "iron_ingot", "iron_block",
			"iron_helmet", "iron_chestplate", "iron_leggings", "iron_boots",
			"iron_sword", "iron_shovel", "iron_axe", "iron_pickaxe",
			"iron_hoe", "gold_ingot", "gold_block", "golden_helmet",
			"golden_chestplate", "golden_leggings", "golden_boots",
			"golden_sword", "golden_shovel", "golden_axe", "golden_pickaxe",
			"golden_hoe", "iron_door", "minecart", "chest_minecart",
			"furnace_minecart", "tnt_minecart", "hopper_minecart", "bucket",
			"lava_bucket", "water_bucket", "milk_bucket", "iron_horse_armor",
			"golden_horse_armor", "golden_carrot", "golden_apple",
			"speckled_melon", "compass", "clock", "cauldron", "magma_cream",
			"ghast_tear", "hopper", "light_weighted_pressure_plate",
			"heavy_weighted_pressure_plate", "daylight_detector", "shears",
			"quartz", "piston", "sticky_piston", "golden_rail",
			"detector_rail", "activator_rail", "glowstone", "redstone_lamp",
			"ender_chest", "enchanted_book", "quartz_block:*", "iron_ore",
			"gold_ore", "lapis_ore", "redstone_ore", "coal_ore", "skull:*",
			"name_tag",

	};

	private static final String[] scrapValuesSuperior = new String[] {
			"diamond", "emerald", "nether_star", "beacon",
			"diamond_horse_armor", "emerald_block", "diamond_block",
			"diamond_helmet", "diamond_chestplate", "diamond_leggings",
			"diamond_boots", "diamond_sword", "diamond_shovel", "diamond_axe",
			"diamond_pickaxe", "diamond_hoe", "anvil", "enchanting_table",
			"golden_apple:1", "jukebox", "diamond_ore", "emerald_ore",

	};

	private static final String[] brownCompost = new String[] { "sapling:*",
			"leaves:*", "leaves2:*", "deadbush", "vine", "wheat", };

	private static final String[] greenCompost = new String[] { "apple",
			"potato", "carrot", "yellow_flower:*", "red_flower:*",
			"tallgrass:*", "waterlily", "double_plant:*", "bread", };

	private static final String[] scrubFromOutput = new String[] {
			"water_bucket", "lava_bucket", "milk_bucket" };

	private static final String[] blockFromScrapping = new String[] {
			"cobblestone", "stone", "sand:*", "sandstone:*", "snowball",
			"cobblestone_wall:*", "dirt:*", "gravel", "stone_slab:*", "grass",
			"netherrack", "ice", "snow", "vine", "hardened_clay",
			"stained_hardened_clay:*", "glass_pane", "stained_glass_pane:*",
			"carpet:*", "flint", "nether_brick", "lever", "end_stone",
			"nether_brick_fence", "clay", "glass_bottle", "ladder", "sugar", };

	public VanillaMinecraft() {
		super(SupportedMod.VANILLA);
	}

	@Override
	public boolean preInit(final Configuration config) {

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

		return true;
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrubFromOutput(scrubFromOutput);

		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		registerItemBlockedFromScrapping(false, "wool:*");
		registerItemBlockedFromScrapping(true, blockFromScrapping);

		registerRecycleToWoodDust(1, "log:*", "log2:*");
		registerRecycleToWoodDust(2, "planks:*");
		registerRecycleToWoodDust(8, "sapling:*", "dye:*");
		registerRecycleToWoodDust(16, "apple", "potato", "carrot", "wheat",
				"reeds", "cactus", "brown_mushroom", "red_mushroom", "pumpkin",
				"nether_wart");
		registerRecycleToWoodDust(32, "wheat_seeds", "pumpkin_seeds",
				"melon_seeds", "melon");

		registerCompostIngredient(CompostIngredient.BROWN, brownCompost);
		registerCompostIngredient(CompostIngredient.GREEN, greenCompost);

		registerPulverizeToDirt("sapling", 0, 5);

		// Recycle some parts
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
			pulverizer.append(Items.diamond_sword).output(Items.diamond)
					.secondaryOutput("dustWood").save();
			pulverizer.append(Items.diamond_hoe).output(Items.diamond, 2)
					.secondaryOutput("dustWood", 2).save();
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

		pulverizer.append(Items.iron_door).output("dustIron", 6).save();
		pulverizer.append(Items.shears).output("dustIron", 2).save();
		pulverizer.append(Blocks.hopper).output("dustIron", 5).save();
		pulverizer.append(Items.minecart).output("dustIron", 5).save();
		pulverizer.append(Items.cauldron).output("dustIron", 7).save();
		pulverizer.append(Items.compass).output("dustIron", 4).save();

		pulverizer.append(Items.bucket, Items.milk_bucket, Items.water_bucket)
				.output("dustIron", 3).save();

		pulverizer.append(Items.golden_carrot, Items.speckled_melon)
				.output("nuggetGold", 8).save();

		pulverizer.append(Items.golden_apple).output("dustGold", 8).save();

		pulverizer.setEnergy(150).append(Blocks.iron_bars)
				.output("nuggetIron", 3).save();

		pulverizer.append(Items.clock).output("dustGold", 4).save();

		if (quantityRottenFleshToLeather > 0) {
			furnace.append(Items.rotten_flesh, quantityRottenFleshToLeather)
					.output(Items.leather).save();
		}

		pulverizer.append(Items.chicken, 4).output(Items.rotten_flesh).save();
		pulverizer.append(Items.beef, 2).output(Items.rotten_flesh)
				.secondaryOutput(Items.bone).chance(25).save();
		pulverizer.append(Items.porkchop, 4).output(Items.rotten_flesh)
				.secondaryOutput(Items.bone).chance(10).save();

		pulverizer.append(Items.iron_helmet, Items.iron_horse_armor)
				.output("dustIron", 5).save();
		pulverizer.append(Items.iron_chestplate).output("dustIron", 8).save();
		pulverizer.append(Items.iron_leggings).output("dustIron", 7).save();
		pulverizer.append(Items.iron_boots).output("dustIron", 4).save();

		pulverizer.append(Items.iron_sword, Items.iron_hoe)
				.output("dustIron", 2).save();
		pulverizer.append(Items.iron_axe, Items.iron_pickaxe)
				.output("dustIron", 3).save();
		pulverizer.append(Items.iron_shovel).output("dustIron").save();

		pulverizer.append(Items.golden_helmet, Items.golden_horse_armor)
				.output("dustGold", 5).save();
		pulverizer.append(Items.golden_chestplate).output("dustGold", 8).save();
		pulverizer.append(Items.golden_leggings).output("dustGold", 7).save();
		pulverizer.append(Items.golden_boots).output("dustGold", 4).save();

		pulverizer.append(Items.golden_sword, Items.golden_hoe)
				.output("dustGold", 2).save();
		pulverizer.append(Items.golden_axe, Items.golden_pickaxe)
				.output("dustGold", 3).save();
		pulverizer.append(Items.golden_shovel).output("dustGold").save();

		pulverizer.append(Blocks.light_weighted_pressure_plate)
				.output("dustGold", 2).save();
		pulverizer.append(Blocks.heavy_weighted_pressure_plate)
				.output("dustIron", 2).save();

		pulverizer.setEnergy(1200).append(Items.apple, 16)
				.append(Items.carrot, 16).append(Items.potato, 16)
				.output(Blocks.dirt).save();

		sawmill.setEnergy(1200).append(Items.wooden_door)
				.output(Blocks.planks, 6).save();
		sawmill.append(Blocks.trapdoor).output(Blocks.planks, 3).save();

		pulverizer.setEnergy(1200).appendSubtypeRange(Items.fish, 0, 4, 8)
				.output(Items.rotten_flesh).save();

		// Glass items
		pulverizer.setEnergy(3200)
				.appendSubtypeRange(Blocks.stained_glass, 0, 15)
				.output(Blocks.sand).save();
		pulverizer.setEnergy(3200).append(Blocks.glass_pane, 8)
				.output(Blocks.sand, 3).save();
		pulverizer.setEnergy(3200)
				.appendSubtypeRange(Blocks.stained_glass_pane, 0, 15, 8)
				.output(Blocks.sand, 3).save();
		pulverizer.setEnergy(3200).append(Items.glass_bottle)
				.output(Blocks.sand).save();

		return true;
	}

	@Override
	public boolean postInit() {

		PileOfRubble.addRubbleDrop(Blocks.cobblestone, 1, 4, 12);
		PileOfRubble.addRubbleDrop(Blocks.stone, 1, 2, 9);
		PileOfRubble.addRubbleDrop(Items.coal, 1, 3, 8);
		PileOfRubble.addRubbleDrop(Blocks.gravel, 1, 2, 9);
		PileOfRubble.addRubbleDrop(Blocks.sand, 1, 2, 8);
		PileOfRubble.addRubbleDrop(Blocks.dirt, 1, 3, 10);
		PileOfRubble.addRubbleDrop(Blocks.clay, 1, 1, 7);

		PileOfRubble.addRubbleDrop(Items.bread, 1, 3, 8);
		PileOfRubble.addRubbleDrop(Items.cooked_beef, 1, 3, 6);
		PileOfRubble.addRubbleDrop(Items.rotten_flesh, 1, 2, 5);
		PileOfRubble.addRubbleDrop(Items.bone, 1, 2, 5);
		PileOfRubble.addRubbleDrop(Blocks.torch, 1, 8, 8);
		PileOfRubble.addRubbleDrop(Blocks.iron_ore, 1, 3, 5);
		PileOfRubble.addRubbleDrop(Blocks.gold_ore, 1, 2, 3);
		PileOfRubble.addRubbleDrop(Items.redstone, 1, 2, 3);
		PileOfRubble.addRubbleDrop(Items.diamond, 1, 1, 1);
		PileOfRubble.addRubbleDrop(Items.emerald, 1, 1, 1);
		PileOfRubble.addRubbleDrop(Blocks.tnt, 1, 1, 4);

		PileOfRubble.addRubbleDrop(new ItemStack(Items.iron_pickaxe, 1,
				ITEM_DAMAGE), 1, 1, 4);
		PileOfRubble.addRubbleDrop(new ItemStack(Items.iron_helmet, 1,
				ITEM_DAMAGE), 1, 1, 3);
		PileOfRubble.addRubbleDrop(new ItemStack(Items.iron_sword, 1,
				ITEM_DAMAGE), 1, 1, 3);

		return true;
	}
}
