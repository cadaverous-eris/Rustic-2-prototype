package rustic.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import rustic.Config;
import rustic.tileentity.TilePot;

public class BlockPot extends BlockItemOrFluidStorage {
	
	public static final int NUM_DESIGNS = 12;
	
	public static final PropertyInteger DESIGN = PropertyInteger.create("design", 0, NUM_DESIGNS - 1);
	
	protected static final AxisAlignedBB POT_0_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1D, 0.875D);
	
	public BlockPot() {
		super("pot", Material.ROCK);
		setHardness(2f);
		setSoundType(SoundType.STONE);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TilePot(numRows() * 9, tankSize() * Fluid.BUCKET_VOLUME);
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
		return POT_0_AABB;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(DESIGN, meta);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(DESIGN);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { DESIGN });
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return 0;
	}
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(DESIGN, meta);
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
		return (side == EnumFacing.DOWN) ? BlockFaceShape.CENTER_BIG : BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean canHoldHotFluid() {
		return true;
	}

	@Override
	public boolean canHoldGas() {
		return false;
	}

	@Override
	public int tankSize() {
		return Config.POT_TANK_SIZE;
	}
	
	@Override
	public int numRows() {
		return Config.POT_INVENTORY_SIZE;
	}
	
	public int getModelStyle(int design) {
		return design < 7 ? 0 : 1;
	}
	
	public float getInnerRadius(int modelStyle, int y) {
		if (modelStyle == 0) {
			if (y >= 10) return 0.125f;
			if (y >= 2) return 0.25f;
		} else if (modelStyle == 1) {
			if (y >= 12) return 0.125f;
			if (y >= 6) return 0.25f;
			if (y >= 3) return 0.1875f;
			if (y >= 2) return 0.125f;
		}
		
		return 0f;
	}

}
