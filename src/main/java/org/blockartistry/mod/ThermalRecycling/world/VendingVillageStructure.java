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

import org.blockartistry.mod.ThermalRecycling.BlockManager;
import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.machines.entity.VendingTileEntity;
import org.blockartistry.mod.ThermalRecycling.util.FakePlayerHelper;
import org.blockartistry.mod.ThermalRecycling.util.XorShiftRandom;
import org.blockartistry.mod.ThermalRecycling.world.villager.VillagerProfession;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.util.ForgeDirection;

public final class VendingVillageStructure extends StructureVillagePieces.Village {

	private static final Random random = XorShiftRandom.shared;
	private static final int HEIGHT = 4;
	private static final int WIDTH = 5;
	private static final int LENGTH = 3;

	private static final int[] orientations = new int[] {
			ForgeDirection.NORTH.ordinal(), ForgeDirection.EAST.ordinal(),
			ForgeDirection.SOUTH.ordinal(), ForgeDirection.WEST.ordinal() };
	
	private int averageGroundLevel = -1;

	public VendingVillageStructure() {
		super();
	}
	
	protected VendingVillageStructure(StructureVillagePieces.Start startPiece,
			int type, Random random, StructureBoundingBox _boundingBox,
			int direction) {
		super(startPiece, type);
		coordBaseMode = direction;
		boundingBox = _boundingBox;
	}
	
	/**
	 * Configures a Vending Machine for village operation.
	 *
	 * @param vte
	 *            Vending TileEntity to configure
	 */
	private static void configure(final VendingTileEntity vte) {

		final ItemStack[] inv = vte.getRawInventory();
		final VillagerProfession profession = VillagerProfession.randomProfession();
		final int count = 2 + random.nextInt(5);

		// Use this method because if the spawn point is created before
		// the world is launched it is not possible to have a FakePlayer
		// "log in".
		vte.setOwnerId(FakePlayerHelper.getFakePlayerID());
		vte.setName(profession.getVendingTitle());
		vte.setNameBackgroundColor(profession.getBackgroundColor());
		vte.setNameColor(profession.getForegroundColor());

		int index = 0;
		for (final Object rm : profession.getTradeList(count)) {
			if (rm instanceof MerchantRecipe) {
				final MerchantRecipe mr = (MerchantRecipe)rm;
				final int base = index + VendingTileEntity.CONFIG_SLOT_START;
				inv[base] = mr.getItemToBuy().copy();
				if (mr.hasSecondItemToBuy())
					inv[base + 6] = mr.getSecondItemToBuy().copy();
				final ItemStack stack = inv[base + 12] = mr.getItemToSell().copy();

				// Put items in inventory to give to the player
				int stacks = 0;
				if (stack.isStackable())
					stacks = random.nextInt(6) + random.nextInt(6) + 2;
				else
					stacks = random.nextInt(3) + 1;

				for (int i = 0; i < stacks; i++)
					vte.addStackToOutput(stack.copy());

				index++;
			}
		}
	}

	@SuppressWarnings("rawtypes")
	public static Object buildComponent(
			StructureVillagePieces.Start startPiece, List pieces,
			Random random, int x, int y, int z, int direction, int type) {
		StructureBoundingBox _boundingBox = StructureBoundingBox
				.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, WIDTH, HEIGHT,
						LENGTH, direction);
		if (canVillageGoDeeper(_boundingBox)) {
			if (StructureComponent.findIntersecting(pieces, _boundingBox) == null) {
				return new VendingVillageStructure(startPiece, type, random,
						_boundingBox, direction);
			}
		}
		return null;
	}
	
	protected boolean generateStructureVending(World world,
			StructureBoundingBox box, Random random, int x, int y, int z) {
		
		int i1 = this.getXWithOffset(x, z);
		int j1 = this.getYWithOffset(y);
		int k1 = this.getZWithOffset(x, z);

		if (box.isVecInside(i1, j1, k1)) {
			world.setBlock(i1, j1, k1, BlockManager.vending, orientations[coordBaseMode], 2);
			world.setBlock(i1, j1 + 1, k1, BlockManager.vendingTop, 0, 2);
			
			VendingTileEntity te = (VendingTileEntity) world.getTileEntity(i1,
					j1, k1);

			if (te != null) {
				ModLog.debug("Village Vending: %d, %d, %d", i1, j1, k1);
				configure(te);
			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean addComponentParts(World world, Random random,
			StructureBoundingBox box) {
		if (averageGroundLevel < 0) {
			averageGroundLevel = getAverageGroundLevel(world, box);
			if (averageGroundLevel < 0) {
				return true;
			}
			boundingBox.offset(0, this.averageGroundLevel - boundingBox.maxY
					+ HEIGHT - 2, 0);
		}

		fillWithBlocks(world, box, 0, 0, 0, WIDTH - 1, HEIGHT - 1, LENGTH - 1,
				Blocks.air, Blocks.air, true);

		for (int xx = 0; xx < WIDTH; xx++) {
			for (int zz = 0; zz < LENGTH; zz++) {
				clearCurrentPositionBlocksUpwards(world, xx, 0, zz, box);
				this.func_151554_b(world, Blocks.dirt, 0, xx, -1, zz, box);
			}
		}

		// floors
		fillWithBlocks(world, box, 0, 0, 0, WIDTH - 1, 0, LENGTH - 1,
				Blocks.gravel, Blocks.gravel, false);

		// walls
		fillWithBlocks(world, box, 0, 1, 0, 0, 1, LENGTH - 1, Blocks.fence,
				Blocks.fence, false);
		fillWithBlocks(world, box, WIDTH - 1, 1, 0, WIDTH - 1, 1, LENGTH - 1,
				Blocks.fence, Blocks.fence, false);
		fillWithBlocks(world, box, 0, 1, LENGTH - 1, WIDTH - 1, 1, LENGTH - 1,
				Blocks.fence, Blocks.fence, false);
		
		for(int i = 0; i < 2; i++) {
			placeBlockAtCurrentPosition(world, Blocks.fence, 0, 0, 2 + i, 0, box);
			placeBlockAtCurrentPosition(world, Blocks.fence, 0, WIDTH - 1, 2 + i, 0, box);
			placeBlockAtCurrentPosition(world, Blocks.fence, 0, 0, 2 + i, LENGTH - 1, box);
			placeBlockAtCurrentPosition(world, Blocks.fence, 0, WIDTH - 1, 2 + i, LENGTH - 1, box);
		}
		
		// Roof
		fillWithBlocks(world, box, 0, 4, 0, WIDTH - 1, 4, LENGTH - 1, Blocks.wooden_slab, Blocks.wooden_slab, false);
		fillWithBlocks(world, box, 1, 4, 1, WIDTH - 2, 4, LENGTH - 2, Blocks.planks, Blocks.planks, false);
		

		final int vendingX = (WIDTH - 1) /2;
		final int vendingY = 1;
		final int vendingZ = (LENGTH - 1) /2;

		// Give some light
		placeBlockAtCurrentPosition(world, Blocks.torch, 0, 0, 2, vendingZ, box);
		placeBlockAtCurrentPosition(world, Blocks.torch, 0, WIDTH - 1, 2, vendingZ, box);

		// Put down our vending machine
		generateStructureVending(world, box, random, vendingX, vendingY, vendingZ);
		
		return true;
	}
}