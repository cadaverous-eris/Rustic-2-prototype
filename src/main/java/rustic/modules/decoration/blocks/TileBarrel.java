package rustic.modules.decoration.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rustic.tileentity.TileItemOrFluidStorage;
import rustic.util.ItemStackHandlerRustic;

public class TileBarrel extends TileItemOrFluidStorage {
	
	public TileBarrel() {
		super();
		itemStackHandler = new ItemStackHandlerRustic() {
			@Override
	        protected void onContentsChanged(int slot) {
				markDirty();
				if (!world.isRemote) {
					IBlockState prevState = world.getBlockState(pos);
					boolean wasClosed = prevState.getValue(BlockBarrel.CLOSED);
					boolean empty = true;
					for (ItemStack stack : stacks) {
						if (!stack.isEmpty()) {
							empty = false;
							break;
						}
					}
					boolean closed = !empty;
					
					if (closed != wasClosed) {
						IBlockState state = prevState.withProperty(BlockBarrel.CLOSED, closed);
						world.setBlockState(pos, state, 2);
					}
				}
	        }
		};
	}
	
	public TileBarrel(int invSize, int capacity) {
		super(invSize, capacity);
		itemStackHandler = new ItemStackHandlerRustic(invSize) {
			@Override
	        protected void onContentsChanged(int slot) {
				markDirty();
				if (!world.isRemote) {
					IBlockState prevState = world.getBlockState(pos);
					boolean wasClosed = prevState.getValue(BlockBarrel.CLOSED);
					boolean empty = true;
					for (ItemStack stack : stacks) {
						if (!stack.isEmpty()) {
							empty = false;
							break;
						}
					}
					boolean closed = !empty;
					
					if (closed != wasClosed) {
						IBlockState state = prevState.withProperty(BlockBarrel.CLOSED, closed);
						world.setBlockState(pos, state, 2);
					}
				}
	        }
		};
	}
	
	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
		return oldState.getBlock() != newState.getBlock();
	}

}
