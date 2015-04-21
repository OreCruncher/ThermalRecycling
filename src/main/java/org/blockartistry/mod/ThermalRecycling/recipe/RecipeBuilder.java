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

package org.blockartistry.mod.ThermalRecycling.recipe;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeBuilder {

	protected int energy;
	protected List<ItemStack> input;
	protected ItemStack output;

	protected static String resolveName(ItemStack stack) {
		String result = null;

		if (stack != null) {

			try {
				result = stack.getDisplayName();
			} catch (Exception e) {
				;
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

	protected void log(String msg) {

		if (!ModOptions.instance.getEnableRecipeLogging())
			return;

		ModLog.info(msg);
	}

	public RecipeBuilder() {

		reset();
	}

	public RecipeBuilder reset() {
		this.energy = 2400;
		this.input = new ArrayList<ItemStack>();
		this.output = null;
		return this;
	}

	public RecipeBuilder setEnergy(int energy) {
		this.energy = energy;
		return this;
	}

	public RecipeBuilder append(String... items) {
		for (String s : items)
			append(s, 1);
		return this;
	}

	public RecipeBuilder append(String item, int quantity) {
		return append(RecipeHelper.getItemStack(item, quantity));
	}

	public RecipeBuilder append(Block... blocks) {
		for (Block b : blocks)
			append(b, 1);
		return this;
	}

	public RecipeBuilder append(Block block, int quantity) {
		return append(new ItemStack(block, quantity));
	}

	public RecipeBuilder append(Item... items) {
		for (Item i : items)
			append(i, 1);
		return this;
	}

	public RecipeBuilder append(Item item, int quantity) {
		return append(new ItemStack(item, quantity));
	}

	public RecipeBuilder append(List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			this.input.add(stack);
		}
		return this;
	}

	public RecipeBuilder append(ItemStack... stacks) {
		for (ItemStack stack : stacks) {
			this.input.add(stack);
		}
		return this;
	}

	public RecipeBuilder appendSubtype(ItemStack stack, int subtype) {
		ItemStack s = stack.copy();
		s.setItemDamage(subtype);
		return append(s);
	}

	public RecipeBuilder appendSubtype(Item item, int subtype) {
		return append(new ItemStack(item, 1, subtype));
	}

	public RecipeBuilder appendSubtypeRange(String item, int start, int end, int quantity) {
		return append(RecipeHelper.getItemStackRange(item, start, end, quantity));
	}
	
	public RecipeBuilder appendSubtypeRange(String item, int start, int end) {
		return append(RecipeHelper.getItemStackRange(item, start, end, 1));
	}

	public RecipeBuilder appendSubtypeRange(Item item, int start, int end, int quantity) {
		return append(RecipeHelper.getItemStackRange(item, start, end, quantity));
	}
	
	public RecipeBuilder appendSubtypeRange(Item item, int start, int end) {
		return append(RecipeHelper.getItemStackRange(item, start, end, 1));
	}

	public RecipeBuilder appendSubtypeRange(Block block, int start, int end, int quantity) {
		return append(RecipeHelper.getItemStackRange(block, start, end, quantity));
	}
	
	public RecipeBuilder appendSubtypeRange(Block block, int start, int end) {
		return append(RecipeHelper.getItemStackRange(block, start, end, 1));
	}

	public RecipeBuilder appendSubtypeRange(ItemStack stack, int start, int end) {
		for (int i = start; i <= end; i++) {
			ItemStack s = stack.copy();
			s.setItemDamage(i);
			input.add(s);
		}

		return this;
	}

	public RecipeBuilder output(Block block) {
		return output(block, 1);
	}

	public RecipeBuilder output(Block block, int quantity) {
		return output(new ItemStack(block, quantity));
	}

	public RecipeBuilder output(Item item) {
		return output(item, 1);
	}

	public RecipeBuilder output(Item item, int quantity) {

		return output(new ItemStack(item, quantity));
	}

	public RecipeBuilder output(String item) {
		return output(item, 1);
	}

	public RecipeBuilder output(String item, int quantity) {
		return output(RecipeHelper.getItemStack(item, quantity));
	}

	public RecipeBuilder output(ItemStack out, int quantity) {
		this.output = out;
		this.output.stackSize = quantity;
		return this;
	}

	public RecipeBuilder output(ItemStack out) {
		this.output = out;
		return this;
	}

	public void save() {

		for (ItemStack i : input) {
			if (saveImpl(i))
				log(toString(i));
		}

		reset();
	}

	protected abstract String toString(ItemStack stack);

	protected abstract boolean saveImpl(ItemStack stack);

	public RecipeBuilder secondaryInput(Block block) {
		throw new NotImplementedException("secondaryInput(Block)");
	}

	public RecipeBuilder secondaryInput(Block block, int quantity) {
		throw new NotImplementedException("secondaryInput(Block, quantity)");
	}

	public RecipeBuilder secondaryInput(Item item) {
		throw new NotImplementedException("secondaryInput(Item)");
	}

	public RecipeBuilder secondaryInput(Item item, int quantity) {
		throw new NotImplementedException("secondaryInput(Item, quantity)");
	}

	public RecipeBuilder secondaryInput(String item) {
		throw new NotImplementedException("secondaryInput(String)");
	}

	public RecipeBuilder secondaryInput(String item, int quantity) {
		throw new NotImplementedException("secondaryInput(String, quantity)");
	}

	public RecipeBuilder secondaryInput(ItemStack sec) {
		throw new NotImplementedException("secondaryInput(ItemStack)");
	}

	public RecipeBuilder secondaryInputSubtype(int type) {
		throw new NotImplementedException("secondaryInputSubtype(type)");
	}

	public RecipeBuilder secondaryOutput(Block block) {
		throw new NotImplementedException("secondaryOutput(Block)");
	}

	public RecipeBuilder secondaryOutput(Block block, int quantity) {
		throw new NotImplementedException("secondaryOutput(Block, quantity)");
	}

	public RecipeBuilder secondaryOutput(Item item) {
		throw new NotImplementedException("secondaryOutput(Item)");
	}

	public RecipeBuilder secondaryOutput(Item item, int quantity) {
		throw new NotImplementedException("secondaryOutput(Item, quantity)");
	}

	public RecipeBuilder secondaryOutput(String item) {
		throw new NotImplementedException("secondaryOutput(String)");
	}

	public RecipeBuilder secondaryOutput(String item, int quantity) {
		throw new NotImplementedException("secondaryOutput(String, quantity)");
	}

	public RecipeBuilder secondaryOutput(ItemStack sec) {
		throw new NotImplementedException("secondaryOutput(ItemStack)");
	}

	public RecipeBuilder secondaryOutputSubtype(int type) {
		throw new NotImplementedException("secondaryOutputSubtype(type)");
	}

	public RecipeBuilder secondaryOutputQuantity(int quantity) {
		throw new NotImplementedException("secondaryOutputQuantity(quantity)");
	}

	public RecipeBuilder chance(int chance) {
		throw new NotImplementedException("chance(chance)");
	}

	public RecipeBuilder fluid(String fluidId) {
		throw new NotImplementedException("fluid(fluidId)");
	}

	public RecipeBuilder fluid(String fluidId, int quantity) {
		throw new NotImplementedException("fluid(fluidId, quantity)");
	}

	public RecipeBuilder fluid(FluidStack stack) {
		throw new NotImplementedException("fluid(fluidStack)");
	}

	public RecipeBuilder fluidQuantity(int quantity) {
		throw new NotImplementedException("fluidQuantity(quantity)");
	}
}
