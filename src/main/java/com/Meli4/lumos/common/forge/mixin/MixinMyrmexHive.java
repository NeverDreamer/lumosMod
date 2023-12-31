package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.bonuses.MyrmexSet;
import com.github.alexthe666.iceandfire.entity.util.MyrmexHive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(MyrmexHive.class)
public class MixinMyrmexHive {

    @Shadow private World world;

    @Inject(method="isPlayerReputationLowEnoughToFight", at=@At("HEAD"), cancellable = true, remap = false)
    public void lumos_playerRep(UUID uuid, CallbackInfoReturnable<Boolean> cir){
        if(world != null){
            PlayerEntity player = world.getPlayerByUuid(uuid);
            if(player != null){
                if (SetBonus.hasArmor(player, MyrmexSet.getInstance())) {
                    cir.setReturnValue(false);
                }
            }
        }


    }
}
