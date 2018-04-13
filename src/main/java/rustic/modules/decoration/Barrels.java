package rustic.modules.decoration;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.Rustic;
import rustic.modules.Feature;
import rustic.modules.Module;
import rustic.modules.decoration.blocks.BlockBarrel;
import rustic.modules.decoration.blocks.TileBarrel;
import rustic.modules.decoration.renderer.BarrelTESR;

public class Barrels extends Feature {

	public static Block barrel;
	
	public Barrels(Module module) {
		super(module, "barrels");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled(); // TODO: add config option
	}
	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void createBlocks() {
		barrel = new BlockBarrel();
	}
	
	@Override
	public void createItems() {
		
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(barrel);
		
		GameRegistry.registerTileEntity(TileBarrel.class, Rustic.MODID + ":barrel");
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, barrel);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(barrel);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new BarrelTESR());
	}

}
