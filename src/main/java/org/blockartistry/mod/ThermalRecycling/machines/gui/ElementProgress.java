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

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.machines.entity.IJobProgress;
import org.blockartistry.mod.ThermalRecycling.machines.entity.MachineStatus;
import org.lwjgl.opengl.GL11;

import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.render.RenderHelper;

public final class ElementProgress extends ElementBase {

	public static final int DEFAULT_SCALE = 42;

	private final IJobProgress progress;

	public ElementProgress(final GuiBase base, final int x, final int y, final IJobProgress progress) {
		super(base, x, y);

		this.texture = MachineStatus.IDLE.getIndicatorTexture();
		this.progress = progress;
		this.sizeX = 24;
		this.sizeY = 17;
		this.texW = 24;
		this.texH = 17;
	}

	protected int getScaled() {
		return (sizeX * progress.getPercentComplete()) / 100;
	}

	@Override
	public void drawBackground(final int mouseX, final int mouseY, final float gameTicks) {

		final int scaledX = getScaled();

		// Need to scale our X size based on progress
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.bindTexture(MachineStatus.IDLE.getIndicatorTexture());
		drawTexturedModalRect(posX, posY, 0, 0, scaledX, sizeY);
	}

	@Override
	public void drawForeground(final int mouseX, final int mouseY) {

		final MachineStatus status = progress.getStatus();
		if (status == MachineStatus.ACTIVE || status == MachineStatus.IDLE)
			return;
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		RenderHelper.bindTexture(status.getIndicatorTexture());
		gui.drawSizedTexturedModalRect(posX + 5, posY + 2, 0, 0, 12, 12, 12, 12);
	}

	@Override
	public void addTooltip(final List<String> list) {

		final MachineStatus status = progress.getStatus();

		String statusString = "???";
		if (status == MachineStatus.ACTIVE)
			statusString = status.getTooltipTextFormatted(progress.getPercentComplete());
		else
			statusString = status.getTooltipText();

		list.add(statusString);
	}
}
