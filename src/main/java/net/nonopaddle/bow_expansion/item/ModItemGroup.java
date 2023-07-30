package net.nonopaddle.bow_expansion.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nonopaddle.bow_expansion.BowExpansion;

public class ModItemGroup {
    
    public static final ItemGroup BOW_EXPANSION_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP, new Identifier(BowExpansion.MOD_ID, "bow_expansion"),
        FabricItemGroup.builder().displayName(Text.translatable("itemGroup.bow_expansion.bow_expansion_group"))
                .icon(() -> new ItemStack(ModItems.MACHINE_GUN)).entries((DisplayContext, entries) -> {
                    // entrées
                    entries.add(ModItems.MACHINE_GUN);
                    entries.add(ModItems.BOLT_ITEM);
                    entries.add(ModItems.LONGBOW);
                    //
                }).build()
    );

    public static void registerItemGroups() {
        /* ItemGroupEvents.MODIFY_ENTRIES_ALL.invoker().modifyEntries().;
        modifyEntriesEvent(Registries.ITEM_GROUP.).register((entries) -> {
            //entries.add(ModItems.EXEMPLE_DE_BLOCK);
        }); */
    }
    
    
}
