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

package org.blockartistry.mod.ThermalRecycling.data.registry;

import org.blockartistry.mod.ThermalRecycling.util.ClassCollection;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

class DamagableItemProfile extends SingleItemProfile {
	
	private static final ClassCollection generic = new ClassCollection();
	
	static {
		generic.add("cofh.api.energy.IEnergyContainerItem");
		generic.add("ic2.api.item.IElectricItem");
	}

	private final boolean exactMatch;
	
	public DamagableItemProfile(final Item item) {
		super(item);
		this.exactMatch = !generic.isAssignableFrom(item);
	}

	/*
	 * Intercept the call to check the incoming stack damage
	 * state vs. the virgin meta.  If they don't match the
	 * recipe does not apply.
	 */
	@Override
	public RecipeData getRecipe(final ItemStack stack) {
		if(this.exactMatch && stack.isItemDamaged())
			return RecipeData.EPHEMERAL;
		
		return super.getRecipe(stack);
	}
}
