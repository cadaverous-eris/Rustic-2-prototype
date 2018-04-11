package rustic.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class ItemSeed extends ItemBase implements IPlantable {
	
	protected final Block plant;
	
	public ItemSeed(String name, Block plant) {
		super(name);
		this.plant = plant;
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack itemstack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        if (facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, itemstack) && state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, this) && world.isAirBlock(pos.up())) {
            world.setBlockState(pos.up(), plant.getDefaultState());

            if (player instanceof EntityPlayerMP) {
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), itemstack);
            }
            
            if (!player.capabilities.isCreativeMode) itemstack.shrink(1);
            return EnumActionResult.SUCCESS;
        }
        
        return EnumActionResult.FAIL;
    }

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		if (plant instanceof IPlantable) {
			return ((IPlantable) plant).getPlantType(world, pos);
		}
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		if (plant instanceof IPlantable) {
			return ((IPlantable) plant).getPlant(world, pos);
		}
		return plant.getDefaultState();
	}

}
