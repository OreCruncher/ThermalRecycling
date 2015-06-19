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
import net.minecraftforge.oredict.OreDictionary;
import cofh.lib.util.helpers.ItemHelper;

import com.google.common.collect.ImmutableList;

public final class DyeHelper {

	private static final int DEFAULT_COLOR = 15;

	private static final List<String> dyeEntries = new ImmutableList.Builder<String>()
			.add("dyeBlack", "dyeRed", "dyeGreen", "dyeBrown", "dyeBlue",
					"dyePurple", "dyeCyan", "dyeLightGray", "dyeGray",
					"dyePink", "dyeLime", "dyeYellow", "dyeLightBlue",
					"dyeMagenta", "dyeOrange", "dyeWhite").build();
	
	private static final String GENERAL_DYE_ENTRY = "dye";

	private DyeHelper() {
	}
	
	public static boolean isDye(final ItemStack stack) {
		final List<ItemStack> possibles = OreDictionary.getOres(GENERAL_DYE_ENTRY);
		if (possibles == null || possibles.isEmpty())
			return false;
		for (final ItemStack item : possibles) {
			if (ItemHelper.itemsEqualForCrafting(stack, item))
				return true;
		}
		
		return false;
	}

	public static int getDyeColor(final ItemStack stack) {

		for (int i = 0; i < dyeEntries.size(); i++) {
			final List<ItemStack> possibles = OreDictionary.getOres(
					dyeEntries.get(i));
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
		return color < 0 || color > 15 ? DEFAULT_COLOR
				: ItemDye.field_150922_c[color];
	}
}
