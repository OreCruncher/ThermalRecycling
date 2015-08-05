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

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public final class EntityEventHandler {
	
	private EntityEventHandler() {
	}
	
	// 1 in n
	private static final int SOYLENT_DROP_RATE = 3;
	
	private static boolean doDropSoylent() {
		return XorShiftRandom.shared.nextInt(SOYLENT_DROP_RATE) == 0;
	}

	private static boolean properSoylentDeath(final LivingDropsEvent event) {
		final DamageSource source = event.source;
		return source != null && source.getEntity() instanceof EntityPlayerMP
				&& !(source.getEntity() instanceof FakePlayer);
	}

	private static boolean soylentCandidate(final Entity entity) {
		return entity instanceof EntityVillager || entity instanceof EntityPlayerMP
				|| (entity instanceof EntityZombie && ((EntityZombie) entity)
						.isVillager());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onLivingDrops(final LivingDropsEvent event) {
		if (doDropSoylent() && soylentCandidate(event.entity) && properSoylentDeath(event)) {
			final EntityItem item = new EntityItem(event.entity.worldObj,
					event.entity.posX, event.entity.posY, event.entity.posZ);
			item.setEntityItemStack(new ItemStack(ItemManager.soylentGreen));
			event.drops.add(item);
		}
	}

	public static void register() {
		MinecraftForge.EVENT_BUS.register(new EntityEventHandler());
	}
}
