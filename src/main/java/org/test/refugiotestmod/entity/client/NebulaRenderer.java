package org.test.refugiotestmod.entity.client;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.test.refugiotestmod.Refugio_testmod;
import org.test.refugiotestmod.entity.custom.NebulaEntity;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NebulaRenderer extends GeoEntityRenderer<NebulaEntity> {
    public NebulaRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NebulaModel());
    }

    @Override
    public Identifier getTexture(NebulaEntity animatable) {
        return new Identifier(Refugio_testmod.MOD_ID, "textures/entity/nebula.png");
    }

    @Override
    public void render(NebulaEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {

        if (entity.isBaby()){
            poseStack.scale(0.6f,0.6f, 0.6f);
        }

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
