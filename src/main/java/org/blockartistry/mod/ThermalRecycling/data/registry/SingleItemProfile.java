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

import org.blockartistry.mod.ThermalRecycling.data.RecipeData;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class SingleItemProfile extends ItemProfile {
	
	private RecipeData recipe = RecipeData.EPHEMERAL;

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
	public RecipeData getRecipeData(final ItemStack stack) {
		return this.recipe;
	}

	@Override
	public void addRecipeData(final ItemStack stack, final RecipeData data) {
		if(data == RecipeData.EPHEMERAL)
			return;
		
		this.recipe = data;
	}

	@Override
	public void writeDiagnostic(final Writer writer) throws IOException {
		writer.write(this.settings.toString());
	}
}
