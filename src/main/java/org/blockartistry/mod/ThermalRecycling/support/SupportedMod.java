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

import cpw.mods.fml.common.Loader;

public enum SupportedMod {

	VANILLA("Minecraft", "minecraft", VanillaMinecraft.class),

	THERMAL_FOUNDATION("Thermal Foundation", "ThermalFoundation",
			ModThermalFoundation.class),

	THERMAL_EXPANSION("Thermal Expansion", "ThermalExpansion",
			ModThermalExpansion.class),

	THERMAL_DYNAMICS("Thermal Dynamics", "ThermalDynamics",
			ModThermalDynamics.class),
			
	REDSTONE_ARSENAL("Redstone Arsenal", "RedstoneArsenal", ModRedstoneArsenal.class),

	MINEFACTORY_RELOADED("MineFactory Reloaded", "MineFactoryReloaded",
			ModMinefactoryReloaded.class),

	THAUMCRAFT("Thaumcraft", "Thaumcraft", ModThaumcraft.class),

	BUILDCRAFT_CORE("BuildCraft|Core", "BuildCraft|Core",
			ModBuildCraftCore.class), BUILDCRAFT_TRANSPORT(
			"BuildCraft|Transport", "BuildCraft|Transport",
			ModBuildCraftTransport.class), BUILDCRAFT_SILICON(
			"BuildCraft|Silicon", "BuildCraft|Silicon",
			ModBuildCraftSilicon.class), BUILDCRAFT_BUILDERS(
			"BuildCraft|Builders", "BuildCraft|Builders",
			ModBuildCraftBuilders.class), BUILDCRAFT_ENERGY(
			"BuildCraft|Energy", "BuildCraft|Energy", ModBuildCraftEnergy.class), BUILDCRAFT_FACTORY(
			"BuildCraft|Factory", "BuildCraft|Factory",
			ModBuildCraftFactory.class),

	FORESTRY("Forestry", "Forestry", ModForestry.class),

	RAILCRAFT("Railcraft", "Railcraft", ModRailcraft.class),

	ADVANCED_GENERATORS("Advanced Generators", "advgenerators",
			ModAdvancedGenerators.class),

	ENDERIO("EnderIO", "EnderIO", ModEnderIO.class),

	IRONCHEST("Iron Chest", "IronChest", ModIronChest.class),

	CHICKEN_CHUNKS("Chicken Chunks", "ChickenChunks", ModChickenChunks.class),

	RFTOOLS("RFTools", "rftools", ModRFTools.class),
	
	ENDER_STORAGE("Ender Storage", "EnderStorage", ModEnderStorage.class),
	
	EXTRABIOMESXL("ExtrabiomesXL", "ExtrabiomesXL", ModExtrabiomesXL.class),
	
	SOLARFLUX("Solar Flux", "SolarFlux", ModSolarFlux.class),
	
	RFDRILLS("RFDrills", "rfdrills", ModRFDrills.class),
	
	SIMPLY_JETPACKS("Simply Jetpacks", "simplyjetpacks", ModSimplyJetpacks.class),
	
	RFWINDMILLS("RF Windmills", "rfwindmill", ModRFWindmills.class),
	
	REDSTONE_ARMORY("Redstone Armory", "RArm", ModRedstoneArmory.class),

	// This is last. Reason is that the plugins have the first crack
	// at recipes and setting up the necessary black list entries
	// prior to the crafting manager recipe scan.
	THERMAL_RECYCLING("Thermal Recycling", "recycling",
			ModThermalRecycling.class);

	private final String name;
	private final String modId;
	private final Class<? extends ModPlugin> pluginFactory;

	private SupportedMod(final String name, final String modId,
			final Class<? extends ModPlugin> clazz) {

		this.name = name;
		this.modId = modId;
		pluginFactory = clazz;

	}

	public ModPlugin getPlugin() {
		try {
			return pluginFactory.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	public String getName() {
		return name;
	}

	public String getModId() {
		return modId;
	}

	public boolean isLoaded() {
		return this == VANILLA || this == THERMAL_RECYCLING
				|| Loader.isModLoaded(modId);
	}

	public static String[] getModIdList() {
		final String[] result = new String[values().length];
		for (int i = 0; i < values().length; i++)
			result[i] = values()[i].getModId();
		return result;
	}
}
