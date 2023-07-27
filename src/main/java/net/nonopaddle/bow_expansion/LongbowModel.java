package net.nonopaddle.bow_expansion;

import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class LongbowModel extends GeoModel<Longbow>{

    @Override
    public Identifier getModelResource(Longbow animatable) {
        return new Identifier(BowExpansion.MOD_ID, "geo/longbow.geo.json");
    }

    @Override
    public Identifier getTextureResource(Longbow animatable) {
        return new Identifier(BowExpansion.MOD_ID, "textures/item/longbow.png");
    }

    @Override
    public Identifier getAnimationResource(Longbow animatable) {
        return new Identifier(BowExpansion.MOD_ID, "animations/longbow.animation.json");
    }
    
}
