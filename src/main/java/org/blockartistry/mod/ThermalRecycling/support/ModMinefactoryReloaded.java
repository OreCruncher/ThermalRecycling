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
import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModMinefactoryReloaded extends ModPlugin {

	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();

	public ModMinefactoryReloaded() {
		super(SupportedMod.MINEFACTORY_RELOADED);
	}

	@Override
	public void apply() {

		// Add the rubber saplings for recycling. We don't want the big daddy.
		pulverizer
				.appendSubtypeRange("MineFactoryReloaded:rubberwood.sapling",
						0, 2, 8).output(Blocks.dirt).save();

		// Range upgrades
		ItemStack is = ItemStackHelper.getItemStack(
				"MineFactoryReloaded:upgrade.radius", 1);
		pulverizer.append(is).output("minecraft:dye:4", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 1).output("ingotTin", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 2).output("ingotIron", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 3).output("ingotCopper", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 4).output("ingotBronze", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 5).output("ingotSilver", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 6).output("ingotGold", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 7).output(Items.quartz, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 8).output(Items.diamond, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 9).output("ingotPlatinum", 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 10).output(Items.emerald, 3)
				.secondaryOutput("nuggetGold").save();
		pulverizer.appendSubtype(is, 11).output(Blocks.cobblestone, 3)
				.secondaryOutput("nuggetGold").save();

		// Laser Focii
		pulverizer.appendSubtypeRange("MineFactoryReloaded:laserfocus", 0, 15)
				.output(Items.emerald, 4).secondaryOutput("nuggetGold", 4)
				.save();

		// Syringe
		furnace.reset()
				.append("MineFactoryReloaded:syringe.empty",
						"MineFactoryReloaded:syringe.zombie",
						"MineFactoryReloaded:syringe.cure",
						"MineFactoryReloaded:syringe.growth",
						"MineFactoryReloaded:syringe.slime",
						"MineFactoryReloaded:syringe.health")
				.output("ingotIron").save();

		// Plastic
		pulverizer.append("MineFactoryReloaded:plastic.sheet")
				.output("MineFactoryReloaded:plastic.raw").save();
		pulverizer.append("MineFactoryReloaded:xpextractor")
				.output("MineFactoryReloaded:plastic.raw", 5).save();
		pulverizer
				.append("MineFactoryReloaded:straw",
						"MineFactoryReloaded:plastic.boots")
				.output("MineFactoryReloaded:plastic.raw", 4).save();
		pulverizer.append("MineFactoryReloaded:record.blank")
				.output("MineFactoryReloaded:plastic.raw", 8).save();

		// Misc
		pulverizer.append("MineFactoryReloaded:needlegun.ammo.empty")
				.output("MineFactoryReloaded:plastic.raw")
				.secondaryOutput("nuggetIron", 2).save();
		pulverizer.append("MineFactoryReloaded:spyglass")
				.output("ingotBronze", 2)
				.secondaryOutput("MineFactoryReloaded:plastic.raw", 2).save();

		// Recycler recipes

		// Planter
		recycler.input("MineFactoryReloaded:machine.0")
				.append("gearCopper", 2).append("ThermalExpansion:material:1")
				.append(Blocks.piston, 2).append("ThermalExpansion:Frame")
				.append(Items.flower_pot)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Fisher
		recycler.input("MineFactoryReloaded:machine.0:1")
				.append("gearIron", 2).append("ThermalExpansion:material:1")
				.append(Items.bucket, 2).append("ThermalExpansion:Frame")
				.append(Items.fishing_rod)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Harvester
		recycler.input("MineFactoryReloaded:machine.0:2")
				.append("gearGold", 2).append("ThermalExpansion:material:1")
				.append("ThermalFoundation:tool.axeInvar", 2)
				.append("ThermalExpansion:Frame").append(Items.shears)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Rancher
		recycler.input("MineFactoryReloaded:machine.0:3").append("gearTin", 2)
				.append("ThermalExpansion:material:1").append(Items.shears, 2)
				.append("ThermalExpansion:Frame")
				.append("MineFactoryReloaded:cable.plastic")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Fertilizer
		recycler.input("MineFactoryReloaded:machine.0:4")
				.append("gearNickel", 2).append("ThermalExpansion:material:1")
				.append(Items.leather, 2).append("ThermalExpansion:Frame")
				.append(Items.glass_bottle)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Vet
		recycler.input("MineFactoryReloaded:machine.0:5")
				.append("gearCopper", 2).append("ThermalExpansion:material:1")
				.append("MineFactoryReloaded:syringe.empty", 3)
				.append("ThermalExpansion:Frame")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Block Breaker
		recycler.input("MineFactoryReloaded:machine.0:7")
				.append("gearIron", 2).append("ThermalExpansion:material:1")
				.append("ThermalFoundation:tool.pickaxeInvar")
				.append("ThermalExpansion:Frame")
				.append("ThermalFoundation:tool.shovelInvar")
				.append("gearInvar")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Weather Collector
		recycler.input("MineFactoryReloaded:machine.0:8")
				.append("gearCopper", 2).append("ThermalExpansion:material:1")
				.append(Items.bucket, 2).append("ThermalExpansion:Frame")
				.append(Blocks.iron_bars)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Sluge Boiler
		recycler.input("MineFactoryReloaded:machine.0:9")
				.append("gearIron", 2).append("ThermalExpansion:material:1")
				.append(Blocks.furnace, 2).append("ThermalExpansion:Frame")
				.append(Items.bucket)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Composter
		recycler.input("MineFactoryReloaded:machine.0:11")
				.append(Items.brick, 2).append("ThermalExpansion:material:1")
				.append(Blocks.piston, 2).append("ThermalExpansion:Frame")
				.append(Blocks.furnace)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Breeder
		recycler.input("MineFactoryReloaded:machine.0:12")
				.append(new ItemStack(Items.dye, 2, 5))
				.append("ThermalExpansion:material:1")
				.append(Items.golden_carrot, 2)
				.append("ThermalExpansion:Frame").append(Items.golden_apple)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Grinder
		recycler.input("MineFactoryReloaded:machine.0:13")
				.append("gearTin", 2).append("ThermalExpansion:material:1")
				.append(Items.book, 2).append("ThermalExpansion:Frame")
				.append("ThermalFoundation:tool.swordInvar")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Auto Enchanter
		recycler.input("MineFactoryReloaded:machine.0:14")
				.append(Items.diamond, 2).append("ThermalExpansion:material:1")
				.append(Items.book, 2).append("ThermalExpansion:Frame")
				.append(Blocks.obsidian)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Chronotyper
		recycler.input("MineFactoryReloaded:machine.0:15")
				.append(new ItemStack(Items.dye, 2, 5))
				.append("ThermalExpansion:material:1").append(Items.emerald, 3)
				.append("ThermalExpansion:Frame")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// DSU
		recycler.input("MineFactoryReloaded:machine.1:3")
				.append("ThermalExpansion:Strongbox:3", 4)
				.append("ThermalExpansion:Frame:8")
				.append("MineFactoryReloaded:plastic.sheet", 4).save();

		// Liquicrafter
		recycler.input("MineFactoryReloaded:machine.1:4")
				.append(Items.book, 2).append("ThermalExpansion:material")
				.append("ThermalExpansion:Tank:1", 2)
				.append("ThermalExpansion:Frame").append(Blocks.crafting_table)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Lava Fabricator
		recycler.input("MineFactoryReloaded:machine.1:5")
				.append(Items.blaze_rod, 2)
				.append("ThermalExpansion:material:1")
				.append(Items.magma_cream, 2).append("ThermalExpansion:Frame")
				.append(Blocks.obsidian)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Steam Boiler
		recycler.input("MineFactoryReloaded:machine.1:6")
				.append(Blocks.nether_brick_stairs, 3)
				.append("ThermalExpansion:Tank:3", 2)
				.append("MineFactoryReloaded:machine.0:9")
				.append("MineFactoryReloaded:plastic.sheet", 3).save();

		// Auto Jukebox
		recycler.input("MineFactoryReloaded:machine.1:7")
				.append(Blocks.jukebox).append("ThermalExpansion:Frame")
				.append("MineFactoryReloaded:plastic.sheet", 4).save();

		// Unifier
		recycler.input("MineFactoryReloaded:machine.1:8")
				.append("gearSilver", 2).append(Items.book)
				.append(Items.comparator, 2).append("ThermalExpansion:Frame")
				.append("ThermalExpansion:meter")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Auto-spawner
		recycler.input("MineFactoryReloaded:machine.1:9")
				.append(Items.emerald, 2).append("ThermalExpansion:material:1")
				.append(Items.magma_cream, 2).append("ThermalExpansion:Frame")
				.append(Items.nether_wart)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Bioreactor
		recycler.input("MineFactoryReloaded:machine.1:10")
				.append(Items.brick, 2).append(Items.sugar)
				.append(Items.slime_ball, 2).append("ThermalExpansion:Frame")
				.append(Items.fermented_spider_eye)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Biofuel Generator
		recycler.input("MineFactoryReloaded:machine.1:11")
				.append(Items.blaze_rod, 2)
				.append("ThermalExpansion:material:2").append(Blocks.piston, 2)
				.append("ThermalExpansion:Frame").append(Blocks.furnace)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Auto dis-enchanter
		recycler.input("MineFactoryReloaded:machine.1:12")
				.append(Items.diamond, 2).append("ThermalExpansion:material:1")
				.append(Items.book, 2).append("ThermalExpansion:Frame")
				.append(Blocks.nether_brick)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Slaughterhouse
		recycler.input("MineFactoryReloaded:machine.1:13")
				.append("ThermalFoundation:tool.axeInvar", 2)
				.append("ThermalExpansion:material:1")
				.append("ThermalFoundation:tool.swordInvar", 2)
				.append("ThermalExpansion:Frame").append("gearInvar")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Meat Packer
		recycler.input("MineFactoryReloaded:machine.1:14")
				.append(Blocks.brick_block, 4)
				.append("ThermalExpansion:material:1")
				.append("ThermalExpansion:Frame")
				.append("ThermalExpansion:igniter")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Enchantment Router
		recycler.input("MineFactoryReloaded:machine.1:15")
				.append(Items.repeater, 3).append("ThermalExpansion:Frame")
				.append(Items.book)
				.append("MineFactoryReloaded:plastic.sheet", 4).save();

		// Laser Drill
		recycler.input("MineFactoryReloaded:machine.2")
				.append(Items.diamond, 3).append("ThermalExpansion:Glass")
				.append("ThermalExpansion:material:1", 2)
				.append("ThermalExpansion:Light", 2)
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

		// Laser Drill pre Charger
		recycler.input("MineFactoryReloaded:machine.2:1")
				.append(Items.diamond, 1).append("ThermalExpansion:material:3")
				.append("ThermalExpansion:Glass", 2)
				.append("ThermalExpansion:Light")
				.append("MineFactoryReloaded:pinkslime")
				.append("MineFactoryReloaded:plastic.sheet", 2).save();

	}
}
