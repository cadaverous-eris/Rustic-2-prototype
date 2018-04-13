package rustic.modules.decoration.blocks;

import net.minecraft.block.state.IBlockState;
import rustic.tileentity.TileItemOrFluidStorage;

public class TilePot extends TileItemOrFluidStorage {
	
	int modelStyle = -1;
	
	public TilePot() {
		super();
	}
	
	public TilePot(int invSize, int capacity) {
		super(invSize, capacity);
	}
	
	public int getModelStyle() {
		if (modelStyle == -1) {
			if (world != null) {
				IBlockState state = world.getBlockState(pos);
				if (state.getBlock() instanceof BlockPot) {
					BlockPot block = (BlockPot) state.getBlock();
					modelStyle = block.getModelStyle(state.getValue(BlockPot.DESIGN));	
					return modelStyle;
				}
			}
		}
		return modelStyle;
	}
	
	// TODO: Allow pots to be used as flower pots

}
