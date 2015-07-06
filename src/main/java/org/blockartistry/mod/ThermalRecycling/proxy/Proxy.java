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

import java.io.BufferedWriter;
import java.io.FileWriter;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;

import org.blockartistry.mod.ThermalRecycling.AchievementManager;
import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.RecipeData;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.events.AnvilHandler;
import org.blockartistry.mod.ThermalRecycling.events.BlockBreakEventHandler;
import org.blockartistry.mod.ThermalRecycling.events.BlockHarvestEventHandler;
import org.blockartistry.mod.ThermalRecycling.events.EntityEventHandler;
import org.blockartistry.mod.ThermalRecycling.events.EntityItemMergeHandler;
import org.blockartistry.mod.ThermalRecycling.events.VendingMachineBreakHandler;
import org.blockartistry.mod.ThermalRecycling.events.WormDropHandler;
import org.blockartistry.mod.ThermalRecycling.items.FuelHandler;
import org.blockartistry.mod.ThermalRecycling.items.scrapbox.UseEffect;
import org.blockartistry.mod.ThermalRecycling.machines.gui.GuiHandler;
import org.blockartistry.mod.ThermalRecycling.support.ModPlugin;
import org.blockartistry.mod.ThermalRecycling.tweaker.MineTweakerSupport;
import org.blockartistry.mod.ThermalRecycling.util.FakePlayerHelper;
import org.blockartistry.mod.ThermalRecycling.util.UpgradeRecipe;
import org.blockartistry.mod.ThermalRecycling.waila.WailaHandler;
import org.blockartistry.mod.ThermalRecycling.world.BiomeDecorationHandler;
import org.blockartistry.mod.ThermalRecycling.world.VendingVillageStructureHandler;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;

public class Proxy {

	private static boolean started = false;
	
	public void preInit(final FMLPreInitializationEvent event, final Configuration config) {
		FakePlayerHelper.initialize("ThermalRecycling");
		
		ModPlugin.preInitPlugins(config);
	}

	public void init(final FMLInitializationEvent event) {

		RecipeSorter.register(ThermalRecycling.MOD_ID + ".UpgradeRecipe",
				UpgradeRecipe.class, Category.SHAPED, "");

		ItemManager.register();
		BlockManager.register();
		AchievementManager.registerAchievements();

		GuiHandler.register();
		FuelHandler.register();

		// Hook for worm drop
		BlockHarvestEventHandler.register();
		BlockHarvestEventHandler.addHook(new WormDropHandler());

		// Hook to prevent vending machines from being broken
		BlockBreakEventHandler.register();
		BlockBreakEventHandler.addHook(new VendingMachineBreakHandler());
		
		// Hook various entity events
		EntityEventHandler.register();
		
		// Village generation
		if(ModOptions.getEnableVillageGen())
			VendingVillageStructureHandler.register();

		if (!ModOptions.getRubblePileDisable())
			BiomeDecorationHandler.register();

		if (!ModOptions.getDisableAnvilRepair())
			AnvilHandler.register();

		EntityItemMergeHandler.register();

		WailaHandler.register();
		
		MineTweakerSupport.initialize();
	}

	public void postInit(final FMLPostInitializationEvent event) {

		ModPlugin.initializePlugins();
		
		ModLog.info("ThermalRecycling's fake player: %s", FakePlayerHelper
				.getProfile().toString());
	}
	
	public void serverStarting(final FMLServerStartingEvent event) {
		
		if(!started) {
			started = true;

			// Do this here since this does the heavy lifting on decomposing
			// the recipe list.  MineTweaker may have changes applied and
			// we need to be sensitive to them.
			ModPlugin.postInitPlugins();
			
			if (ModOptions.getEnableRecipeLogging()) {

				BufferedWriter writer = null;

				try {
					writer = new BufferedWriter(new FileWriter(ThermalRecycling.OUTPUT_FILE));

					if (ModOptions.getEnableDebugLogging())
						ItemData.writeDiagnostic(writer);

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
						ThermalRecycling.OUTPUT_FILE);
			}

		}
	}
	
	public void serverStopping(final FMLServerStoppingEvent event) {
		
	}
}
