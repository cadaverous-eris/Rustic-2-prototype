package rustic.modules.decoration;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.blocks.BlockBase;
import rustic.blocks.BlockDoubleSlabBase;
import rustic.blocks.BlockSlabBase;
import rustic.blocks.BlockStairsBase;
import rustic.items.ItemBlockSlab;
import rustic.modules.Feature;
import rustic.modules.Module;

public class SandstoneBricks extends Feature {

	public static Block sandstone_brick, red_sandstone_brick, sandstone_brick_stairs, red_sandstone_brick_stairs, sandstone_brick_doubleslab, sandstone_brick_slab, red_sandstone_brick_doubleslab, red_sandstone_brick_slab;
	
	public static Item sandstone_brick_slab_item, red_sandstone_brick_slab_item;
	
	public SandstoneBricks(Module module) {
		super(module, "sandstone_bricks");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled(); // TODO: add config option
	}
	
	@Override
	public void createBlocks() {
		sandstone_brick = new BlockBase("sandstone_brick", Material.ROCK).setHardness(0.8f);
		red_sandstone_brick = new BlockBase("red_sandstone_brick", Material.ROCK).setHardness(0.8f);
		sandstone_brick_stairs = new BlockStairsBase(sandstone_brick.getDefaultState(), "sandstone_brick_stairs");
		red_sandstone_brick_stairs = new BlockStairsBase(red_sandstone_brick.getDefaultState(), "red_sandstone_brick_stairs");
		sandstone_brick_doubleslab = new BlockDoubleSlabBase(Material.ROCK, "sandstone_brick_doubleslab").setHardness(0.8f);
		sandstone_brick_slab = new BlockSlabBase(Material.ROCK, "sandstone_brick_slab", sandstone_brick_doubleslab).setHardness(0.8f);
		red_sandstone_brick_doubleslab = new BlockDoubleSlabBase(Material.ROCK, "red_sandstone_brick_doubleslab").setHardness(0.8f);
		red_sandstone_brick_slab = new BlockSlabBase(Material.ROCK, "red_sandstone_brick_slab", red_sandstone_brick_doubleslab).setHardness(0.8f);
	}
	
	@Override
	public void createItems() {
		sandstone_brick_slab_item = new ItemBlockSlab(sandstone_brick_slab, sandstone_brick_doubleslab);
		red_sandstone_brick_slab_item = new ItemBlockSlab(red_sandstone_brick_slab, red_sandstone_brick_doubleslab);
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(sandstone_brick);
		registry.register(red_sandstone_brick);
		registry.register(sandstone_brick_stairs);
		registry.register(red_sandstone_brick_stairs);
		registry.register(sandstone_brick_doubleslab);
		registry.register(sandstone_brick_slab);
		registry.register(red_sandstone_brick_doubleslab);
		registry.register(red_sandstone_brick_slab);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registerItemBlock(registry, sandstone_brick);
		registerItemBlock(registry, red_sandstone_brick);
		registerItemBlock(registry, sandstone_brick_stairs);
		registerItemBlock(registry, red_sandstone_brick_stairs);
		
		registry.register(sandstone_brick_slab_item);
		registry.register(red_sandstone_brick_slab_item);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(sandstone_brick);
		registerModel(red_sandstone_brick);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sandstone_brick_stairs), 0, new ModelResourceLocation(sandstone_brick_stairs.getRegistryName().toString(), "facing=east,half=bottom,shape=straight"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(red_sandstone_brick_stairs), 0, new ModelResourceLocation(red_sandstone_brick_stairs.getRegistryName().toString(), "facing=east,half=bottom,shape=straight"));
		ModelLoader.setCustomModelResourceLocation(sandstone_brick_slab_item, 0, new ModelResourceLocation(sandstone_brick_slab.getRegistryName().toString(), "half=bottom"));
		ModelLoader.setCustomModelResourceLocation(red_sandstone_brick_slab_item, 0, new ModelResourceLocation(red_sandstone_brick_slab.getRegistryName().toString(), "half=bottom"));
	}

}
