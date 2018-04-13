package rustic.modules.alchemy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.Rustic;
import rustic.modules.Feature;
import rustic.modules.Module;
import rustic.modules.alchemy.blocks.BlockAlembic;
import rustic.modules.alchemy.blocks.BlockReceptacle;
import rustic.tileentity.TileFluidStorage;

public class Elixirs extends Feature {

	public static Block alembic, iron_receptacle;
	
	public Elixirs(Module module) {
		super(module, "elixirs");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled();
	}
	
	@Override
	public void createBlocks() {
		alembic = new BlockAlembic();
		iron_receptacle = new BlockReceptacle();
	}
	
	@Override
	public void createItems() {
		
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(alembic);
		registry.register(iron_receptacle);
		
		GameRegistry.registerTileEntity(TileFluidStorage.class, Rustic.MODID + ":fluid_storage");
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, alembic);
		registerItemBlock(registry, iron_receptacle);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(alembic);
		registerModel(iron_receptacle);
	}

}
