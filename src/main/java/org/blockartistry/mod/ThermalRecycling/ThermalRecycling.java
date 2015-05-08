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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.items.scrapbox.UseEffect;
import org.blockartistry.mod.ThermalRecycling.proxy.Proxy;

import com.mojang.authlib.GameProfile;

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
	public static final String VERSION = "@VERSION@";
	public static final String DEPENDENCIES = "required-after:ThermalExpansion;"
			+ "after:BuildCraft|Core;"
			+ "after:ThermalDynamics;"
			+ "after:ThermalFoundation;"
			+ "after:Forestry;"
			+ "after:MineFactoryReloaded;"
			+ "after:Thaumcraft;"
			+ "after:Railcraft;"
			+ "after:advgenerators;"
			+ "after:EnderIO;"
			+ "after:Waila;";

	static final String OUTPUT_FILE = "ThermalRecycling.log";

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

	public static GameProfile gameProfile = new GameProfile(
			UUID.nameUUIDFromBytes("ThermalRecycling".getBytes()),
			"[ThermalRecycling]");

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

		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {

		ModLog.info("ThermalRecycling's fake player: UUID = "
				+ gameProfile.getId().toString() + ", name = '"
				+ gameProfile.getName() + "'!");

		proxy.postInit(event);
		config.save();

		if (ModOptions.getEnableRecipeLogging()) {

			BufferedWriter writer = null;

			try {
				writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

				if (ModOptions.getEnableDebugLogging())
					ItemScrapData.writeDiagnostic(writer);

				ScrappingTables.writeDiagnostic(writer);
				UseEffect.diagnostic(writer);
				RecipeData.writeDiagnostic(writer);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// Close the writer regardless of what happens...
					writer.close();
				} catch (Exception e) {
				}
			}

			ModLog.info("Recipe load complete - check the file %s for details",
					OUTPUT_FILE);
		}
	}
}
