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

package org.blockartistry.mod.ThermalRecycling.machines.gui;

import cofh.api.energy.IEnergyStorage;
import cofh.api.tileentity.IEnergyInfo;

/**
 * Adapter that provides an IEnergyStorage interface on behalf of
 * entities that have an IEnergyInfo interface.  Used with some of
 * the GUI components.
 */
public final class EnergyStorageAdapter implements IEnergyStorage {

	private final IEnergyInfo storage;

	public EnergyStorageAdapter(final IEnergyInfo storage) {
		this.storage = storage;
	}

	@Override
	public int extractEnergy(final int arg0, final boolean arg1) {
		return 0;
	}

	@Override
	public int getEnergyStored() {
		return storage.getInfoEnergyStored();
	}

	@Override
	public int getMaxEnergyStored() {
		return storage.getInfoMaxEnergyStored();
	}

	@Override
	public int receiveEnergy(final int arg0, final boolean arg1) {
		return 0;
	}

}

