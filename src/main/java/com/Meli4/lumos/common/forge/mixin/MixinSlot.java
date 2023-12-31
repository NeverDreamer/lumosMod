package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.bonuses.DeathSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public abstract class MixinSlot {
    @Shadow public abstract ItemStack getStack();

    @Inject(method="canTakeStack", at=@At("HEAD"), cancellable = true)
    public void lumos_canTakeStack(PlayerEntity playerIn, CallbackInfoReturnable<Boolean> cir){
        boolean flag = false;
        for(ItemStack stack: playerIn.getEquipmentAndArmor()){
            if(stack.equals(this.getStack())){
                flag = true;
            }
        }
        if(flag){
            if(playerIn.world.isRemote){
                IBonus bonus = (IBonus) BonusCapability.getBonus(playerIn).orElse((IBonus) null);
                if(bonus!=null){
                    if(SetBonus.hasArmor(playerIn, DeathSet.INSTANCE) && (bonus.getCooldown()-24000)>0){
                        cir.setReturnValue(false);
                    }
                }

            }
            else{
                if(SetBonus.hasArmor(playerIn, DeathSet.INSTANCE)){
                    IBonus bonus = (IBonus) BonusCapability.getBonus(playerIn).orElse((IBonus) null);
                    if(bonus!=null){
                        if((bonus.getCooldown()-24000)>0){
                            cir.setReturnValue(false);
                        }
                    }
                }
            }
        }
    }
}
