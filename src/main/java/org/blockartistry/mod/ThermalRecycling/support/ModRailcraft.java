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
			"fluid.creosote.bottle:*", "stair:*",
			"slab:*", "wall.alpha:*", "wall.beta:*",
			"glass:*",
			"brick.infernal:*", "brick.abyssal:*",
			"brick.sandy:*", "brick.frostbound:*",
			"brick.quarried:*", "brick.bleachedbone:*",
			"brick.bloodstained:*", "brick.nether:*",
			"post.metal:*", "post.metal.platform:*",
			
			};

	static final String[] recipeRevealList = new String[] {
			"stair:6", "stair:43", "stair:40",
			"stair:41", "stair:42", "slab:6",
			"slab:43", "slab:40", "slab:41",
			"slab:42", 
			"machine.alpha", "machine.alpha:2",
			"anvil", "machine.beta:12",
			"stair:7", "stair:8", "slab:7",
			"slab:8", "wall.beta:3", "wall.beta:2",
			"wall.beta:4", };

	static final String[] scrapValuesNone = new String[] {
			"fluid.creosote.bottle:*", "stair:*",
			"slab:*", "wall.alpha:*", "wall.beta:*",
			"glass:*",
			"brick.infernal:*", "brick.abyssal:*",
			"brick.sandy:*", "brick.frostbound:*",
			"brick.quarried:*", "brick.bleachedbone:*",
			"brick.bloodstained:*", "brick.nether:*",
			"fluid.steam.bottle:*", "lantern.stone:*",
			"part.bleached.clay", "routing.table",
			"routing.ticket.gold", "routing.ticket",
			"post:*", "cube:6", "cube:7",
			"armor.overalls", "dust:1", "dust:2",
			"dust:3"
			};

	static final String[] scrapValuesPoor = new String[] {
			"post.metal:*", "post.metal.platform:*",
			"track:*", "track.elevator", "nugget:*" };

	static final String[] scrapValuesStandard = new String[] {
			"stair:6", "stair:43", "stair:40",
			"stair:41", "stair:42", "slab:6",
			"slab:43", "slab:40", "slab:41",
			"slab:42", "wall.beta:2",
			};

	static final String[] scrapValuesSuperior = new String[] {
			"firestone.raw", "machine.alpha",
			"machine.alpha:2", "anvil",
			"machine.beta:12", "stair:7",
			"stair:8", "slab:7", "slab:8",
			"wall.beta:3", "wall.beta:4",
			"machine.epsilon:3"

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
