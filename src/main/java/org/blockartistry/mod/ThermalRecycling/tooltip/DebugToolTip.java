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

package org.blockartistry.mod.ThermalRecycling.tooltip;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.blockartistry.mod.ThermalRecycling.data.ItemScrapData;
import org.blockartistry.mod.ThermalRecycling.util.function.Operation;

public class DebugToolTip  extends Operation<List<String>, ItemStack> {

	@Override
	public void apply(List<String> output, ItemStack stack) {
		
		output.add(EnumChatFormatting.GREEN + "DEBUG:");
		
		// Generate a new we can display to make things real easy
		String name = Item.itemRegistry.getNameForObject(stack
				.getItem());
		
		if(name == null)
			name = "UNKNOWN";
		
		if(stack.getHasSubtypes())
			name += ":" + stack.getItemDamage();
		
		output.add(EnumChatFormatting.LIGHT_PURPLE + name);
		
		ItemScrapData data = ItemScrapData.get(stack);

		if(data.isGeneric())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Generic");
		if(data.isFood())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Food");
		if(data.isScrubbedFromOutput())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Scrubbed");
		if(data.getIgnoreRecipe())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Recipe Ignored");
		if(stack.hasTagCompound())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Compound Data");
	}
}
