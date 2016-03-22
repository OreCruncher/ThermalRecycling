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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.blocks.PileOfRubble;
import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.RecipeHelper;
import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.ItemDefinitions.Handler;
import org.blockartistry.mod.ThermalRecycling.support.ItemDefinitions.RecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.ItemDefinitions.RubbleDrop;
import org.blockartistry.mod.ThermalRecycling.support.ItemDefinitions.Sawdust;
import org.blockartistry.mod.ThermalRecycling.support.recipe.BlastRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FluidTransposerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.FurnaceRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.PulverizerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SawmillRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.SmelterRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.support.recipe.ThermalRecyclerRecipeBuilder;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;
import org.blockartistry.mod.ThermalRecycling.util.PreferredItemStacks;

import com.google.common.base.Optional;
import cpw.mods.fml.common.versioning.InvalidVersionSpecificationException;
import cpw.mods.fml.common.versioning.VersionRange;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public abstract class ModPlugin {

	protected final SupportedMod mod;
	protected final String MOD_CONFIG_SECTION;
	protected final String name;

	protected final SawmillRecipeBuilder sawmill = new SawmillRecipeBuilder();
	protected final PulverizerRecipeBuilder pulverizer = new PulverizerRecipeBuilder();
	protected final FurnaceRecipeBuilder furnace = new FurnaceRecipeBuilder();
	protected final ThermalRecyclerRecipeBuilder recycler = new ThermalRecyclerRecipeBuilder();
	protected final SmelterRecipeBuilder smelter = new SmelterRecipeBuilder();
	protected final FluidTransposerRecipeBuilder fluid = new FluidTransposerRecipeBuilder();
	protected final BlastRecipeBuilder blast = new BlastRecipeBuilder();

	public ModPlugin(final SupportedMod m) {
		this.mod = m;
		this.MOD_CONFIG_SECTION = "recycle.recipe.control." + mod.getModId();
		if (m == SupportedMod.VANILLA)
			this.name = this.mod.getName() + " " + MinecraftForge.MC_VERSION;
		else
			this.name = this.mod.getName() + " " + this.mod.getArtifactVersion().getVersionString();
	}

	public String getModId() {
		return this.mod.getModId();
	}

	public String getName() {
		return this.name;
	}

	public String getVersion() {
		return this.mod.getArtifactVersion().getVersionString();
	}

	public boolean isAcceptibleVersion(final String version) {
		try {
			final VersionRange range = VersionRange.createFromVersionSpec(version);
			return range.containsVersion(this.mod.getArtifactVersion());
		} catch (InvalidVersionSpecificationException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean preInit(final Configuration config) {
		return true;
	}

	public void loadDefinitions() {
		final ItemDefinitions definitions = ItemDefinitions.load(getModId());
		makeRegistrations(definitions);
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

	protected ItemStack resolveItemStack(final String subject) {
		final String name = makeName(subject);
		final Optional<ItemStack> stack = ItemStackHelper.getItemStack(name);
		if (stack.isPresent())
			return stack.get();
		ModLog.warn("[%s] unknown item '%s'", this.mod.getName(), name);
		return null;
	}

	// NOTE THAT THESE REGISTER ROUTINES PREFIX THE STRING WITH
	// THE CURRENT MOD NAME! IF IT NEEDS TO BE ESCAPED PUT A
	// ^ CHARACTER AT THE FRONT!
	protected void registerRecipesToIgnore(final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setRecipeIgnored(stack, true);
		}
	}

	protected void registerRecipesToIgnoreForge(final String... oreList) {
		for (final String ore : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(ore)) {
				ItemRegistry.setRecipeIgnored(stack, true);
			}
		}
	}

	protected void registerRecipesToReveal(final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setRecipeIgnored(stack, false);
		}
	}

	protected void registerScrapValues(final ScrapValue value, final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setScrapValue(stack, value);
		}
	}

	protected void registerScrubFromOutput(final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setScrubbedFromOutput(stack, true);
		}
	}

	protected void registerRecycleToWoodDust(final int inputQuantity, final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				this.recycler.input(stack, inputQuantity).append(PreferredItemStacks.instance.dustWood).save();
		}
	}

	protected void registerRecycleToWoodDust(final List<Sawdust> list) {
		for (final Sawdust dust : list)
			registerRecycleToWoodDust(dust.count, dust.items);
	}

	protected void registerRecycleToWoodDustForge(final int inputQuantity, final String... oreList) {
		for (final String ore : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(ore)) {
				this.recycler.input(stack, inputQuantity).append(PreferredItemStacks.instance.dustWood).save();
			}
		}
	}

	protected void registerCompostIngredient(final CompostIngredient ingredient, final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setCompostIngredientValue(stack, ingredient);
		}
	}

	protected void registerBlockFromVacuum(final boolean flag, final String... list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack("^" + subject)) != null)
				ItemRegistry.setBlockedFromVacuum(stack, flag);
		}
	}

	protected void registerItemBlockedFromScrapping(final boolean status, final String... list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setBlockedFromScrapping(stack, status);
		}
	}

	protected void registerItemBlockedFromScrapping(final boolean status, final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setBlockedFromScrapping(stack, status);
		}
	}

	protected void registerPulverizeToDirt(final List<String> list) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				this.pulverizer.append(stack, 8).output(Blocks.dirt).save();
		}
	}

	protected void registerPileOfRubbleDrop(final List<RubbleDrop> drops) {
		ItemStack stack = null;
		for (final RubbleDrop drop : drops) {
			if ((stack = resolveItemStack(drop.item)) != null)
				PileOfRubble.addRubbleDrop(stack, drop.min, drop.max, drop.weight);
		}
	}

	protected void registerExtractionRecipe(final ItemStack input, final ItemStackItem... entries) {
		final ItemStackWeightTable table = new ItemStackWeightTable();
		for (final ItemStackItem e : entries)
			table.add(e);
		ItemRegistry.setBlockedFromExtraction(input, false);
		RecipeHelper.put(input, table);
	}

	protected void registerBlockedFromExtraction(final List<String> list, final boolean flag) {
		ItemStack stack = null;
		for (final String subject : list) {
			if ((stack = resolveItemStack(subject)) != null)
				ItemRegistry.setBlockedFromExtraction(stack, flag);
		}
	}

	protected void registerScrapValuesForge(final ScrapValue value, final String... oreList) {
		for (final String s : oreList) {
			for (final ItemStack stack : OreDictionaryHelper.getOres(s)) {
				ItemRegistry.setScrapValue(stack, value);
			}
		}
	}

	protected void registerAccessors(final List<RecipeAccessor> list) {
		for (final RecipeAccessor accessor : list)
			RecipeDecomposition.registerAccessor(accessor.recipe, accessor.accessor);
	}

	protected void registerHandlers(final List<Handler> list) {
		for (final Handler handler : list) {
			try {
				final Class<?> clazz = Class.forName(handler.handlerClass);
				if (clazz != null) {
					final ScrapHandler sh = (ScrapHandler) clazz.newInstance();
					ItemStack stack = null;
					for (final String item : handler.items) {
						if ((stack = resolveItemStack(item)) != null)
							ScrapHandler.registerHandler(stack, sh);
					}
				}

			} catch (final Throwable t) {
				t.printStackTrace();
			}
		}
	}

	protected void makeRegistrations(final ItemDefinitions def) {
		registerRecipesToIgnore(def.ignore);
		registerRecipesToReveal(def.reveal);
		registerScrapValues(ScrapValue.NONE, def.none);
		registerScrapValues(ScrapValue.POOR, def.poor);
		registerScrapValues(ScrapValue.STANDARD, def.standard);
		registerScrapValues(ScrapValue.SUPERIOR, def.superior);
		registerCompostIngredient(CompostIngredient.BROWN, def.brown);
		registerCompostIngredient(CompostIngredient.GREEN, def.green);
		registerItemBlockedFromScrapping(true, def.block);
		registerScrubFromOutput(def.scrub);
		registerPileOfRubbleDrop(def.rubble);
		registerPulverizeToDirt(def.toDirt);
		registerRecycleToWoodDust(def.toSawdust);
		registerBlockedFromExtraction(def.extract, false);
		registerAccessors(def.accessors);
		registerHandlers(def.handlers);
	}

	private static List<ModPlugin> plugins = null;

	public static void preInitPlugins(final Configuration config) {

		plugins = SupportedMod.getPluginsForLoadedMods();

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
		for (final ModPlugin plugin : plugins) {
			try {
				ModLog.info("Loading recipes for [%s]", plugin.getName());
				plugin.loadDefinitions();
				plugin.initialize();
			} catch (Exception e) {
				ModLog.warn("Error initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
	}

	public static void postInitPlugins() {
		for (final ModPlugin plugin : plugins) {
			try {
				plugin.postInit();
			} catch (Exception e) {
				ModLog.warn("Error post-initializing plugin [%s]", plugin.getName());
				e.printStackTrace();
			}
		}
		plugins = null;
	}
}
