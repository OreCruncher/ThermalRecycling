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

package org.blockartistry.mod.ThermalRecycling.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.DyeHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import net.minecraft.util.StatCollector;

public enum VillagerProfession {

	FARMER("msg.VendoFormat.Farmer", DyeHelper.COLOR_GREEN, DyeHelper.COLOR_WHITE),
	LIBRARIAN("msg.VendoFormat.Librarian", DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_BLUE),
	PRIEST("msg.VendoFormat.Priest", DyeHelper.COLOR_LIGHTGRAY, DyeHelper.COLOR_GRAY),
	BLACKSMITH("msg.VendoFormat.Blacksmith", DyeHelper.COLOR_BLACK, DyeHelper.COLOR_YELLOW),
	BUTCHER("msg.VendoFormat.Butcher", DyeHelper.COLOR_YELLOW, DyeHelper.COLOR_RED),

	HERDER("msg.VendoFormat.Herder", DyeHelper.COLOR_BROWN, DyeHelper.COLOR_GREEN),
	ARBORIST("msg.VendoFormat.Arborist", DyeHelper.COLOR_GREEN, DyeHelper.COLOR_BROWN),
	HUNTER("msg.VendoFormat.Hunter", DyeHelper.COLOR_RED, DyeHelper.COLOR_YELLOW);

	private static final Random random = XorShiftRandom.shared;
	private static final String VENDO_FORMAT = StatCollector.translateToLocal("msg.VendoFormat");
	private static final List<VillagerProfession> professions = new ArrayList<VillagerProfession>();
	
	static {
		
		professions.add(VillagerProfession.FARMER);
		professions.add(VillagerProfession.LIBRARIAN);
		professions.add(VillagerProfession.PRIEST);
		professions.add(VillagerProfession.BLACKSMITH);
		professions.add(VillagerProfession.BUTCHER);
		
		if(ModOptions.getEnableExtraVillageVendingTypes()) {
			professions.add(VillagerProfession.HERDER);
			professions.add(VillagerProfession.ARBORIST);
			professions.add(VillagerProfession.HUNTER);
		}
	}

	private final String name;
	private final int foreColor;
	private final int backColor;

	private VillagerProfession(final String name, final int fColor, final int bColor) {
		this.name = StatCollector.translateToLocal(name);
		this.foreColor = fColor;
		this.backColor = bColor;
	}

	public static VillagerProfession randomProfession() {
		return professions.get(random.nextInt(professions.size()));
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
}
