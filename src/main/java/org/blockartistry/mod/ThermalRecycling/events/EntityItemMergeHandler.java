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

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public final class EntityItemMergeHandler {

	// Squared range for merge threshold
	private static final double MERGE_RANGE = Math.pow(
			ModOptions.getEntityItemMergeRange(), 2f);

	// Number of ticks between merge attempts.
	private static final int MERGE_INTERVAL = 6;

	// Determine if a merge is possible at this time
	private static boolean doMerge() {
		return MinecraftServer.getServer().getTickCounter() % MERGE_INTERVAL == 0;
	}

	// Just need to concern ourselves with items that can stack and
	// have room in the stack to hold more items.
	private boolean hasStackRoom(final ItemStack stack) {
		return stack.isStackable() && stack.stackSize < stack.getMaxStackSize();
	}

	// Entities that are not dead and have expired delay pickup timers
	// are possible candidates.
	private boolean isPossibleEntity(final EntityItem entity) {
		return !entity.isDead && entity.delayBeforeCanPickup == 0;
	}

	// Gets a list of active EntityItems for the specified world. Also, the
	// EntityItems must have expired delay counters as well as space in their
	// stack for merging.
	private List<EntityItem> getEntityItems(final World world) {
		final List<EntityItem> result = new ArrayList<EntityItem>();
		for (final Object o : world.getLoadedEntityList()) {
			if (o instanceof EntityItem) {
				final EntityItem entity = (EntityItem) o;
				if (isPossibleEntity(entity)
						&& hasStackRoom(entity.getEntityItem())) {
					result.add(entity);
				}
			}
		}
		return result;
	}

	private void mergeItems(final World world) {

		final List<EntityItem> entities = getEntityItems(world);
		if (entities.size() > 1) {
			for (int i = 0; i < entities.size(); i++) {

				final EntityItem e = entities.get(i);

				if (!e.isDead && hasStackRoom(e.getEntityItem())) {
					for (int j = i + 1; j < entities.size(); j++) {
						final EntityItem source = entities.get(j);
						if (!source.isDead
								&& e.getDistanceSqToEntity(source) <= MERGE_RANGE
								&& e.combineItems(source))
							break;
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onWorldTickEvent(final WorldTickEvent event) {
		if (event.side == Side.SERVER && event.phase == Phase.END && doMerge()) {
			mergeItems(event.world);
		}
	}

	public EntityItemMergeHandler() {
		if (MERGE_RANGE > 0) {
			ModLog.info("Item merging enabled with a range of %f",
					ModOptions.getEntityItemMergeRange());
			FMLCommonHandler.instance().bus().register(this);
		}
	}
}
