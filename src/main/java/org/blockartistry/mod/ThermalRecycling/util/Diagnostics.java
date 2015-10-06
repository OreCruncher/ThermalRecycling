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

import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Optional;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public final class Diagnostics {
	private Diagnostics() { }
	
	public void dumpSubItems(final Logger log, final String itemId) {
		final Optional<ItemStack> stack = ItemStackHelper.getItemStack(itemId, 1);
		if (stack.isPresent()) {

			try {
				final ItemStack s = stack.get();
				for (int i = 0; i < 1024; i++) {
					s.setItemDamage(i);
					final String name = ItemStackHelper.resolveName(s);
					if (name != null && !name.isEmpty() && !name.contains("(Destroy)"))
						log.info(String.format("%s:%d = %s", itemId, i, name));
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				;
			}
		}
	}

	public void dumpItemStack(final Logger log, final String title, final ItemStack... items) {

		log.info("");
		log.info(title);
		log.info(StringUtils.repeat('-', 32));

		if (items == null || items.length == 0) {
			log.info("No items in list");
			return;
		}

		for (final ItemStack stack : items) {
			log.info(String.format("%s (%s)", ItemStackHelper.resolveName(stack), stack.toString()));
		}
		log.info(StringUtils.repeat('-', 32));
		log.info(String.format("Total: %d item stacks", items.length));
	}

	public static void dumpFluidRegistry(final Logger log) {

		log.info("Fluid Registry:");

		for (final Entry<String, Fluid> e : FluidRegistry.getRegisteredFluids().entrySet()) {
			log.info(String.format("%s: %s", e.getKey(), e.getValue().getName()));
		}
	}
}
