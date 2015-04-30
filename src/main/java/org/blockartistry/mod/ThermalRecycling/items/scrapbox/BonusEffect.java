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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * This effect causes multiple effects to fire. It is possible for a player to
 * wind up with drops as well as buffs.
 *
 */
public class BonusEffect extends UseEffect {

	int bonusCount;

	/**
	 * Constructs a BonusEffect with the specified number of bonus effects. The
	 * count is NOT inclusive of the original so the actual number of effects is
	 * (1 + count).
	 * 
	 * @param count
	 *            Number of additional effects to trigger
	 */
	public BonusEffect(int count) {
		bonusCount = count;
	}

	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {

		int effectiveCount = bonusCount + 1;
		for (int i = 0; i < effectiveCount; i++) {
			triggerEffect(scrap, world, player);
		}
	}

	@Override
	public String toString() {
		return String.format("BonusEffect x%d", bonusCount);
	}
}
