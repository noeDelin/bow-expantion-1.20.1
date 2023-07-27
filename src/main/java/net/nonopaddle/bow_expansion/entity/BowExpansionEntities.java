package net.nonopaddle.bow_expansion.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.nonopaddle.bow_expansion.BowExpansion;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;

public class BowExpansionEntities {
    public static final EntityType<BoltEntity> BOLT = Registry.register(
        Registries.ENTITY_TYPE, new Identifier(BowExpansion.MOD_ID, "bolt"),
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, BoltEntity::newBolt).build()
    );

    
    
    public static void registerModEntities() {
        BowExpansion.LOGGER.debug("Registering Mod Items For " + BowExpansion.MOD_ID);
    }
}
