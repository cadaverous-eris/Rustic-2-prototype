package rustic.modules;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class Feature {

	public final Module module;
	protected final String featureName;
	
	public Feature(Module module, String name) {
		this.module = module;
		this.featureName = name;
	}
	
	public abstract boolean enabled();
	
	public boolean canLoad() {
		return module.enabled();
	}
	
	public String getName() {
		return module.getName() + "." + featureName;
	}
	
	public void preInit() {
		createBlocks();
		createItems();
	}
	
	public void init() {
		
	}
	
	public void postInit() {
		
	}
	
	public void createBlocks() {
		
	}
	
	public void createItems() {
		
	}
	
	public void registerBlocks(IForgeRegistry<Block> registry) {
		
	}
	
	public void registerItems(IForgeRegistry<Item> registry) {
		
	}
	
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		
	}
	
	protected void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	protected void registerModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
	}
	
}
