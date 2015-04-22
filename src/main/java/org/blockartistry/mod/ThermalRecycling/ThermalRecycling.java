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

import org.blockartistry.mod.ThermalRecycling.recipe.RecipeHelper;
import org.blockartistry.mod.ThermalRecycling.support.ModAdvancedGenerators;
import org.blockartistry.mod.ThermalRecycling.support.ModBuildCraft;
import org.blockartistry.mod.ThermalRecycling.support.ModEnderIO;
import org.blockartistry.mod.ThermalRecycling.support.ModForestry;
import org.blockartistry.mod.ThermalRecycling.support.ModMinefactoryReloaded;
import org.blockartistry.mod.ThermalRecycling.support.ModRailcraft;
import org.blockartistry.mod.ThermalRecycling.support.ModThaumcraft;
import org.blockartistry.mod.ThermalRecycling.support.ModThermalDynamics;
import org.blockartistry.mod.ThermalRecycling.support.ModThermalExpansion;
import org.blockartistry.mod.ThermalRecycling.support.ModThermalFoundation;
import org.blockartistry.mod.ThermalRecycling.support.ModTweaks;
import org.blockartistry.mod.ThermalRecycling.support.VanillaMinecraft;

import net.minecraft.item.ItemStack;
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
	public static final String VERSION = "0.2.2";
	public static final String DEPENDENCIES = "required-after:ThermalExpansion;"
			+ "after:BuildCraft|Core;"
			+ "after:ThermalDynamics;"
			+ "after:Forestry;"
			+ "after:MineFactoryReloaded;"
			+ "after:Thaumcraft;" + "after:Railcraft;" + "after:advgenerators";

	protected void dumpSubItems(String itemId) {
		ItemStack stack = RecipeHelper.getItemStack(itemId, 1);
		if (stack != null) {

			try {
				for (int i = 0; i < 1024; i++) {
					stack.setItemDamage(i);
					String name = stack.getDisplayName();
					if (!name.contains("(Destroy)"))
						ModLog.info("%s:%d = %s", itemId, i, name);
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				;
			}
		}
	}

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

	protected void handle(ModTweaks tweaks) {

		if (!tweaks.isModLoaded()) {
			ModLog.info("Mod [%s] not loaded - skipping", tweaks.getName());
			return;
		}

		ModLog.info("Adding recipes for items from [%s]", tweaks.getName());

		try {

			tweaks.apply(ModOptions.instance);

		} catch (Exception e) {

			ModLog.warn("Exception processing recipes for [%s]",
					tweaks.getName());
			ModLog.catching(e);
		}

	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// some example code
		ModLog.info("CoFH API I see: " + cofh.api.CoFHAPIProps.VERSION);

		handle(new VanillaMinecraft());
		handle(new ModThermalFoundation());
		handle(new ModThermalExpansion());
		handle(new ModThermalDynamics());
		handle(new ModThaumcraft());
		handle(new ModBuildCraft());
		handle(new ModMinefactoryReloaded());
		handle(new ModForestry());
		handle(new ModRailcraft());
		handle(new ModAdvancedGenerators());
		handle(new ModEnderIO());

	}
}
