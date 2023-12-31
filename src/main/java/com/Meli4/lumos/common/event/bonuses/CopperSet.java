package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemCopperArmor;
import com.meteor.extrabotany.common.items.armor.goblinslayer.ItemGoblinSlayerArmor;
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
public class CopperSet extends SetBonus {

    public CopperSet(){};

    public static CopperSet INSTANCE = new CopperSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemCopperArmor.class;}

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){

        if(event.getSource().getTrueSource() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            LivingEntity entity = event.getEntityLiving();
            if(SetBonus.hasArmor(player, INSTANCE)){
                event.setAmount(event.getAmount()+((entity instanceof PlayerEntity) ? 2:4));
            }

        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemCopperArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
