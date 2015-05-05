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

package org.blockartistry.mod.ThermalRecycling.items.scrapbox;

import org.blockartistry.mod.ThermalRecycling.AchievementManager;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This effect causes a specific item type to drop into the world. Useful for
 * named items or special one offs, like nether stars.
 *
 */
public class DropItemEffect extends UseEffectWeightTable.UseEffectItem {

	ItemStack stack;
	int maxQuantity;

	public DropItemEffect(UseEffectWeightTable useEffectWeightTable,
			int weight, ItemStack stack, int quantity) {
		useEffectWeightTable.super(weight);

		this.stack = stack;
		this.maxQuantity = quantity;
	}

	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {
		ItemStack result = new ItemStack(stack.getItem(),
				rnd.nextInt(maxQuantity) + 1, scrap.getItemDamage());
		UseEffect.spawnIntoWorld(result, world, player);

		if (result.getItem() == Items.nether_star)
			player.addStat(AchievementManager.lottoWinner, 1);
	}

	@Override
	public String toString() {
		return String.format("Drop Item [%s] (up to %d)",
				ItemStackHelper.resolveName(stack), maxQuantity);
	}

}
