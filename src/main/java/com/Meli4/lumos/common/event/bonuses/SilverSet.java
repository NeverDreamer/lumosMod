package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemSilverArmor;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SilverSet extends SetBonus {

    public SilverSet(){};


    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemSilverArmor.class;}

    public static SilverSet INSTANCE = new SilverSet();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){

        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(event.getSource().getTrueSource() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getSource().getTrueSource();
                if(SetBonus.hasArmor(player, INSTANCE)){
                    event.setAmount(event.getAmount()-(entity.isEntityUndead() ? 6:4));

                }
            }

        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemSilverArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
