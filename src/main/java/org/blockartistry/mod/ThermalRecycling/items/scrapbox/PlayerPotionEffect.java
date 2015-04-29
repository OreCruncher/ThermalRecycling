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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Gives the player a specific potion effect.
 *
 */
public class PlayerPotionEffect extends UseEffect {

	public static final int AMPLIFIER_LEVEL_1 = 0;
	public static final int AMPLIFIER_LEVEL_2 = 1;
	public static final int AMPLIFIER_LEVEL_3 = 2;

	public static final int DURATION_5SECONDS = 5 * 20;
	public static final int DURATION_15SECONDS = 15 * 20;
	public static final int DURATION_30SECONDS = 30 * 20;
	public static final int DURATION_60SECONDS = 60 * 20;
	public static final int DURATION_120SECONDS = 120 * 20;

	Potion potion;
	int duration;
	int amplifier;
	boolean noBad;

	public PlayerPotionEffect(int duration, int amplifier) {
		this(null, duration, amplifier, true);
	}

	public PlayerPotionEffect(Potion potion, int duration, int amplifier,
			boolean noBad) {
		this.potion = potion;
		this.duration = duration;
		this.amplifier = amplifier;
		this.noBad = noBad;
	}

	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {
		Potion p = potion;
		if (p == null) {
			for (; p == null;) {
				int index = rand.nextInt(Potion.potionTypes.length);
				if (!(noBad && Potion.potionTypes[index].isBadEffect()))
					p = Potion.potionTypes[index];
			}
		}
		player.addPotionEffect(new PotionEffect(p.getId(), duration, amplifier));
	}

	@Override
	public String toString() {
		return String.format("Potion [%s]",
				potion == null ? "random" : potion.getName());
	}
}
