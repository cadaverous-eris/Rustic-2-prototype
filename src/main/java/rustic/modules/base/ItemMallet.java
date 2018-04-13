package rustic.modules.base;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.items.ItemBase;
import rustic.modules.alchemy.Elixirs;
import rustic.modules.alchemy.blocks.BlockAlembic;
import rustic.modules.alchemy.blocks.BlockReceptacle;

public class ItemMallet extends ItemBase {

	public ItemMallet() {
		super("mallet");
		this.setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
		IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
		
		if (player.isSneaking()) {
			NBTTagCompound tag = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
			NBTTagCompound posTag = new NBTTagCompound();
			
			posTag.setInteger("x", pos.getX());
			posTag.setInteger("y", pos.getY());
			posTag.setInteger("z", pos.getZ());
			tag.setTag("Position", posTag);
			stack.setTagCompound(tag);
			
			return EnumActionResult.SUCCESS;
		} else if (block == Elixirs.iron_receptacle && hasStoredPosition(stack)) {
			BlockPos connectionPos = getStoredPosition(stack);
			IBlockState connectionState = world.getBlockState(connectionPos);
			
			if (connectionState.getBlock() == Elixirs.alembic) {
				int dX = connectionPos.getX() - pos.getX();
				int dY = connectionPos.getY() - pos.getY();
				int dZ = connectionPos.getZ() - pos.getZ();
				if (dY == 0 || (dY == 1 && !connectionState.getValue(BlockAlembic.BASE))) {
					EnumFacing dir = null;
					if (dZ == -1 && dX == 0) dir = EnumFacing.NORTH;
					else if (dZ == 1 && dX == 0) dir = EnumFacing.SOUTH;
					else if (dZ == 0 && dX == -1) dir = EnumFacing.WEST;
					else if (dZ == 0 && dX == 1) dir = EnumFacing.EAST;
					
					if (dir != null) {
						world.setBlockState(pos, state.withProperty(BlockReceptacle.CONNECTED, true).withProperty(BlockReceptacle.PIPE_DIR, dir), 2);
						stack.getTagCompound().removeTag("Position");
		        		if (stack.getTagCompound().hasNoTags()) {
		        			stack.setTagCompound(null);
		        		}
						return EnumActionResult.SUCCESS;
					}
				}
			}
		}
		
		return EnumActionResult.PASS;
    }
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        
		if (player.isSneaking() && hasStoredPosition(stack)) {
        	RayTraceResult result = rayTrace(world, player, false);
        	if (result == null || result.typeOfHit == RayTraceResult.Type.MISS) {
        		stack.getTagCompound().removeTag("Position");
        		if (stack.getTagCompound().hasNoTags()) {
        			stack.setTagCompound(null);
        		}
        		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
        	}
        }
		
		return new ActionResult<ItemStack>(EnumActionResult.PASS, stack);
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (hasStoredPosition(stack)) {
			BlockPos pos = getStoredPosition(stack);
			tooltip.add("(" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")");
		}
    }
	
	@Override
	public String getHighlightTip(ItemStack item, String displayName) {
		if (hasStoredPosition(item)) {
			BlockPos pos = getStoredPosition(item);
			return displayName + " (" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + ")";
		}
        return displayName;
    }
	
	public boolean hasStoredPosition(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey("Position", Constants.NBT.TAG_COMPOUND);
	}
	
	public BlockPos getStoredPosition(ItemStack stack) {
		if (hasStoredPosition(stack)) {
			NBTTagCompound posTag = stack.getTagCompound().getCompoundTag("Position");
			return new BlockPos(posTag.getInteger("x"), posTag.getInteger("y"), posTag.getInteger("z"));
		}
		return null;
	}

}
