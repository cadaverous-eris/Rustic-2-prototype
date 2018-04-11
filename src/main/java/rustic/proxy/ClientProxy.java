package rustic.proxy;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import rustic.blocks.IColoredBlock;

public class ClientProxy extends CommonProxy {
	
	private static List<Block> coloredBlocks = new ArrayList<Block>();
	private static List<Item> coloredItems = new ArrayList<Item>();
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
		
		initColorizer();
	}
	
	public static void addColoredBlock(Block block) {
		if (block instanceof IColoredBlock) {
			coloredBlocks.add(block);
		}
	}

	/*public static void addColoredItem(Item item) {
		if (item instanceof IColoredItem) {
			coloredItems.add(item);
		}
	}*/
	
	private void initColorizer() {
		for (Block block : coloredBlocks) {
			if (block instanceof IColoredBlock) {
				IColoredBlock coloredBlock = (IColoredBlock) block;
				if (coloredBlock.getBlockColor() != null) {
					Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler(coloredBlock.getBlockColor(), block);
				}
				if (coloredBlock.getItemColor() != null) {
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(coloredBlock.getItemColor(), block);
				}
			}
		}
		/*for (Item item : coloredItems) {
			if (item instanceof IColoredItem) {
				IColoredItem coloredItem = (IColoredItem) item;
				if (coloredItem.getItemColor() != null) {
					Minecraft.getMinecraft().getItemColors().registerItemColorHandler(coloredItem.getItemColor(), item);
				}
			}
		}*/
	}

}
