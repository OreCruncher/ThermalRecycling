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

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import cpw.mods.fml.common.Loader;

public final class ModThermalFoundation extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "material:2", "material:3", "material:4", "material:16",
			// Nuggets
			"material:96", "material:97", "material:98", "material:99", "material:100", "material:101", "material:102",
			"material:103", "material:104", "material:105", "material:106", "material:107", "material:108",

			"material:512", "material:513", "material:517",

	};

	static final String[] recipeRevealList = new String[] {};

	static final String[] scrapValuesNone = new String[] { "material:2", "material:3", "material:4", "material:16", };

	static final String[] scrapValuesPoor = new String[] { "material:8",

			// Nuggets
			"material:96", "material:97", "material:98", "material:99", "material:100", "material:101", "material:102",
			"material:103", "material:104", "material:105", "material:106", "material:107", "material:108",

			"material:512", "material:513", "material:517",

	};

	static final String[] scrapValuesStandard = new String[] {

	};

	static final String[] scrapValuesSuperior = new String[] { "armor.plateBronze", "armor.plateCopper",
			"armor.platePlatinum", "armor.plateInvar", "armor.plateLead", "armor.plateTin", "armor.plateElectrum",
			"armor.plateNickel", "armor.plateSilver",

	};

	public ModThermalFoundation() {
		super(SupportedMod.THERMAL_FOUNDATION);
	}

	protected static ItemStack pyrotheumDust;

	public static ItemStack getPyrotheumDust(final int quantity) {

		if (pyrotheumDust == null) {
			pyrotheumDust = ItemStackHelper.getItemStack("ThermalFoundation:material:512", 1).get();
		}

		final ItemStack result = pyrotheumDust.copy();
		result.stackSize = quantity;
		return result;
	}

	protected void furnaceRecycleHelperTE(final String oreName) {

		final String ingot = "dust" + oreName;

		pulverizer.append("ThermalFoundation:armor.helmet" + oreName).output(ingot, 5).save();
		pulverizer.append("ThermalFoundation:armor.plate" + oreName).output(ingot, 8).save();
		pulverizer.append("ThermalFoundation:armor.legs" + oreName).output(ingot, 7).save();
		pulverizer.append("ThermalFoundation:armor.boots" + oreName).output(ingot, 4).save();

		pulverizer.append("ThermalFoundation:tool.sword" + oreName, "ThermalFoundation:tool.hoe" + oreName,
				"ThermalFoundation:tool.shears" + oreName, "ThermalFoundation:tool.fishingRod" + oreName,
				"ThermalFoundation:tool.bow" + oreName).output(ingot, 2).save();

		pulverizer.append("ThermalFoundation:tool.shovel" + oreName).output(ingot).save();

		pulverizer.append("ThermalFoundation:tool.pickaxe" + oreName, "ThermalFoundation:tool.axe" + oreName,
				"ThermalFoundation:tool.sickle" + oreName).output(ingot, 3).save();
	}

	protected void recycleGearTE(final String type) {

		pulverizer.append(OreDictionaryHelper.getOres("gear" + type)).output("dust" + type, 4).save();
	}

	void registerExtraTiCGearRecipe(final String gear) {
		recycler.useRecipe(gear).scrubOutput("ingotIron").save();
	}

	@Override
	public boolean initialize() {
		
		registerRecipesToIgnore(recipeIgnoreList);
		// registerRecipesToReveal(recipeRevealList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Big daddy golden apple
		smelter.setEnergy(72000).appendSubtype(Items.golden_apple, 1).secondaryInput(getPyrotheumDust(8))
				.output("blockGold", 8).save();

		// Smelt some blocks!
		smelter.setEnergy(21600).append("blockTin").secondaryInput("blockCopper", 3).output("blockBronze", 4).save();
		smelter.setEnergy(21600).append("blockGold").secondaryInput("blockSilver", 3).output("blockElectrum", 4).save();
		smelter.setEnergy(21600).append("blockIron").secondaryInput("blockNickel", 3).output("blockInvar", 4).save();

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

		// Have to handle gears if ExtraTiC is installed. It provides Tinker's
		// recipes that do not require an iron ingot. Register recipes that
		// remove the iron ingot from the result.
		if (Loader.isModLoaded("ExtraTiC")) {
			registerExtraTiCGearRecipe("ThermalFoundation:material:12");
			registerExtraTiCGearRecipe("ThermalFoundation:material:13");
			registerExtraTiCGearRecipe("ThermalFoundation:material:128");
			registerExtraTiCGearRecipe("ThermalFoundation:material:129");
			registerExtraTiCGearRecipe("ThermalFoundation:material:130");
			registerExtraTiCGearRecipe("ThermalFoundation:material:131");
			registerExtraTiCGearRecipe("ThermalFoundation:material:132");
			registerExtraTiCGearRecipe("ThermalFoundation:material:133");
			registerExtraTiCGearRecipe("ThermalFoundation:material:134");
			registerExtraTiCGearRecipe("ThermalFoundation:material:135");
			registerExtraTiCGearRecipe("ThermalFoundation:material:136");
			registerExtraTiCGearRecipe("ThermalFoundation:material:137");
			registerExtraTiCGearRecipe("ThermalFoundation:material:138");
			registerExtraTiCGearRecipe("ThermalFoundation:material:139");
			registerExtraTiCGearRecipe("ThermalFoundation:material:140");
		}

		// Pile of Rubble - add Copper and Tin ores
		registerPileOfRubbleDrop(1, 3, 5, "Ore:0", "Ore:1");

		return true;
	}
}
