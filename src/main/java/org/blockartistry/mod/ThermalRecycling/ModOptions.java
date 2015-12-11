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
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import net.minecraftforge.common.config.Configuration;

public final class ModOptions {
	
	private static Configuration config;

	private ModOptions() {
	}

	protected static final String CATEGORY_RECYCLE_ENABLE = "recycle.enable";
	protected static final String CATEGORY_RECYCLE_RECIPIES_CONTROL = "recycle.recipe.control";
	protected static final String CATEGORY_LOGGING_CONTROL = "logging";
	protected static final String CATEGORY_MODS = "mods";

	protected static final String CATEGORY_GENERAL = "recycle.general";
	protected static final String CONFIG_ENABLE_BREEDING_CHANGES = "Enable Breeding Changes";
	protected static final String CONFIG_ENABLE_SCRAPBOX_SPAWNING = "Enable Scrapbox Spawning";
	protected static final String CONFIG_WORM_DROP_CHANCE = "Worm Drop Chance";
	protected static final String CONFIG_WORM_DROP_CHANCE_RAIN = "Worm Drop Chance (Rain)";
	protected static final String CONFIG_DISABLE_ANVIL = "Disable Anvil Repair";
	protected static final String CONFIG_ITEM_MERGE_RANGE = "EntityItem Merge Range";
	protected static final String CONFIG_XP_BOTTLE_VALUE = "Bottled Experience Value";
	protected static final String CONFIG_TRASH_LIST = "Inventory Trash List";
	protected static final String CONFIG_ENABLE_VILLAGE_GEN = "Enable Village Worldgen";
	protected static final String CONFIG_ENABLE_EXTRA_VILLAGE_VENDING_TYPES = "Enable Extra Village Vending Types";
	protected static final String CONFIG_VILLAGE_STRUCTURE_WEIGHT = "Village Structure Weight";
	protected static final String CONFIG_VILLAGE_STRUCTURE_COUNT = "Village Structure Count";
	protected static final String CONFIG_ENABLE_ORE_DICTIONARY_SCAN = "Enable Forge OreDictionary Scan";

	protected static final String CATEGORY_RUBBLE = "recycle.rubble";
	protected static final String CONFIG_RUBBLE_PILE_DISABLE = "Disable";
	protected static final String CONFIG_RUBBLE_PILE_DENSITY = "Density";
	protected static final String CONFIG_RUBBLE_PILE_DROP_COUNT = "Number of Drops";
	protected static final String CONFIG_RUBBLE_DIMENSION_LIST = "Generation Dimension List";
	protected static final String CONFIG_RUBBLE_DIMENSION_LIST_AS_BLACK = "Dimension List As Blacklist";

	protected static final String CATEGORY_ENERGETIC_REDSTONE = "recycle.energeticRedstone";
	protected static final String CONFIG_ENERGETIC_REDSTONE_CHANCE = "Energetic Redstone Ore Chance";
	protected static int energeticRedstoneChance = 10;
	protected static final String CONFIG_RTG_BASE_POWER = "Base Energy per Tick";
	protected static int rtgBasePowerPerTick = 2;
	protected static final String CONFIG_RTG_BASE_ENERGY = "Base Energy per Cell";
	protected static int rtgBaseEnergy = 1000000;
	protected static final String CONFIG_ENABLE_URANIUM_RECIPE = "Enable Crafting Using Uranium Dust";
	protected static boolean energeticRedstoneUraniumCrafting = true;

