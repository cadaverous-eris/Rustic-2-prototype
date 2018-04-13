package rustic;

import java.io.File;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.modules.Module;
import rustic.modules.alchemy.Alchemy;
import rustic.modules.base.Base;
import rustic.modules.decoration.Decoration;
import rustic.modules.decoration.Pots;
import rustic.proxy.CommonProxy;

@Mod(modid = Rustic.MODID, name = Rustic.NAME, version = Rustic.VERSION, dependencies = Rustic.DEPENDENCIES)
public class Rustic {
	
	public static final String MODID = "rustic";
	public static final String NAME = "Rustic 2";
	public static final String VERSION = "2.0.0";
	public static final String DEPENDENCIES = "";
	
	@Mod.Instance
	public static Rustic instance;
	
	@SidedProxy(clientSide = "rustic.proxy.ClientProxy", serverSide = "rustic.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	public static final Logger logger = LogManager.getLogger(MODID);
	
	public static Configuration config;
	
	public static ArrayList<Module> modules = new ArrayList();
	
	public static Base base = new Base();
	public static Alchemy alchemy = new Alchemy();
	public static Decoration decoration = new Decoration();
	
	public static CreativeTabs tab = new CreativeTabs("rustic") {
		@Override
		public String getTabLabel() {
			return "rustic";
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(Pots.pot);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		File directory = event.getModConfigurationDirectory();
        Rustic.config = new Configuration(new File(directory.getPath(), "rustic.cfg"));
        Config.readConfig();
		
        modules.add(base);
        if (alchemy.enabled()) modules.add(alchemy);
        if (decoration.enabled()) modules.add(decoration);
        
        modules.forEach(module -> module.preInit());
        
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		modules.forEach(module -> module.init());
		
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (Rustic.config.hasChanged()) {
			Rustic.config.save();
        }
		
		modules.forEach(module -> module.postInit());
		
		proxy.postInit(event);
	}
	
	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			modules.forEach(module -> module.registerBlocks(event.getRegistry()));
		}
		
		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			modules.forEach(module -> module.registerItems(event.getRegistry()));
		}
		
		@SubscribeEvent
		public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
			modules.forEach(module -> module.registerRecipes(event.getRegistry()));
		}
		
		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void registerModels(ModelRegistryEvent event) {
			modules.forEach(module -> module.registerModels(event));
		}
		
	}

}
