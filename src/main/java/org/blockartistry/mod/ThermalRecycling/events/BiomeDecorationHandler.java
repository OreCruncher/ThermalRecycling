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

package org.blockartistry.mod.ThermalRecycling.events;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BiomeDecorationHandler {
	
	private final int MIN_Y = 10;
	private final int MAX_Y = 55;
	private final int SPREAD = MAX_Y - MIN_Y;
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onWorldDecoration(DecorateBiomeEvent.Decorate event) {

		if((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT) && event.type == EventType.FLOWERS) {

			final int attempts = ModOptions.getRubblePileDensity();
			
			for(int i = 0; i < attempts; i++) {

				final int x = event.chunkX + event.rand.nextInt(16) + 8;
				final int z = event.chunkZ + event.rand.nextInt(16) + 8;
				final int y = event.rand.nextInt(SPREAD) + MIN_Y;
				
				if(event.world.isAirBlock(x, y, z) && BlockManager.pileOfRubble.canBlockStay(event.world, x, y, z)) {
					event.world.setBlock(x, y, z, BlockManager.pileOfRubble);
				}
			}
		}
	}
	
	public BiomeDecorationHandler() {
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
	}
}
