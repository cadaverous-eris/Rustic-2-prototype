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
		PacketHandler.registerMessages();
		
		File directory = event.getModConfigurationDirectory();
        Rustic.config = new Configuration(new File(directory.getPath(), "rustic.cfg"));
        Config.readConfig();
	}
	
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Rustic.instance, new GuiProxy());
	}
	
	public void postInit(FMLPostInitializationEvent event) {
		if (Rustic.config.hasChanged()) {
			Rustic.config.save();
        }
	}

}
