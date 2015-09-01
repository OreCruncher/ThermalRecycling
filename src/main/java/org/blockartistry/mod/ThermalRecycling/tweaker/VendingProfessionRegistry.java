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

import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerProfessionCustom;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerTrade;

import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.VendingProfessionRegistry")
public final class VendingProfessionRegistry {

	private VendingProfessionRegistry() {
	}

	@ZenMethod
	public static int createProfession(final String name, final int fColor, final int bColor, final int weight) {

		if (!MineTweakerUtil.checkNotNull(name, "name cannot be null"))
			return -1;

		if (!MineTweakerUtil.checkArgument(name.length() > 0, "invalid name length"))
			return -1;

		if (!MineTweakerUtil.checkArgument(DyeHelper.isValidColor(fColor), "invalid foreground color"))
			return -1;

		if (!MineTweakerUtil.checkArgument(DyeHelper.isValidColor(bColor), "invalid background color"))
			return -1;

		if (!MineTweakerUtil.checkArgument(fColor != bColor, "foreground and background colors must be different"))
			return -1;
		
		if(!MineTweakerUtil.checkArgument(weight > 0, "weight must be greater than 0"))
			return -1;

		return VillagerProfessionCustom.createProfesssion(name, fColor, bColor, weight);
	}
	
	@ZenMethod
	public static int findProfession(final String name) {
		if (!MineTweakerUtil.checkNotNull(name, "name cannot be null"))
			return -1;

		if (!MineTweakerUtil.checkArgument(name.length() > 0, "invalid name length"))
			return -1;

		return VillagerProfessionCustom.findProfession(name);
	}

	@ZenMethod
	public static void addTrade(final int id, final IItemStack give1, final int min1, final int max1,
			final IItemStack give2, final int min2, final int max2, final IItemStack offer, final int offerMin,
			final int offerMax, final float prob) {

		if (!MineTweakerUtil.checkArgument(id >= 0, "invalid profession id"))
			return;

		if (!MineTweakerUtil.checkNotNull(give1, "at least one item needs to be given"))
			return;

		if (!MineTweakerUtil.checkArgument(max1 >= min1, "give max has to be greater or equal to give min"))
			return;

		if (!MineTweakerUtil.checkNotNull(offer, "invalid offer item"))
			return;

		if (!MineTweakerUtil.checkArgument(offerMax >= offerMin, "offer max has to be greater or equal to offer min"))
			return;

		ItemStack g1 = MineTweakerMC.getItemStack(give1);
		ItemStack o = MineTweakerMC.getItemStack(offer);
		ItemStack g2 = null;
		if (give2 != null) {
			if (!MineTweakerUtil.checkArgument(max2 >= min2, "give2 max has to be greater or equal to give2 min"))
				return;

			g2 = MineTweakerMC.getItemStack(give2);
		}

		final Item i1 = g1.getItem();
		final int m1 = g1.getItemDamage();
		final Item i2 = g2 != null ? g2.getItem() : null;
		final int m2 = g2 != null ? g2.getItemDamage() : 0;
		final Item io = o.getItem();
		final int mo = o.getItemDamage();

		final VillagerTrade trade = new VillagerTrade().setWant(i1, m1, min1, max1, i2, m2, min2, max2)
				.setOffer(io, mo, offerMin, offerMax).setProbability(prob);
		VillagerProfessionCustom.addTrade(id, trade);
	}
}
