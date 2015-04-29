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

import java.util.ArrayList;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.Location;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;

/**
 * All entries in the ScrapBox use tables are derived from this class.
 *
 */
public abstract class UseEffect {

	protected static final Random rand = new Random();
	protected static final ArrayList<WeightedRandomEffect> poorEffects = new ArrayList<WeightedRandomEffect>();
	protected static final ArrayList<WeightedRandomEffect> standardEffects = new ArrayList<WeightedRandomEffect>();
	protected static final ArrayList<WeightedRandomEffect> superiorEffects = new ArrayList<WeightedRandomEffect>();

	static void addEffect(ArrayList<WeightedRandomEffect> list, UseEffect effect, int weight) {
		list.add(new WeightedRandomEffect(effect, weight));
	}
	
	protected static void dumpList(String name, ArrayList<WeightedRandomEffect> list) {
		
		int weight = 0;
		for(WeightedRandomEffect e: list)
			weight += e.itemWeight;

		ModLog.info("");
		ModLog.info("Loot table [%s] (total weight %d):", name, weight);
		for(WeightedRandomEffect e: list)
			ModLog.info("%s (%d) - %f%%", e.getEffect().toString(), e.itemWeight, ((float)e.itemWeight * 100) / ((float)weight));
		ModLog.info("");
	}
	
	public static void initialize() {
		
		ItemStack poorScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.POOR);
		ItemStack standardScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.STANDARD);
		ItemStack superiorScrap = new ItemStack(ItemManager.recyclingScrap, 1, RecyclingScrap.SUPERIOR);
		
		addEffect(poorEffects, new NoUseEffect(), 400);
		addEffect(poorEffects, new DropItemEffect(new ItemStack(Blocks.grass)), 100);
		addEffect(poorEffects, new DropItemEffect(new ItemStack(Items.cooked_beef), 4), 100);
		addEffect(poorEffects, new DropItemEffect(poorScrap), 300);
		addEffect(poorEffects, new DropItemEffect(standardScrap), 150);
		addEffect(poorEffects, new DropItemEffect(superiorScrap), 50);
		addEffect(poorEffects, new ExperienceOrbEffect(7), 200);
		addEffect(poorEffects, new ExperienceOrbEffect(15), 50);
		addEffect(poorEffects, new ExperienceOrbEffect(30), 10);
		addEffect(poorEffects, new EnchantedBookEffect(10), 50);
		addEffect(poorEffects, new ChestEffect(ChestEffect.DUNGEON_CHEST), 300);
		addEffect(poorEffects, new PlayerPotionEffect(PlayerPotionEffect.DURATION_15SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_1), 150);
		addEffect(poorEffects, new DropItemEffect(new ItemStack(Items.diamond)), 5);
		addEffect(poorEffects, new DropItemEffect(new ItemStack(Items.emerald)), 3);
		dumpList("Poor Effects", poorEffects);

		addEffect(standardEffects, new NoUseEffect(), 250);
		addEffect(standardEffects, new DropItemEffect(poorScrap), 170);
		addEffect(standardEffects, new DropItemEffect(standardScrap), 300);
		addEffect(standardEffects, new DropItemEffect(superiorScrap), 170);
		addEffect(standardEffects, new BonusEffect(2), 200);
		addEffect(standardEffects, new EnchantedBookEffect(20), 50);
		addEffect(standardEffects, new ExperienceOrbEffect(7), 50);
		addEffect(standardEffects, new ExperienceOrbEffect(15), 200);
		addEffect(standardEffects, new ExperienceOrbEffect(30), 50);
		addEffect(standardEffects, new ChestEffect(ChestEffect.DUNGEON_CHEST), 200);
		addEffect(standardEffects, new ChestEffect(ChestEffect.MINESHAFT_CORRIDOR), 300);
		addEffect(standardEffects, new PlayerPotionEffect(PlayerPotionEffect.DURATION_60SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_2), 150);
		addEffect(standardEffects, new DropItemEffect(new ItemStack(Items.diamond)), 100);
		addEffect(standardEffects, new DropItemEffect(new ItemStack(Items.emerald)), 60);
		dumpList("Standard Effects", standardEffects);

		addEffect(superiorEffects, new NoUseEffect(), 50);
		addEffect(superiorEffects, new DropItemEffect(poorScrap), 50);
		addEffect(superiorEffects, new DropItemEffect(standardScrap), 150);
		addEffect(superiorEffects, new DropItemEffect(superiorScrap), 300);
		addEffect(superiorEffects, new BonusEffect(1), 200);
		addEffect(superiorEffects, new ExperienceOrbEffect(7), 10);
		addEffect(superiorEffects, new ExperienceOrbEffect(15), 50);
		addEffect(superiorEffects, new ExperienceOrbEffect(30), 200);
		addEffect(superiorEffects, new EnchantedBookEffect(30), 50);
		addEffect(superiorEffects, new ChestEffect(ChestEffect.MINESHAFT_CORRIDOR), 200);
		addEffect(superiorEffects, new ChestEffect(ChestEffect.STRONGHOLD_CROSSING), 300);
		addEffect(superiorEffects, new PlayerPotionEffect(PlayerPotionEffect.DURATION_120SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_3), 150);
		addEffect(superiorEffects, new DropItemEffect(new ItemStack(Items.diamond)), 150);
		addEffect(superiorEffects, new DropItemEffect(new ItemStack(Items.emerald)), 90);
		addEffect(superiorEffects, new DropItemEffect(new ItemStack(Items.nether_star)), 10);
		dumpList("Superior Effects", superiorEffects);
	}

	/**
	 * Called when the effect this instance represents is desired. The base
	 * implementation handles the quantity in the stack. This behavior can be
	 * changed in a derived class by not calling the super.
	 * 
	 * This method should not be called outside of this class. It is public to
	 * make testing easier.
	 * 
	 * @param scrap
	 *            ItemStack of scrap being used
	 * @param world
	 * @param player
	 */
	public abstract void apply(ItemStack scrap, World world, EntityPlayer player);

	protected void spawnIntoWorld(ItemStack stack, World world,
			EntityPlayer player) {

		if (stack == null)
			return;

		Location loc = new Location(player);
		ItemStackHelper.spawnIntoWorld(world, stack, loc.x, loc.y, loc.z);

	}

	protected void spawnEntityIntoWorld(Entity entity, World world,
			EntityPlayer player) {

		if (entity == null)
			return;

		Location loc = new Location(player);
		entity.setPosition(loc.x, loc.y, loc.z);
		world.spawnEntityInWorld(entity);
	}

	public static void triggerEffect(ItemStack scrap, World world,
			EntityPlayer player) {

		ArrayList<WeightedRandomEffect> theTable = poorEffects;

		// If null it means cascaded effects.
		if (scrap != null) {
			if (scrap.getItemDamage() == RecyclingScrapBox.STANDARD)
				theTable = standardEffects;
			else if (scrap.getItemDamage() == RecyclingScrapBox.SUPERIOR)
				theTable = superiorEffects;
		}

		// Copy the stack because the affect routine may recurse because of
		// bonus
		// effects
		ItemStack scrap1 = scrap.copy();
		WeightedRandomEffect e = (WeightedRandomEffect) WeightedRandom
				.getRandomItem(rand, theTable);
		e.getEffect().apply(scrap1, world, player);

		// Subtract the real stack. Note that if this is a recursed call this
		// stack will
		// eventually be tossed. Upshot is that the root triggerEffect() will
		// update
		// the real ItemStack.
		scrap.stackSize--;
	}
}
