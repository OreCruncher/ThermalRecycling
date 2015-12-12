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

package org.blockartistry.mod.ThermalRecycling.enchant;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraftforge.common.MinecraftForge;

public abstract class EnchantmentBase extends Enchantment {

	protected EnchantmentBase(int id, int weight, EnumEnchantmentType type) {
		super(id, weight, type);
	}

	@Override
	public String getName() {
		return new StringBuilder().append(ThermalRecycling.MOD_ID).append('.').append(super.getName()).toString();
	}

	public void register() {
		ModLog.info(String.format("Enchant %s registered with ID %d", this.getName(), this.effectId));
		if (addToBookEnchants())
			Enchantment.addToBookList(this);
		if (registerEvents())
			MinecraftForge.EVENT_BUS.register(this);
	}

	public boolean addToBookEnchants() {
		return true;
	}

	public boolean registerEvents() {
		return true;
	}
}
