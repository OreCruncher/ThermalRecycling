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

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ThermalRecycling.MOD_ID, useMetadata = true, dependencies = ThermalRecycling.DEPENDENCIES, version = ThermalRecycling.VERSION)
public final class ThermalRecycling {

	@Instance
	public static ThermalRecycling instance = new ThermalRecycling();
	public static final String MOD_ID = "recycling";
	public static final String MOD_NAME = "Thermal Recycling";
	public static final String VERSION = "0.2.3";
	public static final String DEPENDENCIES = "required-after:ThermalExpansion;"
			+ "after:BuildCraft|Core;"
			+ "after:ThermalDynamics;"
			+ "after:ThermalFoundation;"
			+ "after:Forestry;"
			+ "after:MineFactoryReloaded;"
			+ "after:Thaumcraft;"
			+ "after:Railcraft;"
			+ "after:advgenerators;"
			+ "after:EnderIO;";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		ModLog.setLogger(event.getModLog());

		// Load up our configuration
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());

		config.load();
		ModOptions.instance.load(config);
		config.save();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		boolean doLogging = ModOptions.instance.getEnableRecipeLogging();

		ModLog.info("CoFH API version: " + cofh.api.CoFHAPIProps.VERSION);

		for (SupportedMod mod : SupportedMod.values()) {

			if (!mod.isLoaded()) {

				ModLog.info("Mod [%s (%s)] not detected - skipping",
						mod.getName(), mod.getModId());

			} else if (!ModOptions.instance.getModProcessingEnabled(mod)) {

				ModLog.info("Mod [%s (%s)] not enabled - skipping",
						mod.getName(), mod.getModId());

			} else {

				if (doLogging) {
					ModLog.info("");
				}

				ModLog.info("Loading recipes for [%s]", mod.getName());

				if (doLogging) {
					ModLog.info(StringUtils.repeat('-', 64));
				}

				try {

					mod.getPlugin().apply(ModOptions.instance);

				} catch (InstantiationException e) {
					ModLog.warn("Exception processing recipes for [%s]",
							mod.getName());
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					ModLog.warn("Exception processing recipes for [%s]",
							mod.getName());
					e.printStackTrace();
				} catch(Exception e) {
					ModLog.warn("Exception processing recipes for [%s]",
							mod.getName());
					e.printStackTrace();
				}
			}
		}
	}
}
