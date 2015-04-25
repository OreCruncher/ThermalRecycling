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

import net.minecraft.nbt.NBTTagCompound;

import org.blockartistry.mod.ThermalRecycling.util.INBTSerializer;

public class OperationTracker implements INBTSerializer {
	
	protected int energyPerTick;
	protected int energyRequired;
	protected int progress;
	protected boolean isActive;
	
	public OperationTracker(int energyPerTick, int energyRequired) {
		reset(energyPerTick, energyRequired);
	}
	
	/**
	 * Resets the operation tracker for the next run
	 * 
	 * @param energyPerTick New amount of energy that can be consumed
	 * @param energyRequired New amount of energy required to complete
	 */
	public void reset(int energyPerTick, int energyRequired) {
		this.energyPerTick = energyPerTick;
		this.energyRequired = energyRequired;
		this.progress = 0;
		this.isActive = false;
	}
	
	/**
	 * Resets progress while leaving energy consumption per tick and
	 * required energy alone.
	 */
	public void start() {
		isActive = true;
		progress = 0;
	}
	
	/**
	 * Reports the amount of energy required to perform the next tick of
	 * operation.
	 * 
	 * @return Amount of energy required capped at energyPerTick
	 */
	public int energyRequiredForTick() {
		return Math.min(energyPerTick, energyRequired - progress);
	}
	
	/**
	 * Indicates if progress has reached the end.
	 * 
	 * @return true if complete; false otherwise
	 */
	public boolean isComplete() {
		return progress >= energyRequired;
	}
	
	/**
	 * Indicates if the operation is currently active.  If not active
	 * it could be due to a lack of energy or the operation reaching
	 * a completion state.
	 * 
	 * @return True of the operation is active; false otherwise
	 */
	public boolean isActive() {
		return isActive && !isComplete();
	}
	
	/**
	 * Reports progress in terms of a percent complete.  The value
	 * is normalized for integer reporting.
	 * 
	 * @return 0 - 100 indicating progress
	 */
	public int percentComplete() {
		return (progress * 100)/energyRequired;
	}
	
	/**
	 * Advances progress forward a single tick.
	 */
	public void makeProgress(int energy) {
		if(energy == 0) {
			isActive = false;
		} else {
			isActive = true;
			progress += Math.min(energy, energyRequiredForTick());
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		energyPerTick = nbt.getInteger("energyPerTick");
		energyRequired = nbt.getInteger("energyRequired");
		progress = nbt.getInteger("progress");
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		nbt.setInteger("energyPerTick", energyPerTick);
		nbt.setInteger("energyRequired", energyRequired);
		nbt.setInteger("progress", progress);
	}
}
