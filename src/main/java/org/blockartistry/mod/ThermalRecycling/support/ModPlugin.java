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
import org.blockartistry.mod.ThermalRecycling.data.RecipeHelper;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.recipe.BlastRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FluidTransposerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SawmillRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SmelterRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;
import org.blockartistry.mod.ThermalRecycling.util.PreferredItemStacks;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public abstract class ModPlugin {

	protected final SupportedMod mod;
	protected final String MOD_CONFIG_SECTION;

	protected final SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	protected final PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	protected final FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	protected final ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();
	protected final SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();
	protected final FluidTransposerRecipeBuilder fluid = new FluidTransposerRecipeBuilder();
	protected final BlastRecipeBuilder blast = new BlastRecipeBuilder();

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

	public String getVersion() {
		return mod.getArtifactVersion().getVersionString();
	}

	public boolean isAcceptibleVersion(final String version) {
		try {
			final VersionRange range = VersionRange.createFromVersionSpec(version);
			return range.containsVersion(mod.getArtifactVersion());
		} catch (InvalidVersionSpecificationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean preInit(final Configuration config) {
		return true;
	}

	public boolean initialize() {
		return true;
	}

	public boolean postInit() {
		return true;
	}

	protected String makeName(final String name) {

		if (name == null || name.length() < 2)
			return name;

		final String result;

		if (name.charAt(0) == '^')
			result = name.substring(1);
		else
			result = getModId() + ":" + name;

		return result;
	}

	protected void forEachSubject(final List<String> subjects, final Predicate<ItemStack> op) {
		for (final String s : subjects) {
			final String name = makeName(s);
			final Optional<ItemStack> stack = ItemStackHelper.getItemStack(name);
			if (stack.isPresent())
				op.apply(stack.get());
			else
				ModLog.warn("[%s] unknown item '%s'", mod.getName(), name);
		}
	}

	// NOTE THAT THESE REGISTER ROUTINES PREFIX THE STRING WITH
	// THE CURRENT MOD NAME! IF IT NEEDS TO BE ESCAPED PUT A
	// ^ CHARACTER AT THE FRONT!
	protected void registerRecipesToIgnore(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setRecipeIgnored(elem, true);
				return true;
			}
		});
	}

	protected void registerRecipesToIgnore(final List<String> list) {
		forEachSubject(list, new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setRecipeIgnored(elem, true);
				return true;
			}
		});
	}

	protected void registerRecipesToIgnoreForge(final String... oreList) {
		for (final String ore : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(ore)) {
				ItemRegistry.setRecipeIgnored(stack, true);
			}
		}
	}

	protected void registerRecipesToReveal(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setRecipeIgnored(elem, false);
				return true;
			}
		});
	}

	protected void registerScrapValues(final ScrapValue value, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setScrapValue(elem, value);
				return true;
			}
		});
	}

	protected void registerScrapValues(final ScrapValue value, final List<String> list) {
		forEachSubject(list, new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setScrapValue(elem, value);
				return true;
			}
		});
	}

	protected void registerScrubFromOutput(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setScrubbedFromOutput(elem, true);
				return true;
			}
		});
	}

	protected void registerScrubFromOutput(final List<String> list) {
		forEachSubject(list, new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setScrubbedFromOutput(elem, true);
				return true;
			}
		});
	}

	protected void registerNotScrubFromOutput(final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setScrubbedFromOutput(elem, false);
				return true;
			}
		});
	}

	protected void registerRecycleToWoodDust(final int inputQuantity, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				recycler.input(elem, inputQuantity).append(PreferredItemStacks.instance.dustWood).save();
				return true;
			}
		});
	}

	protected void registerRecycleToWoodDustForge(final int inputQuantity, final String... oreList) {
		for (final String ore : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(ore)) {
				recycler.input(stack, inputQuantity).append(PreferredItemStacks.instance.dustWood).save();
			}
		}
	}

	protected void registerCompostIngredient(final CompostIngredient ingredient, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setCompostIngredientValue(elem, ingredient);
				return true;
			}
		});
	}

	protected void registerCompostIngredient(final CompostIngredient ingredient, final List<String> list) {
		forEachSubject(list, new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				ItemRegistry.setCompostIngredientValue(elem, ingredient);
				return true;
			}
		});
	}

	protected void registerItemBlockedFromScrapping(final boolean status, final String... list) {
		forEachSubject(Arrays.asList(list), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack input) {
				ItemRegistry.setBlockedFromScrapping(input, status);
				return false;
			}
		});
	}

	protected void registerItemBlockedFromScrapping(final boolean status, final List<String> list) {
		forEachSubject(list, new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack input) {
				ItemRegistry.setBlockedFromScrapping(input, status);
				return false;
			}
		});
	}

	protected void registerPulverizeToDirt(final String name, final int rangeStart, final int rangeEnd) {
		forEachSubject(Collections.singletonList(name), new Predicate<ItemStack>() {
			@Override
			public boolean apply(final ItemStack elem) {
				pulverizer.appendSubtypeRange(elem, rangeStart, rangeEnd, 8).output(Blocks.dirt).save();
				return true;
			}
		});
	}

	protected void registerPulverizerToDirtForge(final String... oreList) {
		for (final String ore : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(ore))
				pulverizer.append(stack, 8).output(Blocks.dirt).save();
		}
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

	protected void registerExtractionRecipe(final ItemStack input, final ItemStackItem... entries) {
		final ItemStackWeightTable table = new ItemStackWeightTable();
		for (final ItemStackItem e : entries)
			table.add(e);
		ItemRegistry.setBlockedFromExtraction(input, false);
		RecipeHelper.put(input, table);
	}

	protected void registerScrapValuesForge(final ScrapValue value, final String... oreList) {
		for (final String s : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(s)) {
				ItemRegistry.setScrapValue(stack, value);
			}
		}
	}

	protected void makeRegistrations(final ItemDefinitions def) {
		registerRecipesToIgnore(def.ignore);
		registerScrapValues(ScrapValue.NONE, def.none);
		registerScrapValues(ScrapValue.POOR, def.poor);
		registerScrapValues(ScrapValue.STANDARD, def.standard);
		registerScrapValues(ScrapValue.SUPERIOR, def.superior);
		registerCompostIngredient(CompostIngredient.BROWN, def.brown);
		registerCompostIngredient(CompostIngredient.GREEN, def.green);
		registerItemBlockedFromScrapping(true, def.block);
		registerScrubFromOutput(def.scrub);
	}

	public static void preInitPlugins(final Configuration config) {

		final List<ModPlugin> plugins = SupportedMod.getPluginsForLoadedMods();

		for (final ModPlugin plugin : plugins) {

			try {
				plugin.preInit(config);
			} catch (Exception e) {
				ModLog.warn("Error pre-initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
	}
	

	public static void initializePlugins() {
		final List<ModPlugin> plugins = SupportedMod.getPluginsForLoadedMods();

		for (final ModPlugin plugin : plugins) {

			final String modName = String.format("[%s %s]", plugin.getName(), plugin.getVersion());

			try {
				ModLog.info("Loading recipes for %s", modName);
				plugin.initialize();
			} catch (Exception e) {
				ModLog.warn("Error initializing plugin %s", modName);
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
				final String modName = String.format("[%s %s]", plugin.getName(), plugin.getVersion());
				ModLog.warn("Error post-initializing plugin %s", modName);
				e.printStackTrace();
			}
		}
	}
}
