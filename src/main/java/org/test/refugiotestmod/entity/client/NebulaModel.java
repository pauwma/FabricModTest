package org.test.refugiotestmod.entity.client;

import net.minecraft.util.Identifier;
import org.test.refugiotestmod.Refugio_testmod;
import org.test.refugiotestmod.entity.custom.NebulaEntity;
import software.bernie.geckolib.model.GeoModel;

public class NebulaModel extends GeoModel<NebulaEntity> {
    @Override
    public Identifier getModelResource(NebulaEntity animatable) {
        return new Identifier(Refugio_testmod.MOD_ID, "geo/nebular.geo.json");
    }

    @Override
    public Identifier getTextureResource(NebulaEntity animatable) {
        return new Identifier(Refugio_testmod.MOD_ID, "textures/entity/nebula.png");
    }

    @Override
    public Identifier getAnimationResource(NebulaEntity animatable) {
        return new Identifier(Refugio_testmod.MOD_ID, "animations/model.animation.json");
    }
}
