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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import cofh.lib.gui.GuiBase;
import cofh.lib.gui.element.ElementBase;
import cofh.lib.render.RenderHelper;

public class ElementProgress extends ElementBase {

	public static final int DEFAULT_SCALE = 42;
	static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(
			ThermalRecycling.MOD_ID, "textures/progress_indicator.png");
	static final ResourceLocation JAMMED_TEXTURE = new ResourceLocation(
			ThermalRecycling.MOD_ID, "textures/jammed_indicator.png");

	IJobProgress progress;

	public ElementProgress(GuiBase base, int x, int y, IJobProgress progress) {
		super(base, x, y);

		this.texture = DEFAULT_TEXTURE;
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
	public void drawBackground(int mouseX, int mouseY, float gameTicks) {

		if (progress.isActive()) {
			int scaledX = getScaled();

			// Need to scale our X size based on progress
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.bindTexture(DEFAULT_TEXTURE);
			drawTexturedModalRect(posX, posY, 0, 0, scaledX, sizeY);
		}
	}

	@Override
	public void drawForeground(int mouseX, int mouseY) {

		if (progress.isJammed()) {
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			RenderHelper.bindTexture(JAMMED_TEXTURE);
			drawTexturedModalRect(posX, posY, 0, 0, sizeX, sizeY);
		}
	}

	@Override
	public void addTooltip(List<String> list) {
		if (progress.isJammed()) {
			list.add("Jammed!");
		} else if (progress.isActive()) {
			list.add(String.format("Completion: %d%%",
					progress.getPercentComplete()));
		} else {
			list.add("Idle");
		}
	}
}
