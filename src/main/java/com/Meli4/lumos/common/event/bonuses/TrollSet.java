package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.sammy.malum.core.init.MalumSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class TrollSet extends SetBonus {

    public TrollSet(){}

    private static final TrollSet INSTANCE = new TrollSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemTrollArmor.class;}
    @SubscribeEvent
    public static void onDamaged(LivingHurtEvent event){

        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(SetBonus.hasArmor(player, INSTANCE) && event.getSource().isProjectile()){
                if(new Random().nextFloat() <= 0.3){
                    event.setCanceled(true);
                    if(event.getSource().getTrueSource() != null){
                        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), MalumSounds.ABSTRUSE_BLOCK_RETURNS, SoundCategory.PLAYERS, 1, 1);
                        Entity entity = event.getSource().getTrueSource();
                        entity.attackEntityFrom(event.getSource(), event.getAmount()*0.5F);
                        if(entity.hurtResistantTime == 20){
                            entity.hurtResistantTime = 0;
                            entity.attackEntityFrom(DamageSource.GENERIC, 2);
                        }
                        event.setCanceled(true);
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemTrollArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
