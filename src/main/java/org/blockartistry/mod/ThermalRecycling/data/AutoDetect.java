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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.data.registry.RecipeData;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import net.minecraft.item.ItemStack;

public final class AutoDetect {
	
	private static boolean SKIP = false;

	private AutoDetect() {
	}

	private static float detect(final ItemData data, final Set<ItemStack> recursionList) {
		if (data.auto == null) {
			// ModLog.debug("detect: %s (%s)", data.getName(),
			// data.getInternalName());
			final RecipeData recipe = ItemRegistry.getRecipe(data.stack);
			if (data.isBlockedFromScrapping || recipe == null || !recipe.hasOutput()) {
				data.auto = data.value;
				data.score = data.auto.score;
			} else {
				if(!recursionList.add(data.stack)) {
					ModLog.info("Recursion detected on item : " + ItemStackHelper.resolveInternalName(data.stack));
					data.auto = data.value;
					data.score = data.value.score;
					return data.score;
				}
				
				float score = 0;
				final List<ItemStack> output = recipe.getOutput();
				for (final ItemStack stack : output) {
					final ItemData child = ItemRegistry.get(stack);
					score += detect(child, recursionList) * stack.stackSize;
				}

				score = score / (float) recipe.getMinimumInputQuantityRequired();
				data.auto = ScrapValue.determineValue(score);
				data.score = score;

				if (data.value != data.auto) {
					final StringBuilder builder = new StringBuilder();
					builder.append("MISMATCH: ").append(data.getInternalName());
					builder.append(" value: ").append(data.value.name());
					builder.append(" auto: ").append(data.auto.name());
					ModLog.info(builder.toString());
				}
				
				recursionList.remove(data.stack);
			}
		}

		return data.score;
	}

	public static void detect() {
		if(SKIP)
			return;
		
		// Score the buggers
		for (final ItemData data : ItemRegistry.getItemDataList())
			try {
				final Set<ItemStack> recursion = new HashSet<ItemStack>();
				detect(data, recursion);
			} catch (final Exception ex) {
				ModLog.warn("autoDetect() blew a gasket: %s (%s)", data.getName(), data.getInternalName());
			}
	}
}
