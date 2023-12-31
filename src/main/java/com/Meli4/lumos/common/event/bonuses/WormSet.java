package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.core.network.message.UpdateBonusMessage;
import com.Meli4.lumos.common.event.BonusCapabilityEvent;
import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.ma.effects.EffectInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class WormSet extends SetBonus {

    public WormSet(){}


    private static final WormSet INSTANCE = new WormSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemDeathwormArmor.class;}

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){ return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}
        if(!SetBonus.hasArmor(player, INSTANCE)){return;}
        if(!WormSet.isSandBelow(player)){return;}
        if(!player.world.getDimensionKey().equals(World.OVERWORLD)){return;}
        if(bonus.getCooldown() > 0){return;}

        event.setCanceled(true);
        player.setHealth(3);
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), AMSoundRegistry.BONE_SERPENT_IDLE, SoundCategory.PLAYERS, 1, 1);
        player.addPotionEffect(new EffectInstance(EffectInit.TRUE_INVISIBILITY.get(), 80, 0));
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 120, 2));

        EntityDeathWorm worm1 = IafEntityRegistry.DEATH_WORM.create(player.world);
        EntityDeathWorm worm2 = IafEntityRegistry.DEATH_WORM.create(player.world);
        worm1.setTamedBy(player);
        worm1.setPosition(player.getPosX() + 1, player.getPosY(),player.getPosZ() + 1);
        worm2.setTamedBy(player);
        worm2.setPosition(player.getPosX() - 1, player.getPosY(),player.getPosZ() - 1);
        if(event.getSource().getTrueSource() != null){
            if(event.getSource().getTrueSource().isLiving()){
                worm1.setAttackTarget((LivingEntity) event.getSource().getTrueSource());
                worm2.setAttackTarget((LivingEntity) event.getSource().getTrueSource());
            }
        }


        worm1.addPotionEffect(new EffectInstance(Effects.STRENGTH, 2400, 0));
        worm2.addPotionEffect(new EffectInstance(Effects.STRENGTH, 2400, 0));

        player.world.addEntity(worm1);
        player.world.addEntity(worm2);

        bonus.setCooldown(6000);
        BonusCapabilityEvent.syncEvent(player);
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(!SetBonus.hasArmor(player, INSTANCE)){return;}

        DamageSource source = event.getSource();
        if(source.equals(DamageSource.IN_WALL)){
            event.setCanceled(true);
        } else if (source.getTrueSource() != null) {
            source.getTrueSource().attackEntityFrom(DamageSource.causePlayerDamage(player), 5);
        }


    }

    private static boolean isSandBelow(PlayerEntity player){
        return player.world.getBlockState(player.getPosition().down()) == Blocks.SAND.getDefaultState();
    }


    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemDeathwormArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
