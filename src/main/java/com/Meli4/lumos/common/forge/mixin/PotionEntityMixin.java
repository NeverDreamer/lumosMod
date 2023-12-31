package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.bonuses.PurpleGeodeSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PotionEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin {


    @Inject(method = "applyWater", at=@At(value = "HEAD"))
    public void lumos_onWaterApply(CallbackInfo ci){
        AxisAlignedBB axisalignedbb = ((PotionEntity)(Object)this).getBoundingBox().grow(4.0D, 2.0D, 4.0D);
        List<LivingEntity> list = ((PotionEntity)(Object)this).world.getEntitiesWithinAABB(LivingEntity.class, axisalignedbb);
        if (!list.isEmpty()) {
            for(LivingEntity livingentity : list) {
                double d0 = ((PotionEntity)(Object)this).getDistance(livingentity);
                if (d0 < 16.0D && livingentity instanceof PlayerEntity) {
                    if(SetBonus.hasArmor(((PlayerEntity)livingentity), PurpleGeodeSet.getInstance())){
                        int i = 0;
                        for(ItemStack stack: livingentity.getArmorInventoryList()){
                            if(i==0){
                                stack.damageItem(20, livingentity, (livingEntity) -> {
                                    livingEntity.sendBreakAnimation(EquipmentSlotType.FEET);
                                });
                            }
                            else if(i==1){
                                stack.damageItem(20, livingentity, (livingEntity) -> {
                                    livingEntity.sendBreakAnimation(EquipmentSlotType.LEGS);
                                });
                            }
                            else if(i==2){
                                stack.damageItem(20, livingentity, (livingEntity) -> {
                                    livingEntity.sendBreakAnimation(EquipmentSlotType.CHEST);
                                });
                            }
                            else if(i==3){
                                stack.damageItem(20, livingentity, (livingEntity) -> {
                                    livingEntity.sendBreakAnimation(EquipmentSlotType.HEAD);
                                });
                            }

                            i++;

                        }
                    }

                }
            }
        }
    }

}
