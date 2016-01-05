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

package org.blockartistry.mod.ThermalRecycling.items.scrapbox;

import org.blockartistry.mod.ThermalRecycling.util.EntityHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class SpawnEntityEffect extends UseEffectWeightTable.UseEffectItem{

	private final String type;
	private final String jsonTags;
	private final int maxCount;
	
	public SpawnEntityEffect(final int weight, final String entityType, final int maxCount, final String jsonTags) {
		super(weight);
		this.type = entityType;
		this.maxCount = maxCount;
		this.jsonTags = jsonTags;
	}

	@Override
	public void apply(final ItemStack scrap, final World world, final EntityPlayer player) {
		// Fake players cannot spawn in entities
		if(player instanceof FakePlayer)
			return;
		
		final int count = this.rnd.nextInt(this.maxCount) + 1;
		for(int i = 0; i < count; i++) {
			EntityHelper.summon(player, this.type, this.jsonTags);
		}
	}
}
