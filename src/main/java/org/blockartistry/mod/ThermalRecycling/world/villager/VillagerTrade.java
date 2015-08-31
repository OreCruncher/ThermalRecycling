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

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class VillagerTrade {
	
	private static final Random random = XorShiftRandom.shared;

	private Item want1;
	private int meta1;
	private int min1;
	private int max1;
	private Item want2;
	private int meta2;
	private int min2;
	private int max2;
	private Item offer;
	private int offerMeta;
	private int offerMin;
	private int offerMax;
	private float probability;

	private static int quantity(final int min, final int max) {
		if(min == max) {
			return min;
		}
		
		return random.nextInt(max - min + 1) + min;
	}
	
	private static float adjustProbability(final float prob, final float factor) {
		final float f1 = prob + factor;
		return f1 > 0.9F ? 0.9F - (f1 - 0.9F) : f1;
	}

	public VillagerTrade() {
		
	}
	
	public VillagerTrade setWant(final Item give, final int meta, final int min, final int max) {
		return setWant(give, meta, min, max, null, 0, 0, 0);
	}
	
	// Standard emerald trade
	public VillagerTrade setWant(int min, int max) {
		return setWant(Items.emerald, 0, min, max);
	}
	
	public VillagerTrade setWant(final Item give1, final int meta1, final int min1, final int max1, final Item give2, final int meta2, final int min2, final int max2) {
		assert give1 != null;
		assert max1 >= min1;
		assert meta1 >= 0;
		
		this.want1 = give1;
		this.meta1 = meta1;
		this.min1 = min1;
		this.max1 = max1;
		this.want2 = give2;
		this.meta2 = meta2;
		this.min2 = min2;
		this.max2 = max2;
		return this;
	}
	
	public VillagerTrade setOffer(final Item offer, final int meta, final int min, final int max) {
		this.offer = offer;
		this.offerMeta = meta;
		this.offerMin = min;
		this.offerMax = max;
		return this;
	}

	public VillagerTrade setOffer(final Block offer, final int meta, final int min, final int max) {
		assert offer != null;
		assert max >= min;
		assert meta >= 0;
		
		return setOffer(Item.getItemFromBlock(offer), meta, min, max);
	}

	public VillagerTrade setProbability(final float prob) {
		assert prob > 0.0F;
		
		this.probability = prob;
		return this;
	}
	
	public ItemStack getFirstWant() {
		return want1 != null ? new ItemStack(want1, quantity(min1, max1), meta1) : null;
	}
	
	public ItemStack getSecondWant() {
		return want2 != null ? new ItemStack(want2, quantity(min2, max2), meta2) : null;
	}
	
	public ItemStack getOffer() {
		return new ItemStack(offer, quantity(offerMin, offerMax), offerMeta);
	}

	public boolean isTradeAvailable(final float factor) {
		return random.nextFloat() < adjustProbability(probability, factor);
	}
}
