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

package org.blockartistry.mod.ThermalRecycling.machines.components;

import org.blockartistry.mod.ThermalRecycling.util.INBTSerializer;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyReceiver;

/**
 * Component for handling internal energy storage
 *
 */
public class InternalEnergyStorage implements IEnergyReceiver, INBTSerializer {
	
	protected int capacity;
	protected int energy;
	
	public InternalEnergyStorage(int capacity) {
		this.capacity = capacity;
		this.energy = 0;
	}

	@Override
	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {
		return energy;
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {
		return capacity;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxRecieve, boolean simulate) {
		int result = maxRecieve;
		if((energy + maxRecieve) > capacity) {
			result = capacity - energy;
		}
		
		if(!simulate)
			energy += result;
		
		return result;
	}
	
	/**
	 * Reduces the amount of stored energy by the specified amount.
	 * 
	 * @param amount The amount of energy to remove from storage
	 * @return true if there was enough energy to perform, false otherwise
	 */
	public boolean consumeEnergy(int amount) {
		if(amount > energy)
			return false;
		energy -= amount;
		return true;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		energy = nbt.getInteger("energy");
		if(energy > capacity)
			energy = capacity;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		if(energy < 0)
			energy = 0;
		nbt.setInteger("energy", energy);
	}
}
