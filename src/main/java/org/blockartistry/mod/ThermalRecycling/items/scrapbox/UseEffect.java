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
import org.blockartistry.mod.ThermalRecycling.data.ScrappingTables;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;

public final class UseEffect {

	private UseEffect() {
	}

	private static final UseEffectWeightTable poorEffects = new UseEffectWeightTable();
	private static final UseEffectWeightTable standardEffects = new UseEffectWeightTable();
	private static final UseEffectWeightTable superiorEffects = new UseEffectWeightTable();

	static {

		final ItemStack poorScrap = ScrappingTables.poorScrap;
		final ItemStack standardScrap = ScrappingTables.standardScrap;
		final ItemStack superiorScrap = ScrappingTables.superiorScrap;

		poorEffects.addNoUseEffect(330);
		poorEffects.addDropItemEffect(100, new ItemStack(Blocks.grass), 1);
		poorEffects.addDropItemEffect(100, new ItemStack(Items.cooked_beef), 4);
		poorEffects.addDropItemEffect(300, poorScrap, 3);
		poorEffects.addDropItemEffect(150, standardScrap, 2);
		poorEffects.addDropItemEffect(50, superiorScrap, 1);
		poorEffects.addDropItemEffect(5, new ItemStack(Items.diamond), 1);
		poorEffects.addDropItemEffect(3, new ItemStack(Items.emerald), 1);
		poorEffects.addExperienceEffect(200, 7);
		poorEffects.addExperienceEffect(50, 15);
		poorEffects.addExperienceEffect(10, 30);
		poorEffects.addEnchantedBookEffect(50, 10);
		poorEffects.addChestEffect(300, ChestGenHooks.DUNGEON_CHEST);
		poorEffects.addPotionEffect(150, PlayerPotionEffect.DURATION_15SECONDS, PlayerPotionEffect.AMPLIFIER_LEVEL_1);
		poorEffects.addSpawnEntityEffect(50, "Bat", 4, null);
		poorEffects.addSpawnEntityEffect(15, "Villager", 1, null);

		standardEffects.addNoUseEffect(200);
		standardEffects.addDropItemEffect(170, poorScrap, 2);
		standardEffects.addDropItemEffect(300, standardScrap, 3);
		standardEffects.addDropItemEffect(170, superiorScrap, 2);
		standardEffects.addDropItemEffect(66, new ItemStack(Items.diamond), 1);
		standardEffects.addDropItemEffect(40, new ItemStack(Items.emerald), 1);
		standardEffects.addExperienceEffect(50, 7);
		standardEffects.addExperienceEffect(200, 15);
		standardEffects.addExperienceEffect(50, 30);
		standardEffects.addEnchantedBookEffect(50, 20);
		standardEffects.addChestEffect(200, ChestGenHooks.DUNGEON_CHEST);
		standardEffects.addChestEffect(300, ChestGenHooks.MINESHAFT_CORRIDOR);
		standardEffects.addPotionEffect(150, PlayerPotionEffect.DURATION_60SECONDS,
				PlayerPotionEffect.AMPLIFIER_LEVEL_2);
		standardEffects.addBonusEffect(200, 2);
		standardEffects.addSpawnEntityEffect(50, "Bat", 4, null);

		superiorEffects.addNoUseEffect(50);
		superiorEffects.addDropItemEffect(50, poorScrap, 1);
		superiorEffects.addDropItemEffect(150, standardScrap, 2);
		superiorEffects.addDropItemEffect(300, superiorScrap, 3);
		superiorEffects.addDropItemEffect(150, new ItemStack(Items.diamond), 1);
		superiorEffects.addDropItemEffect(90, new ItemStack(Items.emerald), 1);
		superiorEffects.addDropItemEffect(10, new ItemStack(Items.nether_star), 1);
		superiorEffects.addExperienceEffect(10, 7);
		superiorEffects.addExperienceEffect(50, 15);
		superiorEffects.addExperienceEffect(200, 30);
		superiorEffects.addEnchantedBookEffect(50, 30);
		superiorEffects.addChestEffect(200, ChestGenHooks.MINESHAFT_CORRIDOR);
		superiorEffects.addChestEffect(300, ChestGenHooks.STRONGHOLD_CROSSING);
		superiorEffects.addPotionEffect(150, PlayerPotionEffect.DURATION_120SECONDS,
				PlayerPotionEffect.AMPLIFIER_LEVEL_3);
		superiorEffects.addBonusEffect(200, 2);
		// Zombie and Skeleton horses
		superiorEffects.addSpawnEntityEffect(5, "EntityHorse", 1, "{Type:3,Tame:1,Saddle:1}");
		superiorEffects.addSpawnEntityEffect(5, "EntityHorse", 1, "{Type:4,Tame:1,Saddle:1}");
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

		final UseEffectWeightTable theTable;

		switch (ItemStackHelper.getItemDamage(scrapBox)) {
		case RecyclingScrapBox.STANDARD:
			theTable = standardEffects;
			break;
		case RecyclingScrapBox.SUPERIOR:
			theTable = superiorEffects;
			break;
		default:
			theTable = poorEffects;
			break;
		}

		try {
			theTable.next().apply(scrapBox, world, player);
			return true;
		} catch (final Exception e) {
			ModLog.warn(e.getMessage());
		}
		return false;
	}

	public static void diagnostic(final Writer writer) throws IOException {
		writer.write("\n==========================\nScrapbox Use Effect Tables\n==========================\n");
		poorEffects.diagnostic("Poor Effects", writer);
		standardEffects.diagnostic("Standard Effects", writer);
		superiorEffects.diagnostic("Superior Effects", writer);
	}
}
