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
import java.util.Map.Entry;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;
import org.blockartistry.mod.ThermalRecycling.items.CoreType;
import org.blockartistry.mod.ThermalRecycling.items.ItemLevel;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * Base class from which all scrap handlers are created. Handlers are used by
 * the scrapping system to better fine tune how a mod wants to scrap their
 * items. Handlers are registered during plugin startup and will be used once
 * the mod is up and running.
 * 
 */
public class ScrapHandler {

	// Cache strings for performance
	protected static final StringBuilder builder = new StringBuilder(32);
	protected static final String loreDestroy = StatCollector.translateToLocal("msg.ScrapPreview.destroy");
	protected static final String loreKeep = StatCollector.translateToLocal("msg.ScrapPreview.keep");
	protected static final String loreDust = StatCollector.translateToLocal("msg.ScrapPreview.dust");
	protected static final String loreDebris = StatCollector.translateToLocal("item.Debris.debris.name");
	protected static final String lorePoorScrap = StatCollector.translateToLocal("item.RecyclingScrap.poor.name");
	protected static final String loreStandardScrap = StatCollector.translateToLocal("item.RecyclingScrap.standard.name");
	protected static final String loreSuperiorScrap = StatCollector.translateToLocal("item.RecyclingScrap.superior.name");
	
	protected static final DecimalFormat doubleFormatter = new DecimalFormat("0.0% ");
	
	protected static final int EXPERIENCE_PER_BOTTLE = ModOptions.getXpBottleValue();
	
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
		public boolean shouldJam;
		public int inputQuantityRequired;
		
		// These are filled in by the scrap handler so if it
		// comes back it doesn't have to query data, again.
		public ScrapHandler handler;
		public RecipeData recipeData = null;
		public ExtractionData extractData = null;
		public List<ItemStack> recipeOutput = null;
		public ItemStackWeightTable[] tables = null;

		public ScrappingContext(final ItemStack core, final ItemStack stack) {
			this(core, stack, null);
		}

		public ScrappingContext(final ItemStack core, final ItemStack stack, final RecipeData recipe) {
			this.core = core;
			this.coreType = CoreType.getType(core);
			this.coreLevel = coreType == CoreType.NONE ? ItemLevel.BASIC : ItemLevel.getLevel(core);
			this.handler = ScrapHandler.getHandler(stack);
			this.toProcess = stack.copy();
			this.toProcess.stackSize = 1;
			this.shouldJam = !CoreType.canCoreProcess(this.coreType, stack);
			
			if(this.coreType == CoreType.EXTRACTION) {
				this.extractData = ExtractionData.get(stack);
				this.inputQuantityRequired = this.extractData.getMinimumInputQuantityRequired();
				if(this.extractData.isDefault())
					this.shouldJam = true;
			} else {
				this.recipeData = RecipeData.get(stack);
				this.inputQuantityRequired = this.recipeData.getMinimumInputQuantityRequired();
			}
		}
		
		/**
		 * Determines if the ScrappingContext can be reused based on the core
		 * and stack references provided.
		 * 
		 * @param core Currently installed core in the machine
		 * @param stack The ItemStack being processed
		 * @return true if the context is compatible, false otherwise
		 */
		public boolean canReuse(final ItemStack core, final ItemStack stack) {
			return ItemStack.areItemStacksEqual(this.core, core) && ItemStack.areItemStacksEqual(this.toProcess, stack);
		}
		
		/**
		 * Returns a list of components that result from the scrap
		 * operation.  The parameters of the operation are based on
		 * the ScrappingContext state.
		 * 
		 * @return List of 0 or more ItemStacks that result from scrapping
		 * @throws Exception 
		 */
		public List<ItemStack> scrap() {
			return handler.scrapItems(this);
		}
		
