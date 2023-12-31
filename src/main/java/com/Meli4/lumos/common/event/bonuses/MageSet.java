package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.dkmk100.arsomega.potions.ModPotions;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import elucent.eidolon.entity.SoulfireProjectileEntity;
import elucent.eidolon.item.WarlockRobesItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MageSet extends SetBonus {

    public static MageSet INSTANCE = new MageSet();

    public MageSet(){}

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return WarlockRobesItem.class;}

    @Override
    public int getCount(){return 3;}

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof WarlockRobesItem){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
    @SubscribeEvent
    public static void onDamaged(LivingDamageEvent event){
        Entity attacker = event.getSource().getTrueSource();
        Entity entity = event.getEntity();
        if((attacker instanceof PlayerEntity) && (entity instanceof PlayerEntity)){
            if(event.getSource().isMagicDamage()){
                event.setAmount(event.getAmount() - (SetBonus.hasArmor((PlayerEntity) entity, INSTANCE) ? 2:0));
            }
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingDamageEvent event){
        Entity attacker = event.getSource().getTrueSource();
        if(attacker instanceof PlayerEntity && (event.getEntity() instanceof PlayerEntity)){
            if(SetBonus.hasArmor((PlayerEntity) attacker, INSTANCE)){
                if(!attacker.getPersistentData().contains("mageBonusDamage")){
                    attacker.getPersistentData().putBoolean("mageBonusDamage", true);
                } else if (!attacker.getPersistentData().getBoolean("mageBonusDamage")) {
                    attacker.getPersistentData().putBoolean("mageBonusDamage", true);
                }
                else{
                    if(((PlayerEntity) event.getEntity()).hurtTime == 20){
                        ((PlayerEntity) event.getEntity()).hurtTime = 0;
                        event.getEntity().attackEntityFrom(DamageSource.MAGIC, 2);
                        attacker.getPersistentData().putBoolean("mageBonusDamage", false);
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onProjectileAttack(LivingDamageEvent event){
        Entity proj = event.getSource().getImmediateSource();
        LivingEntity entity = event.getEntityLiving();
        if(proj instanceof SoulfireProjectileEntity ){
            if(event.getSource().getTrueSource() instanceof PlayerEntity){
                entity.addPotionEffect(new EffectInstance(ModPotions.SOUL_FIRE, 100, 0));
            }
        }
    }

    @SubscribeEvent
    public static void onLowHealth(TickEvent.PlayerTickEvent event){
        if (!event.player.world.isRemote)
        {
            if (event.phase == TickEvent.Phase.START)
            {
                PlayerEntity player = event.player;

                if(!player.getPersistentData().contains("lifeDrain")){
                    if(player.getHealth() < 5 && SetBonus.hasArmor(player, INSTANCE)){
                        player.addPotionEffect(new EffectInstance(AMEffectRegistry.SOULSTEAL.getEffect(), 40, 0));
                        player.getPersistentData().remove("lifeDrain");
                        player.getPersistentData().putInt("lifeDrain", 12000);
                    }
                } else if (player.getPersistentData().getInt("lifeDrain") == 0) {
                    player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), AMSoundRegistry.VOID_PORTAL_OPEN, SoundCategory.PLAYERS, 1, 1);
                    if(player.getHealth() < 5 && SetBonus.hasArmor(player, INSTANCE)){
                        player.addPotionEffect(new EffectInstance(AMEffectRegistry.SOULSTEAL.getEffect(), 40, 0));
                        player.getPersistentData().remove("lifeDrain");
                        player.getPersistentData().putInt("lifeDrain", 12000);
                    }
                }
                else{
                    int tick = player.getPersistentData().getInt("lifeDrain");
                    player.getPersistentData().remove("lifeDrain");
                    player.getPersistentData().putInt("lifeDrain", tick-1);

                }
            }
        }




    }
}