	protected static final String CATEGORY_MACHINES_RECYCLER = "machines.recycler";
	protected static final String CATEGORY_MACHINES_COMPOSTER = "machines.composter";
	protected static final String CATEGORY_MACHINES_ASSESSOR = "machines.assessor";
	protected static final String CATEGORY_MACHINES_VENDING = "machines.vending";
	protected static final String CATEGORY_MACHINES_BATTERY_RACK = "machines.batteryrack";
	protected static final String CONFIG_ENABLE_FX = "Enable FX";
	protected static final String CONFIG_SCRAPBOX_BONUS = "Scrapbox Bonus";
	protected static final String CONFIG_ENABLE_ENHANCED_LORE = "Enhanced Lore";
	protected static final String CONFIG_BLACKLIST = "Blacklist";
	protected static final String CONFIG_RECIPE_COMPONENT_BLACKLIST = "Recipe Component Blacklist";
	protected static final String CONFIG_VENDING_ITEM_RENDER_RANGE = "Item Render Range";
	protected static final String CONFIG_VENDING_NAME_RENDER_RANGE = "Name Render Range";
	protected static final String CONFIG_VENDING_QUANTITY_RENDER_RANGE = "Quantity Render Range";
	protected static final String CONFIG_VENDING_BLOCK_PIPE_CONNECTION = "Disallow Pipe Connection";
	protected static final String CONFIG_BATTERY_RACK_TRANSFER = "RF Transfer per Tick";
	protected static final String CONFIG_BONEMEAL_PRODUCED = "Bone Meal Produced";
	
	protected static final String CONFIG_ENERGY_PER_TICK = "RF per tick";
	protected static final String CONFIG_SCRAP_TICKS = "Ticks to scrap";
	protected static final String CONFIG_DECOMP_TICKS = "Ticks to decompose";
	protected static final String CONFIG_EXTRACT_TICKS = "Ticks to extract";

	protected static final String CONFIG_ENABLE_RECIPE_LOGGING = "Enable Recipe Logging";
	protected static final String CONFIG_ENABLE_DEBUG_LOGGING = "Enable Debug Logging";
	protected static final String CONFIG_ENABLE_WAILA = "Enable Waila Display";
	protected static final String CONFIG_WAILA_DATA_LOCATION = "Waila Data Location";
	protected static final String CONFIG_ENABLE_ONLINE_VERSION_CHECK = "Enable Online Version Check";

	protected static final String CATEGORY_FUEL_SETTINGS = "Fuel Settings";
	protected static final String CONFIG_POOR_SCRAP_FUEL_SETTING = "Poor Scrap Fuel Ticks";
	protected static final String CONFIG_STANDARD_SCRAP_FUEL_SETTING = "Standard Scrap Fuel Ticks";
	protected static final String CONFIG_SUPERIOR_SCRAP_FUEL_SETTING = "Superior Scrap Fuel Ticks";
	protected static final String CONFIG_SCRAPBOX_MULTIPLIER = "Scrapbox Multiplier";
	protected static final String CONFIG_DEBRIS_FUEL_SETTING = "Debris Fuel Ticks";
	protected static final String CONFIG_SCRAP_BLOCK_FUEL_SETTING = "Scrap Block Fuel Ticks";
	protected static final String CONFIG_PAPER_LOG_FUEL_SETTING = "Paper Log Fuel Ticks";

	protected static final String CATEGORY_REPAIR_SETTINGS = "Repair Settings";
	protected static final String CONFIG_POOR_SCRAP_REPAIR_SETTING = "Poor Scrap Repair Value";
	protected static final String CONFIG_STANDARD_SCRAP_REPAIR_SETTING = "Standard Scrap Repair Value";
	protected static final String CONFIG_SUPERIOR_SCRAP_REPAIR_SETTING = "Superior Scrap Repair Value";
	protected static final String CONFIG_RENAME_COST = "Rename Cost";
	protected static final String CONFIG_POOR_XP_COST = "Poor Scrap Level Cost";
	protected static final String CONFIG_STANDARD_XP_COST = "Standard Scrap Level Cost";
	protected static final String CONFIG_SUPERIOR_XP_COST = "Superior Scrap Level Cost";
	
