package net.nonopaddle.bow_expansion.rendering;

import net.minecraft.util.Identifier;
import net.nonopaddle.bow_expansion.BowExpansion;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;
import software.bernie.geckolib.model.GeoModel;

public class BoltEntityModel extends GeoModel<BoltEntity> {
    @Override
    public Identifier getModelResource(BoltEntity animatable) {
        return new Identifier(BowExpansion.MOD_ID, "geo/bolt.geo.json");
    }

    @Override
    public Identifier getTextureResource(BoltEntity animatable) {
        return new Identifier(BowExpansion.MOD_ID, "textures/entity/bolt.png");
    }

    @Override
    public Identifier getAnimationResource(BoltEntity animatable) {
        return new Identifier(BowExpansion.MOD_ID, "animations/bolt.animation.json");
    }
}
