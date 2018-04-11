package rustic.tileentity;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public interface IFluidStorage {
	
	public int getCapacity();
	
	public int getAmount();
	
	public FluidStack getFluid();
	
	public FluidTank getFluidTank();

}
