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

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class EnergeticRedstoneOreHandler {
	
	private static final int REPLACE_CHANCE = ModOptions.getEnergeticRedstoneChance();

	// (http://www.minecraftforge.net/forum/index.php/topic,21625.0.html)
	@SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
	public void onEvent(final PopulateChunkEvent.Pre event) {

		final Chunk chunk = event.world.getChunkFromChunkCoords(event.chunkX, event.chunkZ);
		final Block fromBlock = Blocks.redstone_ore; // change this to suit your need
		final Block toBlock = BlockManager.energeticRedstone; // change this to suit your need

		for (final ExtendedBlockStorage storage : chunk.getBlockStorageArray()) {
			if (storage != null) {
				for (int x = 0; x < 16; ++x) {
					for (int y = 0; y < 16; ++y) {
						for (int z = 0; z < 16; ++z) {
							final Block theBlock = storage.getBlockByExtId(x, y, z);
							if (theBlock == fromBlock) {
								if(event.rand.nextInt(REPLACE_CHANCE) == 0) {
									storage.func_150818_a(x, y, z, toBlock);
									storage.setExtBlocklightValue(x, y, z, toBlock.getLightValue());
								}
							}
						}
					}
				}
			}
		}
		
		chunk.setChunkModified();// this is important as it marks it to be saved
	}
	
	public static void register() {
		MinecraftForge.EVENT_BUS.register(new EnergeticRedstoneOreHandler());
	}
}
