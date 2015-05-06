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

import java.util.Collections;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
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
import org.blockartistry.mod.ThermalRecycling.util.function.Apply;

import scala.actors.threadpool.Arrays;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ModPlugin {

	protected final SupportedMod mod;
	protected final String MOD_CONFIG_SECTION;

	final SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	final PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	final FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	final ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();
	final SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();
	final FluidTransposerRecipeBuilder fluid = new FluidTransposerRecipeBuilder();
	final BlastRecipeBuilder blast = new BlastRecipeBuilder();

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

	protected String makeName(String name) {
		String result;

		if (name.startsWith("^"))
			result = name.substring(1);
		else
			result = getModId() + ":" + name;

		return result;
	}

	void forEachSubject(List<String> subjects, Apply<ItemStack> op) {
		for (String s : subjects) {
			String name = makeName(s);
			ItemStack stack = ItemStackHelper.getItemStack(name);
			if (stack != null)
				op.apply(stack);
			else
				ModLog.warn("[%s] unknown item '%s'", mod.getName(), name);
		}
	}

	// NOTE THAT THESE REGISTER ROUTINES PREFIX THE STRING WITH
	// THE CURRENT MOD NAME! IF IT NEEDS TO BE ESCAPED PUT A
	// ^ CHARACTER AT THE FRONT!
	@SuppressWarnings("unchecked")
	protected void registerRecipesToIgnore(String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				ItemScrapData.setRecipeIgnored(elem, true);
			}

		});
	}

	@SuppressWarnings("unchecked")
	protected void registerRecipesToReveal(String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				ItemScrapData.setRecipeIgnored(elem, false);
			}

		});
	}

	@SuppressWarnings("unchecked")
	protected void registerScrapValues(final ScrapValue value, String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				ItemScrapData.setValue(elem, value);
			}

		});
	}

	@SuppressWarnings("unchecked")
	protected void registerScrubFromOutput(String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				ItemScrapData.setScrubbedFromOutput(elem, true);
			}

		});
	}

	@SuppressWarnings("unchecked")
	protected void registerNotScrubFromOutput(String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				ItemScrapData.setScrubbedFromOutput(elem, false);
			}

		});
	}

	
	@SuppressWarnings("unchecked")
	protected void registerRecycleToWoodDust(final int inputQuantity, String... list) {
		forEachSubject(Arrays.asList(list), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				recycler.input(elem, inputQuantity).append(ItemStackHelper.dustWood)
						.save();
			}

		});
	}

	protected void registerPulverizeToDirt(String name,
			final int rangeStart, final int rangeEnd) {

		forEachSubject(Collections.singletonList(name), new Apply<ItemStack>() {

			@Override
			public void apply(ItemStack elem) {
				pulverizer.appendSubtypeRange(elem, rangeStart, rangeEnd, 8)
						.output(Blocks.dirt).save();
			}

		});
	}
}
