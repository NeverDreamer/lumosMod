package com.Meli4.lumos.common.event;

import com.github.alexthe666.iceandfire.item.ItemCopperArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class CopperSet extends SetBonus{

    public String desc1;
    public String desc2;

    public CopperSet(){};

    public CopperSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemCopperArmor){
                count++;
            }
        }
        return count == 4;
    }

    public static CopperSet INSTANCE = new CopperSet("lol1", "lol2");
    @Override
    public void doActiveSkill(PlayerEntity player) {

    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){

        if(event.getSource().getTrueSource() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
            LivingEntity entity = event.getEntityLiving();
            if(CopperSet.INSTANCE.hasArmor(player)){
                event.setAmount(event.getAmount()+((entity instanceof PlayerEntity) ? 2:4));
            }

        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemCopperArmor){
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }
}
