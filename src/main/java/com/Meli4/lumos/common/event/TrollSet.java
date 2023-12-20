package com.Meli4.lumos.common.event;

import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import com.sammy.malum.core.init.MalumSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;
import java.util.Set;
@Mod.EventBusSubscriber
public class TrollSet extends SetBonus {
    public String desc1;
    public String desc2;

    public TrollSet(){}

    public TrollSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    public static TrollSet INSTANCE = new TrollSet("lol1", "lol2");

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemTrollArmor){
                count++;
            }
        }
        return count == 4;
    }

    @SubscribeEvent
    public static void onDamaged(LivingHurtEvent event){

        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(INSTANCE.hasArmor(player) && event.getSource().isProjectile()){
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

    @Override
    public void doActiveSkill(PlayerEntity player) {

    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemTrollArmor){
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }
}
