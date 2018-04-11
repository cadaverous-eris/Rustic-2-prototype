package rustic.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.Item;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.RegistryManager;
import rustic.Rustic;
import rustic.blocks.BlockPot;
import rustic.network.MessagePotMeta;
import rustic.network.PacketHandler;

@Mod.EventBusSubscriber(modid = Rustic.MODID)
public class PotHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onPotMouseWheel(MouseEvent event) {
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		if (player != null) {
			if (event.getButton() == 2 && !event.isButtonstate() && player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(RegistryManager.pot)) {
				RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
				if (mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK && player.world != null) {
					IBlockState state = player.world.getBlockState(mouseOver.getBlockPos());
					if (state.getBlock() == RegistryManager.pot) {
						int damage = state.getValue(BlockPot.DESIGN);
						player.getHeldItemMainhand().setItemDamage(damage);
						PacketHandler.INSTANCE.sendToServer(new MessagePotMeta(damage));
					}
				}
			}
			
			if (player.isSneaking() && event.getDwheel() != 0 && player.getHeldItemMainhand().getItem() == Item.getItemFromBlock(RegistryManager.pot)) {
				event.setCanceled(true);

				int damage = (player.getHeldItemMainhand().getItemDamage() + (event.getDwheel() / 120)) % (BlockPot.NUM_DESIGNS);
				if (damage < 0) {
					damage = BlockPot.NUM_DESIGNS + damage;
				}
				player.getHeldItemMainhand().setItemDamage(damage);
				PacketHandler.INSTANCE.sendToServer(new MessagePotMeta(damage));

			}
		}
	}
	
}
