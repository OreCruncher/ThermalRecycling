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

package org.blockartistry.mod.ThermalRecycling.machines.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

public final class FluidTankComponent implements IMachineFluidTank {

	private FluidStack fluid;
	private int capacity;
	private TileEntity tile;

	public FluidTankComponent(final int capacity) {
		this(null, capacity);
	}

	public FluidTankComponent(final FluidStack stack, final int capacity) {
		this.fluid = stack;
		this.capacity = capacity;
	}

	public FluidTankComponent(final Fluid fluid, final int amount,
			final int capacity) {
		this(new FluidStack(fluid, amount), capacity);
	}

	public void readFromNBT(final NBTTagCompound nbt) {
		if (!nbt.hasKey("Empty")) {
			final FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
			setFluid(fluid);
		} else {
			setFluid(null);
		}
	}

	public void writeToNBT(final NBTTagCompound nbt) {
		if (fluid != null) {
			fluid.writeToNBT(nbt);
		} else {
			nbt.setString("Empty", "");
		}
	}

	public void setFluid(final FluidStack fluid) {
		this.fluid = fluid;
	}

	public void setCapacity(final int capacity) {
		this.capacity = capacity;
	}

	/* IFluidTank */
	@Override
	public FluidStack getFluid() {
		return fluid;
	}

	@Override
	public int getFluidAmount() {
		return fluid == null ? 0 : fluid.amount;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public boolean hasSpace() {
		return getFluidAmount() < getCapacity();
	}
	
	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this);
	}

	@Override
	public int fill(final FluidStack resource, final boolean doFill) {
		if (resource == null) {
			return 0;
		}

		if (!doFill) {
			if (fluid == null) {
				return Math.min(capacity, resource.amount);
			}

			if (!fluid.isFluidEqual(resource)) {
				return 0;
			}

			return Math.min(capacity - fluid.amount, resource.amount);
		}

		if (fluid == null) {
			fluid = new FluidStack(resource,
					Math.min(capacity, resource.amount));

			if (tile != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid,
						tile.getWorldObj(), tile.xCoord, tile.yCoord,
						tile.zCoord, this, fluid.amount));
			}
			return fluid.amount;
		}

		if (!fluid.isFluidEqual(resource)) {
			return 0;
		}
		int filled = capacity - fluid.amount;

		if (resource.amount < filled) {
			fluid.amount += resource.amount;
			filled = resource.amount;
		} else {
			fluid.amount = capacity;
		}

		if (tile != null) {
			FluidEvent.fireEvent(new FluidEvent.FluidFillingEvent(fluid, tile
					.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord,
					this, filled));
		}
		return filled;
	}

	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain) {
		if (fluid == null) {
			return null;
		}

		final int drained = Math.min(maxDrain, fluid.amount);
		final FluidStack f = new FluidStack(fluid, drained);
		
		if (doDrain) {
			fluid.amount -= drained;
			if (fluid.amount <= 0) {
				fluid = null;
			}

			if (tile != null) {
				FluidEvent.fireEvent(new FluidEvent.FluidDrainingEvent(fluid,
						tile.getWorldObj(), tile.xCoord, tile.yCoord,
						tile.zCoord, this, drained));
			}
		}
		
		return f;
	}
}
