package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import elucent.eidolon.Registry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class FroststeelSet extends SetBonus {

    public FroststeelSet(){};

    public static SetBonus getInstance(){return INSTANCE;}
    public String getMaterialName(){return "froststeel";}

    public static FroststeelSet INSTANCE = new FroststeelSet();

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event){
        if(!(event.getEntity() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntity();
        if(!SetBonus.hasArmor(player, INSTANCE) || !(event.getSource().getTrueSource() instanceof LivingEntity)){return;}
        LivingEntity entity = (LivingEntity) event.getSource().getTrueSource();
        entity.addPotionEffect(new EffectInstance(Registry.CHILLED_EFFECT.get(), 40, 0));
        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 140, 0));

    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith(INSTANCE.getMaterialName())){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }
}
