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

package org.blockartistry.mod.ThermalRecycling.world.villager;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.util.WeightTable;

public class VillagerProfessionWeightTable extends WeightTable<VillagerProfessionWeightTable.VillagerProfessionItem> {

	public final static class VillagerProfessionItem extends WeightTable.Item {

		final VillagerProfession profession;

		public VillagerProfessionItem(final VillagerProfession stack, final int weight) {
			super(weight);
			this.profession = stack;
		}

		public VillagerProfession getProfession() {
			return profession;
		}

		@Override
		public String toString() {
			return profession.getName();
		}
	}
	
	public VillagerProfessionWeightTable() {
		super();
	}

	public VillagerProfessionWeightTable(final Random rand) {
		super(rand);
	}

	public VillagerProfession nextProfession() {
		try {
			return next().getProfession();
		} catch (Exception e) {
			ModLog.warn(e.getMessage());
		}
		
		// If we get here something bad happened.  Default to Farmer.
		return VillagerProfession.farmer;
	}
	
	public int find(final String name) {
		for(int i = 0; i < items.size(); i++) {
			final VillagerProfession prof = items.get(i).getProfession();
			if(StringUtils.equalsIgnoreCase(name, prof.getKey())) {
				return i;
			}
		}
		
		return -1;
	}
	
	public VillagerProfession get(final int index) {
		if(index == -1 || index >= items.size())
			return null;
		return items.get(index).getProfession();
	}
}
