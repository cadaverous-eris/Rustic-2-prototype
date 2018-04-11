package rustic.util;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;

public class RusticFluidUtil {
	
	public static Map<Fluid, TextureAtlasSprite> stillTextures = Maps.newHashMap();
	
	public static final int HOT_TEMP = 573;
	
	public static boolean isFluidContainer(ItemStack stack) {
		return FluidUtil.getFluidHandler(stack) != null || FluidUtil.getFluidContained(stack) != null;
	}
	
}
