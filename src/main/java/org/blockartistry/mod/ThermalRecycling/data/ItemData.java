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

package org.blockartistry.mod.ThermalRecycling.data;

import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import com.google.common.base.Preconditions;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Information point about items and their potential value. Used by the
 * scrapping process to determine ScrapValue as well as the Composter for
 * determination of BROWN/GREEN ingredients.
 */
public final class ItemData {

	private static final ScrapValue DEFAULT_SCRAP_VALUE = ScrapValue.STANDARD;
	private static final CompostIngredient DEFAULT_COMPOST_INGREDIENT = CompostIngredient.NONE;

	private static final Map<ItemStackKey, ItemData> cache = new HashMap<ItemStackKey, ItemData>(1024);

	private final String name;
	private ScrapValue value;
	private CompostIngredient compostValue;
	private boolean ignoreRecipe;
	private boolean scrubFromOutput;
	private boolean isFood;
	private boolean isGeneric;
	private boolean isBlockedFromScrapping;

	private static boolean exceptionalFood(final Item item) {
		return item == Items.golden_apple || item == Items.golden_carrot;
	}

	private static void setBlockedFromScrapping(final ItemStack stack) {
		put(stack, new ItemData(stack, get(stack).setBlockedFromScrapping(true)));
	}
	
	static {
		
		String[] whiteList = null;
		final String[] modList = SupportedMod.getModIdList();
		final String[] configList = ModOptions.getModWhitelist();

		if (configList == null || configList.length == 0)
			whiteList = modList;
		else
			whiteList = MyUtils.concat(modList, configList);

		final String modIds = ":" + MyUtils.join(":", whiteList) + ":";

		for (final Item o : GameData.getItemRegistry().typeSafeIterable()) {
			final String name = Item.itemRegistry.getNameForObject(o);
			if (name != null) {

				final String modName = StringUtils.substringBefore(name, ":");
				final Boolean isMinecraft = modName.compareTo("minecraft") == 0;

				final ItemStack stack = new ItemStack(o, 1, OreDictionary.WILDCARD_VALUE);
				final ItemData data = new ItemData(stack);

				if (isMinecraft)
					data.setValue(ScrapValue.NONE);
				else
					data.setValue(modIds.contains(":" + modName + ":") ? DEFAULT_SCRAP_VALUE
							: ScrapValue.NONE);

				final boolean food = o instanceof ItemFood
						&& !exceptionalFood((Item) o);
				data.setIgnoreRecipe(food);
				data.setScrubFromOutput(food);

				cache.put(new ItemStackKey(o), data);
			}
		}
		
		// Scan the OreDictionary looking for blocks/items that we want
		// to prevent from being scrapped.  Collect them in the TreeSet
		// so that there are no duplicates and it is sorted.
		for(final String oreName: OreDictionary.getOreNames()) {
			if(oreName.startsWith("block") || oreName.startsWith("dust") || oreName.startsWith("ingot") || oreName.startsWith("nugget")) {
				for(final ItemStack stack: OreDictionary.getOres(oreName)) {
					setBlockedFromScrapping(stack);
				}
			}
		}
		
		// Add our scrap and boxes
		setBlockedFromScrapping(ScrappingTables.debris);
		setBlockedFromScrapping(ScrappingTables.poorScrap);
		setBlockedFromScrapping(ScrappingTables.standardScrap);
		setBlockedFromScrapping(ScrappingTables.superiorScrap);
		setBlockedFromScrapping(ScrappingTables.poorScrapBox);
		setBlockedFromScrapping(ScrappingTables.standardScrapBox);
		setBlockedFromScrapping(ScrappingTables.superiorScrapBox);
	}

	private ItemData(final ItemStack stack, final ItemData data) {
		this(stack, data.value, data.compostValue, data.ignoreRecipe, data.scrubFromOutput, data.isBlockedFromScrapping);
	}

	private ItemData(final ItemStack stack) {
		this(stack, DEFAULT_SCRAP_VALUE, DEFAULT_COMPOST_INGREDIENT, false, false, false);
	}

	private ItemData(final ItemStack stack, final ScrapValue value,
			final CompostIngredient compost, final boolean ignoreRecipe,
			final boolean scrubFromOutput, final boolean isBlockedFromScrapping) {
		Preconditions.checkNotNull(stack);
		this.name = ItemStackHelper.resolveName(stack);
		this.value = value;
		this.compostValue = compost;
		this.ignoreRecipe = ignoreRecipe;
		this.scrubFromOutput = scrubFromOutput;
		this.isFood = stack.getItem() instanceof ItemFood;
		this.isGeneric = stack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
		this.isBlockedFromScrapping = isBlockedFromScrapping;
	}

	public ItemData setValue(final ScrapValue value) {
		this.value = value;
		return this;
	}

