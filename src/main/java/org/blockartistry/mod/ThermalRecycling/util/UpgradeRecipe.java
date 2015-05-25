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

package org.blockartistry.mod.ThermalRecycling.util;

import org.blockartistry.mod.ThermalRecycling.items.ProcessingCore;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

public final class UpgradeRecipe extends ShapedOreRecipe {

	protected final int level;
	protected final int craftGridSlot = 4;
	
	public UpgradeRecipe(final int level, final ItemStack result, final Object... recipe) {
		super(result, recipe);
		
		this.level = level;
	}
	
    @Override
    public ItemStack getCraftingResult(final InventoryCrafting ic) {
    	
    	// Check the slot position in the crafting grid for
    	// the thing that is being upgraded.  If not present
    	// delegate to the super.
		final ItemStack stack = ic.getStackInSlot(craftGridSlot);
		if (stack == null)
			return super.getCraftingResult(ic);
		
		// Need to make sure the current level of the item is
		// appropriate for the level that is being crafted.
		final int itemLevel = ItemStackHelper.getItemLevel(stack);
		if (level != itemLevel + 1)
			return null;
		
		// The moons align!  Manufacture an appropriate item
		// and return it.
		final ItemStack crafted = stack.copy();
		ItemStackHelper.setLevel(crafted, level);
		
		if(level == ProcessingCore.MAX_CORE_LEVEL) {
			ItemStackHelper.makeGlow(crafted);
		}
		
		return crafted;
    }
}
