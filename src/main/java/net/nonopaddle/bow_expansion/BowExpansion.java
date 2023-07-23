package net.nonopaddle.bow_expansion;

import net.fabricmc.api.ModInitializer;
import software.bernie.geckolib.GeckoLib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BowExpansion implements ModInitializer {
	public static final String MOD_ID = "bow_expansion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModItemGroup.registerItemGroups();
        ModItems.registerModItems();
		GeckoLib.initialize();
	}
}