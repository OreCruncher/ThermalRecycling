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

package org.blockartistry.mod.ThermalRecycling.client;

import java.util.Random;

import net.minecraft.world.World;

public final class ParticleEffects {

	// Code modeled after Redstone Ore block special FX
	public static void spawnParticlesAroundBlock(String particle, World world, int x, int y, int z, Random rand) {
		
		Random r = rand;
		if(r == null)
			r = world.rand;
		
        double d0 = 0.064D;

        for (int l = 0; l < 6; ++l)
        {
            double d1 = x + r.nextFloat();
            double d2 = y + r.nextFloat();
            double d3 = z + r.nextFloat();

            if (l == 0 && !world.getBlock(x, y + 1, z).isOpaqueCube())
            {
                d2 = y + 1 + d0;
            }

            if (l == 1 && !world.getBlock(x, y - 1, z).isOpaqueCube())
            {
                d2 = y + 0 - d0;
            }

            if (l == 2 && !world.getBlock(x, y, z + 1).isOpaqueCube())
            {
                d3 = z + 1 + d0;
            }

            if (l == 3 && !world.getBlock(x, y, z - 1).isOpaqueCube())
            {
                d3 = z + 0 - d0;
            }

            if (l == 4 && !world.getBlock(x + 1, y, z).isOpaqueCube())
            {
                d1 = x + 1 + d0;
            }

            if (l == 5 && !world.getBlock(x - 1, y, z).isOpaqueCube())
            {
                d1 = x + 0 - d0;
            }

            if (d1 < x || d1 > x + 1 || d2 < 0.0D || d2 > y + 1 || d3 < z || d3 > z + 1)
            {
            	world.spawnParticle(particle, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }    

	}
}
