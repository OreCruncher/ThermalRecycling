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

	VANILLA("Minecraft", "vanilla", VanillaMinecraft.class),

	THERMAL_FOUNDATION("Thermal Foundation", "ThermalFoundation",
			ModThermalFoundation.class),

	THERMAL_EXPANSION("Thermal Expansion", "ThermalExpansion",
			ModThermalExpansion.class),

	THERMAL_DYNAMICS("Thermal Dynamics", "ThermalDynamics",
			ModThermalDynamics.class),

	MINEFACTORY_RELOADED("MineFactory Reloaded", "MineFactoryReloaded",
			ModMinefactoryReloaded.class),

	THAUMCRAFT("Thaumcraft", "Thaumcraft", ModThaumcraft.class),

	BUILDCRAFT("BuidlCraft", "BuildCraft|Core", ModBuildCraft.class),

	FORESTRY("Forestry", "Forestry", ModForestry.class),

	RAILCRAFT("Railcraft", "Railcraft", ModRailcraft.class),

	ADVANCED_GENERATORS("Advanced Generators", "advgenerators",
			ModAdvancedGenerators.class),

	ENDERIO("EnderIO", "EnderIO", ModEnderIO.class);

	private final String name;
	private final String modId;
	private final Class<? extends ModPlugin> pluginFactory;

	private SupportedMod(String name, String modId,
			Class<? extends ModPlugin> clazz) {

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
		return this == VANILLA || Loader.isModLoaded(modId);
	}
}
