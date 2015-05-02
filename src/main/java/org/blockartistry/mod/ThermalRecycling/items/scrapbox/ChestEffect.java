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
import net.minecraftforge.common.ChestGenHooks;

/**
 * Drops items that would have randomly spawned in a dungeon chest. There is an
 * option to specify which gen hook to use.
 */
public class ChestEffect extends UseEffectWeightTable.UseEffectItem {

	public static final String MINESHAFT_CORRIDOR = ChestGenHooks.MINESHAFT_CORRIDOR;
	public static final String PYRAMID_DESERT_CHEST = ChestGenHooks.PYRAMID_DESERT_CHEST;
	public static final String PYRAMID_JUNGLE_CHEST = ChestGenHooks.PYRAMID_JUNGLE_CHEST;
	public static final String PYRAMID_JUNGLE_DISPENSER = ChestGenHooks.PYRAMID_JUNGLE_DISPENSER;
	public static final String STRONGHOLD_CORRIDOR = ChestGenHooks.STRONGHOLD_CORRIDOR;
	public static final String STRONGHOLD_LIBRARY = ChestGenHooks.STRONGHOLD_LIBRARY;
	public static final String STRONGHOLD_CROSSING = ChestGenHooks.STRONGHOLD_CROSSING;
	public static final String VILLAGE_BLACKSMITH = ChestGenHooks.VILLAGE_BLACKSMITH;
	public static final String BONUS_CHEST = ChestGenHooks.BONUS_CHEST;
	public static final String DUNGEON_CHEST = ChestGenHooks.DUNGEON_CHEST;

	String category;

	public ChestEffect(UseEffectWeightTable useEffectWeightTable, int weight, String category) {
		useEffectWeightTable.super(weight);
		this.category = category;
	}


	@Override
	public void apply(ItemStack scrap, World world, EntityPlayer player) {
		ChestGenHooks hooks = ChestGenHooks.getInfo(category);
		if (hooks != null) {
			ItemStack stack = hooks.getOneItem(rnd);
			UseEffect.spawnIntoWorld(stack, world, player);
		}
	}

	@Override
	public String toString() {
		return String.format("Chest Effect [%s]", category);
	}
}
