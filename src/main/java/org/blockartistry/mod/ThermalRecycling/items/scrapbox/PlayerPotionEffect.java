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

import java.lang.reflect.Field;

import org.blockartistry.mod.ThermalRecycling.ModLog;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * Gives the player a specific potion effect.
 *
 */
public class PlayerPotionEffect extends UseEffectWeightTable.UseEffectItem {

	private static Field isBadEffect = null;
	
	static {
		
		try {
			
			isBadEffect = ReflectionHelper.findField(Potion.class, "isBadEffect", "field_76418_K");
			
		} catch(Throwable t) {
			
			ModLog.warn("Unable to hook Potion.isBadEffect");
			
			;
		}
	}
	
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

	public PlayerPotionEffect(UseEffectWeightTable useEffectWeightTable,
			int weight, int duration, int amplifier) {
		this(useEffectWeightTable, weight, null, duration, amplifier, true);
	}

	public PlayerPotionEffect(UseEffectWeightTable useEffectWeightTable,
			int weight, Potion potion, int duration, int amplifier,
			boolean noBad) {
		useEffectWeightTable.super(weight);

		this.potion = potion;
		this.duration = duration;
		this.amplifier = amplifier;
		this.noBad = noBad;
	}

	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {
		Potion p = potion;
		
		// If no potion effect is defined, pick one randomly
		if (p == null) {
			// Keep looking until we have a non-null entry that
			// matches the noBad effect criteria.
			try {
				for (; p == null || (noBad && isBadEffect.getBoolean(p));) {
					p = Potion.potionTypes[rnd.nextInt(Potion.potionTypes.length)];
				}
			} catch (Throwable t) {
				;
			}
		}
		player.addPotionEffect(new PotionEffect(p.getId(), duration, amplifier));
	}

	@Override
	public String toString() {
		return String
				.format("Player Potion Effect [%s] (duration: %ds; amplifier: %d; no bad: %s)",
						potion == null ? "random" : potion.getName(),
						duration / 20, amplifier, Boolean.toString(noBad));
	}
}
