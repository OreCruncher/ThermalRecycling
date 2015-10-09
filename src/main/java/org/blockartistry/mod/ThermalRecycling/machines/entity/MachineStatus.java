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

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public enum MachineStatus {

	// The machine is idling waiting for work
	IDLE("msg.MachineStatus.idle", new ResourceLocation(ThermalRecycling.MOD_ID, "textures/progress_indicator.png")),

	// The machine is active doing something
	ACTIVE("msg.MachineStatus.active",
			new ResourceLocation(ThermalRecycling.MOD_ID, "textures/progress_indicator.png")),

	// The machine wants to progress but something is
	// delaying it.
	JAMMED("msg.MachineStatus.jammed", new ResourceLocation(ThermalRecycling.MOD_ID, "textures/jammed_indicator.png")),

	// The machine is out of resources and needs more to
	// continue.
	NEED_MORE_RESOURCES("msg.MachineStatus.needMoreResources",
			new ResourceLocation(ThermalRecycling.MOD_ID, "textures/no_resources_indicator.png")),

	// The machine is out of power - feed it!
	OUT_OF_POWER("msg.MachineStatus.outOfPower",
			new ResourceLocation(ThermalRecycling.MOD_ID, "textures/out_of_power_indicator.png")),

	// The machine can't see the sky
	SKY_BLOCKED("msg.MachineStatus.cantSeeSky",
			new ResourceLocation(ThermalRecycling.MOD_ID, "textures/no_sky_indicator.png")),

	// The machine needs water
	NEEDS_WATER("msg.MachineStatus.needMoreWater",
			new ResourceLocation("textures/items/bucket_water.png"));

	public static MachineStatus map(final int i) {
		return values()[i];
	}

	private final String messageId;
	private final ResourceLocation resource;

	private MachineStatus(final String msg, final ResourceLocation resource) {
		this.messageId = msg;
		this.resource = resource;
	}

	/**
	 * Gets the translated tool tip text for the machine state
	 * 
	 * @return
	 */
	public String getTooltipText() {
		return StatCollector.translateToLocal(messageId);
	}

	/**
	 * Gets the translated tool tip text for the machine state as a format
	 * string.
	 * 
	 * @param parms
	 * @return
	 */
	public String getTooltipTextFormatted(final Object... parms) {
		return StatCollector.translateToLocalFormatted(messageId, parms);
	}

	/**
	 * Gets the display indicator texture for the machine state
	 */
	public ResourceLocation getIndicatorTexture() {
		return resource;
	}
}
