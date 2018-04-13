package rustic.modules.alchemy;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.items.ItemBase;
import rustic.items.ItemFoodBase;
import rustic.items.ItemSeed;
import rustic.modules.Feature;
import rustic.modules.Module;
import rustic.modules.alchemy.blocks.plants.BlockBerryBush;
import rustic.modules.alchemy.blocks.plants.BlockPowerBerryBush;
import rustic.modules.alchemy.blocks.plants.BlockWindThistle;
import rustic.proxy.ClientProxy;

public class Plants extends Feature {

	public static Block wildberry_bush, power_berry_bush, wind_thistle;
	
	public static Item wildberries, power_berries, wind_thistle_flower, wind_thistle_seeds;
	
	public Plants(Module module) {
		super(module, "plants");
	}

	@Override
	public boolean enabled() {
		return module.enabled();
	}
	
	@Override
	public void createBlocks() {
		wildberry_bush = new BlockBerryBush("wildberry_bush") {
			@Override
			public Item getBerry() { return wildberries; }
		};
		power_berry_bush = new BlockPowerBerryBush();
		wind_thistle = new BlockWindThistle();
	}
	
	@Override
	public void createItems() {
		wildberries = new ItemFoodBase("wildberries", 1, 0.75F, false) {
			@Override
			public int getMaxItemUseDuration(ItemStack stack) { return 16; }
		};
		power_berries = new ItemFoodBase("power_berries", 1, 0.5F, false) {
			@Override
			public int getMaxItemUseDuration(ItemStack stack) { return 12; }
		};
		wind_thistle_flower = new ItemBase("wind_thistle_flower");
		wind_thistle_seeds = new ItemSeed("wind_thistle_seeds", wind_thistle);
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(wildberry_bush);
		registry.register(power_berry_bush);
		registry.register(wind_thistle);
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registry.register(wildberries);
		registry.register(power_berries);
		registry.register(wind_thistle_flower);
		registry.register(wind_thistle_seeds);
		
		registerItemBlock(registry, wildberry_bush);
		registerItemBlock(registry, power_berry_bush);
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		registerModel(wildberry_bush);
		registerModel(power_berry_bush);
		registerModel(wind_thistle);
		
		registerModel(wildberries);
		registerModel(power_berries);
		registerModel(wind_thistle_flower);
		registerModel(wind_thistle_seeds);
		
		ClientProxy.addColoredBlock(wildberry_bush);
		ClientProxy.addColoredBlock(power_berry_bush);
		ClientProxy.addColoredBlock(wind_thistle);
	}

}
