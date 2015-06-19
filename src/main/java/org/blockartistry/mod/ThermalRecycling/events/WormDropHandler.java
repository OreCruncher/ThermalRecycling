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

package org.blockartistry.mod.ThermalRecycling.events;

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.items.Material;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;

import com.google.common.base.Predicate;

public class WormDropHandler implements Predicate<HarvestDropsEvent> {

	private static final Random random = XorShiftRandom.shared;
	private static final int DROP_CHANCE = ModOptions.getWormDropChance();
	private static final int DROP_CHANCE_RAIN = ModOptions.getWormDropChanceRain();
	
	private static boolean dropWorms(final World world) {
		return random.nextInt(world.isRaining() ? DROP_CHANCE_RAIN : DROP_CHANCE) == 0;
	}
	
	@Override
	public boolean apply(final HarvestDropsEvent input) {
		
		// Chance of dropping worms IIF not silk touching, it is a player
		// performing the action (no machines), and the block is grass.
		if(input.isSilkTouching || input.harvester == null || input.harvester instanceof FakePlayer || input.block != Blocks.grass)
			return true;
		
		if(dropWorms(input.world)) {
			input.drops.add(new ItemStack(ItemManager.material, 1, Material.WORMS));
		}
		
		return true;
	}
}
