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

import java.util.List;
import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import cpw.mods.fml.common.registry.VillagerRegistry;

public class VendingVillageStructureHandler implements VillagerRegistry.IVillageCreationHandler {

	static {
		MapGenStructureIO.func_143031_a(VendingVillageStructure.class, ThermalRecycling.MOD_ID + "Vend");
		MapGenStructureIO.registerStructure(VendingVillageStructure.class, ThermalRecycling.MOD_ID + "Vend");
	}
	
	private VendingVillageStructureHandler() {
	}

	@Override
	public PieceWeight getVillagePieceWeight(Random random, int i) {
		final int weight = ModOptions.getVillageStructureWeight();
		int count = ModOptions.getVillageStructureCount();
		if(count > 1) {
			count = random.nextInt(count) + 1;
		}
		
		return new PieceWeight(getComponentClass(), weight, count);
	}

	@Override
	public Class<?> getComponentClass() {
		return VendingVillageStructure.class;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object buildComponent(PieceWeight villagePiece, Start startPiece,
			List pieces, Random random, int p1, int p2, int p3, int p4, int p5) {
		return VendingVillageStructure.buildComponent(startPiece, pieces, random, p1, p2, p3, p4, p5);
	}
	
	public static void register() {
		VillagerRegistry.instance().registerVillageCreationHandler(new VendingVillageStructureHandler());
	}
}