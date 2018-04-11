package rustic;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

public class Config {
	
	private final static String CATEGORY_GENERAL = "all.general";
	
	private final static List<String> PROPERTY_ORDER_GENERAL = new ArrayList<String>();
	
	public static int BARREL_INVENTORY_SIZE;
	public static int BARREL_TANK_SIZE;
	public static int POT_INVENTORY_SIZE;
	public static int POT_TANK_SIZE;
	public static int RECEPTACLE_TANK_SIZE;
	
	public static void readConfig() {
		Configuration cfg = Rustic.config;
		try {
			cfg.load();
			initGeneralConfig(cfg);
		} catch (Exception e1) {

		} finally {
			if (cfg.hasChanged()) {
				cfg.save();
			}
		}
	}
	
	private static void initGeneralConfig(Configuration cfg) {
		cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General Options");
		
		BARREL_INVENTORY_SIZE = cfg.getInt("Barrel Inventory Size", CATEGORY_GENERAL, 3, 1, 8, "The number of rows of slots in the GUI for barrels. The number of items they can store will be this value multiplied by 9. This option will have no affect on any barrels that have already been placed in existing worlds.");
		BARREL_TANK_SIZE = cfg.getInt("Barrel Tank Size", CATEGORY_GENERAL, 8, 1, 16, "The number of buckets of fluid barrels can hold. This option will have no affect on any barrels in existing worlds.");
		POT_INVENTORY_SIZE = cfg.getInt("Pot Inventory Size", CATEGORY_GENERAL, 3, 1, 8, "The number of rows of slots in the GUI for pots. The number of items they can store will be this value multiplied by 9. This option will have no affect on any pots that have already been placed in existing worlds.");
		POT_TANK_SIZE = cfg.getInt("Pot Tank Size", CATEGORY_GENERAL, 8, 1, 16, "The number of buckets of fluid pots can hold. This option will have no affect on any pots in existing worlds.");
		RECEPTACLE_TANK_SIZE = cfg.getInt("Receptacle Tank Size", CATEGORY_GENERAL, 8, 1, 16, "The number of buckets of fluid iron receptacles can hold. This option will have no affect on any receptacles in existing worlds.");
		
		PROPERTY_ORDER_GENERAL.add("Barrel Inventory Size");
		PROPERTY_ORDER_GENERAL.add("Barrel Tank Size");
		PROPERTY_ORDER_GENERAL.add("Pot Inventory Size");
		PROPERTY_ORDER_GENERAL.add("Pot Tank Size");
		PROPERTY_ORDER_GENERAL.add("Receptacle Tank Size");
		
		cfg.setCategoryPropertyOrder(CATEGORY_GENERAL, PROPERTY_ORDER_GENERAL);
	}

}
