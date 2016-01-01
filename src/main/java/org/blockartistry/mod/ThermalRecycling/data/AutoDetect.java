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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;

import net.minecraft.item.ItemStack;

public final class AutoDetect {

	private AutoDetect() {
	}

	private static float detect(final ItemData data) {
		if (data.getAutoScrapValue() == null) {
			// ModLog.debug("detect: %s (%s)", data.getName(),
			// data.getInternalName());
			final RecipeData recipe = RecipeData.get(data.getStack());
			if (data.isBlockedFromScrapping() || recipe == null || !recipe.hasOutput()) {
				data.setAutoScrapValue(data.getScrapValue());
				data.setScore(data.getAutoScrapValue().getScore());
			} else {
				float score = 0;
				final List<ItemStack> output = recipe.getOutput();
				for (final ItemStack stack : output) {
					final ItemData child = ItemData.get(stack);
					score += detect(child) * stack.stackSize;
				}

				score = score / (float) recipe.getMinimumInputQuantityRequired();
				data.setAutoScrapValue(ScrapValue.determineValue(score));
				data.setScore(score);

				if (data.getScrapValue() != data.getAutoScrapValue()) {
					final StringBuilder builder = new StringBuilder();
					builder.append("MISMATCH: ").append(data.getInternalName());
					builder.append(" value: ").append(data.getScrapValue().name());
					builder.append(" auto: ").append(data.getAutoScrapValue().name());
					ModLog.info(builder.toString());
				}
			}
		}

		return data.getScore();
	}

	public static void detect() {
		// Score the buggers
		for (final ItemData data : ItemData.getDataList())
			try {
				if (!data.isGeneric())
					detect(data);
			} catch (final Exception ex) {
				ModLog.warn("autoDetect() blew a gasket: %s (%s)", data.getName(), data.getInternalName());
			}
	}
}
