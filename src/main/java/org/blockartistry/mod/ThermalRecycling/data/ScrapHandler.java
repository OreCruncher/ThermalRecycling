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

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.machines.ProcessingCorePolicy;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.MyComparators;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;

/**
 * Base class from which all scrap handlers are created. Handlers are used by
 * the scrapping system to better fine tune how a mod wants to scrap their
 * items. Handlers are registered during plugin startup and will be used once
 * the mod is up and running.
 * 
 */
public abstract class ScrapHandler {

	public class PreviewResult {

		public final ItemStack inputRequired;
		public final List<ItemStack> outputGenerated;

		protected PreviewResult(ItemStack input, List<ItemStack> output) {
			this.inputRequired = input;
			this.outputGenerated = output;
		}
	}

	static final TreeMap<ItemStack, ScrapHandler> handlers = new TreeMap<ItemStack, ScrapHandler>(
			MyComparators.itemStackAscending);
	public static final ScrapHandler generic = new GenericHandler();

	public static void registerHandler(ItemStack stack, ScrapHandler handler) {
		Preconditions.checkNotNull(stack);
		Preconditions.checkNotNull(handler);
		handlers.put(stack.copy(), handler);
	}

	public static ScrapHandler getHandler(ItemStack stack) {
		ScrapHandler handler = handlers.get(stack);
		if (handler == null)
			handler = handlers.get(ItemStackHelper.asGeneric(stack));
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

	protected List<ItemStack> getRecipeOutput(ItemStack stack) {
		return RecipeData.getRecipe(stack);
	}

	protected int getRecipeInputQuantity(ItemStack stack) {
		return RecipeData.getMinimumQuantityToRecycle(stack);
	}

	public PreviewResult preview(ItemStack core, ItemStack stack) {

		ItemStack item = stack.copy();
		item.stackSize = getRecipeInputQuantity(stack);

		List<ItemStack> result = null;
		if (ProcessingCorePolicy.isDecompositionCore(core)) {

			result = getRecipeOutput(stack);

			// If its a decomp core and the item has no recipe, treat as if it
			// were being scrapped directly w/o a core it's the best way
			// to get scrap yield.
			if (result == null)
				core = null;
		}

		if (result == null) {
			result = new ArrayList<ItemStack>();
			ItemStackWeightTable t = ScrappingTables.getTable(core, stack);

			for (ItemStackItem w : t.getEntries()) {

				ItemStack temp = w.getStack();
				if (temp != null && !(ScrappingTables.keepIt(temp) || ScrappingTables.dustIt(temp))) {

					double percent = w.itemWeight * 100F / t.getTotalWeight();
					ItemStackHelper.setItemLore(temp,
							String.format("%-1.2f%% chance", percent));
					result.add(temp);
				}
			}
		}

		return new PreviewResult(item, result);
	}
}
