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

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;

import com.google.common.base.Optional;

import net.minecraft.item.ItemStack;

public final class ItemStackWeightTable extends
		WeightTable<ItemStackWeightTable.ItemStackItem> {

	public final static class ItemStackItem extends WeightTable.Item {

		protected final ItemStack stack;

		public ItemStackItem(final ItemStack stack, final int weight) {
			super(weight);
			this.stack = stack;
		}

		public ItemStack getStack() {
			return stack == null ? null : stack.copy();
		}

		@Override
		public String toString() {
			final StringBuilder builder = new StringBuilder();
			if(ScrappingTables.destroyIt(stack))
				builder.append("Destroy");
			else if(ScrappingTables.keepIt(stack))
				builder.append("Keep Item");
			else if(ScrappingTables.dustIt(stack))
				builder.append("Dust Item");
			else {
				builder.append(ItemStackHelper.resolveName(stack));
				if(stack.stackSize > 1) {
					builder.append("(x");
					builder.append(stack.stackSize);
					builder.append(')');
				}
			}

			return builder.toString();
		}
	}

	public ItemStackWeightTable() {
		super();
	}

	public ItemStackWeightTable(final Random rand) {
		super(rand);
	}

	public Optional<ItemStack> nextStack() {
		try {
			return Optional.fromNullable(next().getStack());
		} catch (Exception e) {
			ModLog.warn(e.getMessage());
		}
		
		return Optional.absent();
	}
}
