package org.test.refugiotestmod.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.test.refugiotestmod.Refugio_testmod;
import org.test.refugiotestmod.entity.ModEntities;

public class ModItems {

    public static final Item NEBULA_SPAWN_EGG = registerItem("nebula_spawn_egg",
            new SpawnEggItem(ModEntities.NEBULA, 0x4e0e82, 0x474542 ,new FabricItemSettings()));


    private static Item registerItem(String name, Item item){
        return Registry.register(Registries.ITEM, new Identifier(Refugio_testmod.MOD_ID, name), item);
    }

}
