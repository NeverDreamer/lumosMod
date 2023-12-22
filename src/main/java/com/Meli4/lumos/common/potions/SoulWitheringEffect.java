package com.Meli4.lumos.common.potions;

import com.Meli4.lumos.common.event.DeathSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierManager;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import org.apache.logging.log4j.LogManager;

import java.util.Map;

public class SoulWitheringEffect extends Effect {
    protected SoulWitheringEffect() {super(EffectType.HARMFUL, 000000);}

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return p_76397_1_%40==0;
    }

    @Override
    public void performEffect(LivingEntity entity, int p_76394_2_) {
        super.performEffect(entity, p_76394_2_);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity)entity);
            if(!player.world.isRemote){
                if(!player.isAlive()){return;}
                switch (p_76394_2_){
                    case 0:
                        player.attackEntityFrom(DamageSource.OUT_OF_WORLD, 10);
                        break;
                    case 1:
                        player.attackEntityFrom(DamageSource.OUT_OF_WORLD, 30);
                        break;
                    case 2:
                        player.attackEntityFrom(DamageSource.OUT_OF_WORLD, player.getMaxHealth()*0.25F);
                        break;
                    case 3:
                        player.attackEntityFrom(DamageSource.OUT_OF_WORLD, player.getMaxHealth()*0.5F);
                        break;
                    case 4:
                        player.attackEntityFrom(DamageSource.OUT_OF_WORLD, player.getMaxHealth());
                        break;
                    case 5:
                        player.setHealth(0);
                        player.onDeath(DamageSource.OUT_OF_WORLD);
                        break;
                    default:
                        break;
                }
                if(p_76394_2_ != 5){
                    //player.removePotionEffect(ModPotions.SOUL_WITHERING.get());
                    player.addPotionEffect(new EffectInstance(ModPotions.SOUL_WITHERING.get(), 279-(p_76394_2_+1)*40, p_76394_2_+1));

                }

            }

        }

    }

    @Override
    public void removeAttributesModifiersFromEntity(LivingEntity entityLivingBaseIn, AttributeModifierManager attributeMapIn, int amplifier) {
        super.removeAttributesModifiersFromEntity(entityLivingBaseIn, attributeMapIn, amplifier);
        if(!(entityLivingBaseIn instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) entityLivingBaseIn;
        if(!DeathSet.INSTANCE.hasArmor(player) || !DeathSet.getMode(player) || (player.getActivePotionEffect(ModPotions.SOUL_WITHERING.get()).getDuration()+1)%40==0){return;}
        player.setHealth(0);
        player.onDeath(DamageSource.OUT_OF_WORLD);
    }
}
