package rustic.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import rustic.RegistryManager;

public class MessagePotMeta implements IMessage {

	int meta = 0;
	
	public MessagePotMeta() {
		
	}
	
	public MessagePotMeta(int i) {
		this.meta = i;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		meta = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(meta);
	}
	
	public static class MessageHolder implements IMessageHandler<MessagePotMeta, IMessage> {
		
		@Override
		public IMessage onMessage(final MessagePotMeta message, final MessageContext ctx) {
			EntityPlayerMP player = ctx.getServerHandler().player;
			for (ItemStack stack : player.inventory.mainInventory) {
				if (stack.getItem() == Item.getItemFromBlock(RegistryManager.pot)) {
					stack.setItemDamage(message.meta);
				}
			}
			for (ItemStack stack : player.inventory.offHandInventory) {
				if (stack.getItem() == Item.getItemFromBlock(RegistryManager.pot)) {
					stack.setItemDamage(message.meta);
				}
			}
			
			return null;
		}
		
	}

}
