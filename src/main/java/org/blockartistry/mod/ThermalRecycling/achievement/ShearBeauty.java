package org.blockartistry.mod.ThermalRecycling.achievement;

import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

import org.blockartistry.mod.ThermalRecycling.ItemManager;
import org.blockartistry.mod.ThermalRecycling.items.Material;

public class ShearBeauty extends Achievement {

	public ShearBeauty() {
		super("shearBeauty", "shearBeauty", -3 /* x */, 0 /* y */, new ItemStack(
				ItemManager.material, 1, Material.GARDEN_SHEARS), null);
	}
}
