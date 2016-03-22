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

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

/**
 * Information point about items and their potential value. Used by the
 * scrapping process to determine ScrapValue as well as the Composter for
 * determination of BROWN/GREEN ingredients.
 */
public final class ItemData {

	public final ItemStack stack;
	public final boolean isFood;

	public ScrapValue value = ScrapValue.STANDARD;
	public CompostIngredient compostValue = CompostIngredient.NONE;
	public boolean ignoreRecipe;
	public boolean scrubFromOutput;
	public boolean isBlockedFromScrapping;
	public boolean isBlockedFromExtraction;
	public boolean isBlockedFromVacuum;

	// Auto evaluation of scrap value
	public ScrapValue auto;
	public float score;

	ItemData(final Item item) {
		this(OreDictionaryHelper.asGeneric(item));
	}
	
	ItemData(final ItemStack stack) {
		this.stack = stack.copy();
		final Item item = stack.getItem();
		this.isFood = item instanceof ItemFood && !exceptionalFood(item);
	}

	public ItemData(final ItemStack stack, final ItemData data) {
		this.stack = stack.copy();
		this.isFood = data.isFood;
		this.value = data.value;
		this.compostValue = data.compostValue;
		this.ignoreRecipe = data.ignoreRecipe;
		this.scrubFromOutput = data.scrubFromOutput;
		this.isBlockedFromExtraction = data.isBlockedFromExtraction;
		this.isBlockedFromScrapping = data.isBlockedFromScrapping;
		this.isBlockedFromVacuum = data.isBlockedFromVacuum;
	}
	
	private static boolean exceptionalFood(final Item item) {
		return ItemStackHelper.equals(item, Items.golden_apple) || ItemStackHelper.equals(item, Items.golden_carrot);
	}
	
	public String getName() {
		String name = ItemStackHelper.resolveName(this.stack);
		if (name.contains("UNKNOWN") || name.contains("Invalid Item") || name.contains("???"))
			name = this.stack.getItem().toString() + ":" + ItemStackHelper.getItemDamage(this.stack);
		return name;
	}

	public String getInternalName() {
		return ItemStackHelper.resolveInternalName(this.stack);
	}
	
	@Override
	public String toString() {
		final boolean isGeneric = OreDictionaryHelper.isGeneric(this.stack);
		final StringBuilder builder = new StringBuilder();
		
		if(isGeneric)
			builder.append("GENERIC: ");
		builder.append(getInternalName());
		if(!isGeneric) {
			builder.append(" (");
			builder.append(this.getName());
			builder.append(")");
		}
		
		builder.append(' ');
		builder.append("[sv: ").append(this.value.name());
		builder.append("; ignoreRecipe: ").append(Boolean.toString(this.ignoreRecipe));
		builder.append("; scrub: ").append(Boolean.toString(this.scrubFromOutput));
		builder.append("; isFood: ").append(Boolean.toString(this.isFood));
		builder.append("; block scrap: ").append(Boolean.toString(this.isBlockedFromScrapping));
		builder.append("; block extract: ").append(Boolean.toString(this.isBlockedFromExtraction));
		builder.append("; block vacuum: ").append(Boolean.toString(this.isBlockedFromVacuum));
		builder.append(']');
		return builder.toString();
	}
}
