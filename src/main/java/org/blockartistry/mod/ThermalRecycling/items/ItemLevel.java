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

package org.blockartistry.mod.ThermalRecycling.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public enum ItemLevel {
	
	// Order is important!
	BASIC,
	HARDENED,
	REINFORCED,
	RESONANT,
	ETHEREAL;
	
	public static ItemStack setLevel(final ItemStack stack, final ItemLevel level) {
		final NBTTagCompound nbt = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
		nbt.setInteger("Level", level.ordinal());
		stack.setTagCompound(nbt);
		return stack;
	}
	
	public static ItemLevel getLevel(final ItemStack stack) {
		ItemLevel result = BASIC;
		
		if(stack != null && stack.hasTagCompound()) {
			final int level = stack.getTagCompound().getInteger("Level");
			result = values()[level];
		}
		
		return result;
	}
	
	public ItemLevel nextLevel() {
		return nextLevel(this);
	}
	
	public static ItemLevel nextLevel(final ItemLevel level) {
		final int t = level.ordinal() + 1;
		if(t > ETHEREAL.ordinal()) {
			return ETHEREAL;
		}
		return values()[t];
	}
	
	public static ItemLevel max(final ItemLevel in1, final ItemLevel in2) {
		return in1.ordinal() > in2.ordinal() ? in1 : in2;
	}
}