		/**
		 * Simulates a scrap operation based on the information in the
		 * ScrappingContext.  Primarly this information is used by
		 * the Scrap Assessor.
		 * 
		 * @return Projected result from if the ItemStack is scrapped
		 */
		public PreviewResult preview() {
			return handler.preview(this);
		}
	}

	private static Map<ItemStackKey, ScrapHandler> handlers = new HashMap<ItemStackKey, ScrapHandler>();
	private static final ScrapHandler generic = new ScrapHandler();
	
	public static void freeze() {
		handlers = ImmutableMap.copyOf(handlers);
	}

	/**
	 * Register a special handler for the ItemStack provided.  The
	 * ItemStack can specify a very specific item, or it can be
	 * a generic case that matches any sub-type of a given Item.
	 * 
	 * @param stack ItemStack to which the handler is keyed
	 * @param handler Handler to use when scrapping the ItemStack
	 */
	public static void registerHandler(final ItemStack stack, final ScrapHandler handler) {
		
		// Possible if mods start changing names or something decides not to have the item
		// load.  Fail silently rather than asserting out.
		if(stack == null) {
			ModLog.warn("ScrapHandler.registerHandler(): key stack is null - missing item from mod?");
			return;
		}
		
		assert handler != null;

		handlers.put(new ItemStackKey(stack), handler);
	}

	private static ScrapHandler getHandler(final ItemStack stack) {
		ScrapHandler handler = handlers.get(ItemStackKey.getCachedKey(stack));
		if (handler == null)
			handler = handlers.get(ItemStackKey.getCachedKey(stack.getItem()));
		return handler == null ? generic : handler;
	}

	// Pseudo estimation of levels - it's close enough
    protected static int getExpToLevel(final int level) {
        return level >= 30 ? 62 + (level - 30) * 7 : (level >= 15 ? 17 + (level - 15) * 3 : 17);
    }

    protected static int estimateTotalXPForLevels(final int levels) {
    	int xp = 0;
    	for(int i = 0; i < levels; i++) {
    		xp += getExpToLevel(i);
    	}
    	
    	return xp;
    }
    
    // Estimation routine from the Anvil repair logic
	protected int getEnchantmentBottleCount(final ScrappingContext ctx) {
		
		int experience = 0;
		
		// NOTE: If you use the "x" option in the inventory display to
		// create an enchanted book getEnchantments() will not see those
		// enchants.  Reason is the tagging in the NBT is different than
		// a normal Vanilla enchanted book.  The books dropped by the
		// scrapbox routines work fine, however, since it utilizes the
		// Vanilla routines for creation.
		@SuppressWarnings("unchecked")
		Map<Integer, Integer> enchants = EnchantmentHelper.getEnchantments(ctx.toProcess);
		
		if(enchants != null && !enchants.isEmpty()) {
			int count = 0;
			for(Entry<Integer, Integer> e: enchants.entrySet()) {
				
				final int enchantId = e.getKey().intValue();
				final Enchantment ench = Enchantment.enchantmentsList[enchantId];
				final int level = Math.min(e.getValue().intValue(), ench.getMaxLevel());

				int l1 = 0;
				count++;
				switch (ench.getWeight()) {
				case 1: // '\001'
					l1 = 8;
					break;

				case 2: // '\002'
					l1 = 4;
					break;

				case 5: // '\005'
					l1 = 2;
					break;

				case 10: // '\n'
					l1 = 1;
					break;
				}
				
				l1 = Math.max(1, l1 / 2);
				experience += count + level * l1;
			}
		}
		
		// Make sure there is at least one bottle if there was
		// an enchant on the item.
		int numberOfBottles = estimateTotalXPForLevels(experience) / EXPERIENCE_PER_BOTTLE;
		if(numberOfBottles == 0 && experience > 0)
			numberOfBottles = 1;

		return numberOfBottles;
	}
	
	protected List<ItemStack> getEnchantmentBottles(final ScrappingContext ctx) {
		
		final List<ItemStack> result = new ArrayList<ItemStack>();
		if(EXPERIENCE_PER_BOTTLE == 0)
			return result;
		
		int bottles = getEnchantmentBottleCount(ctx);

		while(bottles > 0) {
			final int stackSize = Math.min(bottles, 64);
			result.add(new ItemStack(Items.experience_bottle, stackSize));
			bottles -= stackSize;
		}
		
		return result;
	}
	
	protected void initializeContext(final ScrappingContext ctx) {
		// If the recipe data in the context is null it means it hasn't
		// been through the mill.  Process the information.
		if(ctx.recipeOutput == null) {

			// If a decomp core is installed, get a list of outputs from the
			// recipes. If an extraction core and the item is a scrap box,
			// convert to scrap with a bonus.
			if (ctx.coreType == CoreType.DECOMPOSITION) {
				
				ctx.recipeOutput = getRecipeOutput(ctx);
				
				// Add any enchantment bottles from scrapping a
				// magic item/book.
				ctx.recipeOutput.addAll(getEnchantmentBottles(ctx));
	
				// If there isn't a recipe, process the item as if it were being
				// scrapped without any cores.
				if (ctx.recipeOutput.isEmpty()) {
					ctx.recipeOutput = Collections.singletonList(ctx.toProcess);
					ctx.coreType = CoreType.NONE;
					ctx.coreLevel = ItemLevel.BASIC;
				}
				
			} else if (ctx.coreType == CoreType.EXTRACTION) {
				if(ItemStackHelper.equals(ctx.toProcess.getItem(), ItemManager.recyclingScrapBox)) {
					ctx.recipeOutput = Collections.singletonList(new ItemStack(ItemManager.recyclingScrap,
							8 + ModOptions.getScrapBoxBonus(), ItemStackHelper.getItemDamage(ctx.toProcess)));
				}
				else {
					final ItemStack stack = ctx.toProcess.copy();
					stack.stackSize = 1;
					ctx.recipeOutput = Collections.singletonList(stack);
				}
				
				ctx.tables = new ItemStackWeightTable[] { ExtractionData.get(ctx.recipeOutput.get(0)).getOutput() };
			} else {
				ctx.recipeOutput = Collections.singletonList(ctx.toProcess);
			}

			// Get the weight tables if they haven't already been provided.  Extraction cores
			// have special tables for determining output.
			if(ctx.tables == null) {
				ctx.tables = new ItemStackWeightTable[ctx.recipeOutput.size()];
				for(int i = 0; i < ctx.tables.length; i++) {
					final ScrapValue sv = ItemRegistry.get(ctx.recipeOutput.get(i)).value;
					ctx.tables[i] = ScrappingTables.getTable(ctx.coreType, ctx.coreLevel, sv).get();;
				}
			}
		}
	}

	/**
	 * Scraps items based on the incoming context.  The context can be reused
	 * between operations to speed up the process of scrapping.
	 * 
	 * @param ctx Context of the operation
	 * @return Result of the scrap operation.
	 * @throws Exception 
	 */
	public List<ItemStack> scrapItems(final ScrappingContext ctx) {
		
		// Initialize the context as needed
		initializeContext(ctx);
		
		final List<ItemStack> result = new ArrayList<ItemStack>();

		final int dataLength = ctx.recipeOutput.size();
		for (int i = 0; i < dataLength; i++) {

			final ItemStack target = ctx.recipeOutput.get(i);
			final ItemStackWeightTable t = ctx.tables[i];
			
			for (int count = 0; count < target.stackSize; count++) {

				Optional<ItemStack> cupieDoll = t.nextStack();

				if (cupieDoll.isPresent()) {

					if (ScrappingTables.keepIt(cupieDoll.get())) {
						cupieDoll = Optional.of(target.copy());
						cupieDoll.get().stackSize = 1;
					} else if (ScrappingTables.dustIt(cupieDoll.get())) {
						cupieDoll = Optional.of(target.copy());
						cupieDoll.get().stackSize = 1;
						cupieDoll = ItemStackHelper.convertToDustIfPossible(cupieDoll.get());
					}
					
					// Maybe be null in the destroy case
					if(cupieDoll.isPresent()) {
						result.add(cupieDoll.get());
					}
				}
			}
		}

		return result;
	}
	
	
	/**
	 * Method used by the scrap handler to get the recipe to break down an
	 * item.  By default it looks at the RecipeData list to obtain a reverse
	 * recipe that is sourced from Minecraft/Forge.  Specific handlers
	 * extend ScrapHandler to provide their own method for retrieving
	 * recipes for processing.
	 * 
	 * @param ctx Context for the scrapping operation
	 * @return The reverse recipe for the ItemStack that is being scrapped
	 */
	protected List<ItemStack> getRecipeOutput(final ScrappingContext ctx) {
		return MyUtils.clone(ctx.recipeData.getOutput());
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

	/**
	 * Analyzes the recipe and configuration information in the ScrappingContext
	 * and returns back the probable results of a scrapping operation.
	 * 
	 * @param ctx Context for the operation
	 * @return Results of a potential scrapping operation
	 */
	public PreviewResult preview(final ScrappingContext ctx) {
		
		initializeContext(ctx);

		// Get a base template for what the input should look like
		final ItemStack item = ctx.toProcess.copy();
		item.stackSize = ctx.inputQuantityRequired;

		List<ItemStack> result = null;
		
		// If a dcomp core is installed get the output and decorate
		if (ctx.coreType == CoreType.DECOMPOSITION) {

			result = ctx.recipeOutput;
			
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
