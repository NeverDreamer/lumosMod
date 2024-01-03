package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BowItem;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import vazkii.botania.common.item.equipment.tool.bow.ItemCrystalBow;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;

import java.util.UUID;

@Mod.EventBusSubscriber
public class BoneSet extends SetBonus {
    public BoneSet(){};

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "bone_armor";}


    public static BoneSet INSTANCE = new BoneSet();
    private static final String uuid = "4b8973fc-30f3-4b3f-ba6e-c25f0cb7ddcf";

    @SubscribeEvent
    public static void onAggro(LivingSetAttackTargetEvent event){
        if(event.getTarget() == null){return;}
        if(!(event.getEntityLiving() instanceof SkeletonEntity)){return;}
        if(!(event.getTarget() instanceof PlayerEntity)){return;}

        PlayerEntity player = (PlayerEntity) event.getTarget();
        if (!player.world.isRemote){
            if(SetBonus.hasArmor(player, INSTANCE)){
                ((SkeletonEntity) event.getEntityLiving()).setAttackTarget(null);
            }
        }


    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof BowItem)){
                if(player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(uuid)) != null){
                    INSTANCE.removeAttributesModifiersFromEntity(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onStart(LivingEntityUseItemEvent.Start event){
        if(!(event.getEntity() instanceof ServerPlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(!SetBonus.hasArmor(player, INSTANCE)){return;}
        INSTANCE.applyAttributesModifiersToEntity(player);
    }

    @SubscribeEvent
    public static void onStop(LivingEntityUseItemEvent.Stop event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(uuid)) != null){
            INSTANCE.removeAttributesModifiersFromEntity(player);
        }
    }

    @SubscribeEvent
    public static void onTick(LivingEntityUseItemEvent.Tick event){
        if(!(event.getEntity() instanceof ServerPlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof BowItem)){return;}
        if((event.getDuration()%3)==0){event.setDuration(event.getDuration()-1);}
         //33% increase in draw speed
    }

    private void removeAttributesModifiersFromEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.removeModifier(UUID.fromString(uuid));
    }

    private void applyAttributesModifiersToEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.applyPersistentModifier(new AttributeModifier(UUID.fromString(uuid), "boneSpeed", attribute.getBaseValue()*0.15, AttributeModifier.Operation.ADDITION));
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith(INSTANCE.getMaterialName()) || ((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().endsWith(INSTANCE.getMaterialName())){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }
}
