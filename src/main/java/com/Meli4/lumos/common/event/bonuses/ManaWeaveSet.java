package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import shadows.placebo.events.ItemUseEvent;
import vazkii.botania.api.mana.ManaDiscountEvent;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelArmor;
import vazkii.botania.common.item.equipment.armor.manasteel.ItemManasteelHelm;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveArmor;
import vazkii.botania.common.item.equipment.armor.manaweave.ItemManaweaveHelm;
import vazkii.botania.common.item.equipment.tool.manasteel.*;

@Mod.EventBusSubscriber
public class ManaWeaveSet extends SetBonus {
    public ManaWeaveSet(){}

    private static final ManaWeaveSet INSTANCE = new ManaWeaveSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemManaweaveArmor.class;}

    public Class<? extends ArmorItem> getHelmClass(){return ItemManaweaveHelm.class;}

    @SubscribeEvent
    public static void onToolDamaged(ManaDiscountEvent event){
        PlayerEntity player = event.getEntityPlayer();
        if(!SetBonus.hasArmor(player, INSTANCE)){return;}
        Item item = event.getTool().getItem();
        if(item instanceof ItemManasteelAxe || item instanceof ItemManasteelHoe || item instanceof ItemManasteelPick || item instanceof ItemManasteelShears || item instanceof ItemManasteelShovel || item instanceof ItemManasteelSword){
            event.setDiscount(1.0F);
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemManaweaveArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
