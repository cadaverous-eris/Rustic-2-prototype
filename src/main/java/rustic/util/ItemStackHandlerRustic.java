package rustic.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public class ItemStackHandlerRustic extends ItemStackHandler {
	
	public ItemStackHandlerRustic() {
		super();
	}
	
	public ItemStackHandlerRustic(int size) {
		super(size);
	}
	
	public NonNullList<ItemStack> getStacks() {
		return this.stacks;
	}
	
	@Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        validateSlotIndex(slot);
        if (!stack.isEmpty() && ItemStack.areItemStacksEqual(this.stacks.get(slot), stack)) return;
        
        this.stacks.set(slot, stack);
        onContentsChanged(slot);
    }

}