	protected static final String CATEGORY_ENCHANTMENTS = "Enchantments";
	protected static final String CONFIG_ENABLE_PICKUP = "Vacuum Enable";
	protected static boolean vacuumEnable = true;
	protected static final String CONFIG_VACUUM_ID = "Vacuum ID";
	protected static int vacuumId = 0;

	protected static HashMap<SupportedMod, Boolean> enableModProcessing = new HashMap<SupportedMod, Boolean>();
	protected static boolean enableRecipeLogging = true;
	protected static boolean enableDebugLogging = false;
	protected static int wailaDataLocation = 2;
	protected static boolean enableVersionChecking = true;
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
	protected static boolean enableBreedingChanges = true;
	protected static boolean enableTooltips = true;
	protected static int scrapBoxBonus = 1;
	protected static boolean enableAssessorEnhancedLore = true;
	protected static boolean enableScrapboxSpawn = true;
	protected static boolean enableForgeOreDictionaryScan = true;
	protected static int wormDropChance = 20;
	protected static int wormDropChanceRain = 8;
	protected static boolean disableAnvilRepair = false;
	protected static double entityItemMergeRange = 0;
	protected static int xpBottleValue = 44;
	protected static String[] recyclerBlacklist = new String[] { "minecraft:cobblestone", "minecraft:sandstone:*" };
	protected static String[] inventoryTrashList = new String[] { "minecraft:cobblestone", "minecraft:sandstone:*",
			"minecraft:sand:*", "minecraft:gravel", "minecraft:dirt", "minecraft:snowball", };
	protected static String[] recipeComponentBlacklist = new String[] { "ProjectE:item.pe_philosophers_stone" };

	protected static boolean enableVillageWorldgen = true;
	protected static boolean enableExtraVillageVendingTypes = true;
	protected static int villageStructureWeight = 10;
	protected static int villageStructureCount = 1;
	protected static int rubblePileDensity = 80;
	protected static int rubblePileDropCount = 3;
	protected static boolean rubblePileDisable = false;
	protected static int[] rubbleDimensionList = new int[] {};
	protected static boolean rubbleDimensionListAsBlack = true;

	protected static int vendingItemRenderRange = 6;
	protected static int vendingNameRenderRange = 8;
	protected static int vendingQuantityRenderRange = 4;
	protected static boolean vendingDisallowPipeConnection = true;

	protected static int poorScrapRepairValue = 3;
	protected static int standardScrapRepairValue = 6;
	protected static int superiorScrapRepairValue = 12;
	protected static int scrapboxRepairMultiplier = 9;
	protected static int renameCost = 3;
	protected static int poorXPCost = 3;
	protected static int standardXPCost = 4;
	protected static int superiorXPCost = 5;

	protected static int batteryRackTransfer = 80;
	protected static int bonemealProduced = 2;

	protected static int energyPerTick = 40;
	protected static int scrapTicks = 20;
	protected static int decompTicks = 40;
	protected static int extractTicks = 80;
	
