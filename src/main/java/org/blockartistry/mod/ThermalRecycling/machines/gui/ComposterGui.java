package org.blockartistry.mod.ThermalRecycling.machines.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.machines.entity.ComposterTileEntity;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementFluidTank;

public final class ComposterGui extends GuiBase {

	final ComposterTileEntity tileEntity;

	public ComposterGui(final InventoryPlayer playerInventory, final IInventory entity) {
		super(new ComposterContainer(playerInventory, entity),
				new ResourceLocation(ThermalRecycling.MOD_ID,
						"textures/composter_gui.png"));

		this.fontRendererObj = Minecraft.getMinecraft().fontRenderer;

		name = StatCollector.translateToLocal("tile.MachineComposter.name");
		tileEntity = (ComposterTileEntity) entity;
	}

	@Override
	public void initGui() {
		super.initGui();

		// GUI dimension is width 427, height 240
		final ElementFluidTank e = new ElementFluidTank(this, 19, 11, tileEntity.getFluidTank());
		e.setSize(15, 55);
		addElement(e);

		final ElementProgress ep = new ElementProgress(this, 105, 32, tileEntity);
		ep.machineStatusMessages[MachineStatus.OUT_OF_POWER.ordinal()] = "msg.MachineStatus.needMoreWater";
		ep.machineStatusMessages[MachineStatus.NEED_MORE_RESOURCES.ordinal()] = "msg.MachineStatus.cantSeeSky";
		addElement(ep);
	}
}
