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

package org.blockartistry.mod.ThermalRecycling.tweaker;

import org.blockartistry.mod.ThermalRecycling.breeding.BreedingItemManager;
import minetweaker.api.item.IItemStack;
import minetweaker.api.minecraft.MineTweakerMC;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.recycling.BreedingItemRegistry")
public class BreedingItemRegistry {

	private static Class<? extends EntityAnimal> resolveClass(final String entityClass) {
		
		if(!MineTweakerUtil.checkNotNull(entityClass, "Entity class name cannot be null"))
			return null;
		
		if(!MineTweakerUtil.checkArgument(!entityClass.isEmpty(), "Entity class name is invalid"))
			return null;

		final Class<?> clazz = (Class<?>) EntityList.stringToClassMapping.get(entityClass);
		if(!MineTweakerUtil.checkNotNull(clazz, String.format("Unknown entity type '%s'", entityClass)))
			return null;
		
		if(!MineTweakerUtil.checkArgument(!clazz.isAssignableFrom(EntityHorse.class), "Unable to specify breeding items for horses"))
			return null;
		
		final Class<? extends EntityAnimal> animal = clazz.asSubclass(EntityAnimal.class);
		if(!MineTweakerUtil.checkNotNull(animal, "Entity is not an EntityAnimal type"))
			return null;
		
		return animal;
	}
	
	@ZenMethod
	public static void add(final String entityClass, final IItemStack breedingItem) {
		
		if(!MineTweakerUtil.checkNotNull(breedingItem, "Breeding item cannot be null"))
			return;
		
		final Class<? extends EntityAnimal> animal = resolveClass(entityClass);
		if(animal == null)
			return;
		
		final ItemStack stack = MineTweakerMC.getItemStack(breedingItem);
		BreedingItemManager.add(animal, stack);
	}
	
	@ZenMethod
	public static void remove(final String entityClass, final IItemStack breedingItem) {
		if(!MineTweakerUtil.checkNotNull(breedingItem, "Breeding item cannot be null"))
			return;
		
		final Class<? extends EntityAnimal> animal = resolveClass(entityClass);
		if(animal == null)
			return;
		
		final ItemStack stack = MineTweakerMC.getItemStack(breedingItem);
		BreedingItemManager.remove(animal, stack);
	}
}
