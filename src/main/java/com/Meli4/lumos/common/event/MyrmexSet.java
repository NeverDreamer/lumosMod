package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.github.alexthe666.alexsmobs.effect.AMEffectRegistry;
import com.github.alexthe666.alexsmobs.item.ItemModArmor;
import com.github.alexthe666.iceandfire.item.IafArmorMaterial;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.alexthe666.iceandfire.item.ItemScaleArmor;
import com.ma.effects.EffectInit;
import com.meteor.extrabotany.common.items.armor.miku.ItemMikuArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import static com.Meli4.lumos.common.core.network.LumosNetwork.CHANNEL;

@Mod.EventBusSubscriber
public class MyrmexSet extends SetBonus{
    public String desc1;
    public String desc2;

    public MyrmexSet(){}

    public MyrmexSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    public static MyrmexSet INSTANCE = new MyrmexSet("lol1", "lol2");

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ArmorItem){
                if(((ArmorItem) itemstack.getItem()).getArmorMaterial().getName().startsWith("myrmex")){
                    count++;
                }
            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {

    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event) {
        if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) {return;}
        PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
        if (!INSTANCE.hasArmor(attacker)) {return;}
        LivingEntity entity = event.getEntityLiving();
        entity.addPotionEffect(new EffectInstance(AMEffectRegistry.DEBILITATING_STING, 160, 1));
    }

        @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            if(INSTANCE.hasArmor(player)){
                if (!event.player.world.isRemote)
                {
                    player.addPotionEffect(new EffectInstance(AMEffectRegistry.BUG_PHEROMONES, 10, 0));
                }

            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith("myrmex")){
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
            }
        }
    }
}
