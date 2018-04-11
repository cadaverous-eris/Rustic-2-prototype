package rustic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
	
	public static CreativeTabs decor_tab = new CreativeTabs("rustic.decor") {
		@Override
		public String getTabLabel() {
			return "rustic.decor";
		}

		@Override
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(RegistryManager.pot);
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
