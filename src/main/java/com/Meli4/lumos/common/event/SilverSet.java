package com.Meli4.lumos.common.event;

import com.github.alexthe666.iceandfire.item.ItemSilverArmor;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class SilverSet extends SetBonus{

    public String desc1;
    public String desc2;

    public SilverSet(){};

    public SilverSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemSilverArmor){
                count++;
            }
        }
        return count == 4;
    }

    public static SilverSet INSTANCE = new SilverSet("lol1", "lol2");
    @Override
    public void doActiveSkill(PlayerEntity player) {

    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){

        if(event.getEntityLiving() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntityLiving();
            if(event.getSource().getTrueSource() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getSource().getTrueSource();
                if(SilverSet.INSTANCE.hasArmor(player)){
                    event.setAmount(event.getAmount()-(entity.isEntityUndead() ? 6:4));

                }
            }

        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemSilverArmor){
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }
}
