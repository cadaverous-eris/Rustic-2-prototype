package rustic.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import rustic.Rustic;

public class PacketHandler {
	
	public static SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Rustic.MODID);
	
	private static int id = 0;
	
	public static void registerMessages() {
    	INSTANCE.registerMessage(MessagePotMeta.MessageHolder.class, MessagePotMeta.class, id++, Side.SERVER);
    }

}
