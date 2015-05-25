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

/**
 * The Optional.Interface allows for an interface to be stripped from a class if
 * the related mod is not installed. Since we are using Waila the modid is Waila
 * and the interface is IWailaDataProvider. Since we can't directly reference
 * the interface as it may not be installed, the first parameter is a string
 * which represents the class path for this interface.
 */
@Optional.Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = "Waila")
public final class WailaHandler implements IWailaDataProvider {

	/**
	 * Although this is likely not necessary, you can also use the
	 * Optional.Method interface to mark a method to be stripped if a mod is not
	 * detected. In this case we're doing this for all methods which relate to
	 * Waila, so the modid is Waila.
	 * 
	 * The callbackRegister method is used by Waila to register this data
	 * provider. Note that inside this method we initialize a new instance of
	 * this class, this instance is used for a lot of the IWailaRegistrar
	 * methods require an instance of the data provider to work. This will also
	 * call the constructor of this class, which can be used to help initialize
	 * other things. Alternatively you can initialize things within this method
	 * as well.
	 */
	@Optional.Method(modid = "Waila")
	public static void callbackRegister(final IWailaRegistrar register) {

		ModLog.info("Registering Waila handler...");
		final WailaHandler instance = new WailaHandler();

		/**
		 * In this example we will be adding two things to our new block we
		 * added earlier. When our data provider is being registered by waila,
		 * we must also register which methods we are going to use. This may
		 * seem redundant, however this allows us to make our provider targeted
		 * towards a specific block. When registering a provided a class is
		 * required for the second parameter, this class is the class of the
		 * block which you are targeting. This is instance sensitive so you can
		 * use Block.class to target all blocks, or if all of your blocks extend
		 * a base block class then that class can be used instead. Although the
		 * features being added in this example can work on a global level, I
		 * will be making them specific to the new block we have added, and any
		 * blocks which are an instance of BlockOre. This means our body
		 * provider will only be called when looking at the new block or an ore
		 * block.
		 */
		register.registerBodyProvider(instance, Block.class);

		/**
		 * If you plan on using nbt data you will also have to use
		 * registerNBTProvider for the block(s). This method works like all the
		 * other data provider methods, however it is specifically for syncing
		 * nbt data from the server to the client. To show off this feature we
		 * will be targeting any block which extends BlockContainer so this
		 * should in theory apply to all tile entities. I am also going to
		 * register our body provider for BlockContainer, so we can globally add
		 * data for every TileEntity.
		 */
		// register.registerNBTProvider(instance, BlockContainer.class);
		// register.registerBodyProvider(instance, BlockContainer.class);

		/**
		 * Another important part to any waila plugin is configuration options.
		 * Configuration options are handled using strings, and can be access
		 * wherever you have access to IWailaConfigHandler. When registering a
		 * new config option there are two string parameters, the first
		 * represents a category for the config option to be placed under, if
		 * one does not exist it will be generated. The other represents the
		 * name of this option, this is used to retrieve the specific config
		 * value, while also used as unlocalized text for the explination of
		 * this option in the configuration menu. In this example mod I will be
		 * adding three features, one which shows how much exp a block will drop
		 * when broken, another which shows if a block can be used as a beacon
		 * block, and another which shows a tileEntity id. Config options have
		 * been added for all three.
		 */
		// register.addConfig("Example-Plugin", "option.example.showEXP");
		// register.addConfig("Example-Plugin", "option.example.showBeacon");
		// register.addConfig("Example-Plugin", "option.example.showTileID");
	}

	ScrapToolTip scrapToolTip = null;
	DebugToolTip debugToolTip = null;

	public WailaHandler() {

		if (ModOptions.getEnableDebugLogging())
			debugToolTip = new DebugToolTip();

		if (ModOptions.getEnableTooltips())
			scrapToolTip = new ScrapToolTip();

	}

	/**
	 * The Waila hud is devided up into three sections, this is to allow for
	 * data to be aranged in a nice way. This method adds data to the header of
	 * the waila tool tip. This is where the game displays the name of the
	 * block. The accessor is an object wrapper which contains all relevant data
	 * while the config parameter allows you to take advantage of the ingame
	 * config gui.
	 */
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaHead(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		return currenttip;
	}

