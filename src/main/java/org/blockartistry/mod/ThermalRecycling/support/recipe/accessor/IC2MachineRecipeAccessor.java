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

package org.blockartistry.mod.ThermalRecycling.support.recipe.accessor;

import java.util.ArrayList;
import java.util.List;

import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import com.google.common.base.Optional;

import net.minecraft.item.ItemStack;

public class IC2MachineRecipeAccessor extends IC2AccessorBase {

	@Override
	public ItemStack getInput(final Object recipe) {
		final IC2MachineRecipeAdaptor adaptor = (IC2MachineRecipeAdaptor)recipe;
		return adaptor.input.items.get(0).copy();
	}
	
	@Override
	public List<ItemStack> getOutput(final Object recipe) {
		final List<ItemStack> result = new ArrayList<ItemStack>();
		final IC2MachineRecipeAdaptor adaptor = (IC2MachineRecipeAdaptor)recipe;
		final ItemStack candidate = adaptor.output.getInputs().get(0);
		final Optional<ItemStack> stack = ItemStackHelper.getPreferredStack(candidate);
		if(stack.isPresent()) {
			final ItemStack prize = stack.get();
			prize.stackSize = adaptor.output.getAmount();
			result.add(prize);
		}
		return result;
	}
}
