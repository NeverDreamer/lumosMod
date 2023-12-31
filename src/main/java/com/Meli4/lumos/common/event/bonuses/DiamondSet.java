package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.ToggleSetBonus;
import net.minecraft.item.ArmorItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yezon.theabyss.potion.FlyingPotionEffect;

@Mod.EventBusSubscriber
public class DiamondSet extends ToggleSetBonus {
    public DiamondSet(){};

    @Override
    public void doActiveSkill(PlayerEntity player) {
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}

        if(player.isInWater()){
            toggleMode(player);
        }
    }

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "diamond";}

    public static DiamondSet INSTANCE = new DiamondSet();

    @SubscribeEvent
    public static void onActiveTick(TickEvent.PlayerTickEvent event) {
        if(event.player==null){return;}
        PlayerEntity player = event.player;
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}

        if(SetBonus.hasArmor(player, INSTANCE)){
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) {
                if(player.isInWater()){
                    if (bonus.getMode()) {
                        if (!event.player.world.isRemote) {
                            //player.addPotionEffect(new EffectInstance(new FlyingPotionEffect.EffectCustom(), 20, 0));
                            player.addPotionEffect(new EffectInstance(Effects.HUNGER, 20, 0));
                        }
                    }
                }
            }
        }
    }


//    @SubscribeEvent
//        public static void onTick(TickEvent.PlayerTickEvent event){
//
//            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
//            {
//                PlayerEntity player = event.player;
//                if(INSTANCE.hasArmor(player)){
//                    if (!event.player.world.isRemote)
//                    {
//                        player.addPotionEffect(new EffectInstance(Effects.HUNGER, 10, 0));
//                    }
//
//                }
//            }
//        }


    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith("diamond")){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }

    @Override
    public int getCooldown() {
        return 10;
    }
}