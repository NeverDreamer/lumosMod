package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.event.DeathSet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
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
                if(DeathSet.INSTANCE.hasArmor(playerIn) && DeathSet.takeOffTimer > 0){
                    cir.setReturnValue(false);
                }
            }
            else{
                if(DeathSet.INSTANCE.hasArmor(playerIn) && playerIn.getPersistentData().contains("lumosSetBonus")){
                    if(playerIn.getPersistentData().getInt("lumosSetBonus") > 0){
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
