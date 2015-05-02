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

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

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
import net.minecraft.world.World;

/**
 * All entries in the ScrapBox use tables are derived from this class.
 *
 */
public final class UseEffect {

	static final UseEffectWeightTable poorEffects = new UseEffectWeightTable();
	static final UseEffectWeightTable standardEffects = new UseEffectWeightTable();
	static final UseEffectWeightTable superiorEffects = new UseEffectWeightTable();

	static {

		ItemStack poorScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.POOR);
		ItemStack standardScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.STANDARD);
		ItemStack superiorScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.SUPERIOR);

		poorEffects.add(new NoUseEffect(poorEffects, 400));
		poorEffects.add(new DropItemEffect(poorEffects, 100, new ItemStack(Blocks.grass), 1));
		poorEffects.add(new DropItemEffect(poorEffects, 100, new ItemStack(Items.cooked_beef), 4));
		poorEffects.add(new DropItemEffect(poorEffects, 300, poorScrap, 1));
		poorEffects.add(new DropItemEffect(poorEffects, 150, standardScrap, 1));
		poorEffects.add(new DropItemEffect(poorEffects, 50, superiorScrap, 1));
		poorEffects.add(new ExperienceOrbEffect(poorEffects, 200, 7));
		poorEffects.add(new ExperienceOrbEffect(poorEffects, 50, 15));
		poorEffects.add(new ExperienceOrbEffect(poorEffects, 10, 30));
		poorEffects.add(new EnchantedBookEffect(poorEffects, 50, 10));
		poorEffects.add(new ChestEffect(poorEffects, 300, ChestEffect.DUNGEON_CHEST));
		poorEffects.add(new PlayerPotionEffect(poorEffects, 150, PlayerPotionEffect.DURATION_15SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_1));
		poorEffects.add(new DropItemEffect(poorEffects, 5, new ItemStack(Items.diamond), 1));
		poorEffects.add(new DropItemEffect(poorEffects, 3, new ItemStack(Items.emerald), 1));

		standardEffects.add(new NoUseEffect(standardEffects, 250));
		standardEffects.add(new DropItemEffect(standardEffects, 170, poorScrap, 1));
		standardEffects.add(new DropItemEffect(standardEffects, 300, standardScrap, 1));
		standardEffects.add(new DropItemEffect(standardEffects, 170, superiorScrap, 1));
		standardEffects.add(new BonusEffect(standardEffects, 200, 2));
		standardEffects.add(new EnchantedBookEffect(standardEffects, 50, 20));
		standardEffects.add(new ExperienceOrbEffect(standardEffects, 50, 7));
		standardEffects.add(new ExperienceOrbEffect(standardEffects, 200, 15));
		standardEffects.add(new ExperienceOrbEffect(standardEffects, 50, 30));
		standardEffects.add(new ChestEffect(standardEffects, 200, ChestEffect.DUNGEON_CHEST));
		standardEffects.add(new ChestEffect(standardEffects, 300, ChestEffect.MINESHAFT_CORRIDOR));
		standardEffects.add(new PlayerPotionEffect(standardEffects, 150, PlayerPotionEffect.DURATION_60SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_2));
		standardEffects.add(new DropItemEffect(standardEffects, 100, new ItemStack(Items.diamond), 1));
		standardEffects.add(new DropItemEffect(standardEffects, 60, new ItemStack(Items.emerald), 1));

		superiorEffects.add(new NoUseEffect(superiorEffects, 50));
		superiorEffects.add(new DropItemEffect(superiorEffects, 50, poorScrap, 1));
		superiorEffects.add(new DropItemEffect(superiorEffects, 150, standardScrap, 1));
		superiorEffects.add(new DropItemEffect(superiorEffects, 300, superiorScrap, 1));
		superiorEffects.add(new BonusEffect(superiorEffects, 200, 1));
		superiorEffects.add(new ExperienceOrbEffect(superiorEffects, 10, 7));
		superiorEffects.add(new ExperienceOrbEffect(superiorEffects, 50, 15));
		superiorEffects.add(new ExperienceOrbEffect(superiorEffects, 200, 30));
		superiorEffects.add(new EnchantedBookEffect(superiorEffects, 50, 30));
		superiorEffects.add(new ChestEffect(superiorEffects, 200, ChestEffect.MINESHAFT_CORRIDOR));
		superiorEffects.add(new ChestEffect(superiorEffects, 300, ChestEffect.STRONGHOLD_CROSSING));
		superiorEffects.add(new PlayerPotionEffect(superiorEffects, 150, PlayerPotionEffect.DURATION_120SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_3));
		superiorEffects.add(new DropItemEffect(superiorEffects, 150, new ItemStack(Items.diamond), 1));
		superiorEffects.add(new DropItemEffect(superiorEffects, 90, new ItemStack(Items.emerald), 1));
		superiorEffects.add(new DropItemEffect(superiorEffects, 10, new ItemStack(Items.nether_star), 1));
	}

	protected static void dumpList(String name,
			ArrayList<WeightedRandomEffect> list) {

		int weight = 0;
		for (WeightedRandomEffect e : list)
			weight += e.itemWeight;

		ModLog.info("");
		ModLog.info("Loot table [%s] (total weight %d):", name, weight);
		for (WeightedRandomEffect e : list)
			ModLog.info("%s (%d) - %f%%", e.getEffect().toString(),
					e.itemWeight, ((float) e.itemWeight * 100) / (weight));
		ModLog.info("");
	}

	public static void spawnIntoWorld(ItemStack stack, World world,
			EntityPlayer player) {

		if (stack == null)
			return;

		Location loc = new Location(player);
		ItemStackHelper.spawnIntoWorld(world, stack, loc.x, loc.y, loc.z);

	}

	public static void spawnEntityIntoWorld(Entity entity, World world,
			EntityPlayer player) {

		if (entity == null)
			return;

		Location loc = new Location(player);
		entity.setPosition(loc.x, loc.y, loc.z);
		world.spawnEntityInWorld(entity);
	}

	public static void triggerEffect(ItemStack scrap, World world,
			EntityPlayer player) {

		UseEffectWeightTable theTable = poorEffects;

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
		theTable.next().apply(scrap1, world, player);

		// Subtract the real stack. Note that if this is a recursed call this
		// stack will
		// eventually be tossed. Upshot is that the root triggerEffect() will
		// update
		// the real ItemStack.
		scrap.stackSize--;
	}
	
	public static void diagnostic(Writer writer) throws IOException {
		poorEffects.diagnostic("Poor Effects", writer);
		standardEffects.diagnostic("Standard Effects", writer);
		superiorEffects.diagnostic("Superior Effects", writer);
	}
}
