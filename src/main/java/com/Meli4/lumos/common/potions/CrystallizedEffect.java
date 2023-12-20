package com.Meli4.lumos.common.potions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;

@Mod.EventBusSubscriber
public class CrystallizedEffect extends Effect {

    Vector2f pitchYaw = null;

    private static final UUID MOVEMENT_SPEED_UUID = UUID.fromString("889db856-9d63-45c8-8f9a-b9ac68b509a3");
    protected CrystallizedEffect() {
        super(EffectType.BENEFICIAL, 4915330);
        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED_UUID.toString(), -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity entity, int p_76394_2_) {
        super.performEffect(entity, p_76394_2_);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity)entity);
            player.setMotion(player.getMotion().x, -0.6, player.getMotion().z);
            if(player.world.isRemote){
                //player.setHeadRotation(this.pitchYaw.x, 1);
                if(this.pitchYaw != null){
                    player.rotationPitch = this.pitchYaw.x;
                    player.rotationYaw = this.pitchYaw.y;
                }

            }

        }

    }

    @Override
    public void applyAttributesModifiersToEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
        super.applyAttributesModifiersToEntity(entityLivingBaseIn, attributeMapIn,amplifier);
        ///this.yaw = entityLivingBaseIn.rotationYaw;
        this.pitchYaw = entityLivingBaseIn.getPitchYaw();
    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if(player.getActivePotionEffect(ModPotions.CRYSTALLIZED.get()) != null){
                DamageSource src = event.getSource();
                if(src.isMagicDamage()){
                    event.setCanceled(true);
                } else if (src.isUnblockable()) {
                    event.setCanceled(true);
                } else if (!src.isDamageAbsolute() && !src.isExplosion() && !src.isFireDamage()) {
                    event.setCanceled(true);

                }
            }
        }
    }

/*    @SubscribeEvent
    public static void cameraMove(EntityViewRenderEvent.CameraSetup event){
        if(event.getInfo().getRenderViewEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getInfo().getRenderViewEntity();
            if(player.getActivePotionEffect(ModPotions.CRYSTALLIZED.get()) != null){
                event.set
            }
        }
    }*/

}
