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

package org.blockartistry.mod.ThermalRecycling.util;

import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PreferredItemStacksTE extends PreferredItemStacks {

	public PreferredItemStacksTE() {
		super();
		
		final Item materialBase = GameData.getItemRegistry().getObject("ThermalFoundation:material");
		final Item storageBase = GameData.getItemRegistry().getObject("ThermalFoundation:Storage");
		final Item materialBaseTE = GameData.getItemRegistry().getObject("ThermalExpansion:material");

		ingotCopper = new ItemStack(materialBase, 1, 64);
		ingotTin = new ItemStack(materialBase, 1, 65);
		ingotSilver = new ItemStack(materialBase, 1, 66);
		ingotLead = new ItemStack(materialBase, 1, 67);
		ingotNickel = new ItemStack(materialBase, 1, 68);
		ingotPlatinum = new ItemStack(materialBase, 1, 69);
		ingotManaInfused = new ItemStack(materialBase, 1, 70);
		ingotElectrum = new ItemStack(materialBase, 1, 71);
		ingotInvar = new ItemStack(materialBase, 1, 72);
		ingotBronze = new ItemStack(materialBase, 1, 73);
		ingotSignalum = new ItemStack(materialBase, 1, 74);
		ingotLumium = new ItemStack(materialBase, 1, 75);
		ingotEnderium = new ItemStack(materialBase, 1, 76);

		dustIron = new ItemStack(materialBase, 1, 0);
		dustGold = new ItemStack(materialBase, 1, 1);
		dustCopper = new ItemStack(materialBase, 1, 32);
		dustTin = new ItemStack(materialBase, 1, 33);
		dustSilver = new ItemStack(materialBase, 1, 34);
		dustLead = new ItemStack(materialBase, 1, 35);
		dustNickel = new ItemStack(materialBase, 1, 36);
		dustPlatinum = new ItemStack(materialBase, 1, 37);
		dustManaInfused = new ItemStack(materialBase, 1, 38);
		dustElectrum = new ItemStack(materialBase, 1, 39);
		dustInvar = new ItemStack(materialBase, 1, 40);
		dustBronze = new ItemStack(materialBase, 1, 41);
		dustSignalum = new ItemStack(materialBase, 1, 42);
		dustLumium = new ItemStack(materialBase, 1, 43);
		dustEnderium = new ItemStack(materialBase, 1, 44);

		nuggetIron = new ItemStack(materialBase, 1, 8);
		nuggetCopper = new ItemStack(materialBase, 1, 96);
		nuggetTin = new ItemStack(materialBase, 1, 97);
		nuggetSilver = new ItemStack(materialBase, 1, 98);
		nuggetLead = new ItemStack(materialBase, 1, 99);
		nuggetNickel = new ItemStack(materialBase, 1, 100);
		nuggetPlatinum = new ItemStack(materialBase, 1, 101);
		nuggetManaInfused = new ItemStack(materialBase, 1, 102);
		nuggetElectrum = new ItemStack(materialBase, 1, 103);
		nuggetInvar = new ItemStack(materialBase, 1, 104);
		nuggetBronze = new ItemStack(materialBase, 1, 105);
		nuggetSignalum = new ItemStack(materialBase, 1, 106);
		nuggetLumium = new ItemStack(materialBase, 1, 107);
		nuggetEnderium = new ItemStack(materialBase, 1, 108);

		blockCopper = new ItemStack(storageBase, 1, 0);
		blockTin = new ItemStack(storageBase, 1, 1);
		blockSilver = new ItemStack(storageBase, 1, 2);
		blockLead = new ItemStack(storageBase, 1, 3);
		blockNickel = new ItemStack(storageBase, 1, 4);
		blockPlatinum = new ItemStack(storageBase, 1, 5);
		blockManaInfused = new ItemStack(storageBase, 1, 6);
		blockElectrum = new ItemStack(storageBase, 1, 7);
		blockInvar = new ItemStack(storageBase, 1, 8);
		blockBronze = new ItemStack(storageBase, 1, 9);
		blockSignalum = new ItemStack(storageBase, 1, 10);
		blockLumium = new ItemStack(storageBase, 1, 11);
		blockEnderium = new ItemStack(storageBase, 1, 12);

		dustWood = new ItemStack(materialBaseTE, 1, 512);

		dustCoal = new ItemStack(materialBase, 1, 2);
		dustCharcoal = new ItemStack(materialBase, 1, 3);
		sulfer = new ItemStack(materialBase, 1, 16);
		niter = new ItemStack(materialBase, 1, 17);

		dustObsidian = new ItemStack(materialBase, 1, 4);

		gearIron = new ItemStack(materialBase, 1, 12);
		gearGold = new ItemStack(materialBase, 1, 13);
		gearCopper = new ItemStack(materialBase, 1, 128);
		gearTin = new ItemStack(materialBase, 1, 129);
		gearSilver = new ItemStack(materialBase, 1, 130);
		gearLead = new ItemStack(materialBase, 1, 131);
		gearNickel = new ItemStack(materialBase, 1, 132);
		gearPlatinum = new ItemStack(materialBase, 1, 133);
		gearManaInfused = new ItemStack(materialBase, 1, 134);
		gearElectrum = new ItemStack(materialBase, 1, 135);
		gearInvar = new ItemStack(materialBase, 1, 136);
		gearBronze = new ItemStack(materialBase, 1, 137);
		gearSignalum = new ItemStack(materialBase, 1, 138);
		gearLumium = new ItemStack(materialBase, 1, 139);
		gearEnderium = new ItemStack(materialBase, 1, 140);

	}
}
