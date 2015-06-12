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

package org.blockartistry.mod.ThermalRecycling.machines.entity.renderers;

import org.blockartistry.mod.ThermalRecycling.ThermalRecycling;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

@SideOnly(Side.CLIENT)
public final class VendingTileEntityRenderer extends TileEntitySpecialRenderer implements IItemRenderer {

	private static final ResourceLocation texture = new ResourceLocation(
			ThermalRecycling.MOD_ID, "textures/blocks/VendingModel.png");
	private static final ModelBase model = new VendingModel();
	
	private static final RenderItem itemRenderer = new RenderItem();
	private static final EntityItem item = new EntityItem(null);
	
	private static final float BLOCK_SCALE = 0.35F;
	private static final float LEFT_EDGE_OFFSET = 0.25F;
	private static final float TOP_EDGE_OFFSET = 0.1F;
	private static final float FRONT_EDGE_OFFSET = -0.12F;
	private static final float IMAGE_SIZE = 0.28F;
	
	// GL11.glTranslatef( 0.25F - x * 0.28F, 0.1F - y * 0.28F, -0.12F);
	private static final float[] xOffset = new float[] {
		LEFT_EDGE_OFFSET - 0 * IMAGE_SIZE,
		LEFT_EDGE_OFFSET - 1 * IMAGE_SIZE,
		LEFT_EDGE_OFFSET - 2 * IMAGE_SIZE,
	};
	
	private static final float[] yOffset = new float[] {
		TOP_EDGE_OFFSET - 0 * IMAGE_SIZE,
		TOP_EDGE_OFFSET - 1 * IMAGE_SIZE,
		TOP_EDGE_OFFSET - 2 * IMAGE_SIZE,
		TOP_EDGE_OFFSET - 3 * IMAGE_SIZE,
		TOP_EDGE_OFFSET - 4 * IMAGE_SIZE,
		TOP_EDGE_OFFSET - 5 * IMAGE_SIZE,
	};
	
	private static ItemStack[] input1 = new ItemStack[] {
		new ItemStack(Blocks.dirt, 64),
		new ItemStack(Blocks.cobblestone, 64),
		new ItemStack(Blocks.gravel, 64),
		new ItemStack(Blocks.sand, 64),
		new ItemStack(Blocks.cactus, 64),
		new ItemStack(Blocks.crafting_table, 64)
	};

	private static ItemStack[] input2 = new ItemStack[] {
		new ItemStack(Items.redstone, 64),
		new ItemStack(Items.dye, 64, 15),
		new ItemStack(Items.wheat, 64),
		new ItemStack(Items.bucket, 64),
		new ItemStack(Items.wheat_seeds, 64),
		new ItemStack(Items.wooden_door, 64)
	};
	
	private static ItemStack[] output = new ItemStack[] {
		new ItemStack(Blocks.planks, 64),
		new ItemStack(Blocks.log, 64),
		new ItemStack(Blocks.log2, 64),
		new ItemStack(Blocks.sapling, 64),
		new ItemStack(Items.cake, 64),
		new ItemStack(Items.apple, 64)
		
	};

	public VendingTileEntityRenderer() {
		super();
		
		item.hoverStart = 0F;
		itemRenderer.setRenderManager(RenderManager.instance);
	}
	
	
	protected void renderItem(final World world, final ItemStack stack, final int rotation, final int x, final int y) {
		
        item.setEntityItemStack(stack);
		
		GL11.glPushMatrix();
		
        // Flip
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		GL11.glTranslatef( xOffset[x], yOffset[y], FRONT_EDGE_OFFSET);
        GL11.glScalef(BLOCK_SCALE * 1.1F, BLOCK_SCALE * 1.1F, BLOCK_SCALE * 1.1F);

        if(Block.getBlockFromItem(stack.getItem()).isNormalCube()) {
          GL11.glTranslatef(0.0F, 0.22F, 0.0F);
        }
        
        itemRenderer.doRender(item, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);
        GL11.glPopMatrix();	
	}
	
	protected void renderTradeInventory(final TileEntity te, final int rotation) {
		
		for(int i = 0; i < input1.length; i++) {
			renderItem(te.getWorldObj(), input1[i], rotation, 0, i);
			renderItem(te.getWorldObj(), input2[i], rotation, 1, i);
			renderItem(te.getWorldObj(), output[i], rotation, 2, i);
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity te, final double x, final double y, final double z,
			final float scale) {

		GL11.glPushMatrix();
		
		// Offset our image so it aligns right with
		// the voxel grid
		GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);
		bindTexture(texture);

		GL11.glPushMatrix();
		
		// Flip
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		int rotation = 0;
		
		if(te != null) {
			// Rotate based on facing
			rotation = 90 * (te.getBlockMetadata() & 7) - 180;
			GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		} else {
			// Render the item in inventory
			GL11.glRotatef(180, 0F, 1.0F, 0F);
		}
		
		// Render
		model.render((Entity) null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		// Render the contents of the trade inventory
		if(te != null) {
			renderTradeInventory(te, rotation);
		}

		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	@Override
	public boolean handleRenderType(final ItemStack itemStack,
			final ItemRenderType itemRenderType) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType itemRenderType,
			final ItemStack itemStack, final ItemRendererHelper itemRendererHelper) {
		return true;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		renderTileEntityAt(null, 0D, 0D, 0D, 0f);
	}
}
