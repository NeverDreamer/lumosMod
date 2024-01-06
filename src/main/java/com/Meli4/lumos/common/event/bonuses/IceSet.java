package com.Meli4.lumos.common.event.bonuses;
import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.BonusCapabilityEvent;
import com.Meli4.lumos.common.event.SetBonus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tslat.aoa3.content.item.armour.IceArmour;
import org.apache.logging.log4j.LogManager;

import java.util.UUID;


@Mod.EventBusSubscriber
public class IceSet extends SetBonus {
    public IceSet(){};

    public static IceSet INSTANCE = new IceSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return IceArmour.class;}

    public void removeAttributesModifiersFromEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.ARMOR);
        attribute.removeModifier(UUID.fromString(uuid));
    }

    public void applyAttributesModifiersToEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.ARMOR);
        attribute.applyPersistentModifier(new AttributeModifier(UUID.fromString(uuid), "iceArmor", 3, AttributeModifier.Operation.ADDITION));
    }

    private static final String uuid = "4477dc18-b486-412c-b158-59cdd1a92c63";


    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (SetBonus.hasArmor(player, INSTANCE)) {
                if(event.getSource().damageType.equals("dragon_ice")){
                    event.setAmount(0);
                    event.setCanceled(true);
                    player.getArmorInventoryList().forEach((itemStack)->{itemStack.setDamage(0);} );
                    player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 600, 2));
                    IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
                    if(bonus==null){return;}
                    bonus.setDuration(600);
                    BonusCapabilityEvent.syncEvent(player);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onActiveTick(TickEvent.PlayerTickEvent event) {
        if (event.player == null || event.side.isClient() || event.phase != net.minecraftforge.event.TickEvent.Phase.START) {
            return;
        }
        PlayerEntity player = event.player;
        if (SetBonus.hasArmor(player, INSTANCE)) {
            IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
            if(bonus==null){return;}
            if(bonus.getDuration() > 0) {
                if (player.getAttribute(Attributes.ARMOR).getModifier(UUID.fromString(uuid)) == null) {
                    INSTANCE.applyAttributesModifiersToEntity(player);
                }
            }
            else {
                if (player.getAttribute(Attributes.ARMOR).getModifier(UUID.fromString(uuid)) != null) {
                    INSTANCE.removeAttributesModifiersFromEntity(player);
                }
            }

        }
        else {
            if (player.getAttribute(Attributes.ARMOR).getModifier(UUID.fromString(uuid)) != null) {
                INSTANCE.removeAttributesModifiersFromEntity(player);
            }
        }

    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof IceArmour){
            event.getToolTip().add(new StringTextComponent("lol11"));
            event.getToolTip().add(new StringTextComponent("lol22"));
        }
    }
}