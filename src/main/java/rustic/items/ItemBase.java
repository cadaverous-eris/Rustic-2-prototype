package rustic.items;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import rustic.Rustic;

public class ItemBase extends Item {
	
	public ItemBase(String name) {
		super();
		setRegistryName(new ResourceLocation(Rustic.MODID, name));
		setUnlocalizedName(Rustic.MODID + "." + name);
		setCreativeTab(Rustic.decor_tab);
	}

}
