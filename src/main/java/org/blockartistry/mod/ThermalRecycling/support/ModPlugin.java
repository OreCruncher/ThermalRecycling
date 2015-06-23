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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.blocks.PileOfRubble;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
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

import cofh.api.core.IInitializer;

import com.google.common.base.Predicate;

import cpw.mods.fml.common.Optional;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

@Optional.Interface(iface = "cofh.api.core.IInitializer", modid = "CoFHCore", striprefs = true)
public abstract class ModPlugin implements IInitializer {

	protected final SupportedMod mod;
	protected final String MOD_CONFIG_SECTION;

	final SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	final PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	final FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	final ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();
	final SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();
	final FluidTransposerRecipeBuilder fluid = new FluidTransposerRecipeBuilder();
	final BlastRecipeBuilder blast = new BlastRecipeBuilder();

	public ModPlugin(final SupportedMod m) {
		mod = m;
		MOD_CONFIG_SECTION = "recycle.recipe.control." + mod.getModId();
	}

	public String getModId() {
		return mod.getModId();
	}

	public String getName() {
		return mod.getName();
	}

	@Override
	public boolean preInit() {
		return true;
	}

	@Override
	public boolean initialize() {
		return true;
	}

	@Override
	public boolean postInit() {
		return true;
	}

	protected String makeName(final String name) {
		
		if(name == null || name.length() < 2)
			return name;
		
		final String result;

		if (name.charAt(0) == '^')
			result = name.substring(1);
		else
			result = getModId() + ":" + name;

		return result;
	}

	protected void forEachSubject(final List<String> subjects, final Predicate<ItemStack> op) {
		
		Apply.forEach(subjects, new Predicate<String>() {
			
			public boolean apply(final String s) {
				final String name = makeName(s);
				final ItemStack stack = ItemStackHelper.getItemStack(name);
				if (stack != null)
					op.apply(stack);
				else
					ModLog.warn("[%s] unknown item '%s'", mod.getName(), name);
				return true;
			}
			
		});
	}

	// NOTE THAT THESE REGISTER ROUTINES PREFIX THE STRING WITH
	// THE CURRENT MOD NAME! IF IT NEEDS TO BE ESCAPED PUT A
	// ^ CHARACTER AT THE FRONT!
	protected void registerRecipesToIgnore(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setRecipeIgnored(elem, true);
				return true;
			}

		});
	}

	protected void registerRecipesToReveal(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setRecipeIgnored(elem, false);
				return true;
			}

		});
	}

	protected void registerScrapValues(final ScrapValue value, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setValue(elem, value);
				return true;
			}

		});
	}

	protected void registerScrubFromOutput(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setScrubbedFromOutput(elem, true);
				return true;
			}

		});
	}

	protected void registerNotScrubFromOutput(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setScrubbedFromOutput(elem, false);
				return true;
			}

		});
	}

	protected void registerRecycleToWoodDust(final int inputQuantity,
			final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				recycler.input(elem, inputQuantity)
						.append(ItemStackHelper.dustWood).save();
				return true;
			}

		});
	}

	protected void registerCompostIngredient(
			final CompostIngredient ingredient, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				ItemData.setCompostIngredientValue(elem, ingredient);
				return true;
			}
		});
	}
	
	protected void registerItemBlockedFromScrapping(final boolean status, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack input) {
				ItemData.setBlockedFromScrapping(input, status);
				return false;
			}
			
		});
	}

	protected void registerPulverizeToDirt(final String name, final int rangeStart,
			final int rangeEnd) {

		forEachSubject(Collections.singletonList(name), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack elem) {
				pulverizer.appendSubtypeRange(elem, rangeStart, rangeEnd, 8)
						.output(Blocks.dirt).save();
				return true;
			}

		});
	}
	
	protected void registerPileOfRubbleDrop(final int min, final int max, final int weight, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {

			@Override
			public boolean apply(final ItemStack input) {
				PileOfRubble.addRubbleDrop(input, min, max, weight);
				return false;
			}
		});
	}
	
	
	public static void preInitPlugins() {
		
		final List<ModPlugin> plugins = SupportedMod.getPluginsForLoadedMods();

		for (final ModPlugin plugin : plugins) {

			try {
				plugin.preInit();
			} catch (Exception e) {
				ModLog.warn("Error pre-initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
	}
	
	public static void initializePlugins() {
		final List<ModPlugin> plugins = SupportedMod.getPluginsForLoadedMods();

		for (final ModPlugin plugin : plugins) {

			try {
				ModLog.info("Loading recipes for [%s]", plugin.getName());
				plugin.initialize();
			} catch (Exception e) {
				ModLog.warn("Error initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
	}
	
	public static void postInitPlugins() {
		final List<ModPlugin> plugins = SupportedMod.getPluginsForLoadedMods();

		for (final ModPlugin plugin : plugins) {

			try {
				plugin.postInit();
			} catch (Exception e) {
				ModLog.warn("Error post-initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
	}
}
