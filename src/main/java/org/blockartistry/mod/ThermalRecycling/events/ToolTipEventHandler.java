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

package org.blockartistry.mod.ThermalRecycling.events;

import java.util.ArrayList;
import java.util.List;
import org.blockartistry.mod.ThermalRecycling.tooltip.CachingToolTip;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public final class ToolTipEventHandler {

	private static final List<CachingToolTip> hooks = new ArrayList<CachingToolTip>();

	@SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = false)
	public void onToolTipEvent(final ItemTooltipEvent event) {

		if (event == null || event.itemStack == null || event.toolTip == null)
			return;

		for (final CachingToolTip f : hooks)
			f.accept(event.toolTip, event.itemStack);
	}

	private ToolTipEventHandler() {
	}

	public static void register() {
		MinecraftForge.EVENT_BUS.register(new ToolTipEventHandler());
	}

	public static void addHook(final CachingToolTip hook) {
		hooks.add(hook);
	}
}
