package rustic.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import rustic.Rustic;

public class BlockBase extends Block {

	public BlockBase(String name, Material mat) {
		super(mat);
		
		setRegistryName(Rustic.MODID, name);
		setUnlocalizedName(Rustic.MODID + "." + name);
		
		setHardness(1F);
		setResistance(10f);
		setSoundType(SoundType.STONE);
		
		setCreativeTab(Rustic.decor_tab);
	}

}
