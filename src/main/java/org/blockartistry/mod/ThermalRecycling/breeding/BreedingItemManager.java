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

package org.blockartistry.mod.ThermalRecycling.breeding;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackKey;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public final class BreedingItemManager {

	private BreedingItemManager() {

	}

	private static final Map<Class<? extends EntityAnimal>, Set<ItemStackKey>> foodItems = new HashMap<Class<? extends EntityAnimal>, Set<ItemStackKey>>();

	public static void register() {
		MinecraftForge.EVENT_BUS.register(new BreedingItemManager());
	}

	public static void add(final Class<? extends EntityAnimal> clazz, final ItemStack stack) {
		Set<ItemStackKey> targetsFood = foodItems.get(clazz);
		if(targetsFood == null) {
			targetsFood = new HashSet<ItemStackKey>();
			foodItems.put(clazz, targetsFood);
		}
		targetsFood.add(new ItemStackKey(stack));
	}
	
	public static void remove(final Class<? extends EntityAnimal> clazz, final ItemStack stack) {
		final Set<ItemStackKey> targetFoods = foodItems.get(clazz);
		if(targetFoods != null)
			targetFoods.remove(ItemStackKey.getCachedKey(stack));
	}
	
	protected boolean canBreed(final EntityAnimal animal, final EntityPlayerMP player) {
		if(animal.getGrowingAge() != 0 || animal.isInLove())
			return false;
		
		return !(animal instanceof EntityHorse);
	}

	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = false)
	public void entityInteract(final EntityInteractEvent event) {
		if(event.entityPlayer instanceof EntityPlayerMP && event.target instanceof EntityAnimal) {
			final EntityPlayerMP player = (EntityPlayerMP)event.entityPlayer;
			final ItemStack heldItem = player.getCurrentEquippedItem();
			if(heldItem != null) {
				final EntityAnimal animal = (EntityAnimal)event.target;
				if(canBreed(animal, player)) {
					final Set<ItemStackKey> possibleFood = foodItems.get(event.target.getClass());
					if(possibleFood != null && possibleFood.contains(ItemStackKey.getCachedKey(heldItem))) {
						if (!event.entityPlayer.capabilities.isCreativeMode) {
							heldItem.stackSize--;
							if (heldItem.stackSize <= 0)
								player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
						}
						animal.func_146082_f(player);
					}
				}
			}
		}
	}
}
