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

package org.blockartistry.mod.ThermalRecycling;

import org.blockartistry.mod.ThermalRecycling.enchant.Vacuum;

import net.minecraft.enchantment.Enchantment;
import org.blockartistry.mod.ThermalRecycling.enchant.EnchantmentBase;

public final class EnchantManager {

	private EnchantManager() {
	}

	public static EnchantmentBase vacuum;

	private static int resolveId(final int id) {
		if (id > 0)
			return id;

		final int len = Enchantment.enchantmentsList.length;
		for (int i = 1; i < len; i++)
			if (Enchantment.enchantmentsList[i] == null)
				return i;

		return -1;
	}
	
	private static EnchantmentBase add(final Class<? extends EnchantmentBase> clazz, final int configId) {
		final int id = resolveId(configId);
		if (id != -1) {
			try {
				final EnchantmentBase ench = (EnchantmentBase)clazz.getConstructor(int.class).newInstance(id);
				ench.register();
				return ench;
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
		} else {
			ModLog.warn("Unable to obtain ID for enchantment " + clazz.getName());
		}
		return null;
	}

	public static void register() {
		if (ModOptions.getVacuumEnable()) {
			vacuum = add(Vacuum.class, ModOptions.getVacuumId());
			ModOptions.setVacuumId(vacuum.effectId);
		}
	}
}
