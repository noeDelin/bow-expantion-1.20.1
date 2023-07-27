package net.nonopaddle.bow_expansion;

import net.nonopaddle.bow_expansion.item.Longbow;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class LongbowRenderer extends GeoItemRenderer<Longbow>{
    public LongbowRenderer() {
        super(new LongbowModel());
    }
}
