package net.nonopaddle.bow_expantion;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    //// ITEMS ////
    public static final Item MACHINE_GUN = registerItem("machine_gun", 
        new MachineGun(new FabricItemSettings())
    );

    //// BLOCKS ITEM
    /* public static final BlockItem SLIME_PAD_ITEM = registerBlockItem("slime_pad",
        new BlockItem(ModBlocks.EXEMPLE_DE_BLOCK, new FabricItemSettings())
    ); */

    //// FONCTIONS ////
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(BowExpansion.MOD_ID, name), item);
    }

    /* private static BlockItem registerBlockItem(String name, BlockItem item) {
        return Registry.register(Registries.ITEM, new Identifier(BowExpansion.MOD_ID, name), item);
    } */

    public static void registerModItems() {
        BowExpansion.LOGGER.debug("Registering Mod Items For " + BowExpansion.MOD_ID);
    }
}
