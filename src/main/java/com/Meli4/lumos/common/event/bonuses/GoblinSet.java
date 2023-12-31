package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.PressSetBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.meteor.extrabotany.common.items.armor.goblinslayer.ItemGoblinSlayerArmor;
import elucent.eidolon.item.WarlockRobesItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tslat.aoa3.content.entity.mob.overworld.GoblinEntity;

import java.util.Random;

@Mod.EventBusSubscriber
public class GoblinSet extends PressSetBonus {

    public static GoblinSet INSTANCE = new GoblinSet();

    public GoblinSet(){}


    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemGoblinSlayerArmor.class;}

    @Override
    public int getCooldown() {
        return 2000;
    }

    @Override
    public int getDuration() {
        return 400;
    }

    @SubscribeEvent
    public static void onDamaged(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
            //LogManager.getLogger().info(new Random().nextFloat());
            if((bonus.getDuration() > 0) && SetBonus.hasArmor(player, INSTANCE) && new Random().nextFloat() < 0.25){
                event.setCanceled(true);
                if(((PlayerEntity) event.getEntity()).hurtTime == 20){
                    player.hurtTime = 0;
                    player.attackEntityFrom(DamageSource.GENERIC, 1);
                }
            }
        }
    }


    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemGoblinSlayerArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        Entity attacker = event.getSource().getTrueSource();
        Entity entity = event.getEntity();
        if(attacker instanceof PlayerEntity){
            if(SetBonus.hasArmor((PlayerEntity) attacker, INSTANCE)){
                if(entity instanceof GoblinEntity){
                    ((GoblinEntity) entity).setHealth(0);
                    ((GoblinEntity) entity).onDeath(DamageSource.MAGIC);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent event){

        if(event.getSource().getTrueSource() instanceof PlayerEntity){
            PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
            if(SetBonus.hasArmor(attacker, INSTANCE)){
                if(attacker.getActivePotionEffect(Effects.STRENGTH) != null){
                    event.setAmount(Math.max(0, event.getAmount() - (6*(attacker.getActivePotionEffect(Effects.STRENGTH).getAmplifier()+1))));
                }
                event.setAmount(Math.max(0, event.getAmount()-4));
            }
        }
    }

    @SubscribeEvent
    public static void onRegen(LivingHealEvent event){
        BlockPos pos = event.getEntity().getEntity().getPosition();
        World world = event.getEntity().world;
        if(!world.isRemote){
            event.setAmount(event.getAmount() + (float) (world.getLightValue(pos) * 0.03));
        }
    }
}
