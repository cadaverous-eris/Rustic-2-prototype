package rustic.modules.decoration;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.modules.Feature;
import rustic.modules.Module;
import rustic.modules.decoration.blocks.BlockPillar;

public class StonePillars extends Feature {

	public static Block stone_pillar, sandstone_pillar, red_sandstone_pillar;
	
	public StonePillars(Module module) {
		super(module, "stone_pillars");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled(); // TODO: add config option
	}
	
	@Override
	public void createBlocks() {
		stone_pillar = new BlockPillar("stone_pillar").setHardness(1.5f).setResistance(10f);
		sandstone_pillar = new BlockPillar("sandstone_pillar").setHardness(0.8f);
		red_sandstone_pillar = new BlockPillar("red_sandstone_pillar").setHardness(0.8f);
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(stone_pillar);
		registry.register(sandstone_pillar);
		registry.register(red_sandstone_pillar);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, stone_pillar);
		registerItemBlock(registry, sandstone_pillar);
		registerItemBlock(registry, red_sandstone_pillar);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(stone_pillar);
		registerModel(sandstone_pillar);
		registerModel(red_sandstone_pillar);
	}

}
