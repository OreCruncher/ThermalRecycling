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

package org.blockartistry.mod.ThermalRecycling.items;

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.ItemBase;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import cofh.api.energy.IEnergyContainerItem;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHCore", striprefs = true)
public final class RTGEnergyCell extends ItemBase implements IEnergyContainerItem {

	private static final String TEXT_INFINITE = StatCollector.translateToLocal("msg.RTGEnergyCell.Infinite");
	private static final String TEXT_DEPLETED = StatCollector.translateToLocal("msg.RTGEnergyCell.Depleted");
	private static final String TEXT_SEND = StatCollector.translateToLocal("msg.RTGEnergyCell.Send");
	private static final String TEXT_CHARGE = StatCollector.translateToLocal("msg.RTGEnergyCell.Charge");
	
	private static final String PROP_MAX_ENERGY = "maxEnergy";
	private static final String PROP_ENERGY = "energy";
	private static final String PROP_RATE = "rate";

	public static final int POWER_LEVEL_BASE = ModOptions.getRTGBasePowerPerTick();
	public static final int POWER_LEVEL_CREATIVE = POWER_LEVEL_BASE * 8;
	public static final int ENERGY_LEVEL_BASE = ModOptions.getRTGBaseEnergy();
	public static final int ENERGY_LEVEL_CREATIVE = ENERGY_LEVEL_BASE * 8;

	public static final int CREATIVE = 0;
	public static final int RTG = 1;

	protected static void setProperty(final ItemStack stack, final String prop, final int value) {
		NBTTagCompound nbt = stack.getTagCompound();
		if (nbt == null)
			nbt = new NBTTagCompound();
		nbt.setInteger(prop, value);
		stack.setTagCompound(nbt);
	}

	protected static int getProperty(final ItemStack stack, final String prop) {
		if (stack.getItemDamage() == CREATIVE) {
			if (PROP_ENERGY.compareTo(prop) == 0)
				return ENERGY_LEVEL_CREATIVE;
			if (PROP_RATE.compareTo(prop) == 0)
				return POWER_LEVEL_CREATIVE;
			if (PROP_MAX_ENERGY.compareTo(prop) == 0)
				return ENERGY_LEVEL_CREATIVE;
		}
		final NBTTagCompound nbt = stack.getTagCompound();
		return nbt == null ? 0 : nbt.getInteger(prop);
	}

	public static void initialize(final ItemStack stack, final int powerLevel, final int energyLevel) {

		if (stack.getItemDamage() == CREATIVE) {
			setProperty(stack, PROP_ENERGY, ENERGY_LEVEL_CREATIVE);
			setProperty(stack, PROP_MAX_ENERGY, ENERGY_LEVEL_CREATIVE);
			setProperty(stack, PROP_RATE, POWER_LEVEL_CREATIVE);
			ItemStackHelper.makeGlow(stack);
		} else {
			setProperty(stack, PROP_ENERGY, energyLevel);
			setProperty(stack, PROP_MAX_ENERGY, energyLevel);
			setProperty(stack, PROP_RATE, powerLevel);
		}
	}

	public RTGEnergyCell() {
		super("creative", "rtg");

		setUnlocalizedName("RTGEnergyCell");
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	@Override
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		// Cannot be externally charged, so return 0.
		return 0;
	}

	@Override
	public int extractEnergy(final ItemStack stack, final int maxExtract, final boolean simulate) {

		final int rate = getProperty(stack, PROP_RATE);
		int energy = getProperty(stack, PROP_ENERGY);
		final int energyExtracted = Math.min(energy, Math.min(rate, maxExtract));

		if (!simulate && ItemStackHelper.getItemDamage(stack) != CREATIVE) {
			energy -= energyExtracted;
			setProperty(stack, PROP_ENERGY, energy);
		}

		return energyExtracted;
	}

	@Override
	public int getEnergyStored(final ItemStack stack) {
		return getProperty(stack, PROP_ENERGY);
	}

	@Override
	public int getMaxEnergyStored(final ItemStack stack) {
		return getProperty(stack, PROP_MAX_ENERGY);
	}

	@Override
	public int getDisplayDamage(final ItemStack stack) {
		return getProperty(stack, PROP_MAX_ENERGY) - getProperty(stack, PROP_ENERGY);
	}

	@Override
	public int getMaxDamage(final ItemStack stack) {
		return getProperty(stack, PROP_MAX_ENERGY);
	}

	@Override
	public boolean isDamaged(final ItemStack stack) {
		return ItemStackHelper.getItemDamage(stack) != CREATIVE;
	}

	@Override
	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer player,
			@SuppressWarnings("rawtypes") final List info, final boolean p_77624_4_) {

		final int rate = getProperty(stack, PROP_RATE);
		final int energyRemaining = getProperty(stack, PROP_ENERGY);

		String nrgy;
		String r8;
		if (stack.getItemDamage() == CREATIVE)
			nrgy = TEXT_INFINITE;
		else if (energyRemaining == 0)
			nrgy = TEXT_DEPLETED;
		else
			nrgy = String.format("%s RF", energyRemaining);

		if (energyRemaining == 0)
			r8 = TEXT_DEPLETED;
		else
			r8 = String.format("%s RF/t", rate);

		info.add(String.format("%s: %s", TEXT_SEND, r8));
		info.add(String.format("%s: %s", TEXT_CHARGE, nrgy));
	}

	@Override
	public void register() {
		super.register();

		final List<ItemStack> input = new ArrayList<ItemStack>();
		input.add(new ItemStack(ItemManager.material, 1, Material.RTG_HOUSING));

		final ItemStack cell = new ItemStack(ItemManager.material, 1, Material.FUEL_CELL);

		for (int i = 1; i < 9; i++) {
			final int energy = ENERGY_LEVEL_BASE * i;
			final int power = POWER_LEVEL_BASE * i;

			input.add(cell);

			final ItemStack rtg = new ItemStack(ItemManager.energyCell, 1, RTGEnergyCell.RTG);
			RTGEnergyCell.initialize(rtg, power, energy);

			final ShapelessOreRecipe shapeless = new ShapelessOreRecipe(rtg, input.toArray());
			GameRegistry.addRecipe(shapeless);
		}
	}
}
