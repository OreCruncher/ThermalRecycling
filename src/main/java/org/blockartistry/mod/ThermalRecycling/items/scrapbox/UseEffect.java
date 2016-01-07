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

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.data.ScrapValue;
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;
import org.blockartistry.mod.ThermalRecycling.items.scrapbox.UseEffectWeightTable.UseEffectItem;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public final class UseEffect {

	private UseEffect() {
	}

	private static final TIntObjectHashMap<UseEffectWeightTable> effects = new TIntObjectHashMap<UseEffectWeightTable>();

	static {

		final ItemStack poorScrap = ScrappingTables.poorScrap;
		final ItemStack standardScrap = ScrappingTables.standardScrap;
		final ItemStack superiorScrap = ScrappingTables.superiorScrap;

		UseEffectWeightTable table = new UseEffectWeightTable();
		table.addNoUseEffect(330);
		table.addDropItemEffect(100, new ItemStack(Blocks.grass), 1);
		table.addDropItemEffect(100, new ItemStack(Items.cooked_beef), 4);
		table.addDropItemEffect(300, poorScrap, 3);
		table.addDropItemEffect(150, standardScrap, 2);
		table.addDropItemEffect(50, superiorScrap, 1);
		table.addDropItemEffect(5, new ItemStack(Items.diamond), 1);
		table.addDropItemEffect(3, new ItemStack(Items.emerald), 1);
		table.addExperienceEffect(200, 7);
		table.addExperienceEffect(50, 15);
		table.addExperienceEffect(10, 30);
		table.addEnchantedBookEffect(50, 10);
		table.addChestEffect(300, ChestGenHooks.DUNGEON_CHEST);
		table.addPotionEffect(150, PlayerPotionEffect.DURATION_15SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_1);
		table.addSpawnEntityEffect(50, "Bat", 4, null);
		table.addSpawnEntityEffect(15, "Villager", 1, null);
		effects.put(RecyclingScrapBox.POOR, table);

		table = new UseEffectWeightTable();
		table.addNoUseEffect(200);
		table.addDropItemEffect(170, poorScrap, 2);
		table.addDropItemEffect(300, standardScrap, 3);
		table.addDropItemEffect(170, superiorScrap, 2);
		table.addDropItemEffect(66, new ItemStack(Items.diamond), 1);
		table.addDropItemEffect(40, new ItemStack(Items.emerald), 1);
		table.addExperienceEffect(50, 7);
		table.addExperienceEffect(200, 15);
		table.addExperienceEffect(50, 30);
		table.addEnchantedBookEffect(50, 20);
		table.addChestEffect(200, ChestGenHooks.DUNGEON_CHEST);
		table.addChestEffect(300, ChestGenHooks.MINESHAFT_CORRIDOR);
		table.addPotionEffect(150, PlayerPotionEffect.DURATION_60SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_2);
		table.addBonusEffect(200, 2);
		table.addSpawnEntityEffect(50, "Bat", 4, null);
		effects.put(RecyclingScrapBox.STANDARD, table);

		table = new UseEffectWeightTable();
		table.addNoUseEffect(50);
		table.addDropItemEffect(50, poorScrap, 1);
		table.addDropItemEffect(150, standardScrap, 2);
		table.addDropItemEffect(300, superiorScrap, 3);
		table.addDropItemEffect(150, new ItemStack(Items.diamond), 1);
		table.addDropItemEffect(90, new ItemStack(Items.emerald), 1);
		table.addDropItemEffect(10, new ItemStack(Items.nether_star), 1);
		table.addExperienceEffect(10, 7);
		table.addExperienceEffect(50, 15);
		table.addExperienceEffect(200, 30);
		table.addEnchantedBookEffect(50, 30);
		table.addChestEffect(200, ChestGenHooks.MINESHAFT_CORRIDOR);
		table.addChestEffect(300, ChestGenHooks.STRONGHOLD_CROSSING);
		table.addPotionEffect(150, PlayerPotionEffect.DURATION_120SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_3);
		table.addBonusEffect(200, 2);
		// Zombie and Skeleton horses
		table.addSpawnEntityEffect(5, "EntityHorse", 1, "{Type:3,Tame:1,Saddle:1}");
		table.addSpawnEntityEffect(5, "EntityHorse", 1, "{Type:4,Tame:1,Saddle:1}");
		effects.put(RecyclingScrapBox.SUPERIOR, table);
	}

	/**
	 * Triggers the effect of using the scrap box that is currently held. Stack
	 * size will decrement accordingly.
	 */
	public static void triggerEffect(final ItemStack scrap, final World world, final EntityPlayer player) {

		if (scrap == null || scrap.stackSize < 1)
			return;

		if (doEffect(scrap, world, player))
			scrap.stackSize--;
	}

	/**
	 * Applies the effect for the specified scrap box ItemStack. No counts are
	 * adjusted.
	 */
	protected static boolean doEffect(final ItemStack scrapBox, final World world, final EntityPlayer player) {

		final UseEffectWeightTable theTable = effects.get(ItemStackHelper.getItemDamage(scrapBox));
		if (theTable != null && theTable.getTotalWeight() > 0)
			try {
				final UseEffectItem effect = theTable.next();
				if (effect != null) {
					effect.apply(scrapBox, world, player);
					return true;
				}
			} catch (final Exception e) {
				ModLog.error("Unable to apply scrapbox use effect", e);
			}
		return false;
	}

	public static void addNoUseEffect(final ScrapValue value, final int weight) {
		effects.get(value.ordinal() - 1).add(new NoUseEffect(weight));
	}

	public static void addDropItemEffect(final ScrapValue value, final int weight, final ItemStack stack,
			final int maxQuantity) {
		effects.get(value.ordinal() - 1).add(new DropItemEffect(weight, stack, maxQuantity));
	}

	public static void addExperienceEffect(final ScrapValue value, final int weight, final int amount) {
		effects.get(value.ordinal() - 1).add(new ExperienceOrbEffect(weight, amount));
	}

	public static void addEnchantedBookEffect(final ScrapValue value, final int weight, final int level) {
		effects.get(value.ordinal() - 1).add(new EnchantedBookEffect(weight, level));
	}

	public static void addChestEffect(final ScrapValue value, final int weight, final String chest) {
		effects.get(value.ordinal() - 1).add(new ChestEffect(weight, chest));
	}

	public static void addPotionEffect(final ScrapValue value, final int weight, final int duration,
			final int amplifier) {
		effects.get(value.ordinal() - 1).add(new PlayerPotionEffect(weight, duration, amplifier));
	}

	public static void addBonusEffect(final ScrapValue value, final int weight, final int count) {
		effects.get(value.ordinal() - 1).add(new BonusEffect(weight, count));
	}

	public static void addSpawnEntityEffect(final ScrapValue value, final int weight, final String entityType,
			final int maxCount, final String tags) {
		effects.get(value.ordinal() - 1).add(new SpawnEntityEffect(weight, entityType, maxCount, tags));
	}

	public static void reset(final ScrapValue value) {
		effects.put(value.ordinal() - 1, new UseEffectWeightTable());
	}

	public static void diagnostic(final Writer writer) throws IOException {
		writer.write("\n==========================\nScrapbox Use Effect Tables\n==========================\n");
		effects.get(RecyclingScrapBox.POOR).diagnostic("Poor Effects", writer);
		effects.get(RecyclingScrapBox.STANDARD).diagnostic("Standard Effects", writer);
		effects.get(RecyclingScrapBox.SUPERIOR).diagnostic("Superior Effects", writer);
	}
}
