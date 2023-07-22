package net.nonopaddle.bow_expantion;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BowExpansion implements ModInitializer {
	public static final String MOD_ID = "bow_expantion";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		ModItemGroup.registerItemGroups();
        ModItems.registerModItems();
	}
}