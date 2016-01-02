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

import java.io.Writer;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.blockartistry.mod.ThermalRecycling.data.CompostIngredient;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public final class ItemRegistry {

	private static final Map<Item, ItemProfile> registry = new IdentityHashMap<Item, ItemProfile>();

	static {
		for (final Item o : GameData.getItemRegistry().typeSafeIterable()) {
			final ItemProfile profile;
			if (o.getHasSubtypes())
				profile = new MultiItemProfile(o);
			else if (o.isDamageable())
				profile = new DamagableItemProfile(o);
			else
				profile = new SingleItemProfile(o);
			registry.put(o, profile);
		}
	}

	private static ItemProfile getProfile(final ItemStack stack) {
		final Item item = stack.getItem();
		final ItemProfile profile = registry.get(item);
		if (profile == null)
			throw new RuntimeException("Missing profile for Item " + item.toString());
		return profile;
	}

	public static ItemData get(final ItemStack stack) {
		return getProfile(stack).getItemData(stack);
	}

	public static void set(final ItemData data) {
		getProfile(data.stack).addItemData(data);
	}

	public static RecipeData getRecipe(final ItemStack stack) {
		return getProfile(stack).getRecipe(stack);
	}

	public static void setRecipe(final ItemStack stack, final RecipeData recipe) {
		getProfile(stack).addRecipe(stack, recipe);
	}

	public static void removeRecipe(final ItemStack stack) {
		getProfile(stack).removeRecipe(stack);
	}

	public static ExtractionData getExtractionData(final ItemStack stack) {
		return getProfile(stack).getExtractionData(stack);
	}

	public static void setExtractionData(final ItemStack stack, final ExtractionData data) {
		getProfile(stack).addExtractionData(stack, data);
	}

	public static void removeExtractionData(final ItemStack stack) {
		getProfile(stack).removeExtractionData(stack);
	}

	public static void setBlockedFromScrapping(final ItemStack stack, final boolean flag) {
		final ItemData data = get(stack);
		data.isBlockedFromScrapping = flag;
		set(data);
	}

	public static void setBlockedFromExtraction(final ItemStack stack, final boolean flag) {
		final ItemData data = get(stack);
		data.isBlockedFromExtraction = flag;
		set(data);
	}

	public static void setScrapValue(final ItemStack stack, final ScrapValue value) {
		final ItemData data = get(stack);
		data.value = value;
		set(data);
	}

	public static void setCompostIngredientValue(final ItemStack stack, final CompostIngredient value) {
		final ItemData data = get(stack);
		data.compostValue = value;
		set(data);
	}

	public static boolean canBeScrapped(final ItemStack stack) {
		return !getProfile(stack).getItemData(stack).isBlockedFromScrapping;
	}

	public static boolean canBeExtracted(final ItemStack stack) {
		return !getProfile(stack).getItemData(stack).isBlockedFromExtraction;
	}

	public static boolean isRecipeIgnored(final ItemStack stack) {
		final ItemData data = get(stack);
		return data.ignoreRecipe || data.isBlockedFromScrapping;
	}

	public static void setRecipeIgnored(final ItemStack stack, final boolean flag) {
		final ItemData data = get(stack);
		data.ignoreRecipe = flag;
		set(data);
	}

	public static void setRecipeIgnored(final Item item, final boolean flag) {
		setRecipeIgnored(OreDictionaryHelper.asGeneric(item), flag);
	}

	public static void setRecipeIgnored(final Block block, final boolean flag) {
		setRecipeIgnored(Item.getItemFromBlock(block), flag);
	}

	public static boolean isScrubbedFromOutput(final ItemStack stack) {
		return getProfile(stack).getItemData(stack).scrubFromOutput;
	}

	public static void setScrubbedFromOutput(final ItemStack stack, final boolean flag) {
		final ItemData data = get(stack);
		data.scrubFromOutput = flag;
		set(data);
	}

	public static List<ItemData> getItemDataList() {
		final List<ItemData> data = new ArrayList<ItemData>();
		for (final ItemProfile profile : registry.values())
			profile.collectItemData(data);
		return data;
	}

	public static final int DIAG_ITEMDATA = 0;
	public static final int DIAG_RECIPES = 1;
	public static final int DIAG_EXTRACT = 2;

	public static void writeDiagnostic(final Writer writer, final int what) throws Exception {

		switch (what) {
		case ItemRegistry.DIAG_ITEMDATA:
			writer.write("Item Registry:\n");
			break;
		case ItemRegistry.DIAG_RECIPES:
			writer.write("Recycling Recipes:\n");
			break;
		case ItemRegistry.DIAG_EXTRACT:
			writer.write("Extraction Recipes:\n");
			break;
		}

		writer.write("=================================================================\n");
		for (final ItemProfile profile : registry.values())
			profile.writeDiagnostic(writer, what);
		writer.write("=================================================================\n\n\n");
	}

}
