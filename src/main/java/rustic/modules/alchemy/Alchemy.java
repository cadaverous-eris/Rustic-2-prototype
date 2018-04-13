package rustic.modules.alchemy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import rustic.modules.Module;

public class Alchemy extends Module {

	public Plants plants = new Plants(this);
	public Elixirs elixirs = new Elixirs(this);
	
	@Override
	public String getName() {
		return "alchemy";
	}

	@Override
	public boolean enabled() {
		return true; // TODO: add config option
	}
	
	@Override
	public void preInit() {
		enabledFeatures.add(plants);
		enabledFeatures.add(elixirs);
		
		super.preInit();
	}
	
}
