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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ModBuildCraft extends ModPlugin {

	public ModBuildCraft() {
		super(SupportedMod.BUILDCRAFT);
	}

	protected void registerBuildcraftRecipe(IFlexibleRecipe<ItemStack> recipe) {
		
		if(!(recipe instanceof IFlexibleRecipeViewable))
			return;
		
		IFlexibleRecipeViewable view = (IFlexibleRecipeViewable)recipe;
		
		ItemStack output = (ItemStack)view.getOutput();
		
		// Dang facades...
		if(output == null || output.getItem() == null)
			return;
		
		if(ItemScrapData.isRecipeIgnored(output))
			return;
		
		recycler.useRecipe(new RecipeDecomposition(false, output, view.getInputs())).save();
	}

	@Override
	public void apply() {

		registerRecipesToIgnore("BuildCraft|Transport:pipeFacade:*", "BuildCraft|Transport:pipePlug:*", "BuildCraft|Transport:pipeGate:*");
		registerScrapValues(ScrapValue.SUPERIOR, "BuildCraft|Transport:pipeGate:*");
		
		// Scan the recipes
		Collection<IFlexibleRecipe<ItemStack>> recipes = BuildcraftRecipeRegistry.assemblyTable.getRecipes();
		
		for(IFlexibleRecipe<ItemStack> r: recipes)
			registerBuildcraftRecipe(r);
		
		List<? extends IIntegrationRecipe> recipes1 = BuildcraftRecipeRegistry.integrationTable.getRecipes();
		for(IIntegrationRecipe r: recipes1)
			registerBuildcraftRecipe((IFlexibleRecipe<ItemStack>)r);
		
		
		// Gears - metalic gears handled via Thermal Expansion
		sawmill.append("BuildCraft|Core:woodenGearItem").output("dustWood", 4)
				.save();

		// Misc block machines
		sawmill.append("BuildCraft|Builders:libraryBlock")
				.output(Blocks.planks, 6).secondaryOutput(Items.book, 3).save();

		sawmill.append("BuildCraft|Builders:builderBlock")
				.output(Blocks.planks, 12).secondaryOutput(Items.diamond, 8)
				.save();

		sawmill.append("BuildCraft|Builders:architectBlock")
				.output(Blocks.planks, 4).secondaryOutput(Items.diamond, 8)
				.save();

		// Redstone Engine
		sawmill.append("BuildCraft|Energy:engineBlock")
				.output(Blocks.planks, 6).secondaryOutput("ingotIron").save();

		// Stirling Engine
		pulverizer.append("BuildCraft|Energy:engineBlock:1")
				.output(Blocks.cobblestone, 15).secondaryOutput("ingotIron")
				.save();

		sawmill.append("BuildCraft|Factory:autoWorkbenchBlock")
				.output(Blocks.planks, 4).secondaryOutput("dustWood", 16)
				.save();

		// Quarry
		pulverizer.append("BuildCraft|Factory:machineBlock")
				.output(Items.diamond, 11).secondaryOutput("ingotGold", 8)
				.save();
	}
}
