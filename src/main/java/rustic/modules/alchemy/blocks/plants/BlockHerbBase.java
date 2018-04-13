package rustic.modules.alchemy.blocks.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.blocks.BlockBase;
import rustic.blocks.IColoredBlock;

public abstract class BlockHerbBase extends BlockBase implements IColoredBlock, IGrowable, IPlantable {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
	
	public BlockHerbBase(String name) {
		super(name, Material.PLANTS);
		
		setHardness(0.2f);
		setSoundType(SoundType.PLANT);
		
		setTickRandomly(true);
	}
	
	public PropertyInteger getAgeProperty() {
		return AGE;
	}
	
	public int getMaxAge() {
		return 6;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { getAgeProperty() });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(getAgeProperty(), meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(getAgeProperty());
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkAndDropBlock(worldIn, pos, state);

		if (!worldIn.isAreaLoaded(pos, 1)) return;
		
		if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
			int age = state.getValue(getAgeProperty());
			
			if (canHerbGrow(worldIn, pos, state, age)) {
				float f = getGrowthChance(worldIn, pos, state);

				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int) (25F / f) + 1) == 0)) {
					worldIn.setBlockState(pos, state.withProperty(getAgeProperty(), age + 1), 3);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				}
			}
		}
	}
	
	public boolean canHerbGrow(World world, BlockPos pos, IBlockState state, int age) {
		return age < getMaxAge() && world.getLightFromNeighbors(pos.up()) >= 9;
	}
	
	public float getGrowthChance(World world, BlockPos pos, IBlockState state) {
		return 2.5f;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		checkAndDropBlock(worldIn, pos, state);
	}
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
	}
	
	protected void checkAndDropBlock(World world, BlockPos pos, IBlockState state) {
		if (!canBlockStay(world, pos, state)) {
			dropBlockAsItem(world, pos, state, 0);
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		if (state.getBlock() == this) {
			IBlockState soil = world.getBlockState(pos.down());
			return soil.getBlock().canSustainPlant(soil, world, pos.down(), EnumFacing.UP, this);
		}
		return false;
	}
	
	protected void growOutward(World world, BlockPos pos, Random rand) {
		List<BlockPos> positions = new ArrayList<BlockPos>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				if (canPlaceBlockAt(world, pos.add(i, 0, j))) {
					positions.add(pos.add(i, 0, j));
				}
			}
		}
		if (positions.size() > 0) {
			BlockPos placePos = positions.get(rand.nextInt(positions.size()));
			world.setBlockState(placePos, getDefaultState(), 3);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return super.getBoundingBox(state, source, pos).offset(state.getOffset(source, pos));
    }
	
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return NULL_AABB;
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public Block.EnumOffsetType getOffsetType() {
		return Block.EnumOffsetType.XZ;
	}
	
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }
	
	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return state.getValue(getAgeProperty()) < getMaxAge();
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return state.getValue(getAgeProperty()) < getMaxAge();
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int age = state.getValue(getAgeProperty());
		int newAge = age + MathHelper.getInt(rand, 2, 4);
		if (newAge > getMaxAge()) newAge = getMaxAge();
		
		world.setBlockState(pos, state.withProperty(getAgeProperty(), newAge), 3);
	}

	@Override
	public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
		return EnumPlantType.Plains;
	}

	@Override
	public IBlockState getPlant(IBlockAccess world, BlockPos pos) {
		return getDefaultState();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IBlockColor getBlockColor() {
		return new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
				if (world != null && pos != null && tintIndex == 1) {
					return BiomeColorHelper.getGrassColorAtPos(world, pos);
				}
				return ColorizerGrass.getGrassColor(0.5D, 1.0D);
			}
		};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IItemColor getItemColor() {
		return new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (getBlockFromItem(stack.getItem()) != BlockHerbBase.this) return 0xFFFFFF;
				IBlockState state = getDefaultState();
				IBlockColor blockColor = getBlockColor();
				return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
			}
		};
	}

}
