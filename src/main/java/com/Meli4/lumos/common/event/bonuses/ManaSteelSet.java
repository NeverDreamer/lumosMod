package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.klikli_dev.occultism.registry.OccultismEffects;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.tool.bow.ItemCrystalBow;
import vazkii.botania.common.item.equipment.tool.bow.ItemLivingwoodBow;

import java.util.UUID;

@Mod.EventBusSubscriber
public class ManaSteelSet extends SetBonus {

    public ManaSteelSet(){}

    private static final ManaSteelSet INSTANCE = new ManaSteelSet();

    private static final String uuid = "9b152d60-6ccd-449c-8c03-46548ed7845a";


    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemManasteelArmor.class;}

    public Class<? extends ArmorItem> getHelmClass(){return ItemManasteelHelm.class;}

    @SubscribeEvent
    public static void onArrowSpawn(EntityJoinWorldEvent event){
        if(!(event.getEntity() instanceof ArrowEntity) || event.getWorld().isRemote){return;}
        ArrowEntity arrowEntity = (ArrowEntity) event.getEntity();
        if(!(arrowEntity.getShooter() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) arrowEntity.getShooter();
        if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof ItemLivingwoodBow) || !(player.getHeldItemMainhand().getItem() instanceof ItemCrystalBow)){return;}
        arrowEntity.setMotion(arrowEntity.getMotion().scale(1.25));

    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof ItemLivingwoodBow) || !(player.getHeldItemMainhand().getItem() instanceof  ItemCrystalBow)){
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
        if(!SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof ItemLivingwoodBow) || !(player.getHeldItemMainhand().getItem() instanceof  ItemCrystalBow)){return;}
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
    private void removeAttributesModifiersFromEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.removeModifier(UUID.fromString(uuid));
    }

    private void applyAttributesModifiersToEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.applyPersistentModifier(new AttributeModifier(UUID.fromString(uuid), "manasteelSpeed", attribute.getBaseValue()*0.15, AttributeModifier.Operation.ADDITION));
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemManasteelArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
