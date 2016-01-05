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

package org.blockartistry.mod.ThermalRecycling.items.scrapbox;

import java.util.Random;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.blockartistry.mod.ThermalRecycling.util.WeightTable;

public final class UseEffectWeightTable extends WeightTable<UseEffectWeightTable.UseEffectItem> {

	public abstract static class UseEffectItem extends WeightTable.Item {

		public UseEffectItem(final int weight) {
			super(weight);
		}

		public abstract void apply(final ItemStack scrap, final World world, final EntityPlayer player);
	}

	public UseEffectWeightTable() {
		super();
	}

	public UseEffectWeightTable(final Random rand) {
		super(rand);
	}

	public void addNoUseEffect(final int weight) {
		this.add(new NoUseEffect(weight));
	}

	public void addDropItemEffect(final int weight, final ItemStack stack, final int maxQuantity) {
		this.add(new DropItemEffect(weight, stack, maxQuantity));
	}

	public void addExperienceEffect(final int weight, final int amount) {
		this.add(new ExperienceOrbEffect(weight, amount));
	}

	public void addEnchantedBookEffect(final int weight, final int level) {
		this.add(new EnchantedBookEffect(weight, level));
	}

	public void addChestEffect(final int weight, final String chest) {
		this.add(new ChestEffect(weight, chest));
	}

	public void addPotionEffect(final int weight, final int duration, final int amplifier) {
		this.add(new PlayerPotionEffect(weight, duration, amplifier));
	}
	
	public void addBonusEffect(final int weight, final int count) {
		this.add(new BonusEffect(weight, count));
	}
	
	public void addSpawnEntityEffect(final int weight, final String entityType, final int maxCount, final String tags) {
		this.add(new SpawnEntityEffect(weight, entityType, maxCount, tags));
	}
}