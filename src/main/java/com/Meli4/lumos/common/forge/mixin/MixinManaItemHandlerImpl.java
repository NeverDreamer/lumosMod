package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.bonuses.StarIdolSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.common.impl.mana.ManaItemHandlerImpl;
import vazkii.botania.common.item.equipment.bauble.ItemBauble;

@Mixin(ManaItemHandlerImpl.class)
public class MixinManaItemHandlerImpl {

    @Inject(at=@At("HEAD"), method = "requestManaExact", remap = false, cancellable = true)
    public void lumos_OnRequestManaExact(ItemStack stack, PlayerEntity player, int manaToGet, boolean remove, CallbackInfoReturnable<Boolean> ci){
        if((stack.getItem() instanceof IManaUsingItem) && (stack.getItem() instanceof ItemBauble)){
            if(SetBonus.hasArmor(player, StarIdolSet.getInstance())){
                ci.setReturnValue(true);
            }
        }
    }
}
