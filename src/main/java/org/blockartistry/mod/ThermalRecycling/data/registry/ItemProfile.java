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

package org.blockartistry.mod.ThermalRecycling.data.registry;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

abstract class ItemProfile {
	
	final Item item;
	final ItemData settings;
	RecipeData recipe = RecipeData.EPHEMERAL;
	ExtractionData extract = ExtractionData.EPHEMERAL;

	public ItemProfile(final Item item) {
		this.item = item;
		this.settings = new ItemData(item);
		
		// If the item is from Vanilla, or from a mod that is not
		// whitelisted set the scrap value to NONE.
		if(ItemStackHelper.isVanilla(item) || !SupportedMod.isModWhitelisted(item))
			this.settings.value = ScrapValue.NONE;
	}
	
	/**
	 * Obtains the ItemData for the specified ItemStack from
	 * the profile.
	 */
	public abstract ItemData getItemData(final ItemStack stack);
	
	/**
	 * Adds the ItemData to the profile.
	 */
	public abstract void addItemData(final ItemData data);
	
	/**
	 * Obtains the RecipeData for the specified ItemStack
	 * from the profile.
	 */
	public abstract RecipeData getRecipe(final ItemStack stack);
	
	/**
	 * Adds the RecipeData for the specified ItemStack
	 * from the profile.
	 */
	public abstract void addRecipe(final ItemStack stack, final RecipeData data);
	
	/**
	 * Removes a recipe for the specified ItemStack from the
	 * profile.
	 */
	public abstract void removeRecipe(final ItemStack stack);

	/**
	 * Get's any extraction data associated with the ItemStack from
	 * the profile.
	 */
	public abstract ExtractionData getExtractionData(final ItemStack stack);
	
	/**
	 * Adds extraction data associated with the ItemStack from the
	 * profile.
	 */
	public abstract void addExtractionData(final ItemStack stack, final ExtractionData data);
	
	/**
	 * Removes extraction data associated with the ItemStack from
	 * the profile.
	 */
	public abstract void removeExtractionData(final ItemStack stack);
	
	/**
	 * Writes diagnostic information to the provider writer.
	 */
	public abstract void writeDiagnostic(final Writer writer, final int what) throws IOException;
	
	/**
	 * Collects the ItemData entries associated with the profile into
	 * the provided list.
	 */
	public abstract void collectItemData(final List<ItemData> list);
}
