/*
 * This file is part of ModpackInfo, licensed under the MIT License (MIT).
 *
 * Copyright (c) OreCruncher
 * Copyright (c) contributors
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

import java.util.ArrayList;

import cofh.api.modhelpers.ThermalExpansionHelper;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeHelper {

	protected int energy;
	protected ItemStack input;
	protected ItemStack output;
	protected ItemStack secondary;
	protected int secondaryChance;
	protected FluidStack fluid;

	public static ItemStack getItemStack(String name, int quantity) {
		return getItemStack(name, quantity, 0);
	}

	public static ItemStack getItemStack(String name, int quantity, int damage) {

		ItemStack result = null;

		Item i = GameData.getItemRegistry().getObject(name);
		if (i != null) {
			result = new ItemStack(i, quantity, damage);
		} else {
			ArrayList<ItemStack> ores = OreDictionary.getOres(name);
			if (!ores.isEmpty()) {
				result = ores.get(0);
				result.stackSize = quantity;
			}
		}

		if (result == null)
			ModLog.info("Unable to locate item: " + name);

		return result;
	}

	public static FluidStack getFluidStack(String name, int quantity) {

		return FluidRegistry.getFluidStack(name, quantity);

	}

	protected static String resolveName(ItemStack stack) {
		String result = null;

		if (stack != null) {

			try {
				result = stack.getDisplayName();
			} catch (Exception e) {
			}

			if (result == null) {
				try {
					result = stack.getUnlocalizedName();
				} catch (Exception e) {
					;
				}
			}
		}

		return result == null ? "UNKNOWN" : result;
	}

	protected void log(String machine) {

		StringBuilder builder = new StringBuilder();

		if ("Fluid Transposer".compareTo(machine) == 0) {
			builder.append(String.format("%s [%dx %s] => [%dx %s, %dmb %s]",
					machine, input.stackSize, resolveName(input),
					output.stackSize, resolveName(output), fluid.amount,
					fluid.getLocalizedName()));
		} else if ("Induction Smelter".compareTo(machine) == 0) {
			builder.append(String.format("%s [%dx %s, %dx %s] => [%dx %s]",
					machine, input.stackSize, resolveName(input),
					secondary.stackSize, resolveName(secondary),
					output.stackSize, resolveName(output)));

		} else {
			builder.append(String.format("%s [%dx %s] => [%dx %s", machine,
					input.stackSize, resolveName(input), output.stackSize,
					resolveName(output)));

			if (secondary != null && "Furnace".compareTo(machine) != 0) {
				builder.append(String.format(", %dx %s @%d",
						secondary.stackSize, resolveName(secondary),
						secondaryChance));
			}

			builder.append("]");
		}

		ModLog.info(builder.toString());
	}

	public RecipeHelper() {

		this(null, null, null, 0, 2400);
	}

	public RecipeHelper(ItemStack in, ItemStack out, ItemStack secondary,
			int secondaryChance, int energy) {
		this.energy = energy;
		this.input = in;
		this.output = out;
		this.secondary = secondary;
		this.secondaryChance = secondaryChance;
	}

	public RecipeHelper reset() {
		this.energy = 2400;
		this.input = null;
		this.output = null;
		this.secondary = null;
		this.secondaryChance = 0;
		return this;
	}

	public RecipeHelper setEnergy(int energy) {
		this.energy = energy;
		return this;
	}

	public RecipeHelper setInput(String item) {
		return setInput(item, 1);
	}

	public RecipeHelper setInput(String item, int quantity) {
		return setInput(getItemStack(item, quantity));
	}

	public RecipeHelper setInput(Block block) {
		return setInput(block, 1);
	}

	public RecipeHelper setInput(Block block, int quantity) {
		return setInput(new ItemStack(block, quantity));
	}

	public RecipeHelper setInput(Item item) {
		return setInput(item, 1);
	}

	public RecipeHelper setInput(Item item, int quantity) {

		return setInput(new ItemStack(item, quantity));
	}

	public RecipeHelper setInput(ItemStack in) {
		this.input = in;
		return this;
	}

	public RecipeHelper setInputSubtype(int type) {
		if (this.input != null)
			this.input.setItemDamage(type);
		return this;
	}

	public RecipeHelper setInputQuantity(int quantity) {
		if (this.input != null)
			this.input.stackSize = quantity;
		return this;
	}

	public RecipeHelper setOutput(Block block) {
		return setOutput(block, 1);
	}

	public RecipeHelper setOutput(Block block, int quantity) {
		return setOutput(new ItemStack(block, quantity));
	}

	public RecipeHelper setOutput(Item item) {
		return setOutput(item, 1);
	}

	public RecipeHelper setOutput(Item item, int quantity) {

		return setOutput(new ItemStack(item, quantity));
	}

	public RecipeHelper setOutput(String item) {
		return setOutput(item, 1);
	}

	public RecipeHelper setOutput(String item, int quantity) {
		return setOutput(getItemStack(item, quantity));
	}

	public RecipeHelper setOutput(ItemStack out) {
		this.output = out;
		return this;
	}

	public RecipeHelper setOutputSubtype(int type) {
		if (this.output != null)
			this.output.setItemDamage(type);
		return this;
	}

	public RecipeHelper setOutputQuantity(int quantity) {
		if (this.output != null)
			this.output.stackSize = quantity;
		return this;
	}

	public RecipeHelper resetSecondary() {
		this.secondary = null;
		return this;
	}

	public RecipeHelper setSecondary(Block block) {
		return setSecondary(block, 1);
	}

	public RecipeHelper setSecondary(Block block, int quantity) {
		return setSecondary(new ItemStack(block, quantity));
	}

	public RecipeHelper setSecondary(Item item) {
		return setSecondary(item, 1);
	}

	public RecipeHelper setSecondary(Item item, int quantity) {
		return setSecondary(new ItemStack(item, quantity));
	}

	public RecipeHelper setSecondary(String item) {
		return setSecondary(item, 1);
	}

	public RecipeHelper setSecondary(String item, int quantity) {
		return setSecondary(getItemStack(item, quantity));
	}

	public RecipeHelper setSecondary(ItemStack sec) {
		this.secondary = sec;
		return this;
	}

	public RecipeHelper setSecondarySubtype(int type) {
		if (this.secondary != null)
			this.secondary.setItemDamage(type);
		return this;
	}

	public RecipeHelper setSecondaryQuantity(int quantity) {
		if (this.secondary != null)
			this.secondary.stackSize = quantity;
		return this;
	}

	public RecipeHelper setSecondaryChance(int chance) {
		this.secondaryChance = chance;
		return this;
	}

	public RecipeHelper setFluid(String fluidId) {
		setFluid(getFluidStack(fluidId, 1000));
		return this;
	}

	public RecipeHelper setFluid(String fluidId, int quantity) {
		setFluid(getFluidStack(fluidId, quantity));
		return this;
	}

	public RecipeHelper setFluid(FluidStack stack) {
		this.fluid = stack;
		return this;
	}

	public RecipeHelper setFluidQuantity(int quantity) {
		if (this.fluid != null)
			this.fluid.amount = quantity;
		return this;
	}

	public RecipeHelper addAsFurnaceRecipe() {

		if (input != null && output != null) {
			ThermalExpansionHelper.addFurnaceRecipe(energy, input, output);
			log("Furnace");
		}

		return this;
	}

	public RecipeHelper addAsPulverizerRecipe() {

		if (input != null && output != null) {
			ThermalExpansionHelper.addPulverizerRecipe(energy, input, output,
					secondary, secondaryChance);
			log("Pulverizer");
		}

		return this;
	}

	public RecipeHelper addAsSmelterRecipe() {

		if (input != null && output != null) {
			ThermalExpansionHelper.addSmelterRecipe(energy, input, secondary,
					output);
			log("Induction Smelter");
		}

		return this;
	}

	public RecipeHelper addAsSawmillRecipe() {

		if (input != null && output != null) {
			ThermalExpansionHelper.addSawmillRecipe(energy, input, output,
					secondary, secondaryChance);
			log("Sawmill");
		}

		return this;
	}

	public RecipeHelper addAsFluidTransposerRecipe() {
		if (input != null && output != null && fluid != null) {
			ThermalExpansionHelper.addTransposerExtract(energy, input, output,
					fluid, 100, false);
			log("Fluid Transposer");
		}

		return this;
	}
}
