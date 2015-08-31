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

package org.blockartistry.mod.ThermalRecycling.world;

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.FakePlayerHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerProfession;

import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;

public final class VendingSetup {

	private static final Random random = XorShiftRandom.shared;

	private VendingSetup() {
	}

	/**
	 * Configures a Vending Machine for village operation.
	 *
	 * @param vte
	 *            Vending TileEntity to configure
	 */
	public static void configure(final VendingTileEntity vte) {

		final ItemStack[] inv = vte.getRawInventory();
		final VillagerProfession profession = VillagerProfession.randomProfession();
		final int count = 2 + random.nextInt(5);

		// Use this method because if the spawn point is created before
		// the world is launched it is not possible to have a FakePlayer
		// "log in".
		vte.setOwnerId(FakePlayerHelper.getFakePlayerID());
		vte.setName(profession.getVendingTitle());
		vte.setNameBackgroundColor(profession.getBackgroundColor());
		vte.setNameColor(profession.getForegroundColor());

		int index = 0;
		for (final Object rm : profession.getTradeList(count)) {
			if (rm instanceof MerchantRecipe) {
				final MerchantRecipe mr = (MerchantRecipe)rm;
				final int base = index + VendingTileEntity.CONFIG_SLOT_START;
				inv[base] = mr.getItemToBuy().copy();
				if (mr.hasSecondItemToBuy())
					inv[base + 6] = mr.getSecondItemToBuy().copy();
				final ItemStack stack = inv[base + 12] = mr.getItemToSell().copy();

				// Put items in inventory to give to the player
				int stacks = 0;
				if (stack.isStackable())
					stacks = random.nextInt(6) + random.nextInt(6) + 2;
				else
					stacks = random.nextInt(3) + 1;

				for (int i = 0; i < stacks; i++)
					vte.addStackToOutput(stack.copy());

				index++;
			}
		}
	}
}
