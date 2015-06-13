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

package org.blockartistry.mod.ThermalRecycling;

import java.util.HashMap;

import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;
import net.minecraftforge.common.config.Configuration;

public final class ModOptions {

	private ModOptions() {
	}

	protected static final String CATEGORY_RECYCLE_ENABLE = "recycle.enable";
	protected static final String CATEGORY_RECYCLE_RECIPIES_CONTROL = "recycle.recipe.control";
	protected static final String CATEGORY_LOGGING_CONTROL = "logging";
	protected static final String CATEGORY_MODS = "mods";

	protected static final String CATEGORY_GENERAL = "recycle.general";
	protected static final String CONFIG_ENABLE_SCRAPBOX_SPAWNING = "Enable Scrapbox Spawning";
	protected static final String CONFIG_WORM_DROP_CHANCE = "Worm Drop Chance";
	protected static final String CONFIG_DISABLE_ANVIL = "Disable Anvil Repair";
	protected static final String CONFIG_ITEM_MERGE_RANGE = "EntityItem Merge Range";
	
	protected static final String CATEGORY_RUBBLE = "recycle.rubble";
	protected static final String CONFIG_RUBBLE_PILE_DISABLE = "Disable";
	protected static final String CONFIG_RUBBLE_PILE_DENSITY = "Density";
	protected static final String CONFIG_RUBBLE_PILE_DROP_COUNT = "Number of Drops";

	protected static final String CATEGORY_MACHINES_RECYCLER = "machines.recycler";
	protected static final String CATEGORY_MACHINES_COMPOSTER = "machines.composter";
	protected static final String CATEGORY_MACHINES_ASSESSOR = "machines.assessor";
	protected static final String CATEGORY_MACHINES_VENDING = "machines.vending";
	protected static final String CONFIG_ENABLE_FX = "Enable FX";
	protected static final String CONFIG_SCRAPBOX_BONUS = "Scrapbox Bonus";
	protected static final String CONFIG_ENABLE_ENHANCED_LORE = "Enhanced Lore";
	protected static final String CONFIG_BLACKLIST = "Blacklist";
	protected static final String CONFIG_VENDING_ITEM_RENDER_RANGE = "Item Render Range";
	protected static final String CONFIG_VENDING_NAME_RENDER_RANGE = "Name Render Range";
	protected static final String CONFIG_VENDING_QUANTITY_RENDER_RANGE = "Quantity Render Range";

	protected static final String CONFIG_ENABLE_RECIPE_LOGGING = "Enable Recipe Logging";
	protected static final String CONFIG_ENABLE_DEBUG_LOGGING = "Enable Debug Logging";
	protected static final String CONFIG_ENABLE_WAILA = "Enable Waila Display";

	protected static final String CATEGORY_FUEL_SETTINGS = "Fuel Settings";
	protected static final String CONFIG_POOR_SCRAP_FUEL_SETTING = "Poor Scrap Fuel Ticks";
	protected static final String CONFIG_STANDARD_SCRAP_FUEL_SETTING = "Standard Scrap Fuel Ticks";
	protected static final String CONFIG_SUPERIOR_SCRAP_FUEL_SETTING = "Superior Scrap Fuel Ticks";
	protected static final String CONFIG_SCRAPBOX_MULTIPLIER = "Scrapbox Multiplier";
	protected static final String CONFIG_DEBRIS_FUEL_SETTING = "Debris Fuel Ticks";
	protected static final String CONFIG_SCRAP_BLOCK_FUEL_SETTING = "Scrap Block Fuel Ticks";
	protected static final String CONFIG_PAPER_LOG_FUEL_SETTING = "Paper Log Fuel Ticks";

	protected static HashMap<SupportedMod, Boolean> enableModProcessing = new HashMap<SupportedMod, Boolean>();
	protected static boolean enableRecipeLogging = true;
	protected static boolean enableDebugLogging = false;
	protected static boolean enableWailaDisplay = true;
	protected static String[] modWhitelist = new String[] {};
	protected static int poorScrapFuelSetting = 400;
	protected static int standardScrapFuelSetting = 800;
	protected static int superiorScrapFuelSetting = 1600;
	protected static int debrisFuelSetting = 200;
	protected static int scrapBlockFuelSetting = 2000;
	protected static int scrapboxMultiplier = 9;
	protected static int paperLogFuelSetting = 800;
	protected static boolean enableRecyclerFX = true;
	protected static boolean enableComposterFX = true;
	protected static boolean enableTooltips = true;
	protected static int scrapBoxBonus = 1;
	protected static boolean enableAssessorEnhancedLore = true;
	protected static boolean enableScrapboxSpawn = true;
	protected static int wormDropChance = 15;
	protected static boolean disableAnvilRepair = false;
	protected static double entityItemMergeRange = 0;
	protected static String[] recyclerBlacklist = new String[] {
			"minecraft:cobblestone", "minecraft:sandstone:*" };

	protected static int rubblePileDensity = 80;
	protected static int rubblePileDropCount = 3;
	protected static boolean rubblePileDisable = false;
	
