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

package org.blockartistry.mod.ThermalRecycling.support.recipe.accessor;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.item.ItemStack;

public class IC2RecipeAccessor extends IC2AccessorBase {

	private static Field hidden = null;
	private static Field inputAccessor = null;
	
	@Override
	public boolean isHidden(final Object recipe) {
		try {
			if (hidden == null) {
				final Field temp = recipe.getClass().getDeclaredField("hidden");
				temp.setAccessible(true);
				hidden = temp;
			}

			try {
				return hidden.getBoolean(recipe);
			} catch (Exception e) {
			}

		} catch (final Exception e) {
			;
		}

		return false;
	}
	
	@Override
	public List<ItemStack> getOutput(final Object recipe) {
		List<ItemStack> result = null;

		try {

			if (inputAccessor == null) {
				final Field temp = recipe.getClass().getDeclaredField("input");
				temp.setAccessible(true);
				inputAccessor = temp;
			}

			try {
				final Object[] shaped = (Object[]) inputAccessor.get(recipe);
				result = project(shaped);
			} catch (Exception e) {
			}

		} catch (final Exception e) {
			;
		}

		return result;
	}
}
