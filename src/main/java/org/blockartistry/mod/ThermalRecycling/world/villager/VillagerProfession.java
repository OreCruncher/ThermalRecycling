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

package org.blockartistry.mod.ThermalRecycling.world.villager;

import java.lang.reflect.Method;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;
import org.blockartistry.mod.ThermalRecycling.world.FantasyIsland;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerProfessionWeightTable.VillagerProfessionItem;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.StatCollector;
import net.minecraft.village.MerchantRecipeList;

public class VillagerProfession {

	protected static final Random random = XorShiftRandom.shared;
	private static final String VENDO_FORMAT = StatCollector.translateToLocal("msg.VendoFormat");
	protected static final VillagerProfessionWeightTable professions = new VillagerProfessionWeightTable();

	public static final VillagerProfession farmer = new VillagerProfession(0, "msg.VendoFormat.Farmer",
			DyeHelper.COLOR_GREEN, DyeHelper.COLOR_WHITE);
	public static final VillagerProfession librarian = new VillagerProfession(1, "msg.VendoFormat.Librarian",
			DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_BLUE);
	public static final VillagerProfession priest = new VillagerProfession(2, "msg.VendoFormat.Priest",
			DyeHelper.COLOR_LIGHTGRAY, DyeHelper.COLOR_GRAY);
	public static final VillagerProfession blacksmith = new VillagerProfession(3, "msg.VendoFormat.Blacksmith",
			DyeHelper.COLOR_BLACK, DyeHelper.COLOR_YELLOW);
	public static final VillagerProfession butcher = new VillagerProfession(4, "msg.VendoFormat.Butcher",
			DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_RED);

	public static final VillagerProfessionCustom herder;
	public static final VillagerProfessionCustom arborist;
	public static final VillagerProfessionCustom hunter;
	public static final VillagerProfessionCustom tinker;
	public static final VillagerProfessionCustom dyer;

	private static Method tradeList;

