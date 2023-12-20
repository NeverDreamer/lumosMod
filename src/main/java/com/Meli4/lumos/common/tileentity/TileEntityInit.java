package com.Meli4.lumos.common.tileentity;

import com.Meli4.lumos.LumosMod;
import com.Meli4.lumos.common.block.BlockLightSource;
import com.Meli4.lumos.common.block.ModBlocks;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileEntityInit {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, LumosMod.MOD_ID);

    public static final RegistryObject<TileEntityType<BlockLightSourceTile>> BLOCKLIGHTSOURCE = TILE_ENTITY_TYPES.register("blocklight",
            () -> TileEntityType.Builder.create(BlockLightSourceTile::new, ModBlocks.BlockLightSource.get()).build(null));
}
