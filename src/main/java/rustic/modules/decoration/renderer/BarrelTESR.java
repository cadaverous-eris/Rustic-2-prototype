package rustic.modules.decoration.renderer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.DestFactor;
import net.minecraft.client.renderer.GlStateManager.SourceFactor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.fluids.Fluid;
import rustic.modules.decoration.blocks.TileBarrel;
import rustic.util.RusticFluidUtil;

public class BarrelTESR extends TileEntitySpecialRenderer<TileBarrel> {
	
	@Override
	public void render(TileBarrel te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		int blue, green, red, a;
		int lightx, lighty;
		double minU, minV, maxU, maxV, diffU, diffV;
		
		TileBarrel tank = (TileBarrel) te;
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
            
            diffU = sprite.getMaxU() - sprite.getMinU();
            diffV = sprite.getMaxV() - sprite.getMinV();
            
            minU = sprite.getMinU() + diffU * 0.1875;
            maxU = sprite.getMaxU() - diffU * 0.1875;
            minV = sprite.getMinV() + diffV * 0.1875;
            maxV = sprite.getMaxV() - diffV * 0.1875;
            
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
            
            buffer.pos(x + 0.1875, y + 0.125 + 0.8125 * ((float) amount / (float) capacity), z+  0.1875).tex(minU, minV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.8125, y + 0.125 + 0.8125 * ((float) amount / (float) capacity), z + 0.1875).tex(maxU, minV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.8125, y + 0.125 + 0.8125 * ((float) amount / (float) capacity), z + 0.8125).tex(maxU, maxV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			buffer.pos(x + 0.1875, y + 0.125 + 0.8125 * ((float) amount / (float) capacity), z + 0.8125).tex(minU, maxV).lightmap(lightx, lighty).color(red, green, blue, a).endVertex();
			tess.draw();
			
			GlStateManager.disableBlend();
			GlStateManager.enableLighting();
			GlStateManager.enableCull();
			GlStateManager.popAttrib();
		}
	}
	
}
