package rustic.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.Fluid;
import rustic.RegistryManager;
import rustic.blocks.BlockPot;
import rustic.tileentity.TilePot;
import rustic.util.RusticFluidUtil;

public class PotTESR extends TileEntitySpecialRenderer<TilePot> {
	
	@Override
	public void render(TilePot te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int blue, green, red, a;
		int lightx, lighty;
		double minU, minV, maxU, maxV, diffU, diffV;
		
		TilePot tank = (TilePot) te;
		int amount = tank.getAmount();
		int capacity = tank.getCapacity();
		Fluid fluid = tank.getFluid() != null ? tank.getFluid().getFluid() : null;
		if (fluid != null){
			int c = fluid.getColor();
            blue = c & 0xFF;
            green = (c >> 8) & 0xFF;
            red = (c >> 16) & 0xFF;
            a = (c >> 24) & 0xFF;
            
            TextureAtlasSprite sprite = RusticFluidUtil.stillTextures.get(fluid);
            
            if (sprite == null) return;
            
            double fluidHeight = 0.125 + (0.8125 * ((float) amount / (float) capacity));
            float fluidRadius = ((BlockPot) RegistryManager.pot).getInnerRadius(te.getModelStyle(), (int) (fluidHeight * 16));
            
            diffU = sprite.getMaxU() - sprite.getMinU();
            diffV = sprite.getMaxV() - sprite.getMinV();
            
            minU = sprite.getMinU() + (diffU * (0.5 - fluidRadius));
            maxU = sprite.getMinU() + (diffV * (0.5 + fluidRadius));
            minV = sprite.getMinV() + (diffV * (0.5 - fluidRadius));
            maxV = sprite.getMinV() + (diffV * (0.5 + fluidRadius));
            
            int i = getWorld().getCombinedLight(te.getPos(), fluid.getLuminosity());
            lightx = i >> 0x10 & 0xFFFF;
            lighty = i & 0xFFFF;
            
            GlStateManager.pushAttrib();
            GlStateManager.disableCull();
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.blendFunc(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            Tessellator tess = Tessellator.getInstance();
            BufferBuilder buffer = tess.getBuffer();
            buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
            
            buffer.pos(x + 0.5 - fluidRadius, y + fluidHeight, z + 0.5 - fluidRadius).tex(minU, minV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.5 + fluidRadius, y + fluidHeight, z + 0.5 - fluidRadius).tex(maxU, minV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.5 + fluidRadius, y + fluidHeight, z + 0.5 + fluidRadius).tex(maxU, maxV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.5 - fluidRadius, y + fluidHeight, z + 0.5 + fluidRadius).tex(minU, maxV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			tess.draw();
			
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.popAttrib();
		}
	}

}
