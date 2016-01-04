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
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.support.recipe.RecipeDecomposition;
import org.blockartistry.mod.ThermalRecycling.support.recipe.accessor.IC2MachineRecipeAdaptor;
import ic2.api.recipe.IMachineRecipeManager;
import ic2.api.recipe.IRecipeInput;
import ic2.api.recipe.RecipeOutput;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;

public class ModIndustrialCraft extends ModPlugin {

	public ModIndustrialCraft() {
		super(SupportedMod.INDUSTRIAL_CRAFT);
	}

	private void processRecipes(final IMachineRecipeManager manager) {
		for (final Entry<IRecipeInput, RecipeOutput> e : manager.getRecipes().entrySet()) {
			final IC2MachineRecipeAdaptor adaptor = new IC2MachineRecipeAdaptor(e);
			final ItemStack input = RecipeDecomposition.getInput(adaptor);
			if (!ItemRegistry.isRecipeIgnored(input)) {
				final List<ItemStack> output = RecipeDecomposition.decompose(adaptor);
				recycler.input(input).useRecipe(output).save();
			}
		}
	}

	@Override
	public boolean initialize() {

		processRecipes(Recipes.metalformerRolling);
		processRecipes(Recipes.metalformerExtruding);
		processRecipes(Recipes.metalformerCutting);
		processRecipes(Recipes.compressor);

		return true;
	}
}
