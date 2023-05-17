package org.test.refugiotestmod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import org.test.refugiotestmod.entity.ModEntities;
import org.test.refugiotestmod.entity.client.NebulaRenderer;

public class Refugio_testmodClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {


        EntityRendererRegistry.register(ModEntities.NEBULA, NebulaRenderer::new);
    }
}
