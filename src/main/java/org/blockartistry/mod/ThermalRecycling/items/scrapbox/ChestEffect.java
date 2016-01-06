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

import org.blockartistry.mod.ThermalRecycling.util.EntityHelper;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

/**
 * Drops items that would have randomly spawned in a dungeon chest. There is an
 * option to specify which gen hook to use.
 */
public final class ChestEffect extends UseEffectWeightTable.UseEffectItem {

	private final String category;

	public ChestEffect(final int weight, final String category) {
		super(weight);
		this.category = category;
	}

	@Override
	public void apply(final ItemStack scrap, final World world, final EntityPlayer player) {
		final ChestGenHooks hooks = ChestGenHooks.getInfo(this.category);
		if (hooks != null) {
			final ItemStack stack = ItemStackHelper.getPreferredStack(hooks.getOneItem(this.rnd)).get();
			if (stack != null)
				EntityHelper.spawnIntoWorld(stack, world, player);
		}
	}

	@Override
	public String toString() {
		return String.format("Chest Loot [%s]", this.category);
	}
}
