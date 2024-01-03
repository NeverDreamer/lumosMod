package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.ToggleSetBonus;
import net.minecraft.item.ArmorItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.TickEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yezon.theabyss.potion.FlyingPotionEffect;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber
public class GlacerytheSet extends ToggleSetBonus {
    public GlacerytheSet(){};

    @Override
    public void doActiveSkill(PlayerEntity player) {
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}

        if(player.isInWater()){
            toggleMode(player);
        }
    }

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "glacerythe_armor";}


    public static GlacerytheSet INSTANCE = new GlacerytheSet();

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
                            player.addPotionEffect(new EffectInstance(new FlyingPotionEffect.EffectCustom(), 20, 0));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith("glacerythe_armor") || ((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().endsWith("glacerythe_armor")){
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