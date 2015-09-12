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

package org.blockartistry.mod.ThermalRecycling.support.handlers;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.blockartistry.mod.ThermalRecycling.data.ScrapHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;
import org.blockartistry.mod.ThermalRecycling.util.MyUtils;

import com.google.common.collect.ImmutableList;

import buildcraft.api.transport.pluggable.IPipePluggableItem;

public final class BuildCraftGateScrapHandler extends ScrapHandler {

	private static final ItemStack pulsatingChipset = ItemStackHelper
			.getItemStack("BuildCraft|Silicon:redstoneChipset:4");
	private static final ItemStack quartzChipset = ItemStackHelper
			.getItemStack("BuildCraft|Silicon:redstoneChipset:5");
	private static final ItemStack redstoneCompChipset = ItemStackHelper
			.getItemStack("BuildCraft|Silicon:redstoneChipset:6");

	private static final List<ItemStack> basicGate;
	private static final List<ItemStack> ironGate;
	private static final List<ItemStack> goldGate;
	private static final List<ItemStack> diamondGate;
	private static final List<ItemStack> quartzGate;
	private static final List<ItemStack> emeraldGate;

	static {

		final ItemStack redstoneChipset = ItemStackHelper
				.getItemStack("BuildCraft|Silicon:redstoneChipset");
		final ItemStack ironChipset = ItemStackHelper
				.getItemStack("BuildCraft|Silicon:redstoneChipset:1");
		final ItemStack goldenChipset = ItemStackHelper
				.getItemStack("BuildCraft|Silicon:redstoneChipset:2");
		final ItemStack diamondChipset = ItemStackHelper
				.getItemStack("BuildCraft|Silicon:redstoneChipset:3");
		final ItemStack emeraldChipset = ItemStackHelper
				.getItemStack("BuildCraft|Silicon:redstoneChipset:7");

		final ItemStack redPipeWire = ItemStackHelper
				.getItemStack("BuildCraft|Transport:pipeWire");
		final ItemStack bluePipeWire = ItemStackHelper
				.getItemStack("BuildCraft|Transport:pipeWire:1");
		final ItemStack greenPipeWire = ItemStackHelper
				.getItemStack("BuildCraft|Transport:pipeWire:2");
		final ItemStack yellowPipeWire = ItemStackHelper
				.getItemStack("BuildCraft|Transport:pipeWire:3");

		basicGate = new ImmutableList.Builder<ItemStack>()
			.add(redstoneChipset).build();

		ironGate = new ImmutableList.Builder<ItemStack>()
			.add(ironChipset)
			.add(redPipeWire).build();

		goldGate = new ImmutableList.Builder<ItemStack>()
			.add(goldenChipset)
			.add(redPipeWire)
			.add(bluePipeWire).build();

		diamondGate = new ImmutableList.Builder<ItemStack>()
			.add(diamondChipset)
			.add(redPipeWire)
			.add(bluePipeWire)
			.add(greenPipeWire)
			.add(yellowPipeWire).build();

		quartzGate = new ImmutableList.Builder<ItemStack>()
			.add(quartzChipset)
			.add(redPipeWire)
			.add(bluePipeWire)
			.add(greenPipeWire).build();

		emeraldGate = new ImmutableList.Builder<ItemStack>()
			.add(emeraldChipset)
			.add(redPipeWire)
			.add(bluePipeWire)
			.add(greenPipeWire)
			.add(yellowPipeWire).build();
	}

	private static final int MATERIAL_REDSTONE = 0;
	private static final int MATERIAL_IRON = 1;
	private static final int MATERIAL_GOLD = 2;
	private static final int MATERIAL_DIAMOND = 3;
	private static final int MATERIAL_EMERALD = 4;
	private static final int MATERIAL_QUARTZ = 5;
	
	private static int getMaterial(final ItemStack stack) {
		
		int result = MATERIAL_REDSTONE;
		
		if(stack.hasTagCompound()) {
			final NBTTagCompound nbt = stack.getTagCompound();
			result = nbt.getInteger("mat");
		}
		
		return result;
	}
	
	private static List<String> getExpansions(final ItemStack stack) {
		final List<String> result = new ArrayList<String>();

		if(stack.hasTagCompound()) {
			final NBTTagCompound nbt = stack.getTagCompound();
			final NBTTagList expansionList = nbt.getTagList("ex", Constants.NBT.TAG_STRING);
			for (int i = 0; i < expansionList.tagCount(); i++) {
				result.add(expansionList.getStringTagAt(i));
			}
		}
		
		return result;
	}
	
	@Override
	protected List<ItemStack> getRecipeOutput(final ScrappingContext ctx) {

		final ItemStack stack = ctx.toProcess;
		
		if (stack.getItem() instanceof IPipePluggableItem) {

			final List<ItemStack> output = new ArrayList<ItemStack>();

			final int mat = getMaterial(stack);

			switch (mat) {
			case MATERIAL_REDSTONE:
				output.addAll(basicGate);
				break;
			case MATERIAL_IRON:
				output.addAll(ironGate);
				break;
			case MATERIAL_GOLD:
				output.addAll(goldGate);
				break;
			case MATERIAL_DIAMOND:
				output.addAll(diamondGate);
				break;
			case MATERIAL_QUARTZ:
				output.addAll(quartzGate);
				break;
			case MATERIAL_EMERALD:
				output.addAll(emeraldGate);
				break;
			default:
				;
			}

			for (final String id: getExpansions(stack)) {
				if (id.compareTo("buildcraft:pulsar") == 0)
					output.add(pulsatingChipset);
				else if (id.compareTo("buildcraft:timer") == 0)
					output.add(quartzChipset);
				else if (id.compareTo("buildcraft:fader") == 0)
					output.add(redstoneCompChipset);
			}

			return MyUtils.clone(output);
		}

		return super.getRecipeOutput(ctx);
	}
}
