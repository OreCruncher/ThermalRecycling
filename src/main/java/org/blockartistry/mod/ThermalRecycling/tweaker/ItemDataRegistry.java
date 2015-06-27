/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.ThermalRecycling.tweaker;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.ItemDataRegistry")
public final class ItemDataRegistry {

	@ZenMethod
	public static void set(final IItemStack stack, final int compostValue, final int scrapValue, final boolean ignoreRecipe, final boolean scrubFromOutput, final boolean blockFromScrapping) {
		
		if(!MineTweakerSupport.checkArgument(compostValue >=0 && compostValue < 4, "compostValue must be in range 0-3"))
			return;
		
		if(!MineTweakerSupport.checkArgument(scrapValue >= 0 && scrapValue < 5, "scrapValue must be in range 0-4"))
			return;
		
		final CompostIngredient cv = CompostIngredient.values()[compostValue];
		final ScrapValue sv = ScrapValue.values()[scrapValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		final ItemData data = ItemData.get(item);
		data.setValue(sv);
		data.setCompostIngredientValue(cv);
		data.setIgnoreRecipe(ignoreRecipe);
		data.setScrubFromOutput(scrubFromOutput);
		data.setBlockedFromScrapping(blockFromScrapping);
		ItemData.put(item, data);
	}
	
	@ZenMethod
	public static void setCompostIngredient(final IItemStack stack, final int compostValue) {
		
		if(!MineTweakerSupport.checkArgument(compostValue >=0 && compostValue < 4, "compostValue must be in range 0-3"))
			return;

		final CompostIngredient cv = CompostIngredient.values()[compostValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemData.setCompostIngredientValue(item, cv);
	}
	
	@ZenMethod
	public static void setScrapValue(final IItemStack stack, final int scrapValue) {
		
		if(!MineTweakerSupport.checkArgument(scrapValue >= 0 && scrapValue < 5, "scrapValue must be in range 0-4"))
			return;

		final ScrapValue sv = ScrapValue.values()[scrapValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemData.setValue(item, sv);
	}
	
	@ZenMethod
	public static void setIgnoreRecipe(final IItemStack stack, final boolean ignoreRecipe) {
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemData.setRecipeIgnored(item, ignoreRecipe);
	}
	
	@ZenMethod
	public static void setScrubFromOutput(final IItemStack stack, final boolean scrubFromOutput) {
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemData.setScrubbedFromOutput(item, scrubFromOutput);
	}
	
	@ZenMethod
	public static void setBlockedFromScrapping(final IItemStack stack, final boolean blockedFromScrapping) {
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemData.setBlockedFromScrapping(item, blockedFromScrapping);
	}
}
