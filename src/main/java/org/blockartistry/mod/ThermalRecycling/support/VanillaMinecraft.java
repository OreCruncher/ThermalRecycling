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

import org.blockartistry.mod.ThermalRecycling.breeding.BreedingItemManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.PreferredItemStacks;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;

import net.minecraft.entity.passive.EntityPig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public final class VanillaMinecraft extends ModPlugin {

	private static final String CONFIG_ENABLE_DIAMOND_RECIPIES = "Enable Diamond Recycling";
	private static final String CONFIG_ENABLE_NETHER_STAR_RECIPIES = "Enable Nether Star Recycling";
	private static final String CONFIG_QUANTITY_ROTTEN_FLESH_TO_LEATHER = "Quantity Rotten Flesh to Leather";

	private static boolean enableDiamondRecycle = true;
	private static boolean enableNetherStarRecycle = true;
	private static int quantityRottenFleshToLeather = 2;

	public VanillaMinecraft() {
		super(SupportedMod.VANILLA);
	}

	@Override
	public String getVersion() {
		return "1.7.10";
	}

	@Override
	public boolean preInit(final Configuration config) {
		
		super.preInit(config);

		enableDiamondRecycle = config.getBoolean(CONFIG_ENABLE_DIAMOND_RECIPIES, MOD_CONFIG_SECTION,
				enableDiamondRecycle, "Controls whether recycling items for diamonds is enabled");

		enableNetherStarRecycle = config.getBoolean(CONFIG_ENABLE_NETHER_STAR_RECIPIES, MOD_CONFIG_SECTION,
				enableNetherStarRecycle, "Controls whether recycling items for nether stars is enabled");

		quantityRottenFleshToLeather = config.getInt(CONFIG_QUANTITY_ROTTEN_FLESH_TO_LEATHER, MOD_CONFIG_SECTION,
				quantityRottenFleshToLeather, 0, 64,
				"Amount of Rotten Flesh to use to create a piece of leather (0 to disable)");

		return true;
	}

	@Override
	public boolean initialize() {
		
		// Extraction recipes
		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:netherrack", 1).get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:gravel").get(), 40),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:glowstone_dust").get(), 4),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:redstone").get(), 4),
				new ItemStackItem(PreferredItemStacks.instance.dustElectrum, 2));

		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:end_stone").get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:stone").get(), 3),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:ender_pearl").get(), 1));

		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:soul_sand").get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:sand").get(), 8),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:gravel").get(), 7),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:ghast_tear").get(), 1));

		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:pumpkin").get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:pumpkin_seeds", 6).get(), 1));

		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:melon_block").get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:melon_seeds", 12).get(), 1));

		registerExtractionRecipe(ItemStackHelper.getItemStack("minecraft:wheat").get(),
				new ItemStackItem(ItemStackHelper.getItemStack("minecraft:wheat_seeds", 2).get(), 1));

		// Recycle some parts
		sawmill.setEnergy(800).append(Blocks.ladder).output("dustWood", 2).save();
		pulverizer.setEnergy(800).append(Blocks.pumpkin).output(Items.pumpkin_seeds, 4).save();

		pulverizer.setEnergy(800).append(Items.melon).output(Items.melon_seeds).save();
		pulverizer.setEnergy(800).append(Blocks.melon_block).output(Items.melon_seeds, 8).save();
		pulverizer.setEnergy(800).append(Blocks.lit_pumpkin).output(Blocks.torch).save();

		pulverizer.setEnergy(800).append(Items.leather_helmet, Items.saddle).output(Items.leather, 5).save();
		pulverizer.setEnergy(800).append(Items.leather_chestplate).output(Items.leather, 8).save();
		pulverizer.setEnergy(800).append(Items.leather_leggings).output(Items.leather, 7).save();
		pulverizer.setEnergy(800).append(Items.leather_boots).output(Items.leather, 4).save();

		pulverizer.setEnergy(1600).append(Blocks.clay).output(Items.clay_ball, 4).save();

		if (enableDiamondRecycle) {
			pulverizer.append(Items.diamond_helmet).output(Items.diamond, 5).save();
			pulverizer.append(Items.diamond_chestplate).output(Items.diamond, 8).save();
			pulverizer.append(Items.diamond_leggings).output(Items.diamond, 7).save();
			pulverizer.append(Items.diamond_boots).output(Items.diamond, 4).save();
			pulverizer.append(Items.diamond_sword).output(Items.diamond, 2).secondaryOutput("dustWood").save();
			pulverizer.append(Items.diamond_hoe).output(Items.diamond, 2).secondaryOutput("dustWood", 2).save();
			pulverizer.append(Items.diamond_axe, Items.diamond_pickaxe).output(Items.diamond, 3)
					.secondaryOutput("dustWood", 2).save();
			pulverizer.append(Items.diamond_shovel).output(Items.diamond).secondaryOutput("dustWood", 2).save();
			pulverizer.append(Blocks.enchanting_table).output(Items.diamond, 2).secondaryOutput(Blocks.obsidian, 4)
					.save();
			pulverizer.append(Items.diamond_horse_armor).output(Items.diamond, 5).secondaryOutput(Items.leather)
					.chance(50).save();
		}

		if (enableNetherStarRecycle) {

			pulverizer.setEnergy(21600).append(Blocks.beacon).output(Items.nether_star)
					.secondaryOutput(Blocks.obsidian, 3).save();
		}

		pulverizer.append(Items.iron_door).output("dustIron", 6).save();
		pulverizer.append(Items.shears).output("dustIron", 2).save();
		pulverizer.append(Blocks.hopper).output("dustIron", 5).save();
		pulverizer.append(Items.minecart).output("dustIron", 5).save();
		pulverizer.append(Items.cauldron).output("dustIron", 7).save();
		pulverizer.append(Items.compass).output("dustIron", 4).save();

		pulverizer.append(Items.bucket, Items.milk_bucket, Items.water_bucket).output("dustIron", 3).save();

		pulverizer.append(Items.golden_carrot, Items.speckled_melon).output("nuggetGold", 8).save();

		pulverizer.append(Items.golden_apple).output("dustGold", 8).save();

		pulverizer.setEnergy(150).append(Blocks.iron_bars).output("nuggetIron", 3).save();

		pulverizer.append(Items.clock).output("dustGold", 4).save();

		if (quantityRottenFleshToLeather > 0) {
			furnace.append(Items.rotten_flesh, quantityRottenFleshToLeather).output(Items.leather).save();
		}

		pulverizer.append(Items.chicken, 4).output(Items.rotten_flesh).save();
		pulverizer.append(Items.beef, 2).output(Items.rotten_flesh).secondaryOutput(Items.bone).chance(25).save();
		pulverizer.append(Items.porkchop, 4).output(Items.rotten_flesh).secondaryOutput(Items.bone).chance(10).save();

		pulverizer.append(Items.iron_helmet, Items.iron_horse_armor).output("dustIron", 5).save();
		pulverizer.append(Items.iron_chestplate).output("dustIron", 8).save();
		pulverizer.append(Items.iron_leggings).output("dustIron", 7).save();
		pulverizer.append(Items.iron_boots).output("dustIron", 4).save();

		pulverizer.append(Items.iron_sword, Items.iron_hoe).output("dustIron", 2).save();
		pulverizer.append(Items.iron_axe, Items.iron_pickaxe).output("dustIron", 3).save();
		pulverizer.append(Items.iron_shovel).output("dustIron").save();

		pulverizer.append(Items.golden_helmet, Items.golden_horse_armor).output("dustGold", 5).save();
		pulverizer.append(Items.golden_chestplate).output("dustGold", 8).save();
		pulverizer.append(Items.golden_leggings).output("dustGold", 7).save();
		pulverizer.append(Items.golden_boots).output("dustGold", 4).save();

		pulverizer.append(Items.golden_sword, Items.golden_hoe).output("dustGold", 2).save();
		pulverizer.append(Items.golden_axe, Items.golden_pickaxe).output("dustGold", 3).save();
		pulverizer.append(Items.golden_shovel).output("dustGold").save();

		pulverizer.append(Blocks.light_weighted_pressure_plate).output("dustGold", 2).save();
		pulverizer.append(Blocks.heavy_weighted_pressure_plate).output("dustIron", 2).save();

		pulverizer.setEnergy(1200).append(Items.apple, 16).append(Items.carrot, 16).append(Items.potato, 16)
				.output(Blocks.dirt).save();

		sawmill.setEnergy(1200).append(Items.wooden_door).output(Blocks.planks, 6).save();
		sawmill.append(Blocks.trapdoor).output(Blocks.planks, 3).save();

		pulverizer.setEnergy(1200).appendSubtypeRange(Items.fish, 0, 4, 8).output(Items.rotten_flesh).save();

		// Glass items
		pulverizer.setEnergy(3200).appendSubtypeRange(Blocks.stained_glass, 0, 15).output(Blocks.sand).save();
		pulverizer.setEnergy(3200).append(Blocks.glass_pane, 8).output(Blocks.sand, 3).save();
		pulverizer.setEnergy(3200).appendSubtypeRange(Blocks.stained_glass_pane, 0, 15, 8).output(Blocks.sand, 3)
				.save();
		pulverizer.setEnergy(3200).append(Items.glass_bottle).output(Blocks.sand).save();

		// Add additional breeding items
		BreedingItemManager.add(EntityPig.class, new ItemStack(Items.potato));
		BreedingItemManager.add(EntityPig.class, new ItemStack(Blocks.pumpkin));
		BreedingItemManager.add(EntityPig.class, new ItemStack(Blocks.melon_block));

		return true;
	}
}
