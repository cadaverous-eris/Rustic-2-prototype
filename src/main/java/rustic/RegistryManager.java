package rustic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.blocks.BlockAlembic;
import rustic.blocks.BlockBarrel;
import rustic.blocks.BlockBase;
import rustic.blocks.BlockDoubleSlabBase;
import rustic.blocks.BlockPillar;
import rustic.blocks.BlockPot;
import rustic.blocks.BlockReceptacle;
import rustic.blocks.BlockSlabBase;
import rustic.blocks.BlockStairsBase;
import rustic.blocks.plants.BlockBerryBush;
import rustic.blocks.plants.BlockPowerBerryBush;
import rustic.blocks.plants.BlockWindThistle;
import rustic.items.ItemBase;
import rustic.items.ItemBlockSlab;
import rustic.items.ItemFoodBase;
import rustic.items.ItemMallet;
import rustic.items.ItemSeed;
import rustic.proxy.ClientProxy;
import rustic.renderer.BarrelTESR;
import rustic.renderer.PotTESR;
import rustic.tileentity.TileBarrel;
import rustic.tileentity.TileFluidStorage;
import rustic.tileentity.TileItemOrFluidStorage;
import rustic.tileentity.TileItemStorage;
import rustic.tileentity.TilePot;

@Mod.EventBusSubscriber(modid = Rustic.MODID)
@ObjectHolder(Rustic.MODID)
public class RegistryManager {
	
	public static final Block alembic = null, iron_receptacle = null, pot = null, barrel = null, stone_pillar = null, sandstone_pillar = null, red_sandstone_pillar = null, polished_stone = null, polished_sandstone = null, polished_red_sandstone = null, sandstone_brick = null, red_sandstone_brick = null, sandstone_brick_stairs = null, red_sandstone_brick_stairs = null, sandstone_brick_doubleslab = null, sandstone_brick_slab = null, red_sandstone_brick_doubleslab = null, red_sandstone_brick_slab = null, wildberry_bush = null, power_berry_bush = null, wind_thistle = null;
	
	public static final Item mallet = null, sandstone_brick_slab_item = null, red_sandstone_brick_slab_item = null, wildberries = null, power_berries = null, wind_thistle_flower = null, wind_thistle_seeds = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		IForgeRegistry<Block> registry = event.getRegistry();
		
		registry.register(new BlockAlembic());
		registry.register(new BlockReceptacle());
		registry.register(new BlockPot());
		registry.register(new BlockBarrel());
		registry.register(new BlockPillar("stone_pillar").setHardness(1.5f).setResistance(10f));
		registry.register(new BlockPillar("sandstone_pillar").setHardness(0.8f));
		registry.register(new BlockPillar("red_sandstone_pillar").setHardness(0.8f));
		registry.register(new BlockBase("polished_stone", Material.ROCK).setHardness(1.5f).setResistance(10f));
		registry.register(new BlockBase("polished_sandstone", Material.ROCK).setHardness(0.8f));
		registry.register(new BlockBase("polished_red_sandstone", Material.ROCK).setHardness(0.8f));
		Block sandstoneBrick = new BlockBase("sandstone_brick", Material.ROCK).setHardness(0.8f);
		Block redSandstoneBrick = new BlockBase("red_sandstone_brick", Material.ROCK).setHardness(0.8f);
		registry.register(sandstoneBrick);
		registry.register(redSandstoneBrick);
		registry.register(new BlockStairsBase(sandstoneBrick.getDefaultState(), "sandstone_brick_stairs"));
		registry.register(new BlockStairsBase(redSandstoneBrick.getDefaultState(), "red_sandstone_brick_stairs"));
		Block sandstoneBrickDoubleSlab = new BlockDoubleSlabBase(Material.ROCK, "sandstone_brick_doubleslab").setHardness(0.8f);
		Block sandstoneBrickSlab = new BlockSlabBase(Material.ROCK, "sandstone_brick_slab", sandstoneBrickDoubleSlab).setHardness(0.8f);
		registry.register(sandstoneBrickDoubleSlab);
		registry.register(sandstoneBrickSlab);
		Block redSandstoneBrickDoubleSlab = new BlockDoubleSlabBase(Material.ROCK, "red_sandstone_brick_doubleslab").setHardness(0.8f);
		Block redSandstoneBrickSlab = new BlockSlabBase(Material.ROCK, "red_sandstone_brick_slab", redSandstoneBrickDoubleSlab).setHardness(0.8f);
		registry.register(redSandstoneBrickDoubleSlab);
		registry.register(redSandstoneBrickSlab);
		registry.register(new BlockBerryBush("wildberry_bush") {
			@Override
			public Item getBerry() { return wildberries; }
		});
		registry.register(new BlockPowerBerryBush());
		registry.register(new BlockWindThistle());
		
