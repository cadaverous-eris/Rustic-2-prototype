package rustic.modules.alchemy.blocks.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.Rustic;
import rustic.blocks.BlockBase;
import rustic.blocks.IColoredBlock;

public class BlockWindThistle extends BlockHerbBase {

	private static final AxisAlignedBB WIND_THISTLE_AABB = new AxisAlignedBB(0.25, 0, 0.25, 0.75, 0.6875, 0.75);
	
	public BlockWindThistle() {
		super("wind_thistle");
	}
	
	@Override
	public boolean canHerbGrow(World world, BlockPos pos, IBlockState state, int age) {
		return super.canHerbGrow(world, pos, state, age);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		Random rand = world instanceof World ? ((World)world).rand : new Random();
		
		int age = state.getValue(AGE);
		
		if (age == getMaxAge()) drops.add(new ItemStack(Rustic.alchemy.plants.wind_thistle_flower));
		for (int i = 0; i < 3 + fortune; ++i) {
            if (rand.nextInt(2 * getMaxAge()) <= age) {
                drops.add(new ItemStack(Rustic.alchemy.plants.wind_thistle_seeds, 1, 0));
            }
        }
				
		return drops;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Rustic.alchemy.plants.wind_thistle_seeds);
	}
	
	@Override
	public float getGrowthChance(World world, BlockPos pos, IBlockState state) {
		return 2.5f;
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return WIND_THISTLE_AABB.offset(state.getOffset(source, pos));
    }

}
