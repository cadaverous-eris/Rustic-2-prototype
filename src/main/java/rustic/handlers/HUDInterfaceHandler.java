package rustic.handlers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.Rustic;
import rustic.tileentity.IFluidStorage;
import rustic.tileentity.TileFluidStorage;
import rustic.util.RusticFluidUtil;

@Mod.EventBusSubscriber(modid = Rustic.MODID, value = {Side.CLIENT})
public class HUDInterfaceHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onRenderHUD(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.ALL) return;
		
		Minecraft mc = Minecraft.getMinecraft();
		EntityPlayer player = mc.player;
		World world = mc.world;
		RayTraceResult mouseOver = mc.objectMouseOver;
		
		if (player != null && world != null && mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = mouseOver.getBlockPos();
			IBlockState state = world.getBlockState(pos);
			TileEntity te = world.getTileEntity(mouseOver.getBlockPos());
			ItemStack heldItemMain = player.getHeldItemMainhand();
			ItemStack heldItemOff = player.getHeldItemOffhand();
			ScaledResolution scaledresolution = event.getResolution();
			
			int centerX = scaledresolution.getScaledWidth() / 2;
			int centerY = scaledresolution.getScaledHeight() / 2;
			
			if (te != null && te instanceof IFluidStorage) {
				IFluidStorage tile = (IFluidStorage) te;
				
				if (tile.getCapacity() > 0 && (RusticFluidUtil.isFluidContainer(heldItemMain) || RusticFluidUtil.isFluidContainer(heldItemOff))) {
					String info;
					if (tile.getAmount() > 0 && tile.getFluid() != null) {
						info = tile.getAmount() + "/" + tile.getCapacity() + "mb " + tile.getFluid().getFluid().getLocalizedName(tile.getFluid());
					} else {
						info = I18n.format("tank.empty");
					}
					mc.ingameGUI.drawCenteredString(mc.fontRenderer, info, centerX, centerY - 16, 0xFFFFFF);
				}
			}
			
		}
	}
	
}
