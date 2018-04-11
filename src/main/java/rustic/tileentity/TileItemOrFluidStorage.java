package rustic.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import rustic.blocks.BlockFluidStorage;
import rustic.blocks.BlockItemOrFluidStorage;
import rustic.util.ItemStackHandlerRustic;
import rustic.util.RusticFluidUtil;

public class TileItemOrFluidStorage extends TileItemStorage implements IFluidStorage {
	
	protected FluidTank tank = new FluidTank(8 * Fluid.BUCKET_VOLUME) {
		@Override
		protected void onContentsChanged() {
			markDirty();
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		}
	};
	
	public TileItemOrFluidStorage() {
		super();
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
	}
	
	public TileItemOrFluidStorage(int invSize, int capacity) {
		super(invSize);
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
		tank.setCapacity(capacity);
	}
	
	@Override
	public int getCapacity() {
		if (isEmpty()) {
			return tank.getCapacity();
		}
		return 0;
	}

	@Override
	public int getAmount() {
		return tank.getFluidAmount();
	}
	
	@Override
	public FluidStack getFluid() {
		return tank.getFluid();
	}
	
	@Override
	public FluidTank getFluidTank() {
		return tank;
	}
	
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!(state.getBlock() instanceof BlockItemOrFluidStorage)) return false;
		BlockItemOrFluidStorage block = (BlockItemOrFluidStorage) state.getBlock();
		ItemStack heldItem = player.getHeldItem(hand);
		
		if (!heldItem.isEmpty()) {
			if (RusticFluidUtil.isFluidContainer(heldItem)) {
				FluidStack f = FluidUtil.getFluidContained(heldItem);
				if ((f != null && (block.canHoldGas() || !f.getFluid().isGaseous()) && (block.canHoldHotFluid() || f.getFluid().getTemperature() <= RusticFluidUtil.HOT_TEMP)) || f == null) {
					boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side));
					if (didFill) {
						this.world.addBlockEvent(this.pos, this.getBlockType(), 1, 0);
						getWorld().notifyBlockUpdate(pos, state, state, 3);
						this.world.notifyNeighborsOfStateChange(pos, getBlockType(), false);
						this.markDirty();
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		this.invalidate();
		if (!world.isRemote) {
			ItemStack toDrop = new ItemStack(state.getBlock());
			if (tank.getFluidAmount() > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Capacity", tank.getCapacity());
				tank.writeToNBT(tag);
				toDrop.setTagCompound(tag);
			} else if (itemStackHandler != null) {
				fillWithLoot(null);
				for (int i = 0; i < itemStackHandler.getSlots(); i ++) {
					state.getBlock().spawnAsEntity(world, pos, itemStackHandler.getStackInSlot(i));
				}
			}
			state.getBlock().spawnAsEntity(world, pos, toDrop);
		}
		world.setTileEntity(pos, null);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        if (tag.hasKey("Capacity", Constants.NBT.TAG_INT)) {
        	tank.setCapacity(tag.getInteger("Capacity"));
        }
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);
        tag.setInteger("Capacity", tank.getCapacity());
        tank.writeToNBT(tag);
        return tag;
    }
    
    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return getCapacity() > 0;
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return getAmount() == 0;
        return false;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getCapacity() > 0) {
            return (T) tank;
        }
    	if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && getAmount() == 0) {
    		if (!getWorld().isRemote) {
        		fillWithLoot(null);
        	}
            return (T) itemStackHandler;
    	}
        return null;
    }
    
    
	public void fillWithLoot(@Nullable EntityPlayer player) {
		if (getAmount() == 0) super.fillWithLoot(player);
	}
    
    @Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
			markDirty();
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

}
