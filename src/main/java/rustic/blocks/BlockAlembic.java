package rustic.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAlembic extends BlockBase {
	
	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyBool BASE = PropertyBool.create("base");
	
	public static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.5, 1);
	public static final AxisAlignedBB FULL_AABB2 = new AxisAlignedBB(0, -1, 0, 1, 0.5, 1);
	public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.75, 1);
	public static final AxisAlignedBB GOURD_AABB = new AxisAlignedBB(0.25, 0.75, 0.25, 0.75, 1, 0.75);
	public static final AxisAlignedBB NECK_AABB = new AxisAlignedBB(0.375, 1, 0.375, 0.625, 1.5, 0.625);
	public static final AxisAlignedBB HALF_BLOCK_AABB = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	
	
	public BlockAlembic() {
		super("alembic", Material.ROCK);
		setHardness(2f);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, BASE });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState().withProperty(BASE, (meta & 4) > 0);
		return state.withProperty(FACING, EnumFacing.getFront(5 - (meta & 3)));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 5 - ((EnumFacing) state.getValue(FACING)).getIndex();
		if (state.getValue(BASE)) meta |= 4;
		return meta;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
		if (state.getValue(BASE)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, GOURD_AABB);
			addCollisionBoxToList(pos, entityBox, collidingBoxes, NECK_AABB);
		}
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return state.getValue(BASE) ? FULL_BLOCK_AABB : HALF_BLOCK_AABB;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
    public AxisAlignedBB getSelectedBoundingBox(IBlockState state, World worldIn, BlockPos pos) {
        return (state.getValue(BASE) ? FULL_AABB : FULL_AABB2).offset(pos);
    }
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if (state.getValue(BASE) && face == EnumFacing.DOWN) return BlockFaceShape.SOLID;
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
        return state.getValue(BASE) ? EnumBlockRenderType.MODEL : EnumBlockRenderType.INVISIBLE;
    }
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY,
			float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(BASE, true).withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		return super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
	}
	
	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		if (state.getValue(BASE)) {
			world.setBlockState(pos.up(), state.withProperty(BASE, false));
		}
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		if (state.getValue(BASE)){
			world.setBlockToAir(pos.up());
		} else {
			world.setBlockToAir(pos.down());
		}
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

}
