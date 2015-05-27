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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * Base class from which all scrap handlers are created. Handlers are used by
 * the scrapping system to better fine tune how a mod wants to scrap their
 * items. Handlers are registered during plugin startup and will be used once
 * the mod is up and running.
 * 
 */
public abstract class ScrapHandler {

	// Cache strings for performance
	static final StringBuilder builder = new StringBuilder(32);
	static final String loreDestroy = StatCollector.translateToLocal("msg.ScrapPreview.destroy");
	static final String loreKeep = StatCollector.translateToLocal("msg.ScrapPreview.keep");
	static final String loreDust = StatCollector.translateToLocal("msg.ScrapPreview.dust");
	static final String loreDebris = StatCollector.translateToLocal("item.Debris.debris.name");
	static final String lorePoorScrap = StatCollector.translateToLocal("item.RecyclingScrap.poor.name");
	static final String loreStandardScrap = StatCollector.translateToLocal("item.RecyclingScrap.standard.name");
	static final String loreSuperiorScrap = StatCollector.translateToLocal("item.RecyclingScrap.superior.name");
	
	static final DecimalFormat doubleFormatter = new DecimalFormat("0.0% ");
	
	public static class PreviewResult {

		public final ItemStack inputRequired;
		public final List<ItemStack> outputGenerated;

		protected PreviewResult(final ItemStack input, final List<ItemStack> output) {
			this.inputRequired = input;
			this.outputGenerated = output;
		}
	}

	static final Map<ItemStackKey, ScrapHandler> handlers = new HashMap<ItemStackKey, ScrapHandler>();
	
	public static final ScrapHandler generic = new GenericHandler();

	public static void registerHandler(final ItemStack stack, final ScrapHandler handler) {
		Preconditions.checkNotNull(stack);
		Preconditions.checkNotNull(handler);
		handlers.put(new ItemStackKey(stack), handler);
	}

	public static ScrapHandler getHandler(final ItemStack stack) {
		ScrapHandler handler = handlers.get(new ItemStackKey(stack));
		if (handler == null)
			handler = handlers.get(new ItemStackKey(stack.getItem()));
		return handler == null ? generic : handler;
	}

	/**
	 * Scraps the incoming item based on the implementation of the scrap
	 * handler.
	 * 
	 * @param core
	 *            Processing core that is being applied, if any
	 * @param stack
	 *            The ItemStack that is being scrapped
	 * @param preview
	 *            The call is to generate a preview of what is to happen
	 * @return A list of parts that are created by scrapping
	 */
	public abstract List<ItemStack> scrapItems(ItemStack core, ItemStack stack);

	protected List<ItemStack> getRecipeOutput(final ItemStack stack) {
		return RecipeData.getRecipe(stack);
	}

	protected int getRecipeInputQuantity(final ItemStack stack) {
		return RecipeData.getMinimumQuantityToRecycle(stack);
	}
	
	protected ItemStack decorateStack(final ItemLevel level, final ItemStack stack) {

		final List<String> lore = new ArrayList<String>();
		final ScrapValue sv = ItemData.get(stack).getScrapValue();
		final Optional<ItemStackWeightTable> t = ScrappingTables.getTable(CoreType.DECOMPOSITION, level, sv);
		final double totalWeight = t.get().getTotalWeight();

		for (final ItemStackItem w : t.get().getEntries()) {

			builder.setLength(0);;
			final ItemStack temp = w.getStack();
			builder.append(doubleFormatter.format(w.itemWeight / totalWeight));
			
			if(ScrappingTables.destroyIt(temp))
				builder.append(loreDestroy);
			else if(ScrappingTables.keepIt(temp))
				builder.append(loreKeep);
			else if(ScrappingTables.dustIt(temp))
				builder.append(loreDust);
			else if(temp.isItemEqual(ScrappingTables.debris))
				builder.append(loreDebris);
			else if(temp.isItemEqual(ScrappingTables.poorScrap))
				builder.append(lorePoorScrap);
			else if(temp.isItemEqual(ScrappingTables.standardScrap))
				builder.append(loreStandardScrap);
			else if(temp.isItemEqual(ScrappingTables.superiorScrap))
				builder.append(loreSuperiorScrap);
			else
				builder.append("UNKNOWN");
			
			lore.add(builder.toString());
		}
		
		ItemStackHelper.setItemLore(stack, lore);
		return stack;
	}

	public PreviewResult preview(final ItemStack core, final ItemStack stack) {

		CoreType coreType = CoreType.getType(core);
		
		// Get a base template for what the input should look like
		final ItemStack item = stack.copy();
		item.stackSize = 1;

		List<ItemStack> result = null;
		
		// If a dcomp core is installed get the output and decorate
		if (coreType == CoreType.DECOMPOSITION) {

			final RecipeData data = RecipeData.get(stack);
			
			// If there is output then process the result.
			// Otherwise it will fall through and treat the item
			// as "must scrap".
			if(data.hasOutput()) {
				
				item.stackSize = data.getMinimumInputQuantityRequired();
				result = getRecipeOutput(stack); // Need to use this in case of scrap handlers
				//ItemStackHelper.clone(data.getOutput());
				
				if(ModOptions.getEnableAssessorEnhancedLore()) {
					final ItemLevel level = ItemLevel.getLevel(core);
					for(final ItemStack t: result) {
						decorateStack(level, t);
					}
				}
			}
			else {
				coreType = CoreType.NONE;
			}
		}

		if (result == null) {
			
			result = new ArrayList<ItemStack>();
			final ItemLevel coreLevel = coreType != CoreType.NONE ? ItemLevel.getLevel(core) : ItemLevel.BASIC;
			final ScrapValue sv = ItemData.get(stack).getScrapValue();
			final Optional<ItemStackWeightTable> t = ScrappingTables.getTable(coreType, coreLevel, sv);
			final double totalWeight = t.get().getTotalWeight();
			
			for (final ItemStackItem w : t.get().getEntries()) {

				final ItemStack temp = w.getStack();
				if (temp != null
						&& !(ScrappingTables.keepIt(temp) || ScrappingTables
								.dustIt(temp))) {

					builder.setLength(0);
					builder.append(doubleFormatter.format(w.itemWeight / totalWeight));
					builder.append("chance");
					ItemStackHelper.setItemLore(temp, Collections.singletonList(builder.toString()));
					result.add(temp);
				}
			}
		}

		return new PreviewResult(item, result);
	}
}
