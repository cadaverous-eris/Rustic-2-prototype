package rustic.modules.decoration.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import rustic.blocks.BlockBase;

public class BlockPillar extends BlockBase {

	public static final PropertyEnum<Axis> AXIS = BlockRotatedPillar.AXIS;
	
	public BlockPillar(String name) {
		super(name, Material.ROCK);
		setSoundType(SoundType.STONE);
		setDefaultState(blockState.getBaseState().withProperty(AXIS, Axis.Y));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { AXIS });
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        int i = meta & 12;
        Axis axis = i == 4 ? Axis.X : i == 8 ? Axis.Z : Axis.Y;
        return getDefaultState().withProperty(AXIS, axis);
    }
	
	@Override
	public int getMetaFromState(IBlockState state) {
        EnumFacing.Axis axis = state.getValue(AXIS);
        return axis == Axis.X ? 4 : axis == Axis.Z ? 8 : 0;
    }
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return getDefaultState().withProperty(AXIS, facing.getAxis());
    }
	
	@Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this) {
        	world.setBlockState(pos, state.cycleProperty(AXIS));
        	return true;
        }
        return false;
    }
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		switch (rot) {
        case COUNTERCLOCKWISE_90:
        case CLOCKWISE_90:
        	switch (state.getValue(AXIS)) {
        	case X:
        		return state.withProperty(AXIS, Axis.Z);
        	case Z:
        		return state.withProperty(AXIS, Axis.X);
        	default:
        		return state;
        	}

        default:
        	return state;
        }
    }

	@Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this));
    }

}