	/**
	 * This method adds data to the body of the waila tool tip. This is where
	 * you should place the majority of your data. The accessor is an object
	 * wrapper which contains all relevant data while the config parameter
	 * allows you to take advantage of the ingame config gui.
	 */
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaBody(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		if (scrapToolTip != null)
			scrapToolTip.apply(currenttip, itemStack);

		if (debugToolTip != null)
			debugToolTip.apply(currenttip, itemStack);

		return currenttip;

		/*        *//**
		 * In all data provider methods we are given access to several
		 * things. The first is itemStack which is an ItemStack representation
		 * of the block being looked at. The second is currenttip which is a
		 * list of string, each string in this list represents a line in the
		 * body of the tooltip, this can be used to add (and remove) information
		 * to the tooltip. The third is accessor which is a wrapper object in
		 * Waila which holds all relevant information. The final thing we are
		 * given is access to the configuration. (note: this method is only
		 * registered to work with blocks which extend BlockOre BlockContainer
		 * and BlockWailaTestBlock. It will only be called when looking at those
		 * blocks.)
		 */
		/*

        *//**
		 * The first thing we are going to add is the amount of exp dropped
		 * by a block. Since any block can have this value, we only need to
		 * check if this feature is enabled in the configuration. For this
		 * tutorial we are going to assume the player isn't using the fortune
		 * effect, although the level of fortune can be obtained by grabbing the
		 * players held item. It's also worth noting that getExpDrop() is not
		 * meant to be called for grabbing information. Although some blocks
		 * like the test block provide a constant values, blocks like coal ore
		 * will have this value change based on random factors.
		 */
		/*
		 * if (config.getConfig("option.wawla.showEXP"))
		 * currenttip.add("EXP Dropped: " +
		 * accessor.getBlock().getExpDrop(accessor.getWorld(),
		 * accessor.getMetadata(), 0));
		 *//**
		 * The second bit of data we are going to add is if this block is
		 * applicable for being a beacon base. Keep in mind that this is only
		 * being called for our custom block, ore blocks and tile entities, so
		 * blocks like emerald blocks will not trigger this method (to do this
		 * globally for all blocks we would have registered with Block.class).
		 * It's also worth noting that the results of this can change based on
		 * the position of the block, and the position of the beacon. In this
		 * example we are using 0 for those values.
		 */
		/*
		 * if (config.getConfig("option.wawla.showBeacon")) {
		 * 
		 * String tip = (accessor.getBlock().isBeaconBase(accessor.getWorld(),
		 * 0, 0, 0, 0, 0, 0) ? (EnumChatFormatting.GREEN + "YES") :
		 * (EnumChatFormatting.RED + "NO")); currenttip.add("Beacon Block: " +
		 * tip); }
		 *//**
		 * Finally we are going to print the tileid for all tile entities.
		 * For this we are going to check the config, and if the TileEntity is
		 * not null.
		 * 
		 */
		/*
		 * 
		 * if (config.getConfig("option.wawla.showTileID") &&
		 * accessor.getTileEntity() != null) currenttip.add("Tile id: " +
		 * accessor.getNBTData().getString("id"));
		 * 
		 * return currenttip;
		 */
	}

	/**
	 * This method adds data to the tail of the waila tool tip. This is where
	 * the game displays the name of the mod which adds this block to the game.
	 * The accessor is an object wrapper which contains all relevant data while
	 * the config parameter allows you to take advantage of the ingame config
	 * gui.
	 */
	@Override
	@Optional.Method(modid = "Waila")
	public List<String> getWailaTail(final ItemStack itemStack,
			final List<String> currenttip, final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {

		return currenttip;
	}

	@Override
	public ItemStack getWailaStack(final IWailaDataAccessor accessor,
			final IWailaConfigHandler config) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NBTTagCompound getNBTData(final EntityPlayerMP arg0, final TileEntity arg1,
			final NBTTagCompound arg2, final World arg3, final int arg4, final int arg5, final int arg6) {
		// TODO Auto-generated method stub
		return null;
	}
}