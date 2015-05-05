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

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

public class ModThermalFoundation extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
		"material:2",
		"material:3",
		"material:4",
		"material:16",
		// Nuggets
		"material:96",
		"material:97",
		"material:98",
		"material:99",
		"material:100",
		"material:101",
		"material:102",
		"material:103",
		"material:104",
		"material:105",
		"material:106",
		"material:107",
		"material:108",

		"material:512",
		"material:513",
		"material:517",

	};

	static final String[] recipeRevealList = new String[] {};

	static final String[] scrapValuesNone = new String[] {
		"material:2",
		"material:3",
		"material:4",
		"material:16",
	};

	static final String[] scrapValuesPoor = new String[] {
		"material:8",

		// Nuggets
		"material:96",
		"material:97",
		"material:98",
		"material:99",
		"material:100",
		"material:101",
		"material:102",
		"material:103",
		"material:104",
		"material:105",
		"material:106",
		"material:107",
		"material:108",

		"material:512",
		"material:513",
		"material:517",

	};

	static final String[] scrapValuesStandard = new String[] {

	};

	static final String[] scrapValuesSuperior = new String[] {

	};

	public ModThermalFoundation() {
		super(SupportedMod.THERMAL_FOUNDATION);
	}

	protected static ItemStack pyrotheumDust;

	public static ItemStack getPyrotheumDust(int quantity) {

		if (pyrotheumDust == null) {
			pyrotheumDust = ItemStackHelper.getItemStack(
					"ThermalFoundation:material:512", 1);
		}

		ItemStack result = pyrotheumDust.copy();
		result.stackSize = quantity;
		return result;
	}

	protected void furnaceRecycleHelperTE(String oreName) {

		String ingot = "ingot" + oreName;

		furnace.append("ThermalFoundation:armor.helmet" + oreName)
				.output(ingot, 5).save();
		furnace.append("ThermalFoundation:armor.plate" + oreName)
				.output(ingot, 8).save();
		furnace.append("ThermalFoundation:armor.legs" + oreName)
				.output(ingot, 7).save();
		furnace.append("ThermalFoundation:armor.boots" + oreName)
				.output(ingot, 4).save();

		furnace.append("ThermalFoundation:tool.sword" + oreName,
				"ThermalFoundation:tool.hoe" + oreName,
				"ThermalFoundation:tool.shears" + oreName,
				"ThermalFoundation:tool.fishingRod" + oreName,
				"ThermalFoundation:tool.bow" + oreName).output(ingot, 2).save();

		furnace.append("ThermalFoundation:tool.shovel" + oreName).output(ingot)
				.save();

		furnace.append("ThermalFoundation:tool.pickaxe" + oreName,
				"ThermalFoundation:tool.axe" + oreName,
				"ThermalFoundation:tool.sickle" + oreName).output(ingot, 3)
				.save();
	}

	protected void recycleGearTE(String type) {

		furnace.append(OreDictionary.getOres("gear" + type))
				.output("ingot" + type, 4).save();
	}

	@Override
	public void apply() {
		
		registerRecipesToIgnore(recipeIgnoreList);
		registerRecipesToReveal(recipeRevealList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);
		
		// Big daddy golden apple
		smelter.setEnergy(72000).appendSubtype(Items.golden_apple, 1)
				.secondaryInput(getPyrotheumDust(8)).output("blockGold", 8)
				.save();

		// Smelt some blocks!
		smelter.setEnergy(21600).append("blockTin")
				.secondaryInput("blockCopper", 3).output("blockBronze", 4)
				.save();
		smelter.setEnergy(21600).append("blockGold")
				.secondaryInput("blockSilver", 3).output("blockElectrum", 4)
				.save();
		smelter.setEnergy(21600).append("blockIron")
				.secondaryInput("blockNickel", 3).output("blockInvar", 4)
				.save();

		furnaceRecycleHelperTE("Copper");
		furnaceRecycleHelperTE("Tin");
		furnaceRecycleHelperTE("Silver");
		furnaceRecycleHelperTE("Lead");
		furnaceRecycleHelperTE("Nickel");
		furnaceRecycleHelperTE("Electrum");
		furnaceRecycleHelperTE("Invar");
		furnaceRecycleHelperTE("Bronze");
		furnaceRecycleHelperTE("Platinum");

		recycleGearTE("Iron");
		recycleGearTE("Gold");
		recycleGearTE("Copper");
		recycleGearTE("Tin");
		recycleGearTE("Silver");
		recycleGearTE("Lead");
		recycleGearTE("Nickel");
		recycleGearTE("Platinum");
		recycleGearTE("Electrum");
		recycleGearTE("Invar");
		recycleGearTE("Bronze");
		recycleGearTE("Signalum");
		recycleGearTE("Lumium");
		recycleGearTE("Enderium");

	}
}
