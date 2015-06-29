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

package org.blockartistry.mod.ThermalRecycling.world;

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent;
import net.minecraftforge.event.terraingen.DecorateBiomeEvent.Decorate.EventType;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class BiomeDecorationHandler {

	private final int MIN_Y = 5;
	private final int PLACE_ATTEMPTS = 2;

	@SubscribeEvent(priority = EventPriority.LOW)
	public void onWorldDecoration(final DecorateBiomeEvent.Decorate event) {

		if ((event.getResult() == Result.ALLOW || event.getResult() == Result.DEFAULT)
				&& event.type == EventType.FLOWERS) {

			// Calculate the range and scaling based on
			// the world sea level.  Normal sea level is assumed
			// to be 64, which is the Overworld sea level.
			final int groundLevel = event.world.provider
					.getAverageGroundLevel();
			final int attempts = (int) (ModOptions.getRubblePileDensity() * ((float) groundLevel / 64F));
			final int maxY = groundLevel - 4;
			final int spread = maxY - MIN_Y;
			
			// In case someone does something real funky with a
			// dimension.
			if(spread < 1 || attempts < 1)
				return;

			// Use our random routine, but seed with the provided
			// Random. The provided Random seed is deterministic
			// where map gen is concerned.
			final XorShiftRandom random = new XorShiftRandom(event.rand);

			for (int i = 0; i < attempts; i++) {

				final int x = event.chunkX + random.nextInt(16) + 8;
				final int z = event.chunkZ + random.nextInt(16) + 8;
				int y = random.nextInt(spread) + MIN_Y;

				for (int j = 0; j < PLACE_ATTEMPTS; j++) {
					if (event.world.isAirBlock(x, y, z)
							&& BlockManager.pileOfRubble.canBlockStay(
									event.world, x, y, z)) {
						event.world
								.setBlock(x, y, z, BlockManager.pileOfRubble);
						break;
					}
					y--;
				}
			}
		}
	}

	public BiomeDecorationHandler() {
		MinecraftForge.TERRAIN_GEN_BUS.register(this);
	}
}
