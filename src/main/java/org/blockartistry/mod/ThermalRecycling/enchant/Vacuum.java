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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.data.registry.ItemRegistry;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;

public class Vacuum extends EnchantmentBase {

	private static final int WEIGHT = 8; // Slightly less than efficiency
	private static final double SPEED = 0.035D;

	public Vacuum(final int id) {
		super(id, WEIGHT, EnumEnchantmentType.digger);

		setName("Vacuum");
	}

	@Override
	public int getMinEnchantability(final int level) {
		return 1 + 10 * (level - 1);
	}

	@Override
	public int getMaxEnchantability(final int level) {
		return super.getMinEnchantability(level) + 50;
	}

	@Override
	public int getMaxLevel() {
		return 5;
	}

	/**
	 * Enchant can be applied to tools, swords, bows, shears, and hoes.
	 */
	@Override
	public boolean canApply(final ItemStack stack) {
		if (stack == null)
			return false;

		final Item item = stack.getItem();
		if (item instanceof ItemTool || item instanceof ItemSword || item instanceof ItemBow)
			return true;
		return item == Items.shears || item == Items.diamond_hoe || item == Items.golden_hoe || item == Items.iron_hoe;
	}

	@SubscribeEvent
	public void LivingEvent(final LivingUpdateEvent event) {
		if (event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;
			if (player.worldObj.isRemote)
				return;
			applyEffect(player);
		}
	}

	public void applyEffect(final EntityPlayer player) {
		// Have to have an item equipped, and it has to be
		// able to have the enchantment.
		final ItemStack heldItem = player.getCurrentEquippedItem();
		if (!canApply(heldItem))
			return;

		// Get the enchantment level on the item. If it is 0
		// it means it is not present.
		final int level = EnchantmentHelper.getEnchantmentLevel(this.effectId, heldItem);
		if (level == 0)
			return;

		// Calculate the range based on the enchantment level.
		final double range = 2D * level;

		// The bounding box for the search area
		final AxisAlignedBB box = player.boundingBox.expand(range, range, range);

		// Get all of the EntityItems and XP orbs within the
		// bounding box around the player. Note this is a cube
		// in shape. Though an item shows in this list doesn't
		// mean it will be vacuumed.
		@SuppressWarnings("unchecked")
		final List<Entity> nearbyItems = player.worldObj.getEntitiesWithinAABB(EntityItem.class, box);
		@SuppressWarnings("unchecked")
		final List<Entity> xpOrbs = player.worldObj.getEntitiesWithinAABB(EntityXPOrb.class, box);
		nearbyItems.addAll(xpOrbs);

		for (final Entity item : nearbyItems) {

			if (item instanceof EntityItem && ItemRegistry.isBlockedFromVacuum(((EntityItem) item).getEntityItem()))
				continue;

			final double x = player.posX + 0.5D - item.posX;
			final double y = player.posY + 1.0D - item.posY;
			final double z = player.posZ + 0.5D - item.posZ;
			final double distance = Math.sqrt(x * x + y * y + z * z);

			// The actual effect is in a radius (circle) so it is
			// possible that some items will not be affected because
			// the list query operated on cubic space.
			if (distance > range)
				continue;

			// If the item distance is close just have the player
			// suck it up. Otherwise, put the item in motion
			// toward the player.
			if (distance < 1.25D) {
				item.onCollideWithPlayer(player);
			} else {
				item.motionX += x / distance * SPEED;
				item.motionZ += z / distance * SPEED;
				if (y > 0.0D) {
					item.motionY = 0.12D;
				} else {
					item.motionY += y * SPEED;
				}
			}
		}
	}
}
