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

import org.blockartistry.mod.ThermalRecycling.data.ExtractionData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackWeightTable.ItemStackItem;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.ExtractionDataRegistry")
public class ExtractionDataRegistry {

	@ZenMethod
	public static void add(IItemStack input, IItemStack output, int weight) {
		
		if(!MineTweakerUtil.checkNotNull(input, "input cannot be null"))
			return;
		
		if(!MineTweakerUtil.checkArgument(weight > 0, "weight must be greater than 0"))
			return;

		final ItemStack theInput = MineTweakerMC.getItemStack(input);
		final ItemStack theOutput = MineTweakerMC.getItemStack(output);
		
		final ExtractionData data = ExtractionData.get(theInput);
		if(data.isDefault()) {
			final ItemStackWeightTable table = new ItemStackWeightTable();
			table.add(new ItemStackItem(theOutput, weight));
			ItemRegistry.setBlockedFromExtraction(theInput, false);
			ExtractionData.put(theInput, table);
		} else {
			data.getOutput().add(new ItemStackItem(theOutput, weight));
		}
	}
	
	@ZenMethod
	public static void remove(IItemStack input) {
		
		if(!MineTweakerUtil.checkNotNull(input, "input cannot be null"))
			return;
		
		ExtractionData.remove(MineTweakerMC.getItemStack(input));
	}
}
