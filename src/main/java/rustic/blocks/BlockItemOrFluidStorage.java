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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rustic.tileentity.TileFluidStorage;
import rustic.tileentity.TileItemOrFluidStorage;
import rustic.tileentity.TileItemStorage;
import rustic.util.RusticFluidUtil;

public abstract class BlockItemOrFluidStorage extends BlockItemStorage {

	public BlockItemOrFluidStorage(String name, Material mat) {
		super(name, mat);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileItemOrFluidStorage(numRows() * 9, tankSize() * Fluid.BUCKET_VOLUME);
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
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileItemOrFluidStorage tile = (TileItemOrFluidStorage) world.getTileEntity(pos);
		ItemStack stack = player.getHeldItem(hand);
		
		if ((tile.getCapacity() > 0 && FluidUtil.getFluidContained(stack) != null) || tile.getAmount() > 0) {
			return tile.activate(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
		}
		
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase player, ItemStack stack) {
		if (world.getTileEntity(pos) != null) {
			TileItemOrFluidStorage tile = (TileItemOrFluidStorage) world.getTileEntity(pos);
			if (stack.hasTagCompound()) {
				FluidTank tank = tile.getFluidTank();
				NBTTagCompound nbt = stack.getTagCompound();
				
				if (nbt.hasKey("Capacity", Constants.NBT.TAG_INT)) {
					tank.setCapacity(nbt.getInteger("Capacity"));
				}
				tank.readFromNBT(nbt);
				tile.markDirty();
			}
			if (stack.hasDisplayName()) {
				tile.setCustomName(stack.getDisplayName());
				tile.markDirty();
			}
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		((TileItemOrFluidStorage) worldIn.getTileEntity(pos)).breakBlock(worldIn, pos, state);
		worldIn.removeTileEntity(pos);
	}
	
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		return new ArrayList<ItemStack>();
	}
	
	public abstract boolean canHoldHotFluid();
	
	public abstract boolean canHoldGas();
	
	public abstract int tankSize();

}
