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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class ModRailcraft extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
			"Railcraft:fluid.creosote.bottle:*", "Railcraft:stair:*",
			"Railcraft:slab:*", "Railcraft:wall.alpha:*", "Railcraft:wall.beta:*",
			"Railcraft:glass:*",
			"Railcraft:brick.infernal:*", "Railcraft:brick.abyssal:*",
			"Railcraft:brick.sandy:*", "Railcraft:brick.frostbound:*",
			"Railcraft:brick.quarried:*", "Railcraft:brick.bleachedbone:*",
			"Railcraft:brick.bloodstained:*", "Railcraft:brick.nether:*",
			"Railcraft:post.metal:*", "Railcraft:post.metal.platform:*",
			
			};

	static final String[] recipeRevealList = new String[] {
			"Railcraft:stair:6", "Railcraft:stair:43", "Railcraft:stair:40",
			"Railcraft:stair:41", "Railcraft:stair:42", "Railcraft:slab:6",
			"Railcraft:slab:43", "Railcraft:slab:40", "Railcraft:slab:41",
			"Railcraft:slab:42", 
			"Railcraft:machine.alpha", "Railcraft:machine.alpha:2",
			"Railcraft:anvil", "Railcraft:machine.beta:12",
			"Railcraft:stair:7", "Railcraft:stair:8", "Railcraft:slab:7",
			"Railcraft:slab:8", "Railcraft:wall.beta:3", "Railcraft:wall.beta:2",
			"Railcraft:wall.beta:4", };

	static final String[] scrapValuesNone = new String[] {
			"Railcraft:fluid.creosote.bottle:*", "Railcraft:stair:*",
			"Railcraft:slab:*", "Railcraft:wall.alpha:*", "Railcraft:wall.beta:*",
			"Railcraft:glass:*",
			"Railcraft:brick.infernal:*", "Railcraft:brick.abyssal:*",
			"Railcraft:brick.sandy:*", "Railcraft:brick.frostbound:*",
			"Railcraft:brick.quarried:*", "Railcraft:brick.bleachedbone:*",
			"Railcraft:brick.bloodstained:*", "Railcraft:brick.nether:*",
			"Railcraft:fluid.steam.bottle:*", "Railcraft:lantern.stone:*",
			"Railcraft:part.bleached.clay", "Railcraft:routing.table",
			"Railcraft:routing.ticket.gold", "Railcraft:routing.ticket",
			"Railcraft:post:*"
			};

	static final String[] scrapValuesPoor = new String[] {
			"Railcraft:post.metal:*", "Railcraft:post.metal.platform:*",
			"Railcraft:track:*" };

	static final String[] scrapValuesStandard = new String[] {
			"Railcraft:stair:6", "Railcraft:stair:43", "Railcraft:stair:40",
			"Railcraft:stair:41", "Railcraft:stair:42", "Railcraft:slab:6",
			"Railcraft:slab:43", "Railcraft:slab:40", "Railcraft:slab:41",
			"Railcraft:slab:42", "Railcraft:wall.beta:2",
			};

	static final String[] scrapValuesSuperior = new String[] {
			"Railcraft:firestone.raw", "Railcraft:machine.alpha",
			"Railcraft:machine.alpha:2", "Railcraft:anvil",
			"Railcraft:machine.beta:12", "Railcraft:stair:7",
			"Railcraft:stair:8", "Railcraft:slab:7", "Railcraft:slab:8",
			"Railcraft:wall.beta:3", "Railcraft:wall.beta:4",

	};

	public ModRailcraft() {
		super(SupportedMod.RAILCRAFT);
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerRecipesToReveal(recipeRevealList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Add back in some common sense recipes that were excluded above
		recycler.input("Railcraft:machine.beta", 8)
				.append("Railcraft:part.plate", 4).save();
		recycler.input("Railcraft:machine.beta:1", 8)
				.append("Railcraft:part.plate", 4).save();
		recycler.input("Railcraft:machine.beta:2", 8)
				.append("Railcraft:part.plate", 4).append(Blocks.iron_bars, 4)
				.append(Blocks.lever).save();

		recycler.input("Railcraft:machine.beta:13", 8)
				.append("Railcraft:part.plate:1", 4).save();
		recycler.input("Railcraft:machine.beta:14", 8)
				.append("Railcraft:part.plate:1", 4).save();
		recycler.input("Railcraft:machine.beta:15", 8)
				.append("Railcraft:part.plate:1", 4)
				.append(Blocks.iron_bars, 4).append(Blocks.lever).save();

		blast.append("Railcraft:part.plate:1").output("ingotSteel").save();

		// Steel Armor and Tools
		blast.append("Railcraft:armor.steel.helmet").output("ingotSteel", 5)
				.save();
		blast.append("Railcraft:armor.steel.plate").output("ingotSteel", 8)
				.save();
		blast.append("Railcraft:armor.steel.legs").output("ingotSteel", 7)
				.save();
		blast.append("Railcraft:armor.steel.boots").output("ingotSteel", 4)
				.save();
		blast.append("Railcraft:tool.steel.sword", "Railcraft:tool.steel.hoe",
				"Railcraft:tool.steel.shears").output("ingotSteel", 2).save();
		blast.append("Railcraft:tool.steel.axe",
				"Railcraft:tool.steel.pickaxe",
				"Railcraft:tool.crowbar.reinforced",
				"Railcraft:part.turbine.blade").output("ingotSteel", 3).save();
		blast.append("Railcraft:tool.steel.shovel").output("ingotSteel", 1)
				.save();

		// Turbine parts
		blast.append("Railcraft:part.turbine.disk").output("ingotSteel", 33)
				.save();
		blast.append("Railcraft:part.turbine.rotor").output("blockSteel", 11)
				.save();

		// Machines
		// Steam Oven
		blast.append("Railcraft:machine.alpha:3", "Railcraft:machine.alpha:9",
				"Railcraft:machine.alpha:10").output("ingotSteel", 2).save();

		// Steam Turbine Housing
		blast.append("Railcraft:machine.alpha:1").output("ingotSteel", 12)
				.save();

		// Regular iron items
		furnace.append("Railcraft:part.plate").output("ingotIron").save();
		furnace.append("Railcraft:tool.crowbar").output("ingotIron", 3).save();
		// Rolling Machine
		furnace.append("Railcraft:machine.alpha:8").output("ingotIron", 8)
				.save();
		// Smoker
		furnace.append("Railcraft:machine.alpha:5").output("ingotIron", 7)
				.save();

		// Iron Tank Wall
		// Iron Tank gauge
		furnace.appendSubtypeRange("Railcraft:machine.beta", 0, 1)
				.output("nuggetIron", 4).save();
		// Iron Tank Valve
		furnace.append("Railcraft:machine.beta:2").output("nuggetIron", 6)
				.save();
		// LP Boiler Tank
		furnace.append("Railcraft:machine.beta:3").output("ingotIron", 2)
				.save();
		// Commercial Steam Engine
		furnace.append("Railcraft:machine.beta:8").output("ingotIron", 12)
				.save();

		// Steel Tank Wall
		// Steel Tank Gauge
		blast.appendSubtypeRange("Railcraft:machine.beta", 13, 14)
				.output("nuggetSteel", 4).save();
		// Steel Tank Valve
		blast.append("Railcraft:machine.beta:15").output("nuggetSteel", 6)
				.save();
		// HP Boiler Tank
		blast.append("Railcraft:machine.beta:4").output("ingotSteel", 2).save();
		// Industrial Steam Engine
		blast.append("Railcraft:machine.beta:9").output("ingotSteel", 12)
				.save();

		// Liquid Fuel Firebox
		pulverizer.append("Railcraft:machine.beta:6")
				.output("Railcraft:part.plate:1", 4)
				.secondaryOutput("ingotIron", 3).save();

		// Rock Crusher
		pulverizer.append("Railcraft:machine.alpha:15").output("ingotSteel", 2)
				.secondaryOutput(Items.diamond).save();

		// Feed Station
		pulverizer.append("Railcraft:machine.alpha:11")
				.output("Railcraft:part.plate:1", 1)
				.secondaryOutput("ingotGold", 3).save();

		// Trade Station
		pulverizer.append("Railcraft:machine.alpha:6")
				.output("Railcraft:part.plate:1", 4)
				.secondaryOutput(Items.emerald, 2).save();

		// Hobbyist Steam Engine
		pulverizer.append("Railcraft:machine.beta:7").output("nuggetGold", 12)
				.secondaryOutput("ingotIron").save();

		// Gears
		furnace.append("Railcraft:part.gear:3").output("ingotTin", 2).save();
		pulverizer.append("Railcraft:part.gear").output("nuggetGold", 4)
				.secondaryOutput("ingotTin", 2).save();
		pulverizer.append("Railcraft:part.gear:1").output("ingotIron", 4)
				.secondaryOutput("ingotTin", 2).save();
		pulverizer.append("Railcraft:part.gear:2").output("ingotSteel", 4)
				.secondaryOutput("ingotTin", 2).save();

		// Metal posts and platforms
		furnace.append("Railcraft:post:2").output("nuggetSteel", 1).save();
		furnace.append("Railcraft:post:6").output("nuggetSteel", 5).save();
	}
}
