/*
 * This file is part of ModpackInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 * Copyright (c) contributors
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

import org.blockartistry.mod.ThermalRecycling.support.ModThermalExpansion;
import org.blockartistry.mod.ThermalRecycling.support.ModThermalFoundation;
import org.blockartistry.mod.ThermalRecycling.support.VanillaMinecraft;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ThermalRecycling.MOD_ID, useMetadata = true, dependencies = ThermalRecycling.DEPENDENCIES, version = ThermalRecycling.VERSION)
public final class ThermalRecycling {

	@Instance
	public static ThermalRecycling instance = new ThermalRecycling();
	public static final String MOD_ID = "recycling";
	public static final String MOD_NAME = "Thermal Recycling";
	public static final String VERSION = "0.0.2";
	public static final String DEPENDENCIES = "required-after:ThermalExpansion";

	private ModOptions options = new ModOptions();

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
		options.load(config);
		config.save();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// some example code
		ModLog.info("CoFH API I see: " + cofh.api.CoFHAPIProps.VERSION);

		/*
		 * dumpSubItems("ThermalExpansion:Machine");
		 * dumpSubItems("ThermalExpansion:Device");
		 * dumpSubItems("ThermalExpansion:Dynamo");
		 * dumpSubItems("ThermalExpansion:Cell");
		 * dumpSubItems("ThermalExpansion:Tank");
		 * dumpSubItems("ThermalExpansion:Strongbox");
		 * dumpSubItems("ThermalExpansion:Cache");
		 * dumpSubItems("ThermalExpansion:Tesseract");
		 * dumpSubItems("ThermalExpansion:Plate");
		 * dumpSubItems("ThermalExpansion:Light");
		 * dumpSubItems("ThermalExpansion:Frame");
		 * dumpSubItems("ThermalExpansion:Glass");
		 * dumpSubItems("ThermalExpansion:Rockwool");
		 * dumpSubItems("ThermalExpansion:Sponge");
		 * dumpSubItems("ThermalExpansion:capacitor");
		 * dumpSubItems("ThermalExpansion:satchel");
		 * dumpSubItems("ThermalExpansion:diagram");
		 * dumpSubItems("ThermalExpansion:material");
		 * dumpSubItems("ThermalExpansion:augment");
		 * dumpSubItems("ThermalExpansion:florb");
		 */

		(new VanillaMinecraft()).apply(options);
		(new ModThermalFoundation()).apply(options);
		(new ModThermalExpansion()).apply(options);
	}
}
