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

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;

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

	public static class ScrappingContext {
		
		public final ItemStack toProcess;
		public final ItemStack core;
		public CoreType coreType;
		public ItemLevel coreLevel;
		
		// These are filled in by the scrap handler so if it
		// comes back it doesn't have to query data, again.
		public ScrapHandler handler;
		public RecipeData recipeData = null;
		public List<ItemStack> recipeOutput = null;
		public ItemStackWeightTable[] tables = null;
		
		public ScrappingContext(final ItemStack core, final ItemStack stack) {
			this.toProcess = stack;
			this.core = core;
			this.coreType = CoreType.getType(core);
			this.coreLevel = coreType == CoreType.NONE ? ItemLevel.BASIC : ItemLevel.getLevel(core);
			this.handler = ScrapHandler.getHandler(stack);
			this.recipeData = RecipeData.get(stack);
		}
		
		public boolean canReuse(final ItemStack core, final ItemStack stack) {
			return ItemStack.areItemStacksEqual(this.core, core) && ItemStack.areItemStacksEqual(this.toProcess, stack);
		}
		
		public List<ItemStack> scrap() {
			return handler.scrapItems(this);
		}
		
		public PreviewResult preview() {
			return handler.preview(this);
		}
	}

	static final Map<ItemStackKey, ScrapHandler> handlers = new HashMap<ItemStackKey, ScrapHandler>();
	public static final ScrapHandler generic = new GenericHandler();

	public static void registerHandler(final ItemStack stack, final ScrapHandler handler) {
		Preconditions.checkNotNull(stack);
		Preconditions.checkNotNull(handler);
		handlers.put(new ItemStackKey(stack), handler);
	}

	private static ScrapHandler getHandler(final ItemStack stack) {
		ScrapHandler handler = handlers.get(new ItemStackKey(stack));
		if (handler == null)
			handler = handlers.get(new ItemStackKey(stack.getItem()));
		return handler == null ? generic : handler;
	}

	protected void initializeContext(final ScrappingContext ctx) {
		// If the recipe data in the context is null it means it hasn't
		// been through the mill.  Process the information.
		if(ctx.recipeOutput == null) {

			// If a decomp core is installed, get a list of outputs from the
			// recipes. If an extraction core and the item is a scrap box,
			// convert to scrap with a bonus.
			if (ctx.coreType == CoreType.DECOMPOSITION) {
				
				if(ctx.recipeOutput == null)
					ctx.recipeOutput = getRecipeOutput(ctx);
	
				// If there isn't a recipe, process the item as if it were being
				// scrapped without any cores.
				if (ctx.recipeOutput.isEmpty()) {
					ctx.recipeOutput = Collections.singletonList(ctx.toProcess);
					ctx.coreType = CoreType.NONE;
					ctx.coreLevel = ItemLevel.BASIC;
				}
				
			} else if (ctx.coreType == CoreType.EXTRACTION && ctx.toProcess.getItem() == ItemManager.recyclingScrapBox) {
				ctx.recipeOutput = Collections.singletonList(new ItemStack(ItemManager.recyclingScrap,
						8 + ModOptions.getScrapBoxBonus(), ctx.toProcess.getItemDamage()));
			} else {
				ctx.recipeOutput = Collections.singletonList(ctx.toProcess);
			}
			
			ctx.tables = new ItemStackWeightTable[ctx.recipeOutput.size()];
			for(int i = 0; i < ctx.tables.length; i++) {
				ScrapValue sv = ItemData.get(ctx.recipeOutput.get(i)).getScrapValue();
				ctx.tables[i] = ScrappingTables.getTable(ctx.coreType, ctx.coreLevel, sv).get();;
			}
		}
	}

	/**
	 * Scraps items based on the incoming context.  The context can be reused
	 * between operations to speed up the process of scrapping.
	 * 
	 * @param ctx Context of the operation
	 * @return Result of the scrap operation.
	 */
	public abstract List<ItemStack> scrapItems(final ScrappingContext ctx);

	protected List<ItemStack> getRecipeOutput(final ScrappingContext ctx) {
		return ItemStackHelper.clone(ctx.recipeData.getOutput());
	}

	protected void decorateStacks(final ScrappingContext ctx, final List<ItemStack> stack) {
		
		for(int i = 0; i < stack.size(); i++) {
			
			final ItemStack item = stack.get(i);
			final ItemStackWeightTable t = ctx.tables[i];
			final double totalWeight = t.getTotalWeight();

			final List<String> lore = new ArrayList<String>();

			for (final ItemStackItem w : t.getEntries()) {

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
			
			ItemStackHelper.setItemLore(item, lore);
			
		}
	}

	public PreviewResult preview(final ScrappingContext ctx) {
		
		initializeContext(ctx);

		// Get a base template for what the input should look like
		final ItemStack item = ctx.toProcess.copy();
		item.stackSize = 1;

		List<ItemStack> result = null;
		
		// If a dcomp core is installed get the output and decorate
		if (ctx.coreType == CoreType.DECOMPOSITION) {

			item.stackSize = ctx.recipeData.getMinimumInputQuantityRequired();
			result = getRecipeOutput(ctx);
			
			if(ModOptions.getEnableAssessorEnhancedLore()) {
				decorateStacks(ctx, result);
			}
		}

		if (result == null) {
			
			result = new ArrayList<ItemStack>();
			final ItemStackWeightTable t = ctx.tables[0];
			final double totalWeight = t.getTotalWeight();
			
			for (final ItemStackItem w : t.getEntries()) {

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
