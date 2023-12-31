package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StarIdolSet extends SetBonus {


    public StarIdolSet(){};


    private static final StarIdolSet INSTANCE = new StarIdolSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemMikuArmor.class;}

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemMikuArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
