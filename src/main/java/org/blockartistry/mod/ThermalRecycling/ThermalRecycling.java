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
import org.apache.logging.log4j.LogManager;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ThermalRecyclerTables;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiHandler;
import org.blockartistry.mod.ThermalRecycling.proxy.Proxy;
import org.blockartistry.mod.ThermalRecycling.support.ModPlugin;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;

import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ThermalRecycling.MOD_ID, useMetadata = true, dependencies = ThermalRecycling.DEPENDENCIES, version = ThermalRecycling.VERSION)
public final class ThermalRecycling {

	public static final String MOD_ID = "recycling";
	public static final String MOD_NAME = "Thermal Recycling";
	public static final String VERSION = "0.2.4";
	public static final String DEPENDENCIES = "required-after:ThermalExpansion;"
			+ "after:BuildCraft|Core;"
			+ "after:ThermalDynamics;"
			+ "after:ThermalFoundation;"
			+ "after:Forestry;"
			+ "after:MineFactoryReloaded;"
			+ "after:Thaumcraft;"
			+ "after:Railcraft;" + "after:advgenerators;" + "after:EnderIO;";

	@Instance(MOD_ID)
	protected static ThermalRecycling instance;

	public static ThermalRecycling instance() {
		return instance;
	}

	@SidedProxy(clientSide = "org.blockartistry.mod.ThermalRecycling.proxy.ProxyClient", serverSide = "org.blockartistry.mod.ThermalRecycling.proxy.Proxy")
	protected static Proxy proxy;

	public static Proxy proxy() {
		return proxy;
	}

	protected static Configuration config;

	public static Configuration config() {
		return config;
	}

	public ThermalRecycling() {
		ModLog.setLogger(LogManager.getLogger(MOD_ID));
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// Load up our configuration
		config = new Configuration(event.getSuggestedConfigurationFile());

		config.load();
		ModOptions.load(config);
		config.save();

		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {

		ItemManager.registerItems();
		BlockManager.registerBlocks();
		ThermalRecyclerTables.initialize();

		new GuiHandler();

		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		boolean doLogging = ModOptions.getEnableRecipeLogging();

		ModLog.info("CoFH API version: " + cofh.api.CoFHAPIProps.VERSION);

		for (SupportedMod mod : SupportedMod.values()) {

			if (!mod.isLoaded()) {

				ModLog.info("Mod [%s (%s)] not detected - skipping",
						mod.getName(), mod.getModId());

			} else {

				// Get the plugin to process
				ModPlugin plugin = mod.getPlugin();
				if (plugin == null)
					continue;

				plugin.init(config);

				if (!ModOptions.getModProcessingEnabled(mod)) {

					ModLog.info("Mod [%s (%s)] not enabled - skipping",
							plugin.getName(), plugin.getModId());

				} else {

					if (doLogging) {
						ModLog.info("");
					}

					ModLog.info("Loading recipes for [%s]", plugin.getName());

					if (doLogging) {
						ModLog.info(StringUtils.repeat('-', 64));
					}

					try {
						plugin.apply();
					} catch (Exception e) {

						ModLog.warn("Error processing recipes!");
						e.printStackTrace();

					}
				}
			}
		}

		config.save();
		proxy.postInit(event);
	}
}