	static {

		try {
			tradeList = ReflectionHelper.findMethod(EntityVillager.class, null,
					new String[] { "func_70950_c", "addDefaultEquipmentAndRecipies" }, int.class);
			tradeList.setAccessible(true);
		} catch (Throwable t) {
			ModLog.warn("Unable to hook EntityVillager.addDefaultEquimentAndRecipes");
		}

		professions.add(new VillagerProfessionItem(farmer, 100));
		professions.add(new VillagerProfessionItem(blacksmith, 100));
		professions.add(new VillagerProfessionItem(butcher, 100));
		professions.add(new VillagerProfessionItem(librarian, 50));
		professions.add(new VillagerProfessionItem(priest, 75));

		if (ModOptions.getEnableExtraVillageVendingTypes()) {

			herder = new VillagerProfessionCustom("msg.VendoFormat.Herder", DyeHelper.COLOR_BROWN,
					DyeHelper.COLOR_GREEN);
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 90, 1, 1).setProbability(0.5F));
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 91, 1, 1).setProbability(0.5F));
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 92, 1, 1).setProbability(0.5F));
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 93, 1, 1).setProbability(0.5F));
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 94, 1, 1).setProbability(0.2F));
			herder.addTrade(new VillagerTrade().setWant(2, 4).setOffer(Items.spawn_egg, 95, 1, 1).setProbability(0.2F));
			herder.addTrade(
					new VillagerTrade().setWant(4, 7).setOffer(Items.spawn_egg, 100, 1, 1).setProbability(0.1F));
			professions.add(new VillagerProfessionItem(herder, 80));

			arborist = new VillagerProfessionCustom("msg.VendoFormat.Arborist", DyeHelper.COLOR_GREEN,
					DyeHelper.COLOR_BROWN);
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log, 0, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log, 1, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log, 2, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log, 3, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log2, 0, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.log2, 1, 32, 32).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 0, 4, 4).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 1, 4, 4).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 2, 4, 4).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 3, 4, 4).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 4, 4, 4).setProbability(0.5F));
			arborist.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.sapling, 5, 4, 4).setProbability(0.5F));
			professions.add(new VillagerProfessionItem(arborist, 100));

			hunter = new VillagerProfessionCustom("msg.VendoFormat.Hunter", DyeHelper.COLOR_RED,
					DyeHelper.COLOR_YELLOW);
			hunter.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Items.bone, 0, 4, 4).setProbability(0.5F));
			hunter.addTrade(
					new VillagerTrade().setWant(1, 2).setOffer(Items.rotten_flesh, 0, 4, 4).setProbability(0.5F));
			hunter.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Items.spider_eye, 0, 4, 4).setProbability(0.5F));
			hunter.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Items.feather, 0, 16, 16).setProbability(0.5F));
			hunter.addTrade(new VillagerTrade().setWant(3, 5).setOffer(Items.blaze_rod, 0, 2, 2).setProbability(0.2F));
			hunter.addTrade(new VillagerTrade().setWant(5, 8).setOffer(Items.ghast_tear, 0, 1, 1).setProbability(0.2F));
			hunter.addTrade(new VillagerTrade().setWant(5, 8).setOffer(Items.skull, 0, 1, 1).setProbability(0.15F));
			hunter.addTrade(new VillagerTrade().setWant(24, 30).setOffer(Items.skull, 1, 1, 1).setProbability(0.1F));
			hunter.addTrade(new VillagerTrade().setWant(5, 8).setOffer(Items.skull, 2, 1, 1).setProbability(0.15F));
			hunter.addTrade(new VillagerTrade().setWant(5, 8).setOffer(Items.skull, 4, 1, 1).setProbability(0.15F));
			professions.add(new VillagerProfessionItem(hunter, 50));

			tinker = new VillagerProfessionCustom("msg.VendoFormat.Tinker", DyeHelper.COLOR_BLUE,
					DyeHelper.COLOR_YELLOW);
			tinker.addTrade(
					new VillagerTrade().setWant(1, 2).setOffer(Blocks.golden_rail, 0, 1, 2).setProbability(0.5F));
			tinker.addTrade(
					new VillagerTrade().setWant(1, 2).setOffer(Blocks.detector_rail, 0, 2, 4).setProbability(0.5F));
			tinker.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.rail, 0, 16, 24).setProbability(0.5F));
			tinker.addTrade(
					new VillagerTrade().setWant(1, 2).setOffer(Blocks.activator_rail, 0, 2, 4).setProbability(0.5F));
			tinker.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Items.repeater, 0, 2, 4).setProbability(0.5F));
			tinker.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Items.comparator, 0, 1, 2).setProbability(0.5F));
			tinker.addTrade(
					new VillagerTrade().setWant(2, 4).setOffer(Blocks.daylight_detector, 0, 1, 1).setProbability(0.5F));
			tinker.addTrade(
					new VillagerTrade().setWant(2, 4).setOffer(Blocks.redstone_lamp, 0, 1, 1).setProbability(0.5F));
			tinker.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.piston, 0, 2, 4).setProbability(0.5F));
			tinker.addTrade(
					new VillagerTrade().setWant(1, 2).setOffer(Blocks.sticky_piston, 0, 1, 2).setProbability(0.5F));
			tinker.addTrade(new VillagerTrade().setWant(1, 2).setOffer(Blocks.tnt, 0, 3, 4).setProbability(0.5F));
			professions.add(new VillagerProfessionItem(tinker, 50));

			dyer = new VillagerProfessionCustom("msg.VendoFormat.Dyer", DyeHelper.COLOR_WHITE, DyeHelper.COLOR_MAGENTA);
			for (int i = 0; i < 16; i++)
				dyer.addTrade(new VillagerTrade().setWant(1, 1).setOffer(Items.dye, i, 12, 16).setProbability(0.7F));
			professions.add(new VillagerProfessionItem(dyer, 80));

		} else {

			herder = null;
			arborist = null;
			hunter = null;
			tinker = null;
			dyer = null;

		}
	}

	private final String key;
	private final int id;
	private final String name;
	private final int foreColor;
	private final int backColor;

	protected VillagerProfession(final int id, final String name, final int fColor, final int bColor) {
		this.key = name;
		this.id = id;
		this.name = StatCollector.translateToLocal(name);
		this.foreColor = fColor;
		this.backColor = bColor;
	}

	public static VillagerProfession randomProfession() {
		return professions.nextProfession();
	}

	public String getKey() {
		return key;
	}

	public String getName() {
		return name;
	}

	public String getVendingTitle() {
		return String.format(VENDO_FORMAT, name);
	}

	public int getForegroundColor() {
		return foreColor;
	}

	public int getBackgroundColor() {
		return backColor;
	}

	public MerchantRecipeList getTradeList(final int count) {
		final EntityVillager temp = new EntityVillager(FantasyIsland.instance, id);
		try {
			tradeList.invoke(temp, count);
		} catch (Throwable t) {
		}
		return temp.getRecipes(null);
	}
}
