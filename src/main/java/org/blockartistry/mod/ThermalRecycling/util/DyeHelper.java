/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

package org.blockartistry.mod.ThermalRecycling.util;

import java.util.List;

import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import com.google.common.collect.ImmutableList;

public final class DyeHelper {

	public static final int COLOR_BLACK = 0;
	public static final int COLOR_RED = 1;
	public static final int COLOR_GREEN = 2;
	public static final int COLOR_BROWN = 3;
	public static final int COLOR_BLUE = 4;
	public static final int COLOR_PURPLE = 5;
	public static final int COLOR_CYAN = 6;
	public static final int COLOR_LIGHTGRAY = 7;
	public static final int COLOR_GRAY = 8;
	public static final int COLOR_PINK = 9;
	public static final int COLOR_LIME = 10;
	public static final int COLOR_YELLOW = 11;
	public static final int COLOR_LIGHTBLUE = 12;
	public static final int COLOR_MAGENTA = 13;
	public static final int COLOR_ORANGE = 14;
	public static final int COLOR_WHITE = 15;

	private static final int DEFAULT_COLOR = COLOR_WHITE;

	private static final List<String> dyeEntries = new ImmutableList.Builder<String>()
			.add("dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue",
					"dyePurple", "dyeCyan", "dyeLightGray", "dyeGray",
					"dyePink", "dyeLime", "dyeYellow", "dyeLightBlue",
					"dyeMagenta", "dyeOrange", "dyeWhite").build();

	private static final String GENERAL_DYE_ENTRY = "dye";

	private DyeHelper() {
	}

	public static boolean isDye(final ItemStack stack) {
		return OreDictionaryHelper.isOneOfThese(stack, GENERAL_DYE_ENTRY);
	}

	public static int getDyeColor(final ItemStack stack) {

		for (int i = 0; i < dyeEntries.size(); i++) {
			final List<ItemStack> possibles = OreDictionaryHelper.getOres(dyeEntries
					.get(i));
			if (possibles == null || possibles.isEmpty())
				continue;
			for (final ItemStack item : possibles) {
				if (ItemStackHelper.areEqual(stack, item))
					return i;
			}
		}

		return DEFAULT_COLOR;
	}

	public static int getDyeRenderColor(final int color) {
		return color < 0 || color > 15 ? ItemDye.field_150922_c[DEFAULT_COLOR]
				: ItemDye.field_150922_c[color];
	}
	
	public static boolean isValidColor(final int color) {
		return color >= 0 && color < dyeEntries.size();
	}
}
