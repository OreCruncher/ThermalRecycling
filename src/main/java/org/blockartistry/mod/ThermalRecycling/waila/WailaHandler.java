/* This file is part of ThermalRecycling, licensed under the MIT License (MIT).
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

//
// Thank you Darkhax!
//
// https://github.com/Minecraft-Forge-Tutorials/Waila-Integration
//

package org.blockartistry.mod.ThermalRecycling.waila;

import java.util.List;

import org.blockartistry.mod.ThermalRecycling.ModLog;
import org.blockartistry.mod.ThermalRecycling.ModOptions;
import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.blockartistry.mod.ThermalRecycling.tooltip.DebugToolTip;
import org.blockartistry.mod.ThermalRecycling.tooltip.ScrapToolTip;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import cpw.mods.fml.common.Optional;

@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila")
public final class WailaHandler implements IWailaDataProvider {
	
	private static final String OPTION_REVEAL_ON_SNEAKING = ThermalRecycling.MOD_ID + ".sneakingonly";
	
	private static boolean revealOnSneak(final IWailaConfigHandler config) {
		return config.getConfig(OPTION_REVEAL_ON_SNEAKING, false);
	}
	
	private List<String> gatherText(final ItemStack stack, final List<String> text, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		
		// If the player only wants to reveal when sneaking...
		if(revealOnSneak(config) && !accessor.getPlayer().isSneaking())
			return text;
		
		if (scrapToolTip != null)
			scrapToolTip.apply(text, stack);

		if (debugToolTip != null)
			debugToolTip.apply(text, stack);

		return text;
	}
	
	@Optional.Method(modid = "Waila")
	public static void callbackRegister(final IWailaRegistrar register) {

		ModLog.info("Registering Waila handler...");
		final WailaHandler instance = new WailaHandler();

		switch(ModOptions.getWailaDataLocation()) {
		case 0:
			register.registerHeadProvider(instance, Block.class);
			break;
		case 1:
		default:
			register.registerBodyProvider(instance, Block.class);
			break;
		case 2:
			register.registerTailProvider(instance, Block.class);
			break;
		}
		
		// Options
		register.addConfig(ThermalRecycling.MOD_NAME, OPTION_REVEAL_ON_SNEAKING, false);
	}

	private ScrapToolTip scrapToolTip = null;
	private DebugToolTip debugToolTip = null;

	public WailaHandler() {

		if (ModOptions.getEnableDebugLogging())
			debugToolTip = new DebugToolTip();

		if (ModOptions.getEnableTooltips())
			scrapToolTip = new ScrapToolTip();

	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		return gatherText(itemStack, currenttip, accessor, config);
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		return gatherText(itemStack, currenttip, accessor, config);
	}

	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		return gatherText(itemStack, currenttip, accessor, config);
	}

	@Override
	public ItemStack getWailaStack(final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {
		return null;
	}

	@Override
	public NBTTagCompound getNBTData(final EntityPlayerMP arg0, final TileEntity arg1,
			final NBTTagCompound arg2, final World arg3, final int arg4, final int arg5, final int arg6) {
		return null;
	}
}