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

package org.blockartistry.mod.ThermalRecycling.world.villager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

public class VillagerProfessionCustom extends VillagerProfession {

	private List<VillagerTrade> trades = new ArrayList<VillagerTrade>();

	public VillagerProfessionCustom(final String name, final int fColor, final int bColor) {
		super(-1, name, fColor, bColor);
	}
	
	public static int createProfesssion(final String name, final int fColor, final int bColor) {
		// Find an existing entry
		VillagerProfessionCustom vpc = null;
		for(int i = 0; i < professions.size(); i++) {
			final VillagerProfession prof = professions.get(i);
			if(StringUtils.equalsIgnoreCase(name, prof.getName())) {
				if(prof instanceof VillagerProfessionCustom) {
					return i;
				} else {
					
					// Not a custom type.  Return -1 for error.
					return -1;
				}
			}
		}
		
		// If we didn't find one add it
		final int idx = professions.size();
		vpc = new VillagerProfessionCustom(name, fColor, bColor);
		professions.add(vpc);
		return idx;
	}
	
	public static void addTrade(final int id, final VillagerTrade trade) {
		final VillagerProfession prof = professions.get(id);
		if(prof instanceof VillagerProfessionCustom) {
			final VillagerProfessionCustom vpc = (VillagerProfessionCustom) prof;
			vpc.addTrade(trade);
		}
	}
	
	public VillagerProfession addTrade(final VillagerTrade trade) {
		trades.add(trade);
		return this;
	}

	@SuppressWarnings("unchecked")
	public MerchantRecipeList getTradeList(final int count) {
		final MerchantRecipeList temp = new MerchantRecipeList();
		final float factor = MathHelper.sqrt_float((float) count) * 0.2F;
		
		for(final VillagerTrade t: trades) {
			if(t.isTradeAvailable(factor)) {
				final ItemStack want1 = t.getFirstWant();
				final ItemStack want2 = t.getSecondWant();
				final ItemStack offer = t.getOffer();
				temp.add(new MerchantRecipe(want1, want2, offer));
			}
		}
		
		if (temp.isEmpty()) {
			EntityVillager.func_146091_a(temp, Items.gold_ingot, random, 1.0F);
		} else {
			Collections.shuffle(temp);
		}

		final MerchantRecipeList result = new MerchantRecipeList();

		for (int l = 0; l < count && l < temp.size(); ++l) {
			result.add(temp.get(l));
		}

		return result;
	}

}
