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
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import buildcraft.api.recipes.IIntegrationRecipe;
import net.minecraft.item.ItemStack;

public final class ModBuildCraftTransport extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "pipeFacade:*",
			"pipePlug:*", "pipeGate:*", "pipeWaterproof", };

	static final String[] scrapValuesNone = new String[] { "pipeFacade:*",
			"pipePlug:*", "pipeWaterproof", "pipeWire:*",
			"item.buildcraftPipe.pipeitemswood",
			"item.buildcraftPipe.pipeitemsemerald",
			"item.buildcraftPipe.pipeitemscobblestone",
			"item.buildcraftPipe.pipeitemsstone",
			"item.buildcraftPipe.pipeitemsquartz",
			"item.buildcraftPipe.pipeitemsiron",
			"item.buildcraftPipe.pipeitemsgold",
			"item.buildcraftPipe.pipeitemsdiamond",
			"item.buildcraftPipe.pipeitemsobsidian",
			"item.buildcraftPipe.pipeitemslapis",
			"item.buildcraftPipe.pipeitemsdaizuli",
			"item.buildcraftPipe.pipeitemssandstone",
			"item.buildcraftPipe.pipeitemsvoid",
			"item.buildcraftPipe.pipeitemsemzuli",
			"item.buildcraftPipe.pipeitemsstripes",
			"item.buildcraftPipe.pipeitemsclay",
			"item.buildcraftPipe.pipefluidswood",
			"item.buildcraftPipe.pipefluidscobblestone",
			"item.buildcraftPipe.pipefluidsstone",
			"item.buildcraftPipe.pipefluidsquartz",
			"item.buildcraftPipe.pipefluidsiron",
			"item.buildcraftPipe.pipefluidsgold",
			"item.buildcraftPipe.pipefluidsemerald",
			"item.buildcraftPipe.pipefluidsdiamond",
			"item.buildcraftPipe.pipefluidssandstone",
			"item.buildcraftPipe.pipefluidsvoid",
			"item.buildcraftPipe.pipepowerwood",
			"item.buildcraftPipe.pipepowercobblestone",
			"item.buildcraftPipe.pipepowerstone",
			"item.buildcraftPipe.pipepowerquartz",
			"item.buildcraftPipe.pipepoweriron",
			"item.buildcraftPipe.pipepowergold",
			"item.buildcraftPipe.pipepowerdiamond",
			"item.buildcraftPipe.pipepoweremerald",
			"item.buildcraftPipe.pipepowersandstone",
			"item.buildcraftPipe.pipestructurecobblestone", };

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] { "pipeGate:*" };

	public ModBuildCraftTransport() {
		super(SupportedMod.BUILDCRAFT_TRANSPORT);
	}

	protected void registerBuildcraftRecipe(final IFlexibleRecipe<ItemStack> recipe) {

		if (!(recipe instanceof IFlexibleRecipeViewable))
			return;

		final IFlexibleRecipeViewable view = (IFlexibleRecipeViewable) recipe;

		final ItemStack output = (ItemStack) view.getOutput();

		// Dang facades...
		if (output == null || output.getItem() == null)
			return;

		if (ItemScrapData.isRecipeIgnored(output))
			return;

		recycler.input(output)
			.useRecipe(RecipeDecomposition.decomposeBuildCraft(output, view.getInputs()))
			.save();
	}

	@Override
	public void apply() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		// Scan the recipes
		final Collection<IFlexibleRecipe<ItemStack>> recipes = BuildcraftRecipeRegistry.assemblyTable
				.getRecipes();

		for (final IFlexibleRecipe<ItemStack> r : recipes)
			registerBuildcraftRecipe(r);

		final List<? extends IIntegrationRecipe> recipes1 = BuildcraftRecipeRegistry.integrationTable
				.getRecipes();
		for (final IIntegrationRecipe r : recipes1)
			registerBuildcraftRecipe(r);
	}
}
