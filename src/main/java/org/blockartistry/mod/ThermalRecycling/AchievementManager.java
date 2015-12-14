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

import org.blockartistry.mod.ThermalRecycling.items.Material;
import org.blockartistry.mod.ThermalRecycling.items.RecyclingScrapBox;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.AchievementPage;

public final class AchievementManager extends AchievementPage {

	// Ordering is important. Independent achievements first,
	// dependent achievements after.
	//
	// Don't forget to update registerAchievements so that the
	// achievement gets registered with Minecraft!
	//
	// Don't forget to update the AchievementManager CTOR so that
	// the achievement will show on the proper page!

	public static final Achievement lottoWinner = new Achievement("lottoWinner", "lottoWinner", -3/* x */, -2 /* y */,
			new ItemStack(Items.nether_star), null);

	public static final Achievement doinTheTrash = new Achievement("doinTheTrash", "doinTheTrash", -3 /* x */,
			-1 /* y */, new ItemStack(ItemManager.material, 1, Material.LITTER_BAG), AchievementManager.feelingScrappy);

	public static final Achievement shearBeauty = new Achievement("shearBeauty", "shearBeauty", -3 /* x */, 0 /* y */,
			new ItemStack(ItemManager.material, 1, Material.GARDEN_SHEARS), null);

	public static final Achievement dystopianFuture = new Achievement("dystopianFuture", "dystopianFuture", -3 /* x */,
			1 /* y */, new ItemStack(ItemManager.soylentGreen), null);

	public static final Achievement powerUp = new Achievement("powerUp", "powerUp", -3 /* x */,
			2 /* y */, new ItemStack(ItemManager.energyCell), null);

	public static final Achievement feelingScrappy = new Achievement("feelingScrappy", "feelingScrappy", 0 /* x */,
			0 /* y */, new ItemStack(BlockManager.thermalRecycler), null);

	public static final Achievement doingMyPart = new Achievement("doingMyPart", "doingMyPart", 1 /* x */, 1 /* y */,
			new ItemStack(ItemManager.recyclingScrapBox, 1, RecyclingScrapBox.STANDARD),
			AchievementManager.feelingScrappy);

	protected static AchievementManager page;

	public static void registerAchievements() {

		lottoWinner.registerStat();
		doinTheTrash.registerStat();
		shearBeauty.registerStat();
		dystopianFuture.registerStat();
		powerUp.registerStat();

		feelingScrappy.registerStat();
		doingMyPart.registerStat();

		page = new AchievementManager();

		// Register our instances
		registerAchievementPage(page);
		FMLCommonHandler.instance().bus().register(page);

	}

	public AchievementManager() {
		super(StatCollector.translateToLocal("itemGroup.ThermalRecycling"), lottoWinner, doinTheTrash, feelingScrappy,
				doingMyPart, shearBeauty, dystopianFuture, powerUp);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onItemCrafted(final ItemCraftedEvent event) {

		if (event.crafting.isItemEqual(new ItemStack(BlockManager.thermalRecycler))) {
			event.player.addStat(feelingScrappy, 1);
		} else if(event.crafting.isItemEqual(new ItemStack(ItemManager.energyCell))) {
			event.player.addStat(powerUp, 1);
		}
	}
}
