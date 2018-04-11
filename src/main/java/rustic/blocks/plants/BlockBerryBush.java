package rustic.blocks.plants;

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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.blocks.BlockBase;
import rustic.blocks.IColoredBlock;

public abstract class BlockBerryBush extends BlockBase implements IColoredBlock, IPlantable, IGrowable {

	public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 6);
	
	protected static final AxisAlignedBB BUSH_AABB = new AxisAlignedBB(0.125D, 0.0D, 0.125D, 0.875D, 1D, 0.875D);
	
	public BlockBerryBush(String name) {
		super(name, Material.PLANTS);
		
		setSoundType(SoundType.PLANT);
		setHardness(0.5f);
		setLightOpacity(0);
		
		setTickRandomly(true);
		
		Blocks.FIRE.setFireInfo(this, 40, 80);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { AGE });
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(AGE, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(AGE);
	}
	
	public abstract Item getBerry();
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		checkAndDropBlock(worldIn, pos, state);

		if (!worldIn.isAreaLoaded(pos, 1)) return;
		
		if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
			int age = state.getValue(AGE);
			
			if (age < 6) {
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt(11) == 0)) {
					worldIn.setBlockState(pos, state.withProperty(AGE, age + 1), 3);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				}
			}
		}
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
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = super.getDrops(world, pos, state, fortune);
		
		if (getBerry() != null) {
			if (state.getValue(AGE) > 5) {
				drops.add(new ItemStack(getBerry(), 2));
			} else if (state.getValue(AGE) > 4) {
				drops.add(new ItemStack(getBerry()));
			}
		}
		
		return drops;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = player.getHeldItem(hand);
		int age = state.getValue(AGE);
		
		if (age > 4 && !(heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15)) {
			world.setBlockState(pos, state.withProperty(AGE, 0), 3);
			ItemStack stack = new ItemStack(getBerry(), age > 5 ? 2 : 1);
			if (!player.addItemStackToInventory(stack)) {
			    spawnAsEntity(world, pos.offset(player.getHorizontalFacing().getOpposite()), stack);
			}
			return true;
		}
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		checkAndDropBlock(worldIn, pos, state);
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
	
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		IBlockState soil = worldIn.getBlockState(pos.down());
		return super.canPlaceBlockAt(worldIn, pos) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), EnumFacing.UP, this);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BUSH_AABB;
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
	public BlockFaceShape getBlockFaceShape(IBlockAccess world, IBlockState state, BlockPos pos, EnumFacing side) {
        return BlockFaceShape.UNDEFINED;
    }

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		int age = state.getValue(AGE);
		if (age > 5) {
			growOutward(world, pos, rand);
		} else {
			int newAge = age + MathHelper.getInt(rand, 2, 4);
			if (newAge > 6) newAge = 6;
			world.setBlockState(pos, state.withProperty(AGE, newAge), 3);
		}
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
					return BiomeColorHelper.getFoliageColorAtPos(world, pos);
				}
				return ColorizerFoliage.getFoliageColorBasic();
			}
		};
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IItemColor getItemColor() {
		return new IItemColor() {
			@Override
			public int colorMultiplier(ItemStack stack, int tintIndex) {
				if (getBlockFromItem(stack.getItem()) != BlockBerryBush.this) return 0xFFFFFF;
				IBlockState state = getDefaultState();
				IBlockColor blockColor = getBlockColor();
				return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
			}
		};
	}

}
