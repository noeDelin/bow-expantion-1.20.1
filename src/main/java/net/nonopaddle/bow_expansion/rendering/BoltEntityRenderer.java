package net.nonopaddle.bow_expansion.rendering;

import net.minecraft.util.Identifier;
import net.nonopaddle.bow_expansion.BowExpansion;
import net.nonopaddle.bow_expansion.entity.custom.BoltEntity;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.client.util.math.MatrixStack;

public class BoltEntityRenderer extends GeoEntityRenderer<BoltEntity>{

    public BoltEntityRenderer(Context context) {
        super(context, new BoltEntityModel());
    }

    @Override
    public Identifier getTextureLocation(BoltEntity entity) {
        return new Identifier(BowExpansion.MOD_ID, "textures/entity/bolt.png");
    }
    
    @Override
    public void render(BoltEntity entity, float entityYaw, float partielTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partielTick, poseStack, bufferSource, packedLight);
    }
}

