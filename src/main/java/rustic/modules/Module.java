package rustic.modules;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class Module {
	
	protected ArrayList<Feature> enabledFeatures = new ArrayList<Feature>();
	
	public abstract String getName();
	
	public abstract boolean enabled();
	
	public void preInit() {
		enabledFeatures.forEach(feature -> feature.preInit());
	}
	
	public void init() {
		enabledFeatures.forEach(feature -> feature.init());
	}
	
	public void postInit() {
		enabledFeatures.forEach(feature -> feature.postInit());
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		enabledFeatures.forEach(feature -> feature.registerBlocks(registry));
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		enabledFeatures.forEach(feature -> feature.registerItems(registry));
	}
	
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		enabledFeatures.forEach(feature -> feature.registerRecipes(registry));
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		enabledFeatures.forEach(feature -> feature.registerModels(event));
	}

}
