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
import org.blockartistry.mod.ThermalRecycling.ItemManager;
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

public final class UseEffect {
	
	private UseEffect() {}

	static final UseEffectWeightTable poorEffects = new UseEffectWeightTable();
	static final UseEffectWeightTable standardEffects = new UseEffectWeightTable();
	static final UseEffectWeightTable superiorEffects = new UseEffectWeightTable();

	static {

		final ItemStack poorScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.POOR);
		final ItemStack standardScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.STANDARD);
		final ItemStack superiorScrap = new ItemStack(ItemManager.recyclingScrap, 1,
				RecyclingScrap.SUPERIOR);

		poorEffects.add(new NoUseEffect(400));
		poorEffects.add(new DropItemEffect(100, new ItemStack(
				Blocks.grass), 1));
		poorEffects.add(new DropItemEffect(100, new ItemStack(
				Items.cooked_beef), 4));
		poorEffects.add(new DropItemEffect(300, poorScrap, 1));
		poorEffects.add(new DropItemEffect(150, standardScrap, 1));
		poorEffects.add(new DropItemEffect(50, superiorScrap, 1));
		poorEffects.add(new ExperienceOrbEffect(200, 7));
		poorEffects.add(new ExperienceOrbEffect(50, 15));
		poorEffects.add(new ExperienceOrbEffect(10, 30));
		poorEffects.add(new EnchantedBookEffect(50, 10));
		poorEffects.add(new ChestEffect(300, ChestEffect.DUNGEON_CHEST));
		poorEffects.add(new PlayerPotionEffect(150,
				PlayerPotionEffect.DURATION_15SECONDS,
				PlayerPotionEffect.AMPLIFIER_LEVEL_1));
		poorEffects.add(new DropItemEffect(5, new ItemStack(
				Items.diamond), 1));
		poorEffects.add(new DropItemEffect(3, new ItemStack(
				Items.emerald), 1));

		standardEffects.add(new NoUseEffect(250));
		standardEffects.add(new DropItemEffect(170, poorScrap,
				1));
		standardEffects.add(new DropItemEffect(300,
				standardScrap, 1));
		standardEffects.add(new DropItemEffect(170,
				superiorScrap, 1));
		standardEffects.add(new BonusEffect(200, 2));
		standardEffects.add(new EnchantedBookEffect(50, 20));
		standardEffects.add(new ExperienceOrbEffect(50, 7));
		standardEffects.add(new ExperienceOrbEffect(200, 15));
		standardEffects.add(new ExperienceOrbEffect(50, 30));
		standardEffects.add(new ChestEffect(200,
				ChestEffect.DUNGEON_CHEST));
		standardEffects.add(new ChestEffect(300,
				ChestEffect.MINESHAFT_CORRIDOR));
		standardEffects.add(new PlayerPotionEffect(150,
				PlayerPotionEffect.DURATION_60SECONDS,
				PlayerPotionEffect.AMPLIFIER_LEVEL_2));
		standardEffects.add(new DropItemEffect(100,
				new ItemStack(Items.diamond), 1));
		standardEffects.add(new DropItemEffect(60,
				new ItemStack(Items.emerald), 1));

		superiorEffects.add(new NoUseEffect(50));
		superiorEffects.add(new DropItemEffect(50, poorScrap,
				1));
		superiorEffects.add(new DropItemEffect(150,
				standardScrap, 1));
		superiorEffects.add(new DropItemEffect(300,
				superiorScrap, 1));
		superiorEffects.add(new BonusEffect(200, 1));
		superiorEffects.add(new ExperienceOrbEffect(10, 7));
		superiorEffects.add(new ExperienceOrbEffect(50, 15));
		superiorEffects.add(new ExperienceOrbEffect(200, 30));
		superiorEffects.add(new EnchantedBookEffect(50, 30));
		superiorEffects.add(new ChestEffect(200,
				ChestEffect.MINESHAFT_CORRIDOR));
		superiorEffects.add(new ChestEffect(300,
				ChestEffect.STRONGHOLD_CROSSING));
		superiorEffects.add(new PlayerPotionEffect(150,
				PlayerPotionEffect.DURATION_120SECONDS,
				PlayerPotionEffect.AMPLIFIER_LEVEL_3));
		superiorEffects.add(new DropItemEffect(150,
				new ItemStack(Items.diamond), 1));
		superiorEffects.add(new DropItemEffect(90,
				new ItemStack(Items.emerald), 1));
		superiorEffects.add(new DropItemEffect(10,
				new ItemStack(Items.nether_star), 1));
	}

	public static void spawnIntoWorld(final ItemStack stack, final World world,
			final EntityPlayer player) {

		if (stack == null)
			return;

		final Location loc = new Location(player);
		ItemStackHelper.spawnIntoWorld(world, stack, loc.x, loc.y, loc.z);

	}

	public static void spawnEntityIntoWorld(final Entity entity, final World world,
			final EntityPlayer player) {

		if (entity == null)
			return;

		final Location loc = new Location(player);
		entity.setPosition(loc.x, loc.y, loc.z);
		world.spawnEntityInWorld(entity);
	}

	public static void triggerEffect(final ItemStack scrap, final World world,
			final EntityPlayer player) {

		UseEffectWeightTable theTable = poorEffects;

		// If null it means cascaded effects.
		ItemStack scrap1 = null;
		if (scrap != null) {
			if (scrap.getItemDamage() == RecyclingScrapBox.STANDARD)
				theTable = standardEffects;
			else if (scrap.getItemDamage() == RecyclingScrapBox.SUPERIOR)
				theTable = superiorEffects;
			scrap1 = scrap.copy();
		}

		theTable.next().apply(scrap1, world, player);

		// Subtract the real stack. Note that if this is a recursed call this
		// stack will eventually be tossed. Upshot is that the root
		// triggerEffect() will update the real ItemStack.
		scrap.stackSize--;
	}

	public static void diagnostic(final Writer writer) throws IOException {

		writer.write("\n==========================\nScrapbox Use Effect Tables\n==========================\n");
		poorEffects.diagnostic("Poor Effects", writer);
		standardEffects.diagnostic("Standard Effects", writer);
		superiorEffects.diagnostic("Superior Effects", writer);
	}
}
