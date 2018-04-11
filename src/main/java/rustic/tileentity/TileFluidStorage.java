package rustic.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
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
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import rustic.blocks.BlockFluidStorage;
import rustic.util.RusticFluidUtil;

public class TileFluidStorage extends TileEntity implements IFluidStorage {
	
	protected FluidTank tank = new FluidTank(8 * Fluid.BUCKET_VOLUME) {
		@Override
		protected void onContentsChanged() {
			markDirty();
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 2);
		}
	};
	
	public TileFluidStorage() {
		super();
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
	}
	
	public TileFluidStorage(int capacity) {
		super();
		tank.setTileEntity(this);
		tank.setCanFill(true);
		tank.setCanDrain(true);
		tank.setCapacity(capacity);
	}
	
	public int getCapacity() {
		return tank.getCapacity();
	}

	public int getAmount() {
		return tank.getFluidAmount();
	}
	
	public FluidStack getFluid() {
		return tank.getFluid();
	}
	
	public FluidTank getFluidTank() {
		return tank;
	}
	
	public boolean activate(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!(state.getBlock() instanceof BlockFluidStorage)) return false;
		BlockFluidStorage block = (BlockFluidStorage) state.getBlock();
		ItemStack heldItem = player.getHeldItem(hand);
		
		if (!heldItem.isEmpty()) {
			if (RusticFluidUtil.isFluidContainer(heldItem)) {
				FluidStack f = FluidUtil.getFluidContained(heldItem);
				if ((f != null && (block.canHoldGas() || !f.getFluid().isGaseous()) && (block.canHoldHotFluid() || f.getFluid().getTemperature() <= RusticFluidUtil.HOT_TEMP)) || f == null) {
					boolean didFill = FluidUtil.interactWithFluidHandler(player, hand, this.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side));
					if (didFill) {
						this.world.addBlockEvent(this.pos, this.getBlockType(), 1, 0);
						getWorld().notifyBlockUpdate(pos, state, state, 3);
						this.world.notifyNeighborsOfStateChange(this.pos, this.getBlockType(), true);
						this.markDirty();
					}
				}
				return true;
			}
		}
		return false;
	}

	public void breakBlock(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		this.invalidate();
		if (!world.isRemote && !player.capabilities.isCreativeMode) {
			ItemStack toDrop = new ItemStack(state.getBlock());
			if (tank.getFluidAmount() > 0) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setInteger("Capacity", tank.getCapacity());
				tank.writeToNBT(tag);
				toDrop.setTagCompound(tag);
			}
			world.spawnEntity(new EntityItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, toDrop));
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
    	return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
    	if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }
        return super.getCapability(capability, facing);
    }
	
	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		return new SPacketUpdateTileEntity(getPos(), 0, getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		readFromNBT(pkt.getNbtCompound());
	}
	
	@Override
	public boolean receiveClientEvent(int id, int type) {
		if (id == 1) {
			getWorld().notifyBlockUpdate(pos, getWorld().getBlockState(pos), getWorld().getBlockState(pos), 3);
			world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
			markDirty();
			return true;
		} else {
			return super.receiveClientEvent(id, type);
		}
	}

}
