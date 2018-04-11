package rustic.tileentity;

import javax.annotation.Nullable;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import rustic.blocks.BlockItemStorage;
import rustic.container.ContainerItemStorage;
import rustic.util.ItemStackHandlerRustic;

public class TileItemStorage extends TileEntityLockableLoot {
	
	protected ItemStackHandlerRustic itemStackHandler;
	
	public TileItemStorage() {
		super();
		itemStackHandler = new ItemStackHandlerRustic() {
			@Override
	        protected void onContentsChanged(int slot) {
	            markDirty();
	        }
		};
	}
	
	public TileItemStorage(int size) {
		this();
		itemStackHandler.setSize(size);
	}
	
	@Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (!this.checkLootAndRead(compound) && compound.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) compound.getTag("items"));
        }
        if (compound.hasKey("CustomName", 8)) {
            this.setCustomName(compound.getString("CustomName"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        if (!this.checkLootAndWrite(compound)) {
        	compound.setTag("items", itemStackHandler.serializeNBT());
        }
        if (this.hasCustomName()) {
            compound.setString("CustomName", this.customName);
        }
        return compound;
    }
    
    public boolean canInteractWith(EntityPlayer playerIn) {
        return !isInvalid() && world.getTileEntity(pos) == this && playerIn.getDistanceSq(pos.add(0.5D, 0.5D, 0.5D)) <= 64D;
    }
    
    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
        	if (!getWorld().isRemote) {
        		fillWithLoot(null);
        	}
            return (T) itemStackHandler;
        }
        return super.getCapability(capability, facing);
    }
    
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
		this.invalidate();
		if (itemStackHandler != null && !world.isRemote) {
			fillWithLoot(null);
			for (int i = 0; i < itemStackHandler.getSlots(); i ++){
				state.getBlock().spawnAsEntity(world, pos, itemStackHandler.getStackInSlot(i));
			}
		}
		world.setTileEntity(pos, null);
	}
	
	@Override
	public int getSizeInventory() {
		return itemStackHandler.getSlots();
	}
	
	public int getRows() {
		return (int) Math.ceil(itemStackHandler.getSlots() / 9f);
	}

	@Override
	public boolean isEmpty() {
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
			if (!itemStackHandler.getStackInSlot(i).isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public ItemStack getStackInSlot(int index) {
		fillWithLoot(null);
		return itemStackHandler.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count) {
		fillWithLoot(null);
		return itemStackHandler.extractItem(index, count, false);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		fillWithLoot(null);
		ItemStack stack = itemStackHandler.getStackInSlot(index);
		itemStackHandler.setStackInSlot(index, ItemStack.EMPTY);
		return stack;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		fillWithLoot(null);
		itemStackHandler.setStackInSlot(index, stack);
	}
	
	@Override
	public void clear() {
		fillWithLoot(null);
		for (int i = 0; i < itemStackHandler.getSlots(); i++) {
			itemStackHandler.setStackInSlot(i, ItemStack.EMPTY);
		}
	}
	
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return canInteractWith(player);
	}

	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.rustic." + getBlockType().getRegistryName().getResourcePath();
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		if (!world.isRemote) {
			fillWithLoot(playerIn);
		}
		return new ContainerItemStorage(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return "rustic:itemstorage";
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return itemStackHandler.getStacks();
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
	public void setLootTable(ResourceLocation file, long seed) {
		super.setLootTable(file, seed);
		markDirty();
	}

}