	protected static int vendingItemRenderRange = 6;
	protected static int vendingNameRenderRange = 8;
	protected static int vendingQuantityRenderRange = 4;

	public static void load(final Configuration config) {

		enableDebugLogging = config.getBoolean(CONFIG_ENABLE_DEBUG_LOGGING,
				CATEGORY_LOGGING_CONTROL, enableDebugLogging,
				"Enables/disables debug logging of the mod");

		enableRecipeLogging = config
				.getBoolean(CONFIG_ENABLE_RECIPE_LOGGING,
						CATEGORY_LOGGING_CONTROL, enableRecipeLogging,
						"Enables/disables logging of recipes to the Forge log during startup");

		enableWailaDisplay = config.getBoolean(CONFIG_ENABLE_WAILA,
				CATEGORY_MODS, enableWailaDisplay,
				"Enables/disables display of scrap information via Waila");

		for (final SupportedMod mod : SupportedMod.values()) {

			final boolean b = config
					.getBoolean(
							mod.getModId(),
							CATEGORY_RECYCLE_ENABLE,
							true,
							String.format(
									"Enable/Disable whether recycling recipes for items from [%s] are added",
									mod.getName()));

			enableModProcessing.put(mod, b);
		}

		modWhitelist = config.getStringList("Whitelist", CATEGORY_MODS,
				modWhitelist, "ModIds to add to the internal whitelist");

		enableTooltips = config.getBoolean("Enable Tooltips", CATEGORY_MODS,
				enableTooltips,
				"Controls the display of tooltips in the client");

		poorScrapFuelSetting = config.getInt(CONFIG_POOR_SCRAP_FUEL_SETTING,
				CATEGORY_FUEL_SETTINGS, poorScrapFuelSetting, 0,
				Integer.MAX_VALUE,
				"Number of ticks Poor Scrap will burn in a furnace");

		standardScrapFuelSetting = config.getInt(
				CONFIG_STANDARD_SCRAP_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				standardScrapFuelSetting, 0, Integer.MAX_VALUE,
				"Number of ticks Standard Scrap will burn in a furnace");

		superiorScrapFuelSetting = config.getInt(
				CONFIG_SUPERIOR_SCRAP_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				superiorScrapFuelSetting, 0, Integer.MAX_VALUE,
				"Number of ticks Superior Scrap will burn in a furnace");

		debrisFuelSetting = config.getInt(CONFIG_DEBRIS_FUEL_SETTING,
				CATEGORY_FUEL_SETTINGS, debrisFuelSetting, 0,
				Integer.MAX_VALUE,
				"Number of ticks Debris will burn in a furnace");

		scrapBlockFuelSetting = config.getInt(CONFIG_SCRAP_BLOCK_FUEL_SETTING,
				CATEGORY_FUEL_SETTINGS, scrapBlockFuelSetting, 0,
				Integer.MAX_VALUE,
				"Number of ticks a Block of Scrap will burn in a furnace");

		scrapboxMultiplier = config
				.getInt(CONFIG_SCRAPBOX_MULTIPLIER, CATEGORY_FUEL_SETTINGS,
						scrapboxMultiplier, 0, Integer.MAX_VALUE,
						"Number of ticks a ScrapBox will burn in multiples of the base scrap type");

		enableRecyclerFX = config.getBoolean(CONFIG_ENABLE_FX,
				CATEGORY_MACHINES_RECYCLER, enableRecyclerFX,
				"Control whether client displays visual effects");

		enableComposterFX = config.getBoolean(CONFIG_ENABLE_FX,
				CATEGORY_MACHINES_COMPOSTER, enableComposterFX,
				"Control whether client displays visual effects");

		enableAssessorEnhancedLore = config
				.getBoolean(CONFIG_ENABLE_ENHANCED_LORE,
						CATEGORY_MACHINES_ASSESSOR, enableAssessorEnhancedLore,
						"Control whether enhanced lore is provided in the Scrap Assessor View");

		recyclerBlacklist = config
				.getStringList(CONFIG_BLACKLIST, CATEGORY_MACHINES_RECYCLER,
						recyclerBlacklist,
						"List of items to prevent the Thermal Recycler from accepting as input");

		scrapBoxBonus = config
				.getInt(CONFIG_SCRAPBOX_BONUS,
						CATEGORY_MACHINES_RECYCLER,
						scrapBoxBonus,
						0,
						4,
						"The bonus amount of scrap a scrapbox will get when processed with Core: Extraction");

		enableScrapboxSpawn = config.getBoolean(CONFIG_ENABLE_SCRAPBOX_SPAWNING,
				CATEGORY_GENERAL, enableScrapboxSpawn,
				"Controls whether a Scrap Box will spawn items on right click");

		paperLogFuelSetting = config.getInt(CONFIG_PAPER_LOG_FUEL_SETTING,
				CATEGORY_FUEL_SETTINGS, paperLogFuelSetting, 0,
				Integer.MAX_VALUE,
				"Number of ticks a Paper Log will burn in a furnace");

		wormDropChance = config.getInt(CONFIG_WORM_DROP_CHANCE,
				CATEGORY_GENERAL, wormDropChance, 0,
				Integer.MAX_VALUE,
				"Chance that breaking a grass block will drop worms (1 in N)");
		
		rubblePileDensity = config.getInt(CONFIG_RUBBLE_PILE_DENSITY,
				CATEGORY_RUBBLE, rubblePileDensity, 0,
				Integer.MAX_VALUE,
				"Attempts per chunk to place rubble piles (higher more frequent discovery)");

		rubblePileDropCount = config.getInt(CONFIG_RUBBLE_PILE_DROP_COUNT,
				CATEGORY_RUBBLE, rubblePileDropCount, 0,
				Integer.MAX_VALUE,
				"Number of stacks to drop when rubble pile is broken");

		rubblePileDisable = config.getBoolean(CONFIG_RUBBLE_PILE_DISABLE,
				CATEGORY_RUBBLE, rubblePileDisable,
				"Enable/Disable Pile of Rubble worldgen");

		disableAnvilRepair = config.getBoolean(CONFIG_DISABLE_ANVIL,
				CATEGORY_GENERAL, disableAnvilRepair,
				"Enable/Disable repair of items using scrap in an anvil");

		entityItemMergeRange = config.getFloat(CONFIG_ITEM_MERGE_RANGE,
				CATEGORY_GENERAL, (float) entityItemMergeRange, 0F,
				6F,
				"Max distance to merge items on the ground (0 to disable)");

		vendingItemRenderRange = config.getInt(CONFIG_VENDING_ITEM_RENDER_RANGE,
				CATEGORY_MACHINES_VENDING, vendingItemRenderRange, 0,
				64,
				"Block range when items are rendered in a Vending Machine");

		vendingNameRenderRange = config.getInt(CONFIG_VENDING_NAME_RENDER_RANGE,
				CATEGORY_MACHINES_VENDING, vendingNameRenderRange, 0,
				64,
				"Block range when the name is rendered for a Vending Machine");

		vendingQuantityRenderRange = config.getInt(CONFIG_VENDING_QUANTITY_RENDER_RANGE,
				CATEGORY_MACHINES_VENDING, vendingQuantityRenderRange, 0,
				64,
				"Block range when item quantities are rendered for a Vending Machine");
	}

