package net.nonopaddle.bow_expansion.rendering;

import net.nonopaddle.bow_expansion.item.MachineGun;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MachineGunRenderer extends GeoItemRenderer<MachineGun>{

    public MachineGunRenderer() {
        super(new MachineGunModel());
    }
    
}
