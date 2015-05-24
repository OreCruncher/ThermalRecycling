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
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.support.SupportedMod;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.MyComparators;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import cofh.lib.util.helpers.ItemHelper;

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Information point about items and their potential value as scrap. Used by the
 * Thermal Recycler when deciding what to do when it decides to scrap a part or
 * item.
 * 
 * Operationally, any item that is not found in this is considered to have
 * ScrapValue.POOR value. A plugin can alter this by setting it's own value.
 * 
 */
public final class ItemScrapData {

	public static final ScrapValue DEFAULT_SCRAP_VALUE = ScrapValue.STANDARD;

	static final TreeMap<ItemStack, ItemScrapData> itemData = new TreeMap<ItemStack, ItemScrapData>(
			MyComparators.itemStackAscending);

	ItemStack stack;
	ScrapValue value;
	CompostIngredient compostValue;
	boolean ignoreRecipe;
	boolean scrubFromOutput;
	boolean isFood;

	static boolean exceptionalFood(Item item) {
		return item == Items.golden_apple || item == Items.golden_carrot;
	}
	
	static boolean exceptionalFood(ItemStack stack) {
		return exceptionalFood(stack.getItem());
	}

	static {

		String[] whiteList = null;
		String[] modList = SupportedMod.getModIdList();
		String[] configList = ModOptions.getModWhitelist();

		if (configList == null || configList.length == 0)
			whiteList = modList;
		else
			whiteList = MyUtils.concat(modList, configList);

		String modIds = ":" + MyUtils.join(":", whiteList) + ":";

		for (Object o : Item.itemRegistry) {
			if (o instanceof Item || o instanceof Block) {
				String name = Item.itemRegistry.getNameForObject(o);
				if (name != null) {

					String modName = StringUtils.substringBefore(name, ":");
					Boolean isMinecraft = modName.compareTo("minecraft") == 0;

					ItemScrapData data = null;

					if (o instanceof Item)
						data = get((Item) o);
					else
						data = get((Block) o);

					if (isMinecraft)
						data.setValue(ScrapValue.NONE);
					else
						data.setValue(modIds.contains(":" + modName + ":") ? DEFAULT_SCRAP_VALUE
								: ScrapValue.NONE);

					boolean food = o instanceof ItemFood && !exceptionalFood((Item)o);
					data.setIgnoreRecipe(food);
					data.setScrubFromOutput(food);

					put(data);
				}
			}
		}
	}

	static ItemStack asGeneric(Block block) {
		return new ItemStack(Item.getItemFromBlock(block), 1,
				OreDictionary.WILDCARD_VALUE);
	}

	protected ItemScrapData(ItemScrapData data) {
		Preconditions.checkNotNull(data);

		this.stack = data.stack;
		this.value = data.value;
		this.ignoreRecipe = data.ignoreRecipe;
		this.scrubFromOutput = data.scrubFromOutput;
		this.isFood = data.isFood;
		this.compostValue = data.compostValue;
	}

	protected ItemScrapData(Block block) {
		this(new ItemStack(block, 1, OreDictionary.WILDCARD_VALUE),
				DEFAULT_SCRAP_VALUE, false, false);
	}

	protected ItemScrapData(Item item) {
		this(new ItemStack(item, 1, OreDictionary.WILDCARD_VALUE),
				DEFAULT_SCRAP_VALUE, false, false);
	}

	protected ItemScrapData(ItemStack stack) {
		this(stack, DEFAULT_SCRAP_VALUE, false, false);
	}

	public ItemScrapData(ItemStack stack, ScrapValue value,
			boolean ignoreRecipe, boolean scrubFromOutput) {
		this(stack, value, CompostIngredient.NONE, ignoreRecipe,
				scrubFromOutput);
	}

