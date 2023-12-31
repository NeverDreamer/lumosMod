package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MyrmexSet extends SetBonus {
    public MyrmexSet(){}

    public static MyrmexSet INSTANCE = new MyrmexSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "myrmex";}

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) {return;}
        PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
        if (!SetBonus.hasArmor(attacker, INSTANCE)) {return;}
        LivingEntity entity = event.getEntityLiving();
        entity.addPotionEffect(new EffectInstance(AMEffectRegistry.DEBILITATING_STING, 160, 1));
    }

        @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            if(SetBonus.hasArmor(player, INSTANCE)){
                if (!event.player.world.isRemote)
                {
                    player.addPotionEffect(new EffectInstance(AMEffectRegistry.BUG_PHEROMONES, 10, 0));
                }

            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith("myrmex")){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }
}
