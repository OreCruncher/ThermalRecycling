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

package org.blockartistry.mod.ThermalRecycling;

import java.util.ArrayList;

import com.google.common.base.Preconditions;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInterModComms;

public class ModCommunicationHandler {

	public void send(String receiver, String subject, String message) {
		FMLInterModComms.sendMessage(receiver, subject, message);
	}

	public void send(String receiver, String subject, NBTTagCompound message) {
		FMLInterModComms.sendMessage(receiver, subject, message);
	}

	@EventHandler
	public void receive(FMLInterModComms.IMCEvent event) {

		for (final FMLInterModComms.IMCMessage msg : event.getMessages()) {

			if ("ThermalRecyclerRecipe".compareTo(msg.key) == 0) {

				if (msg.isNBTMessage()) {

					try {

						NBTTagCompound nbt = msg.getNBTValue();
						Preconditions.checkNotNull(nbt, "Invalid NBT value");

						ItemStack input = ItemStack.loadItemStackFromNBT(nbt
								.getCompoundTag("Input"));
						Preconditions.checkNotNull(input, "Invalid Input");

						ArrayList<ItemStack> output = new ArrayList<ItemStack>();

						NBTTagList nbttaglist = nbt.getTagList("Output", 10);

						for (int i = 0; i < nbttaglist.tagCount(); ++i) {
							output.add(ItemStack
									.loadItemStackFromNBT(nbttaglist
											.getCompoundTagAt(i)));
						}

						Preconditions.checkState(!output.isEmpty(),
								"No output ItemStacks were specified");

						// Make the call to set the recipe here

					} catch (Exception e) {
						ModLog.info(
								"[%s; '%s'] Exception processing IMC recipe message",
								msg.getSender(), msg.key);
						e.printStackTrace();
					}

				} else {
					ModLog.info(
							"[%s; '%s'] ThermalRecyclerRecipe messages must be NBTTagCompound",
							msg.getSender(), msg.key);
				}

			} else {
				ModLog.info("[%s; '%s'] Unknown message", msg.getSender(),
						msg.key);
			}
		}
	}
}
