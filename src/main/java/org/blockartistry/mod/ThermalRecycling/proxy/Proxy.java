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

package org.blockartistry.mod.ThermalRecycling.proxy;

import org.blockartistry.mod.ThermalRecycling.AchievementManager;
import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.items.FuelHandler;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiHandler;
import org.blockartistry.mod.ThermalRecycling.support.ModPlugin;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

public class Proxy {

	public void preInit(FMLPreInitializationEvent event) {
	}

	public void init(FMLInitializationEvent event) {

		new ItemManager();
		new BlockManager();
		AchievementManager.registerAchievements();

		new GuiHandler();
		new FuelHandler();

	}

	public void postInit(FMLPostInitializationEvent event) {

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

				plugin.init(ThermalRecycling.config());

				if (!ModOptions.getModProcessingEnabled(mod)) {

					ModLog.info("Mod [%s (%s)] not enabled - skipping",
							plugin.getName(), plugin.getModId());

				} else {

					ModLog.info("Loading recipes for [%s]", plugin.getName());

					try {
						plugin.apply();
					} catch (Exception e) {
						ModLog.warn("Error processing recipes!");
						e.printStackTrace();
					}
				}
			}
		}
	}
}
