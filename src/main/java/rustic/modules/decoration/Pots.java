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
import rustic.modules.decoration.blocks.BlockPot;
import rustic.modules.decoration.blocks.TilePot;
import rustic.modules.decoration.renderer.PotTESR;
import rustic.network.PacketHandler;

public class Pots extends Feature {

	public static Block pot;
	
	public Pots(Module module) {
		super(module, "pots");
	}
	
	@Override
	public boolean enabled() {
		return module.enabled(); // TODO: add config option
	}
	
	@Override
	public void preInit() {
		super.preInit();
		
		PacketHandler.registerMessage(MessagePotMeta.MessageHolder.class, MessagePotMeta.class, Side.SERVER);
		// TODO: register event handler
	}
	
	@Override
	public void createBlocks() {
		pot = new BlockPot();
	}
	
	@Override
	public void createItems() {
		
	}
	
	@Override
	public void registerBlocks(IForgeRegistry<Block> registry) {
		registry.register(pot);
		
		GameRegistry.registerTileEntity(TilePot.class, Rustic.MODID + ":pot");
	}
	
	@Override
	public void registerItems(IForgeRegistry<Item> registry) {
		registry.register(new ItemBlock(pot) {
			@Override
			public int getMetadata(int damage) {
				return damage;
			}
		}.setMaxDamage(0).setRegistryName(pot.getRegistryName()));
	}
	
	@Override
	public void registerRecipes(IForgeRegistry<IRecipe> registry) {
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels(ModelRegistryEvent event) {
		for (int i = 0; i < BlockPot.NUM_DESIGNS; i++) ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(pot), i, new ModelResourceLocation(pot.getRegistryName().toString(), "design=" + i));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TilePot.class, new PotTESR());
	}

}
