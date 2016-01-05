package org.blockartistry.mod.ThermalRecycling.util;

import java.util.Random;

import org.blockartistry.mod.ThermalRecycling.ModLog;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityHelper {

	private static final Random random = XorShiftRandom.shared;

	/**
	 * Spawns an entity of the specified type at the player location. Parameters
	 * of the entity are tweaked by the provided Json tag string if present.
	 */
	public static void summon(final EntityPlayer player, final String entityType, final String json) {

		final double d0 = (double) player.getPlayerCoordinates().posX + 0.5D;
		final double d1 = (double) player.getPlayerCoordinates().posY;
		final double d2 = (double) player.getPlayerCoordinates().posZ + 0.5D;

		final World world = player.getEntityWorld();

		NBTTagCompound nbt = new NBTTagCompound();

		if (json != null && !json.isEmpty()) {
			try {
				final NBTBase nbtbase = JsonToNBT.func_150315_a(json);

				if (!(nbtbase instanceof NBTTagCompound)) {
					ModLog.warn("Malformed json string");
					return;
				}

				nbt = (NBTTagCompound) nbtbase;
			} catch (final NBTException ex) {
				ex.printStackTrace();
				return;
			}
		}

		nbt.setString("id", entityType);
		final Entity theEntity = EntityList.createEntityFromNBT(nbt, world);

		if (theEntity == null) {
			ModLog.warn("Unable to summon entity");
			return;
		}

		theEntity.setLocationAndAngles(d0, d1, d2, theEntity.rotationYaw, theEntity.rotationPitch);

		world.spawnEntityInWorld(theEntity);
		Entity entity2 = theEntity;

		for (NBTTagCompound compound = nbt; entity2 != null
				&& compound.hasKey("Riding", 10); compound = compound.getCompoundTag("Riding")) {
			Entity entity = EntityList.createEntityFromNBT(compound.getCompoundTag("Riding"), world);

			if (entity != null) {
				entity.setLocationAndAngles(d0, d1, d2, entity.rotationYaw, entity.rotationPitch);
				world.spawnEntityInWorld(entity);
				entity2.mountEntity(entity);
			}

			entity2 = entity;
		}
	}

	/**
	 * Spawns the ItemStack into the world. If it is a large stack it is broken
	 * down into smaller stacks and spun out at different velocities.
	 * 
	 * @param world
	 * @param stack
	 * @param x
	 * @param y
	 * @param z
	 */
	public static void spawnIntoWorld(final World world, final ItemStack stack, final double x, final double y,
			final double z) {

		if (stack == null)
			return;

		final float f = random.nextFloat() * 0.8F + 0.1F;
		final float f1 = random.nextFloat() * 0.8F + 0.1F;
		final float f2 = random.nextFloat() * 0.8F + 0.1F;

		while (stack.stackSize > 0) {
			int j = random.nextInt(21) + 10;

			if (j > stack.stackSize) {
				j = stack.stackSize;
			}

			stack.stackSize -= j;

			final EntityItem item = new EntityItem(world, x + f, y + f1, z + f2,
					new ItemStack(stack.getItem(), j, ItemStackHelper.getItemDamage(stack)));

			if (stack.hasTagCompound()) {
				item.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
			}

			world.spawnEntityInWorld(item);
		}
	}
}