	public ScrapValue getScrapValue() {
		return value;
	}

	public CompostIngredient getCompostIngredientValue() {
		return compostValue;
	}

	public ItemData setCompostIngredientValue(final CompostIngredient value) {
		this.compostValue = value;
		return this;
	}

	public ItemData setIgnoreRecipe(final boolean flag) {
		this.ignoreRecipe = flag;
		return this;
	}

	public boolean getIgnoreRecipe() {
		return ignoreRecipe;
	}

	public ItemData setScrubFromOutput(final boolean flag) {
		this.scrubFromOutput = flag;
		return this;
	}

	public boolean isScrubbedFromOutput() {
		return scrubFromOutput;
	}

	public boolean isFood() {
		return isFood;
	}

	public boolean isGeneric() {
		return isGeneric;
	}
	
	public boolean isBlockedFromScrapping() {
		return isBlockedFromScrapping;
	}
	
	public ItemData setBlockedFromScrapping(final boolean flag) {
		isBlockedFromScrapping = flag;
		return this;
	}

	@Override
	public String toString() {

		return String
				.format("%s%s [scrap value: %s; ignoreRecipe: %s; scrubFromOutput: %s; isFood: %s]",
						name,
						(isGeneric() ? " (GENERIC)" : ""), value.name(),
						Boolean.toString(ignoreRecipe), Boolean
								.toString(scrubFromOutput), Boolean
								.toString(isFood));
	}

	public static void setValue(final ItemStack stack, final ScrapValue value) {
		Preconditions.checkNotNull(stack);
		final ItemData data = getForWrite(stack);
		data.setValue(value);
		put(stack, data);
	}

	public static void setCompostIngredientValue(final ItemStack stack,
			final CompostIngredient value) {
		Preconditions.checkNotNull(stack);
		final ItemData data = getForWrite(stack);
		data.setCompostIngredientValue(value);
		put(stack, data);
	}

	private static ItemData getForWrite(final ItemStack stack) {
		ItemData data = get(stack);
		if (data == null)
			data = new ItemData(stack);
		else if (data.isGeneric() && stack.getItemDamage() != OreDictionary.WILDCARD_VALUE) {
			data = new ItemData(stack, data);
		}
		return data;
	}

	public static ItemData get(final ItemStack stack) {
		Preconditions.checkNotNull(stack);
		ItemData data = cache.get(ItemStackKey.getCachedKey(stack));
		if (data == null)
			data = cache.get(ItemStackKey.getCachedKey(stack.getItem()));
		if (data == null)
			data = new ItemData(stack);
		return data;
	}

	public static void put(final ItemStack stack, final ItemData data) {
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(stack);
		cache.put(new ItemStackKey(stack), data);
	}

	public static ItemData put(final ItemStack stack,
			final ScrapValue value, final boolean ignoreRecipe,
			final boolean scrubFromOutput) {
		Preconditions.checkNotNull(stack);
		final ItemData data = getForWrite(stack);
		data.setValue(value).setIgnoreRecipe(ignoreRecipe)
				.setScrubFromOutput(scrubFromOutput);
		put(stack, data);
		return data;
	}

	
	public static boolean canBeScrapped(final ItemStack stack) {
		return !get(stack).isBlockedFromScrapping();
	}

	public static boolean isRecipeIgnored(final ItemStack stack) {
		final ItemData data = get(stack);
		return data != null && (data.getIgnoreRecipe() || data.isBlockedFromScrapping());
	}

	public static void setRecipeIgnored(final Item item, final boolean flag) {
		Preconditions.checkNotNull(item);
		final ItemStack generic = ItemStackHelper.asGeneric(item);
		put(generic, get(generic).setIgnoreRecipe(flag));
	}

	public static void setRecipeIgnored(final Block block, final boolean flag) {
		Preconditions.checkNotNull(block);
		setRecipeIgnored(Item.getItemFromBlock(block), flag);
	}

	public static void setRecipeIgnored(final ItemStack stack,
			final boolean flag) {
		Preconditions.checkNotNull(stack);
		final ItemData data = getForWrite(stack);
		data.setIgnoreRecipe(flag);
		put(stack, data);
	}

	public static boolean isScrubbedFromOutput(final ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return get(stack).isScrubbedFromOutput();
	}

	public static void setScrubbedFromOutput(final ItemStack stack,
			final boolean flag) {
		Preconditions.checkNotNull(stack);
		final ItemData data = getForWrite(stack);
		data.setScrubFromOutput(flag);
		put(stack, data);
	}

	public static void writeDiagnostic(final Writer writer) throws Exception {

		writer.write("Item Info:\n");
		writer.write("=================================================================\n");
		for (final ItemData d : cache.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}
}