	public ItemScrapData(ItemStack stack, ScrapValue value,
			CompostIngredient compost, boolean ignoreRecipe,
			boolean scrubFromOutput) {
		Preconditions.checkNotNull(stack);
		this.stack = stack;
		this.value = value;
		this.compostValue = compost;
		this.ignoreRecipe = ignoreRecipe;
		this.scrubFromOutput = scrubFromOutput;
		this.isFood = stack.getItem() instanceof ItemFood;
	}

	public ItemScrapData setValue(ScrapValue value) {
		this.value = value;
		return this;
	}

	public ScrapValue getScrapValue() {
		return value;
	}

	public CompostIngredient getCompostIngredientValue() {
		return compostValue;
	}

	public ItemScrapData setCompostIngredientValue(CompostIngredient value) {
		this.compostValue = value;
		return this;
	}

	public ItemScrapData setIgnoreRecipe(boolean flag) {
		this.ignoreRecipe = flag;
		return this;
	}

	public boolean getIgnoreRecipe() {
		return ignoreRecipe;
	}

	public ItemScrapData setScrubFromOutput(boolean flag) {
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
		return stack.getItemDamage() == OreDictionary.WILDCARD_VALUE;
	}

	@Override
	public String toString() {

		return String
				.format("%s%s [scrap value: %s; ignoreRecipe: %s; scrubFromOutput: %s; isFood: %s]",
						ItemStackHelper.resolveName(stack),
						(isGeneric() ? " (GENERIC)" : ""), value.name(),
						Boolean.toString(ignoreRecipe), Boolean
								.toString(scrubFromOutput), Boolean
								.toString(isFood));
	}

	public static ScrapValue getValue(Item item) {
		Preconditions.checkNotNull(item);
		return get(item).value;
	}

	public static ScrapValue getValue(Block block) {
		Preconditions.checkNotNull(block);
		return get(block).value;
	}

	public static ScrapValue getValue(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return get(stack).value;
	}

	public static void setValue(Item item, ScrapValue value) {
		Preconditions.checkNotNull(item);
		put(get(item).setValue(value));
	}

	public static void setValue(Block block, ScrapValue value) {
		Preconditions.checkNotNull(block);
		put(get(block).setValue(value));
	}

