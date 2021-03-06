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

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import org.blockartistry.mod.ThermalRecycling.data.registry.ItemData;
import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.OreDictionaryHelper;

public final class DebugToolTip extends CachingToolTip {
	
	private static final StringBuilder builder = new StringBuilder(32);

	@Override
	public void addToToolTip(final List<String> output, final ItemStack stack) {

		output.add(EnumChatFormatting.GREEN + "DEBUG:");

		builder.setLength(0);
		builder.append(EnumChatFormatting.LIGHT_PURPLE);
		builder.append(ItemStackHelper.resolveInternalName(stack));
		output.add(builder.toString());
		
		final List<String> names = OreDictionaryHelper.getOreNamesForStack(stack);
		for(final String oreName: names)
			output.add(EnumChatFormatting.WHITE + oreName);

		final ItemData data = ItemRegistry.get(stack);
		if(data == null)
			return;

		//if (data.isGeneric())
		//	output.add(EnumChatFormatting.LIGHT_PURPLE + "Generic");
		if (data.isFood)
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Food");
		if (data.scrubFromOutput)
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Scrubbed");
		if (data.ignoreRecipe)
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Recipe Ignored");
		if(data.isBlockedFromExtraction)
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Blocked: Extraction");
		if(data.isBlockedFromScrapping)
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Blocked: Scrapping");
		
		if(data.auto != null) {
			output.add(EnumChatFormatting.GOLD + "Auto: " + data.auto.name());
			output.add(EnumChatFormatting.GOLD + "Score: " + data.score);
		}
			
		if (stack.hasTagCompound())
			output.add(EnumChatFormatting.LIGHT_PURPLE + "Compound Data");
	}
}
