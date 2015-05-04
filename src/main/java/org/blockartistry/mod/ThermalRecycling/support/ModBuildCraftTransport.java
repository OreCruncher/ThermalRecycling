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

public class ModBuildCraftTransport extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] {
			"BuildCraft|Transport:pipeFacade:*",
			"BuildCraft|Transport:pipePlug:*",
			"BuildCraft|Transport:pipeGate:*" };

	static final String[] scrapValuesNone = new String[] {
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemswood",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsemerald",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemscobblestone",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsquartz",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsiron",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsgold",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsdiamond",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsobsidian",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemslapis",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsdaizuli",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemssandstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsvoid",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsemzuli",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsstripes",
			"BuildCraft|Transport:item.buildcraftPipe.pipeitemsclay",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidswood",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidscobblestone",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsquartz",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsiron",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsgold",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsemerald",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsdiamond",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidssandstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipefluidsvoid",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowerwood",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowercobblestone",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowerstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowerquartz",
			"BuildCraft|Transport:item.buildcraftPipe.pipepoweriron",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowergold",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowerdiamond",
			"BuildCraft|Transport:item.buildcraftPipe.pipepoweremerald",
			"BuildCraft|Transport:item.buildcraftPipe.pipepowersandstone",
			"BuildCraft|Transport:item.buildcraftPipe.pipestructurecobblestone", };

	static final String[] scrapValuesPoor = new String[] {};

	static final String[] scrapValuesStandard = new String[] {};

	static final String[] scrapValuesSuperior = new String[] { "BuildCraft|Transport:pipeGate:*" };

	public ModBuildCraftTransport() {
		super(SupportedMod.BUILDCRAFT_TRANSPORT);
	}

	protected void registerBuildcraftRecipe(IFlexibleRecipe<ItemStack> recipe) {

		if (!(recipe instanceof IFlexibleRecipeViewable))
			return;

		IFlexibleRecipeViewable view = (IFlexibleRecipeViewable) recipe;

		ItemStack output = (ItemStack) view.getOutput();

		// Dang facades...
		if (output == null || output.getItem() == null)
			return;

		if (ItemScrapData.isRecipeIgnored(output))
			return;

		recycler.useRecipe(
				new RecipeDecomposition(false, output, view.getInputs()))
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
		Collection<IFlexibleRecipe<ItemStack>> recipes = BuildcraftRecipeRegistry.assemblyTable
				.getRecipes();

		for (IFlexibleRecipe<ItemStack> r : recipes)
			registerBuildcraftRecipe(r);

		List<? extends IIntegrationRecipe> recipes1 = BuildcraftRecipeRegistry.integrationTable
				.getRecipes();
		for (IIntegrationRecipe r : recipes1)
			registerBuildcraftRecipe(r);
	}
}
