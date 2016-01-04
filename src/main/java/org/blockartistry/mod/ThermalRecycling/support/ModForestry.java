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

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ForestryCarpenterRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ForestryFabricatorRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.ForestryRecipeAccessor;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Optional;

import forestry.api.storage.StorageManager;

public final class ModForestry extends ModPlugin {

	protected final boolean isForestry4x;
	protected final boolean isForestry41x;

	public ModForestry() {
		super(SupportedMod.FORESTRY);

		this.isForestry4x = isAcceptibleVersion("[4,)");
		this.isForestry41x = isAcceptibleVersion("[4.1,)");

		if (this.isForestry41x) {
			RecipeDecomposition.registerAccessor("forestry.api.recipes.ICarpenterRecipe",
					new ForestryCarpenterRecipeAccessor());
			RecipeDecomposition.registerAccessor("forestry.api.recipes.IFabricatorRecipe",
					new ForestryFabricatorRecipeAccessor());
		} else {
			RecipeDecomposition.registerAccessor("forestry.core.utils.ShapedRecipeCustom",
					new ForestryRecipeAccessor());
		}
	}

	@Deprecated
	protected void registerForestryRecipes(final Map<Object[], Object[]> entry) {
		for (final Entry<Object[], Object[]> e : entry.entrySet()) {
			if (e.getValue().length == 1 && e.getValue()[0] instanceof ItemStack) {
				final ItemStack stack = (ItemStack) e.getValue()[0];
				if (!ItemRegistry.isRecipeIgnored(stack))
					recycler.input(stack).useRecipe(RecipeDecomposition.decomposeForestry(stack, e.getKey())).save();
			}
		}
	}

	protected <T> void register(final Collection<T> recipes) {
		for (final T r : recipes) {
			final ItemStack input = RecipeDecomposition.getInput(r);
			if (input == null || ItemRegistry.isRecipeIgnored(input))
				continue;
			// ModLog.info("FORESTRY: %s", ItemStackHelper.resolveName(input));
			recycler.input(input).useRecipe(RecipeDecomposition.decompose(r)).save();
		}
	}

	@Override
	public boolean preInit(final Configuration config) {

		try {
			StorageManager.crateRegistry.registerCrate(ScrappingTables.debris, "cratedDebris");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.poorScrap, "cratedPoorScrap");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.poorScrapBox, "cratedPoorScrapBox");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.standardScrap, "cratedStandardScrap");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.standardScrapBox, "cratedStandardScrapBox");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.superiorScrap, "cratedSuperiorScrap");
			StorageManager.crateRegistry.registerCrate(ScrappingTables.superiorScrapBox, "cratedSuperiorScrapBox");
		} catch (Throwable t) {
			ModLog.warn("Unable to register crates with Forestry, skipping...");
			ModLog.warn("(Is Forestry up to date?)");
		}

		return true;
	}
	
	@Override
	public void loadDefinitions() {
		String defName = getModId();
		if(!this.isForestry4x)
			defName += "3x";
		final ItemDefinitions definitions = ItemDefinitions.load(defName);
		makeRegistrations(definitions);
	}

	@Override
	public boolean initialize() {

		// Scan the item registry looking for "crated" things - we want
		// to blacklist recipes and set scrap value to POOR. Should
		// get something for the effort of making these crates.
		for (final Object o : Item.itemRegistry.getKeys()) {
			final String itemName = (String) o;
			if (itemName.startsWith("Forestry:crated") || itemName.startsWith("recycling:crated")) {
				final Optional<ItemStack> stack = ItemStackHelper.getItemStack(itemName);
				final ItemData data = ItemRegistry.get(stack.get());
				data.ignoreRecipe = true;
				data.scrubFromOutput = true;
				data.value = ScrapValue.POOR;
				ItemRegistry.set(data);
			}
		}

		// Machine casings
		sawmill.append("Forestry:impregnatedCasing").output(Blocks.planks, 32).save();
		pulverizer.append("Forestry:sturdyMachine").output("ingotBronze", 8).save();
		pulverizer.append("Forestry:hardenedMachine").output("ingotBronze", 8).secondaryOutput(Items.diamond, 4).save();

		// Survivalist tools
		pulverizer.append("Forestry:bronzePickaxe", "Forestry:kitPickaxe").output("dustBronze", 3).save();
		pulverizer.append("Forestry:bronzeShovel", "Forestry:kitShovel").output("dustBronze").save();

		// Misc
		pulverizer.setEnergy(200).append("Forestry:canEmpty").output("nuggetTin", 2).secondaryOutput("nuggetTin")
				.chance(10).save();

		// Glass. Does not apply to Forestry 4x since it was removed.
		if (!isForestry4x)
			pulverizer.setEnergy(3200).appendSubtypeRange("Forestry:stained", 0, 15).output(Blocks.sand).save();

		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean postInit() {

		// Dig into the Forestry crafting data and extract additional recipes
		if (!this.isForestry41x) {
			registerForestryRecipes(forestry.api.recipes.RecipeManagers.carpenterManager.getRecipes());
			registerForestryRecipes(forestry.api.recipes.RecipeManagers.fabricatorManager.getRecipes());
		} else {
			register(forestry.api.recipes.RecipeManagers.carpenterManager.recipes());
			register(forestry.api.recipes.RecipeManagers.fabricatorManager.recipes());
		}
		return true;
	}
}