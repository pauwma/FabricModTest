package org.test.refugiotestmod.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.test.refugiotestmod.Refugio_testmod;
import org.test.refugiotestmod.entity.custom.NebulaEntity;

public class ModEntities {

    public static final EntityType<NebulaEntity> NEBULA = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(Refugio_testmod.MOD_ID, "nebula"),
            FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, NebulaEntity::new)
                    .dimensions(new EntityDimensions(1.5f, 1.5f, false)).build());

}
