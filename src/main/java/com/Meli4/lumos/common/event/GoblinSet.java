package com.Meli4.lumos.common.event;

import com.meteor.extrabotany.common.items.armor.goblinslayer.ItemGoblinSlayerArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
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
public class GoblinSet extends SetBonus{

    public static GoblinSet INSTANCE = new GoblinSet("lol1", "lol2");

    public GoblinSet(){}
    public GoblinSet(String desc1, String desc2) {
        super(desc1, desc2);
    }

    @Override
    public boolean hasArmor(PlayerEntity player){
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemGoblinSlayerArmor){
                count++;
            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {
        CompoundNBT nbt = player.getPersistentData();
        player.getPersistentData().remove("lumosSetBonus");
        player.getPersistentData().remove("lumosSetBonusCD");
        player.getPersistentData().putInt("lumosSetBonus", 400);
        player.getPersistentData().putInt("lumosSetBonusCD", 2000);
    }

    @SubscribeEvent
    public static void onDamaged(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            //LogManager.getLogger().info((player.getPersistentData().getInt("lumosSetBonus") > 0));
            //LogManager.getLogger().info(new Random().nextFloat());
            if((player.getPersistentData().getInt("lumosSetBonus") > 0) && INSTANCE.hasArmor(player) && new Random().nextFloat() < 0.25){
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
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        Entity attacker = event.getSource().getTrueSource();
        Entity entity = event.getEntity();
        if(attacker instanceof PlayerEntity){
            if(INSTANCE.hasArmor((PlayerEntity) attacker)){
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
            if(INSTANCE.hasArmor((PlayerEntity) attacker)){
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
