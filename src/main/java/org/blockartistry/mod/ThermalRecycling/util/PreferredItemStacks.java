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

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public abstract class PreferredItemStacks {
	
	public static PreferredItemStacks instance = new PreferredItemStacksTE().register();

	protected Map<String, ItemStack> preferred = new HashMap<String, ItemStack>();
	
	public ItemStack ingotIron = new ItemStack(Items.iron_ingot);
	public ItemStack ingotGold = new ItemStack(Items.gold_ingot);
	public ItemStack ingotCopper;
	public ItemStack ingotTin;
	public ItemStack ingotSilver;
	public ItemStack ingotLead;
	public ItemStack ingotNickel;
	public ItemStack ingotPlatinum;
	public ItemStack ingotBronze;
	public ItemStack ingotInvar;
	public ItemStack ingotElectrum;
	public ItemStack ingotSignalum;
	public ItemStack ingotEnderium;
	public ItemStack ingotLumium;
	public ItemStack ingotManaInfused;
	
	public ItemStack blockIron = new ItemStack(Blocks.iron_block);
	public ItemStack blockGold = new ItemStack(Blocks.gold_block);
	public ItemStack blockCopper;
	public ItemStack blockTin;
	public ItemStack blockSilver;
	public ItemStack blockLead;
	public ItemStack blockNickel;
	public ItemStack blockPlatinum;
	public ItemStack blockBronze;
	public ItemStack blockInvar;
	public ItemStack blockElectrum;
	public ItemStack blockSignalum;
	public ItemStack blockEnderium;
	public ItemStack blockLumium;
	public ItemStack blockManaInfused;
	public ItemStack blockGlass = new ItemStack(Blocks.glass);
	
	public ItemStack dustIron;
	public ItemStack dustGold;
	public ItemStack dustCopper;
	public ItemStack dustTin;
	public ItemStack dustSilver;
	public ItemStack dustLead;
	public ItemStack dustNickel;
	public ItemStack dustPlatinum;
	public ItemStack dustManaInfused;
	public ItemStack dustElectrum;
	public ItemStack dustInvar;
	public ItemStack dustBronze;
	public ItemStack dustSignalum;
	public ItemStack dustLumium;
	public ItemStack dustEnderium;
	public ItemStack dustRedstone = new ItemStack(Items.redstone);

	public ItemStack nuggetIron;
	public ItemStack nuggetGold = new ItemStack(Items.gold_nugget);
	public ItemStack nuggetCopper;
	public ItemStack nuggetTin;
	public ItemStack nuggetSilver;
	public ItemStack nuggetLead;
	public ItemStack nuggetNickel;
	public ItemStack nuggetPlatinum;
	public ItemStack nuggetManaInfused;
	public ItemStack nuggetElectrum;
	public ItemStack nuggetInvar;
	public ItemStack nuggetBronze;
	public ItemStack nuggetSignalum;
	public ItemStack nuggetLumium;
	public ItemStack nuggetEnderium;
	
	public ItemStack blockDiamond = new ItemStack(Blocks.diamond_block);
	public ItemStack blockEmerald = new ItemStack(Blocks.emerald_block);
	
	public ItemStack gemDiamond = new ItemStack(Items.diamond);
	public ItemStack gemEmerald = new ItemStack(Items.emerald);
	
	public ItemStack dustWood;

	public ItemStack dustCoal;
	public ItemStack dustCharcoal;
	public ItemStack sulfer;
	public ItemStack niter;

	public ItemStack dustObsidian;

	public ItemStack gearIron;
	public ItemStack gearGold;
	public ItemStack gearCopper;
	public ItemStack gearTin;
	public ItemStack gearSilver;
	public ItemStack gearLead;
	public ItemStack gearNickel;
	public ItemStack gearPlatinum;
	public ItemStack gearManaInfused;
	public ItemStack gearElectrum;
	public ItemStack gearInvar;
	public ItemStack gearBronze;
	public ItemStack gearSignalum;
	public ItemStack gearLumium;
	public ItemStack gearEnderium;

	public PreferredItemStacks() {
	}
	
	protected void registerStack(final String name, final ItemStack stack) {
		if(stack != null)
			preferred.put(name, stack);
	}
	
	protected PreferredItemStacks register() {
		
		registerStack("ingotIron", ingotIron);
		registerStack("ingotGold", ingotGold);
		registerStack("blockIron", blockIron);
		registerStack("blockGold", blockGold);
		registerStack("blockDiamond", blockDiamond);
		registerStack("blockEmerald", blockEmerald);
		registerStack("gemDiamond", gemDiamond);
		registerStack("gemEmerald", gemEmerald);
		registerStack("nuggetGold", nuggetGold);
		
		registerStack("ingotCopper", ingotCopper);
		registerStack("ingotTin", ingotTin);
		registerStack("ingotSilver", ingotSilver);
		registerStack("ingotLead", ingotLead);
		registerStack("ingotNickel", ingotNickel);
		registerStack("ingotPlatinum", ingotPlatinum);
		registerStack("ingotManaInfused", ingotManaInfused);
		registerStack("ingotElectrum", ingotElectrum);
		registerStack("ingotInvar", ingotInvar);
		registerStack("ingotBronze", ingotBronze);
		registerStack("ingotSignalum", ingotSignalum);
		registerStack("ingotLumium", ingotLumium);
		registerStack("ingotEnderium", ingotEnderium);

		registerStack("nuggetIron", nuggetIron);
		registerStack("nuggetCopper", nuggetCopper);
		registerStack("nuggetTin", nuggetTin);
		registerStack("nuggetSilver", nuggetSilver);
		registerStack("nuggetLead", nuggetLead);
		registerStack("nuggetNickel", nuggetNickel);
		registerStack("nuggetPlatinum", nuggetPlatinum);
		registerStack("nuggetManaInfused", nuggetManaInfused);
		registerStack("nuggetElectrum", nuggetElectrum);
		registerStack("nuggetInvar", nuggetInvar);
		registerStack("nuggetBronze", nuggetBronze);
		registerStack("nuggetSignalum", nuggetSignalum);
		registerStack("nuggetLumium", nuggetLumium);
		registerStack("nuggetEnderium", nuggetEnderium);

		registerStack("dustIron", dustIron);
		registerStack("dustGold", dustGold);
		registerStack("dustCopper", dustCopper);
		registerStack("dustTin", dustTin);
		registerStack("dustSilver", dustSilver);
		registerStack("dustLead", dustLead);
		registerStack("dustNickel", dustNickel);
		registerStack("dustPlatinum", dustPlatinum);
		registerStack("dustManaInfused", dustManaInfused);
		registerStack("dustElectrum", dustElectrum);
		registerStack("dustInvar", dustInvar);
		registerStack("dustBronze", dustBronze);
		registerStack("dustSignalum", dustSignalum);
		registerStack("dustLumium", dustLumium);
		registerStack("dustEnderium", dustEnderium);

		registerStack("dustCoal", dustCoal);
		registerStack("dustCharcoal", dustCharcoal);
		registerStack("dustSulfer", sulfer);
		registerStack("dustObsidian", dustObsidian);
		registerStack("dustRedstone", dustRedstone);

		registerStack("blockCopper", blockCopper);
		registerStack("blockTin", blockTin);
		registerStack("blockSilver", blockSilver);
		registerStack("blockLead", blockLead);
		registerStack("blockFerrous", blockNickel);
		registerStack("blockPlatinum", blockPlatinum);
		registerStack("blockManaInfused", blockManaInfused);
		registerStack("blockElectrum", blockElectrum);
		registerStack("blockInvar", blockInvar);
		registerStack("blockBronze", blockBronze);
		registerStack("blockSignalum", blockSignalum);
		registerStack("blockLumium", blockLumium);
		registerStack("blockEnderium", blockEnderium);
		registerStack("blockGlass", blockGlass);

		registerStack("gearIron", gearIron);
		registerStack("gearGold", gearGold);
		registerStack("gearCopper", gearCopper);
		registerStack("gearTin", gearTin);
		registerStack("gearSilver", gearSilver);
		registerStack("gearLead", gearLead);
		registerStack("gearNickel", gearNickel);
		registerStack("gearPlatinum", gearPlatinum);
		registerStack("gearManaInfused", gearManaInfused);
		registerStack("gearElectrum", gearElectrum);
		registerStack("gearInvar", gearInvar);
		registerStack("gearBronze", gearBronze);
		registerStack("gearSignalum", gearSignalum);
		registerStack("gearLumium", gearLumium);
		registerStack("gearEnderium", gearEnderium);

		registerStack("thermalexpansion:machineIron", gearIron);
		registerStack("thermalexpansion:machineGold", gearGold);
		registerStack("thermalexpansion:machineCopper", gearCopper);
		registerStack("thermalexpansion:machineTin", gearTin);
		registerStack("thermalexpansion:machineSilver", gearSilver);
		registerStack("thermalexpansion:machineLead", gearLead);
		registerStack("thermalexpansion:machineNickel", gearNickel);
		registerStack("thermalexpansion:machinePlatinum", gearPlatinum);
		registerStack("thermalexpansion:machineMithril", gearManaInfused);
		registerStack("thermalexpansion:machineElectrum", gearElectrum);
		registerStack("thermalexpansion:machineInvar", gearInvar);
		registerStack("thermalexpansion:machineBronze", gearBronze);
		registerStack("thermalexpansion:machineSignalum", gearSignalum);
		registerStack("thermalexpansion:machineLumium", gearLumium);
		registerStack("thermalexpansion:machineEnderium", gearEnderium);

		preferred = ImmutableMap.copyOf(preferred);
		return this;
	}
	
	public ItemStack get(final String name) {
		return preferred.get(name);
	}
}