	public static boolean getEnableRecipeLogging() {
		return enableRecipeLogging;
	}

	public static boolean getEnableDebugLogging() {
		return enableDebugLogging;
	}

	public static boolean getModProcessingEnabled(final SupportedMod mod) {
		final Boolean result = enableModProcessing.get(mod);
		return result == null ? false : result;
	}

	public static String[] getModWhitelist() {
		return modWhitelist;
	}

	public static int getPaperLogFuelSetting() {
		return paperLogFuelSetting;
	}
	
	public static int getDebrisFuelSetting() {
		return debrisFuelSetting;
	}

	public static int getScrapBlockFuelSetting() {
		return scrapBlockFuelSetting;
	}

	public static int getPoorScrapFuelSetting() {
		return poorScrapFuelSetting;
	}

	public static int getStandardScrapFuelSetting() {
		return standardScrapFuelSetting;
	}

	public static int getSuperiorScrapFuelSetting() {
		return superiorScrapFuelSetting;
	}

	public static int getScrapBoxMultiplier() {
		return scrapboxMultiplier;
	}

	public static boolean getEnableRecyclerFX() {
		return enableRecyclerFX;
	}

	public static boolean getEnableComposterFX() {
		return enableComposterFX;
	}

	public static boolean getEnableTooltips() {
		return enableTooltips;
	}

	public static boolean getEnableWaila() {
		return enableWailaDisplay;
	}

	public static int getScrapBoxBonus() {
		return scrapBoxBonus;
	}

	public static boolean getEnableAssessorEnhancedLore() {
		return enableAssessorEnhancedLore;
	}

	public static String[] getRecyclerBlacklist() {
		return recyclerBlacklist;
	}
	
	public static boolean getEnableScrapboxSpawn() {
		return enableScrapboxSpawn;
	}
	
	public static int getWormDropChance() {
		return wormDropChance;
	}
	
	public static int getRubblePileDensity() {
		return rubblePileDensity;
	}
	
	public static int getRubblePileDropCount() {
		return rubblePileDropCount;
	}
	
	public static boolean getRubblePileDisable() {
		return rubblePileDisable;
	}
	
	public static boolean getDisableAnvilRepair() {
		return disableAnvilRepair;
	}
	
	public static double getEntityItemMergeRange() {
		return entityItemMergeRange;
	}

	public static int getVendingItemRenderRange() {
		return vendingItemRenderRange;
	}
	
	public static int getVendingNameRenderRange() {
		return vendingNameRenderRange;
	}
	
	public static int getVendingQuantityRenderRange() {
		return vendingQuantityRenderRange;
	}
}
