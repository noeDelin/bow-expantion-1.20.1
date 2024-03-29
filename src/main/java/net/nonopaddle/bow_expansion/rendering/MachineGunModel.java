package net.nonopaddle.bow_expansion.rendering;

import net.minecraft.util.Identifier;
import net.nonopaddle.bow_expansion.BowExpansion;
import net.nonopaddle.bow_expansion.item.MachineGun;
import software.bernie.geckolib.model.GeoModel;

public class MachineGunModel extends GeoModel<MachineGun> {

    @Override
    public Identifier getModelResource(MachineGun animatable) {
        return new Identifier(BowExpansion.MOD_ID, "geo/machine_gun.geo.json");
    }

    @Override
    public Identifier getTextureResource(MachineGun animatable) {
        return new Identifier(BowExpansion.MOD_ID, "textures/item/machine_gun.png");
    }

    @Override
    public Identifier getAnimationResource(MachineGun animatable) {
        return new Identifier(BowExpansion.MOD_ID, "animations/machine_gun.animation.json");
    }
    
}
