package rustic.modules.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.blocks.BlockBase;
import rustic.modules.Feature;
import rustic.modules.Module;

public class PolishedStones extends Feature {

	public static Block polished_stone, polished_sandstone, polished_red_sandstone;
	
	public PolishedStones(Module module) {
		super(module, "polished_stones");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled(); // TODO: add config option
	}
	
	@Override
	public void createBlocks() {
		polished_stone = new BlockBase("polished_stone", Material.ROCK).setHardness(1.5f).setResistance(10f);
		polished_sandstone = new BlockBase("polished_sandstone", Material.ROCK).setHardness(0.8f);
		polished_red_sandstone = new BlockBase("polished_red_sandstone", Material.ROCK).setHardness(0.8f);
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(polished_stone);
		registry.register(polished_sandstone);
		registry.register(polished_red_sandstone);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, polished_stone);
		registerItemBlock(registry, polished_sandstone);
		registerItemBlock(registry, polished_red_sandstone);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(polished_stone);
		registerModel(polished_sandstone);
		registerModel(polished_red_sandstone);
	}

}
