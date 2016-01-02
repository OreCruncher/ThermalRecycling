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

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class SingleItemProfile extends ItemProfile {
	
	public SingleItemProfile(final Item item) {
		super(item);
	}

	@Override
	public ItemData getItemData(final ItemStack stack) {
		return this.settings;
	}

	@Override
	public void addItemData(final ItemData data) {
		// Do nothing
	}
	
	@Override
	public RecipeData getRecipe(final ItemStack stack) {
		return this.recipe;
	}

	@Override
	public void addRecipe(final ItemStack stack, final RecipeData data) {
		if(data == RecipeData.EPHEMERAL)
			return;
		
		this.recipe = data;
	}

	@Override
	public void removeRecipe(final ItemStack stack) {
		this.recipe = RecipeData.EPHEMERAL;
	}

	@Override
	public ExtractionData getExtractionData(final ItemStack stack) {
		return this.extract;
	}

	@Override
	public void addExtractionData(final ItemStack stack, final ExtractionData data) {
		if(data == ExtractionData.EPHEMERAL)
			return;
		this.extract = data;
	}

	@Override
	public void removeExtractionData(final ItemStack stack) {
		this.extract = ExtractionData.EPHEMERAL;
	}

	@Override
	public void writeDiagnostic(final Writer writer, final int what) throws IOException {
		switch(what) {
		case ItemRegistry.DIAG_EXTRACT:
			if(this.extract != ExtractionData.EPHEMERAL) {
				writer.write(this.extract.toString());
				writer.write("\n");
			}
			break;
		case ItemRegistry.DIAG_ITEMDATA:
			writer.write(this.settings.toString());
			writer.write("\n");
			break;
		case ItemRegistry.DIAG_RECIPES:
			if(this.recipe != RecipeData.EPHEMERAL) {
				writer.write(this.recipe.toString());
				writer.write("\n");
			}
			break;
		}
	}

	@Override
	public void collectItemData(final List<ItemData> list) {
		list.add(this.settings);
	}

}
