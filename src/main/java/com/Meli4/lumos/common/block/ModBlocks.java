package com.Meli4.lumos.common.block;

import com.Meli4.lumos.LumosMod;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModBlocks {


    public static final DeferredRegister<Block> BLOCKS
            = DeferredRegister.create(ForgeRegistries.BLOCKS, LumosMod.MOD_ID);

    public static final RegistryObject<Block> BlockLightSource = BLOCKS.register("block_light", () -> new BlockLightSource(AbstractBlock.Properties.create(Material.AIR).doesNotBlockMovement().setAir().setLightLevel((state) -> {
        return 15;
    })));
/*    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }*/

/*    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        //registerBlockItem(name, toReturn);
        return BLOCKS.register(name, block);
    }*/

    /*private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().group(ModItemGroup.TUTORIAL_GROUP)));
    }*/
}