	public static void setValue(ItemStack stack, ScrapValue value) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = getForWrite(stack);
		data.setValue(value);
		put(data);
	}

	public static ItemScrapData get(Item item) {
		Preconditions.checkNotNull(item);
		ItemScrapData result = getForWrite(ItemStackHelper.asGeneric(item));
		if (result == null)
			result = new ItemScrapData(item);
		return result;
	}

	public static ItemScrapData get(Block block) {
		Preconditions.checkNotNull(block);
		return get(Item.getItemFromBlock(block));
	}

	public static CompostIngredient getCompostIngredientValue(Item item) {
		Preconditions.checkNotNull(item);
		return get(item).compostValue;
	}

	public static CompostIngredient getCompostIngredientValue(Block block) {
		Preconditions.checkNotNull(block);
		return get(block).compostValue;
	}

	public static CompostIngredient getCompostIngredientValue(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		return get(stack).compostValue;
	}

	public static void setCompostIngredientValue(Item item,
			CompostIngredient value) {
		Preconditions.checkNotNull(item);
		put(get(item).setCompostIngredientValue(value));
	}

	public static void setCompostIngredientValue(Block block,
			CompostIngredient value) {
		Preconditions.checkNotNull(block);
		put(get(block).setCompostIngredientValue(value));
	}

	public static void setCompostIngredientValue(ItemStack stack,
			CompostIngredient value) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = getForWrite(stack);
		data.setCompostIngredientValue(value);
		put(data);
	}

	static ItemScrapData getForWrite(ItemStack stack) {
		ItemScrapData data = get(stack);
		if (data == null)
			data = new ItemScrapData(stack);
		else if (data.stack.getItemDamage() != stack.getItemDamage()) {
			data = new ItemScrapData(data);
			data.stack = stack;
		}
		return data;
	}

	public static ItemScrapData get(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = itemData.get(stack);
		if (data == null)
			data = itemData.get(ItemStackHelper.asGeneric(stack));
		if( data == null)
			data = new ItemScrapData(stack);
		return data;
	}

	public static void put(ItemScrapData data) {
		Preconditions.checkNotNull(data);
		Preconditions.checkNotNull(data.stack);
		itemData.put(data.stack, data);
	}

	public static ItemScrapData put(Item item, ScrapValue value,
			boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(item);
		ItemScrapData data = get(item);
		data.setValue(value).setIgnoreRecipe(ignoreRecipe)
				.setScrubFromOutput(scrubFromOutput);
		put(data);
		return data;
	}

	public static ItemScrapData put(Block block, ScrapValue value,
			boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(block);
		return put(Item.getItemFromBlock(block), value, ignoreRecipe,
				scrubFromOutput);
	}

	public static ItemScrapData put(ItemStack stack, ScrapValue value,
			boolean ignoreRecipe, boolean scrubFromOutput) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = getForWrite(stack);
		data.setValue(value).setIgnoreRecipe(ignoreRecipe)
				.setScrubFromOutput(scrubFromOutput);
		put(data);
		return data;
	}
	
	protected static boolean isOreDictionaryType(ItemStack stack) {
		return ItemHelper.isBlock(stack) || ItemHelper.isDust(stack)
				|| ItemHelper.isIngot(stack) || ItemHelper.isNugget(stack);
	}

	public static boolean isRecipeIgnored(Item item) {
		ItemScrapData data = get(item);
		return data.getIgnoreRecipe();
	}

	public static boolean isRecipeIgnored(Block block) {
		return isRecipeIgnored(Item.getItemFromBlock(block));
	}

	public static boolean isRecipeIgnored(ItemStack stack) {
		if (isOreDictionaryType(stack))
			return true;
		ItemScrapData data = get(stack);

		return data != null && (data.getIgnoreRecipe());
	}

	public static void setRecipeIgnored(Item item, boolean flag) {
		Preconditions.checkNotNull(item);
		put(get(item).setIgnoreRecipe(flag));
	}

	public static void setRecipeIgnored(Block block, boolean flag) {
		Preconditions.checkNotNull(block);
		setRecipeIgnored(Item.getItemFromBlock(block), flag);
	}

	public static void setRecipeIgnored(ItemStack stack, boolean flag) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = getForWrite(stack);
		data.setIgnoreRecipe(flag);
		put(data);
	}

	public static boolean isScrubbedFromOutput(Item item) {
		Preconditions.checkNotNull(item);
		ItemScrapData data = get(item);
		return data.isScrubbedFromOutput();
	}

	public static boolean isScrubbedFromOutput(Block block) {
		Preconditions.checkNotNull(block);
		return isScrubbedFromOutput(Item.getItemFromBlock(block));
	}

	public static boolean isScrubbedFromOutput(ItemStack stack) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = get(stack);
		return data.isScrubbedFromOutput();
	}

	public static void setScrubbedFromOutput(Item item, boolean flag) {
		Preconditions.checkNotNull(item);
		put(get(item).setScrubFromOutput(flag));
	}

	public static void setScrubbedFromOutput(Block block, boolean flag) {
		Preconditions.checkNotNull(block);
		setScrubbedFromOutput(Item.getItemFromBlock(block), flag);
	}

	public static void setScrubbedFromOutput(ItemStack stack, boolean flag) {
		Preconditions.checkNotNull(stack);
		ItemScrapData data = getForWrite(stack);
		data.setScrubFromOutput(flag);
		put(data);
	}

	public static void writeDiagnostic(Writer writer) throws Exception {

		writer.write("Item Info:\n");
		writer.write("=================================================================\n");
		for (ItemScrapData d : itemData.values())
			writer.write(String.format("%s\n", d.toString()));
		writer.write("=================================================================\n");
	}
}
