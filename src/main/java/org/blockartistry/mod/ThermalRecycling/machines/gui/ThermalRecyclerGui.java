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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ThermalRecyclerTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cofh.api.energy.IEnergyStorage;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementEnergyStored;

public final class ThermalRecyclerGui extends GuiBase {

	final ThermalRecyclerTileEntity tileEntity;

	class EnergyStorageAdapter implements IEnergyStorage {

		final ThermalRecyclerTileEntity tileEntity;

		public EnergyStorageAdapter(final ThermalRecyclerTileEntity entity) {
			tileEntity = entity;
		}

		@Override
		public int extractEnergy(final int arg0, final boolean arg1) {
			return 0;
		}

		@Override
		public int getEnergyStored() {
			return tileEntity.getInfoEnergyStored();
		}

		@Override
		public int getMaxEnergyStored() {
			return tileEntity.getInfoMaxEnergyStored();
		}

		@Override
		public int receiveEnergy(final int arg0, final boolean arg1) {
			return 0;
		}

	}

	public ThermalRecyclerGui(final InventoryPlayer playerInventory, final IInventory entity) {
		super(new ThermalRecyclerContainer(playerInventory, entity),
				new ResourceLocation(ThermalRecycling.MOD_ID,
						"textures/thermalrecycler_gui.png"));

		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;

		name = StatCollector.translateToLocal("tile.MachineThermalRecycler.name");
		tileEntity = (ThermalRecyclerTileEntity) entity;
	}

	@Override
	public void initGui() {
		super.initGui();
		// GUI dimension is width 427, height 240
		final ElementEnergyStored e = new ElementEnergyStored(this, 12, 18,
				new EnergyStorageAdapter(tileEntity));
		addElement(e);

		final ElementProgress ep = new ElementProgress(this, 77, 34, tileEntity);
		addElement(ep);
	}
}
