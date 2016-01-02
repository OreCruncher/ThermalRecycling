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

import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/*
 * Represents an item that has multiple sub-types.
 */
public class MultiItemProfile extends ItemProfile {
	
	protected final TIntObjectHashMap<ItemData> itemData = new TIntObjectHashMap<ItemData>();
	protected final TIntObjectHashMap<RecipeData> recipeData = new TIntObjectHashMap<RecipeData>();
	protected final TIntObjectHashMap<ExtractionData> extractData = new TIntObjectHashMap<ExtractionData>();

	public MultiItemProfile(final Item item) {
		super(item);
	}

	@Override
	public ItemData getItemData(final ItemStack stack) {
		if(OreDictionaryHelper.isGeneric(stack))
			return this.settings;
		
		ItemData data = this.itemData.get(stack.getItemDamage());
		if(data != null)
			return data;
		
		return new ItemData(stack, this.settings);
	}

	@Override
	public void addItemData(final ItemData data) {
		if(this.settings != data)
			this.itemData.put(data.stack.getItemDamage(), data);
	}
	
	@Override
	public RecipeData getRecipe(final ItemStack stack) {
		if(OreDictionaryHelper.isGeneric(stack))
			return this.recipe;
		
		RecipeData data = this.recipeData.get(stack.getItemDamage());
		if(data != null)
			return data;
		
		return this.recipe;
	}

	@Override
	public void addRecipe(final ItemStack stack, final RecipeData data) {
		if(OreDictionaryHelper.isGeneric(stack)) {
			this.recipe = data;
		} else {
			final int meta = stack.getItemDamage();
			this.recipeData.put(meta, data);
		}
	}

	@Override
	public void removeRecipe(final ItemStack stack) {
		if(OreDictionaryHelper.isGeneric(stack))
			this.recipe = RecipeData.EPHEMERAL;
		else
			this.recipeData.remove(stack.getItemDamage());
	}
	
	@Override
	public void collectItemData(final List<ItemData> list) {
		list.addAll(this.itemData.valueCollection());
	}

	@Override
	public ExtractionData getExtractionData(final ItemStack stack) {
		if(OreDictionaryHelper.isGeneric(stack))
			return this.extract;
		
		ExtractionData data = this.extractData.get(stack.getItemDamage());
		if(data != null)
			return data;
		
		return this.extract;
	}

	@Override
	public void addExtractionData(final ItemStack stack, final ExtractionData data) {
		if(OreDictionaryHelper.isGeneric(stack)) {
			this.extract = data;
		} else {
			final int meta = stack.getItemDamage();
			this.extractData.put(meta, data);
		}
	}

	@Override
	public void removeExtractionData(final ItemStack stack) {
		if(OreDictionaryHelper.isGeneric(stack))
			this.extract = ExtractionData.EPHEMERAL;
		else
			this.extractData.remove(stack.getItemDamage());
	}
	
	@Override
	public void writeDiagnostic(final Writer writer, final int what) throws IOException {
		switch(what) {
		case ItemRegistry.DIAG_EXTRACT:
			if(this.extract != ExtractionData.EPHEMERAL) {
				writer.write(this.extract.toString());
				writer.write("\n");
			}
			for(final ExtractionData data: this.extractData.valueCollection()) {
				writer.write(data.toString());
				writer.write("\n");
			}
			break;
		case ItemRegistry.DIAG_ITEMDATA:
			writer.write(this.settings.toString());
			writer.write("\n");
			for(final ItemData data: this.itemData.valueCollection()) {
				writer.write(data.toString());
				writer.write("\n");
			}
			break;
		case ItemRegistry.DIAG_RECIPES:
			if(this.recipe != RecipeData.EPHEMERAL) {
				writer.write(this.recipe.toString());
				writer.write("\n");
			}
			for(final RecipeData data: this.recipeData.valueCollection()) {
				writer.write(data.toString());
				writer.write("\n");
			}
			break;
		}
	}
}
