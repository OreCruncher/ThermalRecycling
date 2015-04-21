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

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import cofh.api.modhelpers.ThermalExpansionHelper;

public class FluidTransposerRecipeBuilder extends RecipeBuilder {

	protected FluidStack fluid;

	public FluidTransposerRecipeBuilder() {
		fluid = null;
	}

	@Override
	public RecipeBuilder reset() {
		fluid = null;
		return super.reset();
	}

	@Override
	public RecipeBuilder fluid(String fluidId) {
		fluid(RecipeHelper.getFluidStack(fluidId, 1000));
		return this;
	}

	@Override
	public RecipeBuilder fluid(String fluidId, int quantity) {
		fluid(RecipeHelper.getFluidStack(fluidId, quantity));
		return this;
	}

	@Override
	public RecipeBuilder fluid(FluidStack stack) {
		this.fluid = stack;
		return this;
	}

	@Override
	public RecipeBuilder fluidQuantity(int quantity) {
		if (this.fluid != null)
			this.fluid.amount = quantity;
		return this;
	}

	@Override
	protected boolean saveImpl(ItemStack stack) {
		if (output != null && fluid != null) {
			ThermalExpansionHelper.addTransposerExtract(energy, stack, output,
					fluid, 100, false);

			return true;
		}

		return false;
	}

	@Override
	protected String toString(ItemStack in) {

		return String.format("Fluid Transposer [%dx %s] => [%dx %s, %dmb %s]",
				in.stackSize, resolveName(in), output.stackSize,
				resolveName(output), fluid.amount, fluid.getLocalizedName());
	}
}
