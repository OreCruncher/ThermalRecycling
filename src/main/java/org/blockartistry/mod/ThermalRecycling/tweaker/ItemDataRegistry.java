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
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.ItemDataRegistry")
public final class ItemDataRegistry {

	@ZenMethod
	public static void set(final IItemStack stack, final int compostValue, final int scrapValue, final boolean ignoreRecipe, final boolean scrubFromOutput, final boolean blockFromScrapping) {
		
		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		if(!MineTweakerUtil.checkArgument(compostValue >=0 && compostValue < 3, "compostValue must be in range 0-2"))
			return;
		
		if(!MineTweakerUtil.checkArgument(scrapValue >= 0 && scrapValue < 4, "scrapValue must be in range 0-3"))
			return;
		
		final CompostIngredient cv = CompostIngredient.values()[compostValue];
		final ScrapValue sv = ScrapValue.values()[scrapValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		final ItemData data = ItemRegistry.get(item);
		data.value = sv;
		data.compostValue = cv;
		data.ignoreRecipe = ignoreRecipe;
		data.scrubFromOutput = scrubFromOutput;
		data.isBlockedFromScrapping = blockFromScrapping;
		ItemRegistry.set(data);
	}
	
	@ZenMethod
	public static void setCompostIngredient(final IItemStack stack, final int compostValue) {
		
		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		if(!MineTweakerUtil.checkArgument(compostValue >=0 && compostValue < 2, "compostValue must be in range 0-2"))
			return;

		final CompostIngredient cv = CompostIngredient.values()[compostValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemRegistry.setCompostIngredientValue(item, cv);
	}
	
	@ZenMethod
	public static void setScrapValue(final IItemStack stack, final int scrapValue) {
		
		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		if(!MineTweakerUtil.checkArgument(scrapValue >= 0 && scrapValue < 4, "scrapValue must be in range 0-3"))
			return;

		final ScrapValue sv = ScrapValue.values()[scrapValue];
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemRegistry.setScrapValue(item, sv);
	}
	
	@ZenMethod
	public static void setIgnoreRecipe(final IItemStack stack, final boolean ignoreRecipe) {

		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemRegistry.setRecipeIgnored(item, ignoreRecipe);
	}
	
	@ZenMethod
	public static void setScrubFromOutput(final IItemStack stack, final boolean scrubFromOutput) {

		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemRegistry.setScrubbedFromOutput(item, scrubFromOutput);
	}
	
	@ZenMethod
	public static void setBlockedFromScrapping(final IItemStack stack, final boolean blockedFromScrapping) {

		if(!MineTweakerUtil.checkNotNull(stack, "stack cannot be null"))
			return;
		
		final ItemStack item = MineTweakerMC.getItemStack(stack);
		ItemRegistry.setBlockedFromScrapping(item, blockedFromScrapping);
	}
}
