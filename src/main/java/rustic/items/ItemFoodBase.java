package rustic.items;

import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import rustic.Rustic;

public class ItemFoodBase extends ItemFood {

	public ItemFoodBase(String name, int amount, float saturation, boolean isWolfFood) {
		super(amount, saturation, isWolfFood);
		setRegistryName(new ResourceLocation(Rustic.MODID, name));
		setUnlocalizedName(Rustic.MODID + "." + name);
		setCreativeTab(Rustic.decor_tab);
	}
	
}
