package rustic.blocks;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.tileentity.TileFluidStorage;

public abstract class BlockFluidStorage extends BlockTile {

	public BlockFluidStorage(String name, Material mat) {
		super(name, mat);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileFluidStorage(tankSize() * Fluid.BUCKET_VOLUME);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		super.addInformation(stack, player, tooltip, advanced);
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		
		if (nbttagcompound != null) {
            if (nbttagcompound.hasKey("FluidName", Constants.NBT.TAG_STRING) && nbttagcompound.hasKey("Amount", Constants.NBT.TAG_INT) && nbttagcompound.hasKey("Capacity", Constants.NBT.TAG_INT)) {
            	Fluid fluid = FluidRegistry.getFluid(nbttagcompound.getString("FluidName"));
            	int amount = nbttagcompound.getInteger("Amount");
            	int capacity = nbttagcompound.getInteger("Capacity");
            	FluidStack fluidStack = new FluidStack(fluid, amount);
                tooltip.add(fluid.getLocalizedName(fluidStack));
                tooltip.add(amount + "/" + capacity + "mb");
            }    
        }
	}
	
	@Override
	public void onBlockHarvested(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
		((TileFluidStorage) world.getTileEntity(pos)).breakBlock(world, pos, state, player);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		return ((TileFluidStorage) world.getTileEntity(pos)).activate(world, pos, state, player, hand, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		super.onBlockPlacedBy(world, pos, state, player, stack);
		if (stack.hasTagCompound() && world.getTileEntity(pos) != null) {
			TileFluidStorage tile = (TileFluidStorage) world.getTileEntity(pos);
			FluidTank tank = tile.getFluidTank();
			NBTTagCompound nbt = stack.getTagCompound();
			
			if (nbt.hasKey("Capacity", Constants.NBT.TAG_INT)) {
				tank.setCapacity(nbt.getInteger("Capacity"));
			}
			tank.readFromNBT(nbt);
			
			tile.markDirty();
		}
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<ItemStack>();
	}
	
	public abstract boolean canHoldHotFluid();
	
	public abstract boolean canHoldGas();
	
	public abstract int tankSize();

}
