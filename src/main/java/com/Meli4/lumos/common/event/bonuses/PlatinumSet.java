package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vectorwing.farmersdelight.registry.ModEffects;

import java.util.Random;

@Mod.EventBusSubscriber
public class PlatinumSet extends SetBonus {

    public PlatinumSet(){};

    public static PlatinumSet INSTANCE = new PlatinumSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public IArmorMaterial getMaterial(){return ArmorMaterials.PLATINUM;}

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            if(SetBonus.hasArmor(player, INSTANCE)){
                player.addPotionEffect(new EffectInstance(Effects.LUCK, 10, 4));
                player.addPotionEffect(new EffectInstance(ModEffects.COMFORT.get(), 10, 1));
                if(player.isInWater()){
                    player.setMotion(player.getMotion().x, -1, player.getMotion().z);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onDamaged(LivingEquipmentChangeEvent event){
        if(event.getFrom().getItem() instanceof ArmorItem && event.getTo().getItem() instanceof ArmorItem){
            int durFrom = event.getFrom().getDamage();
            int durTo = event.getTo().getDamage();
            if(new Random().nextFloat() <= 0.3){
                event.getTo().setDamage(durFrom- (durFrom-durTo)*2);
            }

        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().equals(ArmorMaterials.PLATINUM)){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }

        }
    }
}
