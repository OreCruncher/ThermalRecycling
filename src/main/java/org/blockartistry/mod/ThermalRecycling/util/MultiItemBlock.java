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

import com.google.common.base.Preconditions;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public abstract class MultiItemBlock extends ItemBlock {

	// These are shared with the associated MultiBlock. MultiBlock
	// "owns" the information that goes into these values.
	protected final String[] names;
	protected final String myUnlocalizedName;

	protected MultiItemBlock(final Block block) {
		super(block);

		// MultiBlock and MultiItemBlock are tied at the hip
		final MultiBlock b = (MultiBlock) block;
		Preconditions.checkNotNull(b, "Block is not a MultiBlock!");

		names = b.names;
		myUnlocalizedName = b.myUnlocalizedName;

		setHasSubtypes(names.length > 1);
		setUnlocalizedName(myUnlocalizedName);
	}

	@Override
	public int getMetadata(final int damage) {
		return damage;
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		final int i = MathHelper
				.clamp_int(ItemStackHelper.getItemDamage(stack), 0, names.length - 1);
		return getUnlocalizedName() + "." + names[i];
	}
}