	public static void load(final Configuration cfg) {

		// Save a reference
		config = cfg;
		
		String comment = "Enables/disables debug logging of the mod";
		enableDebugLogging = config.getBoolean(CONFIG_ENABLE_DEBUG_LOGGING, CATEGORY_LOGGING_CONTROL,
				enableDebugLogging, comment);

		comment = "Enables/disables logging of recipes to the Forge log during startup";
		enableRecipeLogging = config.getBoolean(CONFIG_ENABLE_RECIPE_LOGGING, CATEGORY_LOGGING_CONTROL,
				enableRecipeLogging, comment);

		comment = "Enables/disables online version checking";
		enableVersionChecking = config.getBoolean(CONFIG_ENABLE_ONLINE_VERSION_CHECK, CATEGORY_LOGGING_CONTROL,
				enableVersionChecking, comment);

		comment = "Enables/disables display Waila integration";
		enableWailaDisplay = config.getBoolean(CONFIG_ENABLE_WAILA, CATEGORY_MODS, enableWailaDisplay, comment);

		comment = "Display data in Waila 0: header, 1: body, 2: tail";
		wailaDataLocation = config.getInt(CONFIG_WAILA_DATA_LOCATION, CATEGORY_MODS, wailaDataLocation, 0, 2, comment);

		for (final SupportedMod mod : SupportedMod.values()) {

			comment = String.format("Enable/Disable whether recycling recipes for items from [%s] are added",
					mod.getName());
			final boolean b = config.getBoolean(mod.getModId(), CATEGORY_RECYCLE_ENABLE, true, comment);

			enableModProcessing.put(mod, b);
		}

		comment = "ModIds to add to the internal whitelist";
		modWhitelist = config.getStringList("Whitelist", CATEGORY_MODS, modWhitelist, comment);

		comment = "Controls the display of Scrap Value information in the client (Waila and NEI)";
		enableTooltips = config.getBoolean("Enable Tooltips", CATEGORY_MODS, enableTooltips, comment);

		comment = "Number of ticks Poor Scrap will burn in a furnace";
		poorScrapFuelSetting = config.getInt(CONFIG_POOR_SCRAP_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				poorScrapFuelSetting, 0, Integer.MAX_VALUE, comment);

		comment = "Number of ticks Standard Scrap will burn in a furnace";
		standardScrapFuelSetting = config.getInt(CONFIG_STANDARD_SCRAP_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				standardScrapFuelSetting, 0, Integer.MAX_VALUE, comment);

		comment = "Number of ticks Superior Scrap will burn in a furnace";
		superiorScrapFuelSetting = config.getInt(CONFIG_SUPERIOR_SCRAP_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				superiorScrapFuelSetting, 0, Integer.MAX_VALUE, comment);

		comment = "Number of ticks Debris will burn in a furnace";
		debrisFuelSetting = config.getInt(CONFIG_DEBRIS_FUEL_SETTING, CATEGORY_FUEL_SETTINGS, debrisFuelSetting, 0,
				Integer.MAX_VALUE, comment);

		comment = "Number of ticks a Block of Scrap will burn in a furnace";
		scrapBlockFuelSetting = config.getInt(CONFIG_SCRAP_BLOCK_FUEL_SETTING, CATEGORY_FUEL_SETTINGS,
				scrapBlockFuelSetting, 0, Integer.MAX_VALUE, comment);

		comment = "Number of ticks a ScrapBox will burn in multiples of the base scrap type";
		scrapboxMultiplier = config.getInt(CONFIG_SCRAPBOX_MULTIPLIER, CATEGORY_FUEL_SETTINGS, scrapboxMultiplier, 0,
				Integer.MAX_VALUE, comment);

		comment = "Control whether client displays visual effects";
		enableRecyclerFX = config.getBoolean(CONFIG_ENABLE_FX, CATEGORY_MACHINES_RECYCLER, enableRecyclerFX, comment);

		comment = "Control whether client displays visual effects";
		enableComposterFX = config.getBoolean(CONFIG_ENABLE_FX, CATEGORY_MACHINES_COMPOSTER, enableComposterFX,
				comment);

		comment = "Amount of bone meal produced in a single recipe";
		bonemealProduced = config.getInt(CONFIG_BONEMEAL_PRODUCED, CATEGORY_MACHINES_COMPOSTER, bonemealProduced, 1, 16,
				comment);

		comment = "Control whether enhanced lore is provided in the Scrap Assessor View";
		enableAssessorEnhancedLore = config.getBoolean(CONFIG_ENABLE_ENHANCED_LORE, CATEGORY_MACHINES_ASSESSOR,
				enableAssessorEnhancedLore, comment);

		comment = "List of items to prevent the Thermal Recycler from accepting as input";
		recyclerBlacklist = config.getStringList(CONFIG_BLACKLIST, CATEGORY_MACHINES_RECYCLER, recyclerBlacklist,
				comment);

		comment = "Recipes containing these items will be ignored";
		recipeComponentBlacklist = config.getStringList(CONFIG_RECIPE_COMPONENT_BLACKLIST, CATEGORY_MACHINES_RECYCLER,
				recipeComponentBlacklist, comment);

		comment = "List of items to delete from inventory when using a Litter Bag";
		inventoryTrashList = config.getStringList(CONFIG_TRASH_LIST, CATEGORY_GENERAL, inventoryTrashList, comment);

		comment = "Controls whether mod specific village generation will occur";
		enableVillageWorldgen = config.getBoolean(CONFIG_ENABLE_VILLAGE_GEN, CATEGORY_GENERAL, enableVillageWorldgen,
				comment);

		comment = "Enables extra Village Gen vending types for Vending Machine";
		enableExtraVillageVendingTypes = config.getBoolean(CONFIG_ENABLE_EXTRA_VILLAGE_VENDING_TYPES, CATEGORY_GENERAL,
				enableExtraVillageVendingTypes, comment);

		comment = "Relative weight for structure during village generation (higher = more common)";
		villageStructureWeight = config.getInt(CONFIG_VILLAGE_STRUCTURE_WEIGHT, CATEGORY_GENERAL,
				villageStructureWeight, 1, 1000, comment);

		comment = "Max number of vending machines to generate in a village";
		villageStructureCount = config.getInt(CONFIG_VILLAGE_STRUCTURE_COUNT, CATEGORY_GENERAL, villageStructureCount,
				1, 5, comment);

		comment = "The bonus amount of scrap a scrapbox will get when processed with Core: Extraction";
		scrapBoxBonus = config.getInt(CONFIG_SCRAPBOX_BONUS, CATEGORY_MACHINES_RECYCLER, scrapBoxBonus, 0, 4, comment);

		comment = "Controls whether a Scrap Box will spawn items on right click";
		enableScrapboxSpawn = config.getBoolean(CONFIG_ENABLE_SCRAPBOX_SPAWNING, CATEGORY_GENERAL, enableScrapboxSpawn,
				comment);

		comment = "Enable/Disable Forge dictionary scan for setting scrap values";
		enableForgeOreDictionaryScan = config.getBoolean(CONFIG_ENABLE_ORE_DICTIONARY_SCAN, CATEGORY_GENERAL,
				enableForgeOreDictionaryScan, comment);

		comment = "Number of ticks a Paper Log will burn in a furnace";
		paperLogFuelSetting = config.getInt(CONFIG_PAPER_LOG_FUEL_SETTING, CATEGORY_FUEL_SETTINGS, paperLogFuelSetting,
				0, Integer.MAX_VALUE, comment);

		comment = "Chance that breaking a grass block will drop worms (1 in N)";
		wormDropChance = config.getInt(CONFIG_WORM_DROP_CHANCE, CATEGORY_GENERAL, wormDropChance, 1, Integer.MAX_VALUE,
				comment);

		comment = "Chance that breaking a grass block will drop worms when raining (1 in N)";
		wormDropChanceRain = config.getInt(CONFIG_WORM_DROP_CHANCE_RAIN, CATEGORY_GENERAL, wormDropChanceRain, 1,
				Integer.MAX_VALUE, comment);

		comment = "Attempts per chunk to place rubble piles (higher more frequent discovery)";
		rubblePileDensity = config.getInt(CONFIG_RUBBLE_PILE_DENSITY, CATEGORY_RUBBLE, rubblePileDensity, 0,
				Integer.MAX_VALUE, comment);

		comment = "Number of stacks to drop when rubble pile is broken";
		rubblePileDropCount = config.getInt(CONFIG_RUBBLE_PILE_DROP_COUNT, CATEGORY_RUBBLE, rubblePileDropCount, 0,
				Integer.MAX_VALUE, comment);

		comment = "Enable/Disable Pile of Rubble worldgen";
		rubblePileDisable = config.getBoolean(CONFIG_RUBBLE_PILE_DISABLE, CATEGORY_RUBBLE, rubblePileDisable, comment);

		comment = "Dimension list for Pile of Rubble generation";
		String temp = config.getString(CONFIG_RUBBLE_DIMENSION_LIST, CATEGORY_RUBBLE, "", comment);
		try {
			rubbleDimensionList = MyUtils.split(",", temp);
		} catch (Throwable e) {
			ModLog.warn("Error in rubble dimension list: %s", temp);
		}

		comment = "Dimension list is a black list rather than white list";
		rubbleDimensionListAsBlack = config.getBoolean(CONFIG_RUBBLE_DIMENSION_LIST_AS_BLACK, CATEGORY_RUBBLE,
				rubbleDimensionListAsBlack, comment);

		comment = "Enable/Disable repair of items using scrap in an anvil";
		disableAnvilRepair = config.getBoolean(CONFIG_DISABLE_ANVIL, CATEGORY_GENERAL, disableAnvilRepair, comment);

		comment = "Max distance to merge items on the ground (0 to disable)";
		entityItemMergeRange = config.getFloat(CONFIG_ITEM_MERGE_RANGE, CATEGORY_GENERAL, (float) entityItemMergeRange,
				0F, 6F, comment);

		comment = "Divisor value for calculating number of bottles to return when scrapping (higher means less bottles; 0 disables)";
		xpBottleValue = config.getInt(CONFIG_XP_BOTTLE_VALUE, CATEGORY_GENERAL, xpBottleValue, 0, Integer.MAX_VALUE,
				comment);

		comment = "Block range when items are rendered in a Vending Machine";
		vendingItemRenderRange = config.getInt(CONFIG_VENDING_ITEM_RENDER_RANGE, CATEGORY_MACHINES_VENDING,
				vendingItemRenderRange, 0, 64, comment);

		comment = "Block range when the name is rendered for a Vending Machine";
		vendingNameRenderRange = config.getInt(CONFIG_VENDING_NAME_RENDER_RANGE, CATEGORY_MACHINES_VENDING,
				vendingNameRenderRange, 0, 64, comment);

		comment = "Block range when item quantities are rendered for a Vending Machine";
		vendingQuantityRenderRange = config.getInt(CONFIG_VENDING_QUANTITY_RENDER_RANGE, CATEGORY_MACHINES_VENDING,
				vendingQuantityRenderRange, 0, 64, comment);

		comment = "Blocks connection of item transport pipes to a Vending Machine";
		vendingDisallowPipeConnection = config.getBoolean(CONFIG_VENDING_BLOCK_PIPE_CONNECTION,
				CATEGORY_MACHINES_VENDING, vendingDisallowPipeConnection, comment);

		comment = "Poor Scrap Repair Value";
		poorScrapRepairValue = config.getInt(CONFIG_POOR_SCRAP_REPAIR_SETTING, CATEGORY_REPAIR_SETTINGS,
				poorScrapRepairValue, 0, 64, comment);

		comment = "Standard Scrap Repair Value";
		standardScrapRepairValue = config.getInt(CONFIG_STANDARD_SCRAP_REPAIR_SETTING, CATEGORY_REPAIR_SETTINGS,
				standardScrapRepairValue, 0, 64, comment);

		comment = "Superior Scrap Repair Value";
		superiorScrapRepairValue = config.getInt(CONFIG_SUPERIOR_SCRAP_REPAIR_SETTING, CATEGORY_REPAIR_SETTINGS,
				superiorScrapRepairValue, 0, 64, comment);

		comment = "Multiplier for a Scrap Box";
		scrapboxRepairMultiplier = config.getInt(CONFIG_SCRAPBOX_MULTIPLIER, CATEGORY_REPAIR_SETTINGS,
				scrapboxRepairMultiplier, 0, 64, comment);

		comment = "Level cost to rename an item";
		renameCost = config.getInt(CONFIG_RENAME_COST, CATEGORY_REPAIR_SETTINGS, renameCost, 0, 64, comment);

		comment = "Level cost to use Poor Scrap for repair";
		poorXPCost = config.getInt(CONFIG_POOR_XP_COST, CATEGORY_REPAIR_SETTINGS, poorXPCost, 0, 64, comment);

		comment = "Level cost to use Standard Scrap for repair";
		standardXPCost = config.getInt(CONFIG_STANDARD_XP_COST, CATEGORY_REPAIR_SETTINGS, standardXPCost, 0, 64,
				comment);

		comment = "Level cost to use Superior Scrap for repair";
		superiorXPCost = config.getInt(CONFIG_SUPERIOR_XP_COST, CATEGORY_REPAIR_SETTINGS, superiorXPCost, 0, 64,
				comment);

		comment = "Redstone Ore replace chance for Energetic Redstone Ore (0 disable)";
		energeticRedstoneChance = config.getInt(CONFIG_ENERGETIC_REDSTONE_CHANCE, CATEGORY_ENERGETIC_REDSTONE,
				energeticRedstoneChance, 0, Integer.MAX_VALUE, comment);

		comment = "RF/t based on the number of Fuel Cells used to create";
		rtgBasePowerPerTick = config.getInt(CONFIG_RTG_BASE_POWER, CATEGORY_ENERGETIC_REDSTONE, rtgBasePowerPerTick, 0,
				Integer.MAX_VALUE, comment);

		comment = "RF Energy per Fuel Cell used to create";
		rtgBaseEnergy = config.getInt(CONFIG_RTG_BASE_ENERGY, CATEGORY_ENERGETIC_REDSTONE, rtgBaseEnergy, 0,
				Integer.MAX_VALUE, comment);

		comment = "Maximum RF per tick to transfer from the installed energy container";
		batteryRackTransfer = config.getInt(CONFIG_BATTERY_RACK_TRANSFER, CATEGORY_MACHINES_BATTERY_RACK,
				batteryRackTransfer, 0, Integer.MAX_VALUE, comment);

		comment = "Enable crafting of Energetic Redstone Dust using Uranium dust";
		energeticRedstoneUraniumCrafting = config.getBoolean(CONFIG_ENABLE_URANIUM_RECIPE, CATEGORY_ENERGETIC_REDSTONE,
				energeticRedstoneUraniumCrafting, comment);

		comment = "Enable additional food items for animal breeding";
		enableBreedingChanges = config.getBoolean(CONFIG_ENABLE_BREEDING_CHANGES, CATEGORY_GENERAL,
				enableBreedingChanges, comment);
		
		comment = "RF/t to operate";
		energyPerTick = config.getInt(CONFIG_ENERGY_PER_TICK, CATEGORY_MACHINES_RECYCLER, energyPerTick, 0,
				Integer.MAX_VALUE, comment);

		comment = "Number of ticks required to scrap an item without a decomposition core";
		scrapTicks = config.getInt(CONFIG_SCRAP_TICKS, CATEGORY_MACHINES_RECYCLER, scrapTicks, 0,
				Integer.MAX_VALUE, comment);
		
		comment = "Number of ticks required when using a decomposition core";
		decompTicks = config.getInt(CONFIG_DECOMP_TICKS, CATEGORY_MACHINES_RECYCLER, decompTicks, 0,
				Integer.MAX_VALUE, comment);
		
		comment = "Number of ticks required when using an extraction core";
		extractTicks = config.getInt(CONFIG_EXTRACT_TICKS, CATEGORY_MACHINES_RECYCLER, extractTicks, 0,
				Integer.MAX_VALUE, comment);

		// Enchantment
		comment = "Enable the Vacuum enchantment";
		vacuumEnable = config.getBoolean(CONFIG_ENABLE_PICKUP, CATEGORY_ENCHANTMENTS,
				vacuumEnable, comment);
		
		comment = "ID of the Vacuum enchantment";
		vacuumId = config.getInt(CONFIG_VACUUM_ID, CATEGORY_ENCHANTMENTS, vacuumId, 0,
				255, comment);
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

	public static boolean getOnlineVersionChecking() {
		return enableVersionChecking;
	}

	public static boolean getEnableWaila() {
		return enableWailaDisplay;
	}

	public static int getWailaDataLocation() {
		return wailaDataLocation;
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

	public static String[] getInventoryTrashList() {
		return inventoryTrashList;
	}

	public static boolean getEnableScrapboxSpawn() {
		return enableScrapboxSpawn;
	}

	public static boolean getEnableForgeOreDictionaryScan() {
		return enableForgeOreDictionaryScan;
	}

	public static int getWormDropChance() {
		return wormDropChance;
	}

	public static int getWormDropChanceRain() {
		return wormDropChanceRain;
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

	public static boolean getRubbleDimensionListAsBlack() {
		return rubbleDimensionListAsBlack;
	}

	public static int[] getRubbleDimensionList() {
		return rubbleDimensionList;
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

	public static boolean getVendingDisallowPipeConnection() {
		return vendingDisallowPipeConnection;
	}

	public static int getPoorScrapRepairValue() {
		return poorScrapRepairValue;
	}

	public static int getStandardScrapRepairValue() {
		return standardScrapRepairValue;
	}

	public static int getSuperiorScrapRepairValue() {
		return superiorScrapRepairValue;
	}

	public static int getScrapboxRepairMultiplier() {
		return scrapboxRepairMultiplier;
	}

	public static int getRepairRenameCost() {
		return renameCost;
	}

	public static int getPoorRepairXPCost() {
		return poorXPCost;
	}

	public static int getStandardRepairXPCost() {
		return standardXPCost;
	}

	public static int getSuperiorRepairXPCost() {
		return superiorXPCost;
	}

	public static int getXpBottleValue() {
		return xpBottleValue;
	}

	public static boolean getEnableVillageGen() {
		return enableVillageWorldgen;
	}

	public static boolean getEnableExtraVillageVendingTypes() {
		return enableExtraVillageVendingTypes;
	}

	public static int getVillageStructureWeight() {
		return villageStructureWeight;
	}

	public static int getVillageStructureCount() {
		return villageStructureCount;
	}

	public static String[] getRecipeComponentBlacklist() {
		return recipeComponentBlacklist;
	}

	public static int getEnergeticRedstoneChance() {
		return energeticRedstoneChance;
	}

	public static int getRTGBasePowerPerTick() {
		return rtgBasePowerPerTick;
	}

	public static int getRTGBaseEnergy() {
		return rtgBaseEnergy;
	}

	public static int getBatteryRackTransfer() {
		return batteryRackTransfer;
	}

	public static boolean getEnergeticRedstoneUraniumCrafting() {
		return energeticRedstoneUraniumCrafting;
	}
	
	public static int getBonemealProduced() {
		return bonemealProduced;
	}
	
	public static boolean getEnableBreedingChanges() {
		return enableBreedingChanges;
	}

	public static int getEnergyPerTick() {
		return energyPerTick;
	}
	
	public static int getScrapTicks() {
		return scrapTicks;
	}
	
	public static int getDecompTicks() {
		return decompTicks;
	}
	
	public static int getExtractTicks() {
		return extractTicks;
	}

	// Enchantment
	public static boolean getVacuumEnable() {
		return vacuumEnable;
	}
	
	public static int getVacuumId() {
		return vacuumId;
	}
	
	public static void setVacuumId(final int id) {
		vacuumId = id;
		config.getCategory(CATEGORY_ENCHANTMENTS.toLowerCase()).get(CONFIG_VACUUM_ID).set(id);
	}
}
