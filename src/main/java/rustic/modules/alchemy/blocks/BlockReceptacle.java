package rustic.modules.alchemy.blocks;

import java.util.List;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
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
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import rustic.Config;
import rustic.blocks.BlockFluidStorage;
import rustic.modules.alchemy.Elixirs;

public class BlockReceptacle extends BlockFluidStorage {
	
	public static final PropertyDirection PIPE_DIR = PropertyDirection.create("pipe_direction", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool CONNECTED = PropertyBool.create("connected");
	
	public static final AxisAlignedBB RECEPTACLE_AABB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 1, 0.75);
	public static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.5, 0.75);
	public static final AxisAlignedBB NECK_AABB = new AxisAlignedBB(0.375, 0.5, 0.375, 0.625, 1, 0.625);
	public static final AxisAlignedBB NECK_COVERED_AABB = new AxisAlignedBB(0.375, 0.5, 0.375, 0.625, 1.125, 0.625);
	
	public BlockReceptacle() {
		super("iron_receptacle", Material.IRON);
		setSoundType(SoundType.METAL);
		setHardness(3f);
		setDefaultState(blockState.getBaseState().withProperty(PIPE_DIR, EnumFacing.NORTH).withProperty(CONNECTED, false));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PIPE_DIR, CONNECTED });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PIPE_DIR, EnumFacing.getFront(5 - (meta & 3))).withProperty(CONNECTED, (meta & 4) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 5 - ((EnumFacing) state.getValue(PIPE_DIR)).getIndex();
		if (state.getValue(CONNECTED)) meta |= 4;
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
		addCollisionBoxToList(pos, entityBox, collidingBoxes, BASE_AABB);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, state.getValue(CONNECTED) ? NECK_COVERED_AABB : NECK_AABB);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return RECEPTACLE_AABB;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		if (face == EnumFacing.DOWN) return BlockFaceShape.CENTER_BIG;
		return BlockFaceShape.UNDEFINED;
	}
	
	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
	
	/*
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(CONNECTED, world.getBlockState(pos.offset(state.getValue(PIPE_DIR))).getBlock() == RegistryManager.alembic);
	}
	*/
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if (state.getValue(CONNECTED) && fromPos.equals(pos.offset(state.getValue(PIPE_DIR)))) {
			if (world.getBlockState(fromPos).getBlock() != Elixirs.alembic) {
				world.setBlockState(pos, state.withProperty(CONNECTED, false), 2);
			}
		}
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(PIPE_DIR, rot.rotate(state.getValue(PIPE_DIR)));
    }
	
	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(PIPE_DIR)));
    }

	@Override
	public boolean canHoldHotFluid() {
		return true;
	}

	@Override
	public boolean canHoldGas() {
		return true;
	}

	@Override
	public int tankSize() {
		return Config.RECEPTACLE_TANK_SIZE;
	}

}
