package rustic.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import rustic.Config;
import rustic.tileentity.TileBarrel;
import rustic.tileentity.TileItemOrFluidStorage;
import rustic.tileentity.TileItemStorage;

public class BlockBarrel extends BlockItemOrFluidStorage {

	public static final PropertyBool CLOSED = PropertyBool.create("closed");
	
	protected static final AxisAlignedBB BARREL_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1D, 0.875D);
	
	public BlockBarrel() {
		super("barrel", Material.WOOD);
		setHardness(2f);
		setSoundType(SoundType.WOOD);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { CLOSED });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CLOSED, meta == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CLOSED) ? 1 : 0;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBarrel(numRows() * 9, tankSize() * Fluid.BUCKET_VOLUME);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BARREL_AABB;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean canHoldHotFluid() {
		return false;
	}

	@Override
	public boolean canHoldGas() {
		return false;
	}

	@Override
	public int tankSize() {
		return Config.BARREL_TANK_SIZE;
	}

	@Override
	public int numRows() {
		return Config.BARREL_INVENTORY_SIZE;
	}

}
