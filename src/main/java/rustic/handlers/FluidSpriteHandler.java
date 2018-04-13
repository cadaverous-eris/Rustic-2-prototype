package rustic.handlers;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.Rustic;
import rustic.util.RusticFluidUtil;

@Mod.EventBusSubscriber(modid = Rustic.MODID, value = {Side.CLIENT})
public class FluidSpriteHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onTexturesLoaded(TextureStitchEvent.Post event) {
		for (Fluid fluid : FluidRegistry.getRegisteredFluids().values()) {
			if (fluid.getStill() != null) {
				TextureAtlasSprite stillSprite = event.getMap().getAtlasSprite(fluid.getStill().toString());
				if (stillSprite != null) {
					RusticFluidUtil.stillTextures.put(fluid, stillSprite);
				}
			}
		}
	}

}
