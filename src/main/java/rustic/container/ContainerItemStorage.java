package rustic.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import rustic.tileentity.TileItemStorage;

public class ContainerItemStorage extends Container {
	
	private TileItemStorage te;
	
	public ContainerItemStorage(IInventory playerInventory, TileItemStorage te) {
		this.te = te;
		
		addOwnSlots();
		addPlayerSlots(playerInventory);
	}
	
	public TileItemStorage getTile() {
		return te;
	}
	
	private void addPlayerSlots(IInventory playerInventory) {
		int teRows = te.getRows();
		
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 9; col++) {
				int x = 8 + (col * 18);
				int y = (row * 18) + 30 + (teRows * 18);
				this.addSlotToContainer(new Slot(playerInventory, col + row * 9 + 9, x, y));
			}
		}
		for (int row = 0; row < 9; row++) {
			int x = 8 + (row * 18);
			int y = 88 + (teRows * 18);
			this.addSlotToContainer(new Slot(playerInventory, row, x, y));
		}
	}

	private void addOwnSlots() {
		IItemHandler itemHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		int teRows = te.getRows();
		int slotIndex = 0;
		
		for (int row = 0; row < teRows; row++) {
			for (int col = 0; col < 9 && slotIndex < te.getSizeInventory(); col++) {
				int x = 8 + col * 18;
				int y = row * 18 + 17;
				this.addSlotToContainer(new SlotItemHandler(itemHandler, slotIndex, x, y));
				slotIndex++;
			}
		}
	}

	@Nullable
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < te.getSizeInventory()) {
                if (!this.mergeItemStack(itemstack1, te.getSizeInventory(), inventorySlots.size(), true)) {
                	return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, te.getSizeInventory(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
            	slot.putStack(ItemStack.EMPTY);
            } else {
            	slot.onSlotChanged();
            }
        }

        return itemstack;
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return te.canInteractWith(playerIn);
	}

}
