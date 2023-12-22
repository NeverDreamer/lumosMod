package com.Meli4.lumos.common.potions;

import com.Meli4.lumos.LumosMod;
import com.Meli4.lumos.common.block.ModBlocks;
import com.Meli4.lumos.common.tileentity.BlockLightSourceTile;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModPotions {

    public static final DeferredRegister<Effect> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, LumosMod.MOD_ID);

    public static final RegistryObject<Effect> CRYSTALLIZED = POTIONS.register("crystallized",
            CrystallizedEffect::new);
    public static final RegistryObject<Effect> DEATH_MARK = POTIONS.register("death_mark",
            DeathMarkEffect::new);

    public static final RegistryObject<Effect> SOUL_WITHERING = POTIONS.register("soul_withering",
            SoulWitheringEffect::new);
}
