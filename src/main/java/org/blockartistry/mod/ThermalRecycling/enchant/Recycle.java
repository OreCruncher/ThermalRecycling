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

package org.blockartistry.mod.ThermalRecycling.enchant;

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;

public class Recycle extends EnchantmentBase {

	private static final Random random = new XorShiftRandom();
	private static final int EGG_DROP_CHANCE = ModOptions.getRecycleChance();
	private static final int WEIGHT = 1; // Same as Silk Touch

	public Recycle(final int id) {
		super(id, WEIGHT, EnumEnchantmentType.weapon);
		this.setName("Recycle");
	}
	
	@Override
	public boolean registerEvents() {
		return ModOptions.getRecycleChance() > 0;
	}

	@Override
	public int getMinEnchantability(final int level) {
		return 15;
	}

	@Override
	public int getMaxEnchantability(final int level) {
		return super.getMinEnchantability(level) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 1;
	}

	private boolean isProperDeath(final LivingDropsEvent event) {
		final DamageSource source = event.source;

		// If the source is not known...
		if (source == null)
			return false;

		// If the source isn't a player it's not proper.  Note that this
		// could include a fake player.
		if (!(source.getEntity() instanceof EntityPlayerMP))
			return false;

		final EntityPlayerMP player = (EntityPlayerMP) source.getEntity();
		final ItemStack heldItem = player.getHeldItem();
		return heldItem != null && EnchantmentHelper.getEnchantmentLevel(this.effectId, heldItem) > 0;
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onLivingDrops(final LivingDropsEvent event) {
		// If the entity died the right way and the random chance its...
		if (isProperDeath(event) && random.nextInt(EGG_DROP_CHANCE) == 0) {
			// Get the type ID of the entity that died
			final int entityId = EntityList.getEntityID(event.entity);
			if (entityId != 0 && EntityList.entityEggs.containsKey(entityId)) {
				final EntityItem item = new EntityItem(event.entity.worldObj, event.entity.posX, event.entity.posY,
						event.entity.posZ);
				item.setEntityItemStack(new ItemStack(Items.spawn_egg, 1, entityId));
				event.drops.add(item);
			}
		}
	}
}