		GameRegistry.registerTileEntity(TileFluidStorage.class, Rustic.MODID + ":fluid_storage");
		GameRegistry.registerTileEntity(TilePot.class, Rustic.MODID + ":pot");
		GameRegistry.registerTileEntity(TileBarrel.class, Rustic.MODID + ":barrel");
	}
	
	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event) {
		IForgeRegistry<Item> registry = event.getRegistry();
		
		registerItemBlock(registry, alembic);
		registerItemBlock(registry, iron_receptacle);
		registry.register(new ItemBlock(pot) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}
		}.setMaxDamage(0).setRegistryName(pot.getRegistryName()));
		registerItemBlock(registry, barrel);
		registerItemBlock(registry, stone_pillar);
		registerItemBlock(registry, sandstone_pillar);
		registerItemBlock(registry, red_sandstone_pillar);
		registerItemBlock(registry, polished_stone);
		registerItemBlock(registry, polished_sandstone);
		registerItemBlock(registry, polished_red_sandstone);
		registerItemBlock(registry, sandstone_brick);
		registerItemBlock(registry, red_sandstone_brick);
		registerItemBlock(registry, sandstone_brick_stairs);
		registerItemBlock(registry, red_sandstone_brick_stairs);
		registry.register(new ItemBlockSlab(sandstone_brick_slab, sandstone_brick_doubleslab));
		registry.register(new ItemBlockSlab(red_sandstone_brick_slab, red_sandstone_brick_doubleslab));
		registerItemBlock(registry, wildberry_bush);
		registerItemBlock(registry, power_berry_bush);
		
		registry.register(new ItemMallet());
		registry.register(new ItemFoodBase("wildberries", 1, 0.75F, false) {
			@Override
			public int getMaxItemUseDuration(ItemStack stack) { return 16; }
		});
		registry.register(new ItemFoodBase("power_berries", 1, 0.5F, false) {
			@Override
			public int getMaxItemUseDuration(ItemStack stack) { return 12; }
		});
		
		registry.register(new ItemBase("wind_thistle_flower"));
		registry.register(new ItemSeed("wind_thistle_seeds", wind_thistle));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
    public static void setupModels(ModelRegistryEvent event) {
		registerModel(alembic);
		registerModel(iron_receptacle);
		registerModel(barrel);
		registerModel(stone_pillar);
		registerModel(sandstone_pillar);
		registerModel(red_sandstone_pillar);
		registerModel(polished_stone);
		registerModel(polished_sandstone);
		registerModel(polished_red_sandstone);
		registerModel(sandstone_brick);
		registerModel(red_sandstone_brick);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(sandstone_brick_stairs), 0, new ModelResourceLocation(sandstone_brick_stairs.getRegistryName().toString(), "facing=east,half=bottom,shape=straight"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(red_sandstone_brick_stairs), 0, new ModelResourceLocation(red_sandstone_brick_stairs.getRegistryName().toString(), "facing=east,half=bottom,shape=straight"));
		ModelLoader.setCustomModelResourceLocation(sandstone_brick_slab_item, 0, new ModelResourceLocation(sandstone_brick_slab.getRegistryName().toString(), "half=bottom"));
		ModelLoader.setCustomModelResourceLocation(red_sandstone_brick_slab_item, 0, new ModelResourceLocation(red_sandstone_brick_slab.getRegistryName().toString(), "half=bottom"));
		registerModel(wildberry_bush);
		registerModel(power_berry_bush);
		registerModel(wind_thistle);
		
		for (int i = 0; i < BlockPot.NUM_DESIGNS; i++) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(pot), i, new ModelResourceLocation(pot.getRegistryName().toString(), "design=" + i));
		
		registerModel(mallet);
		registerModel(wildberries);
		registerModel(power_berries);
		registerModel(wind_thistle_flower);
		registerModel(wind_thistle_seeds);
		
		ClientProxy.addColoredBlock(wildberry_bush);
		ClientProxy.addColoredBlock(power_berry_bush);
		ClientProxy.addColoredBlock(wind_thistle);
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new PotTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBarrel.class, new BarrelTESR());
	}
	
	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
		int id = 0;
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerEntityRenderers(RegistryEvent.Register<EntityEntry> event) {
		
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerSprites(TextureStitchEvent.Pre event) {
		//event.getMap().registerSprite(new ResourceLocation(Rustic.MODID, "blocks/block_texture"));
	}
	
	private static void registerItemBlock(IForgeRegistry<Item> registry, Block block) {
		registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName().toString(), "inventory"));
	}
	
	@SideOnly(Side.CLIENT)
	private static void registerModel(Item item) {
		ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName().toString()));
	}
	
}
