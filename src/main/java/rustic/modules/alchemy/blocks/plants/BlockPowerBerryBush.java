package rustic.modules.alchemy.blocks.plants;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.Rustic;

public class BlockPowerBerryBush extends BlockBerryBush {
	
	public BlockPowerBerryBush() {
		super("power_berry_bush");
	}
	
	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		checkAndDropBlock(world, pos, state);

		if (!world.isAreaLoaded(pos, 1)) return;
		
		if (world.canBlockSeeSky(pos) && world.isDaytime() && world.getLightFromNeighbors(pos.up()) >= 9) {
			int age = state.getValue(AGE);
			
			if (age < 6) {
				if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(world, pos, state, rand.nextInt(16) == 0)) {
					world.setBlockState(pos, state.withProperty(AGE, age + 1), 3);
					net.minecraftforge.common.ForgeHooks.onCropsGrowPost(world, pos, state, world.getBlockState(pos));
				}
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public IBlockColor getBlockColor() {
		return new IBlockColor() {
			@Override
			public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
				int color;
				if (world != null && pos != null && tintIndex == 1) {
					color = BiomeColorHelper.getFoliageColorAtPos(world, pos);
				} else {
					color = ColorizerFoliage.getFoliageColorBasic();
				}
				
				int red = (color >> 16) & 0xFF;
				int green = (color >> 8) & 0xFF;
				int blue = color & 0xFF;
				
				red = (int) (red * 0.75);
				blue = (int) (blue * 1.25);
				
				color = (red << 16) | (green << 8) | blue;
				
				return color;
			}
		};
	}

	@Override
	public Item getBerry() {
		return Rustic.alchemy.plants.power_berries;
	}

}
