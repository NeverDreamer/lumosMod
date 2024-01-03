package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.yezon.theabyss.TheabyssModVariables;
import org.apache.logging.log4j.LogManager;
import vazkii.botania.common.item.equipment.tool.bow.ItemCrystalBow;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;

@Mod.EventBusSubscriber
public class AberytheSet extends SetBonus {

    public AberytheSet(){};

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "aberythe_armor";}


    public static AberytheSet INSTANCE = new AberytheSet();

    @SubscribeEvent
    public static void onArrowSpawn(EntityJoinWorldEvent event){
        if(!(event.getEntity() instanceof ArrowEntity) || event.getWorld().isRemote){return;}
        ArrowEntity arrowEntity = (ArrowEntity) event.getEntity();
        if(!(arrowEntity.getShooter() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) arrowEntity.getShooter();
        if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof CrossbowItem)){return;}
        player.getCapability(TheabyssModVariables.PLAYER_VARIABLES_CAPABILITY, (Direction)null).ifPresent((capability) -> {
            if(capability.Mana >=2){
                capability.Mana = capability.Mana-2;
                capability.syncPlayerVariables(player);
                arrowEntity.getPersistentData().putBoolean("shouldInflictSomniumDamage", true);
            }
            else{
                arrowEntity.getPersistentData().putBoolean("shouldInflictSomniumDamage", false);
            }
        });
    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event){
        if(!(event.getSource().getTrueSource() instanceof PlayerEntity)){return;}
        PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
        if(!SetBonus.hasArmor(attacker, INSTANCE) || event.getSource().getImmediateSource() instanceof PlayerEntity || !(event.getSource().getImmediateSource() instanceof AbstractArrowEntity)){return;}
        AbstractArrowEntity arrow = (AbstractArrowEntity) event.getSource().getImmediateSource();
        LivingEntity entity = event.getEntityLiving();
        if(arrow.getPersistentData().contains("shouldInflictSomniumDamage")){
            if(arrow.getPersistentData().getBoolean("shouldInflictSomniumDamage")){
                if(entity.hurtResistantTime == 20){
                    entity.hurtResistantTime = 0;
                    entity.attackEntityFrom(DamageSource.MAGIC, 4);
                }
            }
            entity.addPotionEffect(new EffectInstance(ModPotions.HEX_EFFECT, 100, 3));
            entity.addPotionEffect(new EffectInstance(Effects.WITHER, 60, 1));
        }

    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith(INSTANCE.getMaterialName()) || ((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().endsWith(INSTANCE.getMaterialName()    )){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }
}
