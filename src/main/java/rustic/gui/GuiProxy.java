package rustic.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import rustic.container.ContainerItemStorage;
import rustic.tileentity.TileItemStorage;

public class GuiProxy implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileItemStorage) {
			return ((TileItemStorage) te).createContainer(player.inventory, player);
		}
		
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity te = world.getTileEntity(pos);
		
		if (te instanceof TileItemStorage) {
			return new GuiItemStorage((ContainerItemStorage) ((TileItemStorage) te).createContainer(player.inventory, player), player.inventory);
		}
		
		return null;
	}

}
