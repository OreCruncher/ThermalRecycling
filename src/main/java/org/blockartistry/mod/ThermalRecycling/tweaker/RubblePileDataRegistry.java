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

package org.blockartistry.mod.ThermalRecycling.tweaker;

import org.blockartistry.mod.ThermalRecycling.blocks.PileOfRubble;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.RubblePileDataRegistry")
public class RubblePileDataRegistry {
	
	private RubblePileDataRegistry() {
		
	}
	
	@ZenMethod
	public static void add(final IItemStack item, final int min, final int max, final int weight) {
		
		if (!MineTweakerUtil.checkNotNull(item, "item cannot be null"))
			return;

		if (!MineTweakerUtil.checkArgument(min >= 0, "min value must be greater than 0"))
			return;

		if (!MineTweakerUtil.checkArgument(max >= min, "max value must be greater or equal to min value"))
			return;

		if (!MineTweakerUtil.checkArgument(weight > 0, "weight value must be greater than 0"))
			return;

		final ItemStack stack = MineTweakerMC.getItemStack(item);
		PileOfRubble.addRubbleDrop(stack, min, max, weight);
	}
	
	@ZenMethod
	public static void remove(final IItemStack item) {

		if (!MineTweakerUtil.checkNotNull(item, "item cannot be null"))
			return;
		
		final ItemStack stack = MineTweakerMC.getItemStack(item);
		PileOfRubble.removeRubbleDrop(stack);
	}
}
