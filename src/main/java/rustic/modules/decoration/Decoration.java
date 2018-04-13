package rustic.modules.decoration;

import rustic.modules.Module;

public class Decoration extends Module {

	public Pots pots = new Pots(this);
	public Barrels barrels = new Barrels(this);
	public SandstoneBricks sandstoneBricks = new SandstoneBricks(this);
	public PolishedStones polishedStones = new PolishedStones(this);
	public StonePillars stonePillars = new StonePillars(this);
	
	@Override
	public String getName() {
		return "decoration";
	}

	@Override
	public boolean enabled() {
		return true; // TODO: add config option
	}
	
	@Override
	public void preInit() {
		if (pots.enabled()) enabledFeatures.add(pots);
		if (barrels.enabled()) enabledFeatures.add(barrels);
		if (sandstoneBricks.enabled()) enabledFeatures.add(sandstoneBricks);
		if (polishedStones.enabled()) enabledFeatures.add(polishedStones);
		if (stonePillars.enabled()) enabledFeatures.add(stonePillars);
		
		super.preInit();
	}

}
