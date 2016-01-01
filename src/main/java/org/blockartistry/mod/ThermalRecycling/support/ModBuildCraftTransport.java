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
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.BEFlexibleRecipeAccessor;

import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import net.minecraft.item.ItemStack;

public final class ModBuildCraftTransport extends ModPlugin {

	static final String[] recipeIgnoreList = new String[] { "pipeFacade:*", "pipePlug", "pipeGate", "pipeWaterproof", };

	static final String[] scrapValuesNone = new String[] { "pipeFacade:*", "pipePlug", "pipeWaterproof", "pipeLens:*",
			"item.buildcraftPipe.pipeitemswood:*", "item.buildcraftPipe.pipeitemscobblestone:*",
			"item.buildcraftPipe.pipeitemsstone:*", "item.buildcraftPipe.pipeitemslapis:*",
			"item.buildcraftPipe.pipeitemssandstone:*", "item.buildcraftPipe.pipeitemsvoid:*",
			"item.buildcraftPipe.pipeitemsclay:*", "item.buildcraftPipe.pipefluidswood:*",
			"item.buildcraftPipe.pipefluidscobblestone:*", "item.buildcraftPipe.pipefluidsstone:*",
			"item.buildcraftPipe.pipefluidssandstone:*", "item.buildcraftPipe.pipefluidsvoid:*",
			"item.buildcraftPipe.pipefluidsclay:*", "item.buildcraftPipe.pipepowerwood:*",
			"item.buildcraftPipe.pipepowercobblestone:*", "item.buildcraftPipe.pipepowerstone:*",
			"item.buildcraftPipe.pipepowersandstone:*", "item.buildcraftPipe.pipestructurecobblestone:*", };

	static final String[] scrapValuesPoor = new String[] { "pipePowerAdapter", "pipeWire:*",
			"item.buildcraftPipe.pipeitemsobsidian:*", "item.buildcraftPipe.pipeitemsgold:*",
			"item.buildcraftPipe.pipepowergold:*", "item.buildcraftPipe.pipeitemsquartz:*",
			"item.buildcraftPipe.pipepowerquartz:*", "item.buildcraftPipe.pipefluidsquartz:*",
			"item.buildcraftPipe.pipeitemsiron:*", "item.buildcraftPipe.pipefluidsiron:*",
			"item.buildcraftPipe.pipepoweriron:*", "item.buildcraftPipe.pipefluidsgold:*",

			"pipeLens:16", "pipeLens:17", "pipeLens:18", "pipeLens:19", "pipeLens:20", "pipeLens:21", "pipeLens:22",
			"pipeLens:23", "pipeLens:24", "pipeLens:25", "pipeLens:26", "pipeLens:27", "pipeLens:28", "pipeLens:29",
			"pipeLens:30", "pipeLens:31", "pipeLens:33" };

	static final String[] scrapValuesStandard = new String[] { "item.buildcraftPipe.pipeitemsdiamond:*",
			"item.buildcraftPipe.pipeitemsemerald:*", "item.buildcraftPipe.pipepoweremerald:*",
			"item.buildcraftPipe.pipeitemsdaizuli:*", "item.buildcraftPipe.pipefluidsdiamond:*",
			"item.buildcraftPipe.pipepowerdiamond:*", "item.buildcraftPipe.pipefluidsemerald:*",
			"item.buildcraftPipe.pipeitemsemzuli:*", };

	static final String[] scrapValuesSuperior = new String[] { "pipeGate" };

	public ModBuildCraftTransport() {
		super(SupportedMod.BUILDCRAFT_TRANSPORT);

		RecipeDecomposition.registerAccessor(IFlexibleRecipeViewable.class, new BEFlexibleRecipeAccessor());
	}

	protected void registerBuildcraftRecipe(final IFlexibleRecipe<ItemStack> recipe) {

		if (!(recipe instanceof IFlexibleRecipeViewable))
			return;

		final IFlexibleRecipeViewable view = (IFlexibleRecipeViewable) recipe;

		final ItemStack output = (ItemStack) view.getOutput();

		// Dang facades...
		if (output == null || output.getItem() == null)
			return;

		if (ItemData.isRecipeIgnored(output))
			return;

		recycler.input(output).useRecipe(RecipeDecomposition.decompose(view)).save();
	}

	@Override
	public boolean initialize() {

		registerRecipesToIgnore(recipeIgnoreList);
		registerScrapValues(ScrapValue.NONE, scrapValuesNone);
		registerScrapValues(ScrapValue.POOR, scrapValuesPoor);
		registerScrapValues(ScrapValue.STANDARD, scrapValuesStandard);
		registerScrapValues(ScrapValue.SUPERIOR, scrapValuesSuperior);

		return true;
	}

	@Override
	public boolean postInit() {

		// Scan the recipes
		final Collection<IFlexibleRecipe<ItemStack>> recipes = BuildcraftRecipeRegistry.assemblyTable.getRecipes();

		for (final IFlexibleRecipe<ItemStack> r : recipes)
			registerBuildcraftRecipe(r);

		// BuildCraft 7.x busted the API - so much for backward compatibility
		// and interface contracts...
		/*
		 * final List<? extends IIntegrationRecipe> recipes1 =
		 * BuildcraftRecipeRegistry.integrationTable .getRecipes(); for (final
		 * IIntegrationRecipe r : recipes1) registerBuildcraftRecipe(r);
		 */

		return true;
	}
}
