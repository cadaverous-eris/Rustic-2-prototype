package rustic.modules.decoration.handlers;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.Rustic;
import rustic.modules.decoration.Barrels;
import rustic.modules.decoration.Pots;
import rustic.modules.decoration.blocks.BlockPot;
import rustic.modules.decoration.model.BarrelItemModel;
import rustic.modules.decoration.model.PotItemModel;
import rustic.util.RusticFluidUtil;

@Mod.EventBusSubscriber(modid = Rustic.MODID, value = {Side.CLIENT})
public class ModelHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelBake(ModelBakeEvent event) {
		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(Barrels.barrel.getRegistryName(), "inventory");
		Object object = event.getModelRegistry().getObject(modelResourceLocation);
		if (object != null && object instanceof IBakedModel) {
			IBakedModel model = (IBakedModel) object;
			IBakedModel barrelModel = new BarrelItemModel(model);
			event.getModelRegistry().putObject(modelResourceLocation, barrelModel);
		}
		
		for (int i = 0; i < BlockPot.NUM_DESIGNS; i++) {
			modelResourceLocation = new ModelResourceLocation(Pots.pot.getRegistryName(), "design=" + i);
			object = event.getModelRegistry().getObject(modelResourceLocation);
			if (object != null && object instanceof IBakedModel) {
				IBakedModel model = (IBakedModel) object;
				IBakedModel potModel = new PotItemModel(model);
				event.getModelRegistry().putObject(modelResourceLocation, potModel);
			}
		}
	}

}
