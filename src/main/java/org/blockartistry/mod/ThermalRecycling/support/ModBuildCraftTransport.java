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
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.BEFlexibleRecipeAccessor;

import buildcraft.api.recipes.BuildcraftRecipeRegistry;
import buildcraft.api.recipes.IFlexibleRecipe;
import buildcraft.api.recipes.IFlexibleRecipeViewable;
import net.minecraft.item.ItemStack;

public final class ModBuildCraftTransport extends ModPlugin {

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

		if (ItemRegistry.isRecipeIgnored(output))
			return;

		recycler.input(output).useRecipe(RecipeDecomposition.decompose(view)).save();
	}

	@Override
	public boolean initialize() {

		final ItemDefinitions definitions = ItemDefinitions.load(getModId());
		makeRegistrations(definitions);

		return true;
	}

	@Override
	public boolean postInit() {

		// Scan the recipes
		final Collection<IFlexibleRecipe<ItemStack>> recipes = BuildcraftRecipeRegistry.assemblyTable.getRecipes();

		for (final IFlexibleRecipe<ItemStack> r : recipes)
			registerBuildcraftRecipe(r);

		// TODO: 7.x integration table

		return true;
	}
}
