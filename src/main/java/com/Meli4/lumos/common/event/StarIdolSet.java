package com.Meli4.lumos.common.event;

import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class StarIdolSet extends SetBonus{

    public String desc1;
    public String desc2;

    public StarIdolSet(){};

    public StarIdolSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    public static StarIdolSet INSTANCE = new StarIdolSet("lol1", "lol2");
    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemMikuArmor){
                count++;
            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemMikuArmor){
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }
}
