package org.test.refugiotestmod;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.refugiotestmod.entity.ModEntities;
import org.test.refugiotestmod.entity.custom.NebulaEntity;

public class Refugio_testmod implements ModInitializer {

    public static final String MOD_ID = "refugio-testmod";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {

        LOGGER.info("INFO - Mod iniciado!");

        FabricDefaultAttributeRegistry.register(ModEntities.NEBULA, NebulaEntity.setAttributes());

    }
}
