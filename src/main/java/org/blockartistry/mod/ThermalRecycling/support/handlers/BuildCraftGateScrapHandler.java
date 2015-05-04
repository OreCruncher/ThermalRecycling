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
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.blockartistry.mod.ThermalRecycling.data.handlers.GenericHandler;
import org.blockartistry.mod.ThermalRecycling.util.ItemStackHelper;

import buildcraft.api.gates.IGateExpansion;
import buildcraft.transport.gates.GateDefinition.GateMaterial;
import buildcraft.transport.gates.ItemGate;

public class BuildCraftGateScrapHandler extends GenericHandler {

	static final ItemStack pulsatingChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:4");
	static final ItemStack quartzChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:5");
	static final ItemStack redstoneCompChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:6");

	static final List<ItemStack> basicGate;
	static final List<ItemStack> ironGate;
	static final List<ItemStack> goldGate;
	static final List<ItemStack> diamondGate;
	static final List<ItemStack> quartzGate;
	static final List<ItemStack> emeraldGate;
	
	static {

		ItemStack redstoneChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset");
		ItemStack ironChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:1");
		ItemStack goldenChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:2");
		ItemStack diamondChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:3");
		ItemStack emeraldChipset = ItemStackHelper.getItemStack("BuildCraft|Silicon:redstoneChipset:7");

		ItemStack redPipeWire = ItemStackHelper.getItemStack("BuildCraft|Transport:pipeWire");
		ItemStack bluePipeWire = ItemStackHelper.getItemStack("BuildCraft|Transport:pipeWire:1");
		ItemStack greenPipeWire = ItemStackHelper.getItemStack("BuildCraft|Transport:pipeWire:2");
		ItemStack yellowPipeWire = ItemStackHelper.getItemStack("BuildCraft|Transport:pipeWire:3");
		
		basicGate = new ArrayList<ItemStack>();
		basicGate.add(redstoneChipset);
		
		ironGate = new ArrayList<ItemStack>();
		ironGate.add(ironChipset);
		ironGate.add(redPipeWire);
		
		goldGate = new ArrayList<ItemStack>();
		goldGate.add(goldenChipset);
		goldGate.add(redPipeWire);
		goldGate.add(bluePipeWire);
		
		diamondGate = new ArrayList<ItemStack>();
		diamondGate.add(diamondChipset);
		diamondGate.add(redPipeWire);
		diamondGate.add(bluePipeWire);
		diamondGate.add(greenPipeWire);
		diamondGate.add(yellowPipeWire);
		
		quartzGate = new ArrayList<ItemStack>();
		quartzGate.add(quartzChipset);
		quartzGate.add(redPipeWire);
		quartzGate.add(bluePipeWire);
		quartzGate.add(greenPipeWire);
		
		emeraldGate = new ArrayList<ItemStack>();
		emeraldGate.add(emeraldChipset);
		emeraldGate.add(redPipeWire);
		emeraldGate.add(bluePipeWire);
		emeraldGate.add(greenPipeWire);
		emeraldGate.add(yellowPipeWire);
	}

	@Override
	protected List<ItemStack> getRecipeOutput(ItemStack stack) {

		if (stack.getItem() instanceof ItemGate) {

			List<ItemStack> output = new ArrayList<ItemStack>();

			GateMaterial mat = ItemGate.getMaterial(stack);
			Set<IGateExpansion> expansions = ItemGate.getInstalledExpansions(stack);
			
			switch(mat) {
			case REDSTONE:
				output.addAll(basicGate);
				break;
			case IRON:
				output.addAll(ironGate);
				break;
			case GOLD:
				output.addAll(goldGate);
				break;
			case DIAMOND:
				output.addAll(diamondGate);
				break;
			case QUARTZ:
				output.addAll(quartzGate);
				break;
			case EMERALD:
				output.addAll(emeraldGate);
				break;
			default:
				;
			}
			
			for(IGateExpansion ex : expansions) {
				String id = ex.getUniqueIdentifier();
				if(id.compareTo("buildcraft:pulsar") == 0)
					output.add(pulsatingChipset);
				else if(id.compareTo("buildcraft:timer") == 0)
					output.add(quartzChipset);
				else if(id.compareTo("buildcraft:fader") == 0)
					output.add(redstoneCompChipset);
			}
			
			return ItemStackHelper.clone(output);
		}

		return super.getRecipeOutput(stack);
	}
}
