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

package org.blockartistry.mod.ThermalRecycling.util;

import java.lang.ref.WeakReference;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;

// Leveraged from the BuildCraft source code.
public final class FakePlayerHelper {

	protected static GameProfile profile = null;
	protected static WeakReference<EntityPlayer> fakePlayer = new WeakReference<EntityPlayer>(
			null);

	public static void initialize(GameProfile profile) {
		Preconditions.checkNotNull(profile, "GameProfile cannot be null");
		FakePlayerHelper.profile = profile;
	}

	public static void initialize(String name) {
		Preconditions.checkNotNull(name, "String (name) cannot be null");

		FakePlayerHelper.profile = new GameProfile(UUID.nameUUIDFromBytes(name
				.getBytes()), "[" + name + "]");
	}
	
	public static final GameProfile getProfile() {
		return profile;
	}

	private static WeakReference<EntityPlayer> createNewPlayer(WorldServer world) {
		EntityPlayer player = FakePlayerFactory.get(world, profile);
		return new WeakReference<EntityPlayer>(player);
	}

	private static WeakReference<EntityPlayer> createNewPlayer(
			WorldServer world, int x, int y, int z) {
		EntityPlayer player = FakePlayerFactory.get(world, profile);
		player.posX = x;
		player.posY = y;
		player.posZ = z;
		return new WeakReference<EntityPlayer>(player);
	}

	public final static WeakReference<EntityPlayer> getFakePlayer(
			WorldServer world) {

		Preconditions.checkNotNull(world, "WorldServer cannot be null");
		Preconditions.checkNotNull(profile, "GameProfile not initialized");

		if (fakePlayer.get() == null) {
			fakePlayer = createNewPlayer(world);
		} else {
			fakePlayer.get().worldObj = world;
		}

		return fakePlayer;
	}

	public final static WeakReference<EntityPlayer> getFakePlayer(
			WorldServer world, int x, int y, int z) {

		Preconditions.checkNotNull(world, "WorldServer cannot be null");
		Preconditions.checkNotNull(profile, "GameProfile not initialized");

		if (fakePlayer.get() == null) {
			fakePlayer = createNewPlayer(world, x, y, z);
		} else {
			fakePlayer.get().worldObj = world;
			fakePlayer.get().posX = x;
			fakePlayer.get().posY = y;
			fakePlayer.get().posZ = z;
		}

		return fakePlayer;
	}
}
