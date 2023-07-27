package net.nonopaddle.bow_expansion;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.nonopaddle.bow_expansion.entity.BowExpansionEntities;
import net.nonopaddle.bow_expansion.rendering.BoltEntityRenderer;

public class BowExpansionClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(BowExpansionEntities.BOLT, BoltEntityRenderer::new);
    }
    
}
