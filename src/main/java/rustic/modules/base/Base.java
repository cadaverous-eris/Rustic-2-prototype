package rustic.modules.base;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.modules.Module;

public class Base extends Module {
	
	public static ItemMallet mallet;
	
	public String getName() {
		return "base";
	}

	@Override
	public boolean enabled() {
		return true;
	}
	
	@Override
	public void preInit() {
		mallet = new ItemMallet();
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registry.register(mallet);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomModelResourceLocation(mallet, 0, new ModelResourceLocation(mallet.getRegistryName().toString()));
	}

}
