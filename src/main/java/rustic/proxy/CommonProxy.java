package rustic.proxy;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import rustic.Config;
import rustic.Rustic;
import rustic.gui.GuiProxy;
import rustic.network.PacketHandler;

public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		
	}
	
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Rustic.instance, new GuiProxy());
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}
