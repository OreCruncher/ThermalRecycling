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

import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.recipe.BlastRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FluidTransposerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SawmillRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SmelterRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ModPlugin {

	protected final SupportedMod mod;
	protected final String MOD_CONFIG_SECTION;

	SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();
	SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();
	FluidTransposerRecipeBuilder fluid = new FluidTransposerRecipeBuilder();
	BlastRecipeBuilder blast = new BlastRecipeBuilder();

	public ModPlugin(SupportedMod m) {
		mod = m;
		MOD_CONFIG_SECTION = "recycle.recipe.control." + mod.getModId();
	}

	public String getModId() {
		return mod.getModId();
	}

	public String getName() {
		return mod.getName();
	}

	public void init(Configuration config) {
		// Nothing by default - override in a derived class to get
		// config options for the plugin
	}

	public abstract void apply();
	
	protected static void registerRecipesToIgnore(String[] list) {
		for(String s: list) {
			ItemStack stack = ItemStackHelper.getItemStack(s);
			if(stack != null)
				ItemScrapData.setRecipeIgnored(stack, true);
		}
	}
	
	protected static void registerScrapValues(String[] list, ScrapValue value) {
		for(String s: list)
			ItemScrapData.setValue(ItemStackHelper.getItemStack(s), value);
	}

}
