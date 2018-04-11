package rustic.blocks;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import rustic.Rustic;

public class BlockStairsBase extends BlockStairs {
	
	public BlockStairsBase(IBlockState state, String name) {
		super(state);
		
		setRegistryName(name);
		setUnlocalizedName(Rustic.MODID + "." + name);
		
		setLightOpacity(0);
		
		setCreativeTab(Rustic.decor_tab);
	}

}
