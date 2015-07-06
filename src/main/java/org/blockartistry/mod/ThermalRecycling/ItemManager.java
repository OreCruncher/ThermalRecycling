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

package org.blockartistry.mod.ThermalRecycling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.blockartistry.mod.ThermalRecycling.items.Debris;
import org.blockartistry.mod.ThermalRecycling.items.FoodItem;
import org.blockartistry.mod.ThermalRecycling.items.FoodItem.OnEat;
import org.blockartistry.mod.ThermalRecycling.items.Material;
import org.blockartistry.mod.ThermalRecycling.items.PaperLogMaker;
import org.blockartistry.mod.ThermalRecycling.items.ProcessingCore;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrap;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;

/**
 * Contains references to all Items in the mod as well as logic for
 * registration.
 *
 */
public final class ItemManager {

	private ItemManager() {
	}

	public static final RecyclingScrap recyclingScrap = new RecyclingScrap();
	public static final RecyclingScrapBox recyclingScrapBox = new RecyclingScrapBox();
	public static final ProcessingCore processingCore = new ProcessingCore();

	public static final Debris debris = new Debris();
	public static final Material material = new Material();
	public static final PaperLogMaker paperLogMaker = new PaperLogMaker();

	public static final FoodItem soylentGreen = new FoodItem("soylentGreen",
			10, 1.0F, false).setLore(
			StatCollector.translateToLocal("msg.itLooksEdible")).setOnEaten(
			new OnEat() {
				@Override
				public void onEat(ItemStack stack, World world,
						EntityPlayer player) {
					player.addStat(AchievementManager.dystopianFuture, 1);
				}
			});

	public static void register() {

		recyclingScrap.register();
		recyclingScrapBox.register();
		processingCore.register();

		debris.register();
		material.register();
		paperLogMaker.register();

		soylentGreen.register();
	}
}
