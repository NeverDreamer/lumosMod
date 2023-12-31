package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentArmor;
import com.meteor.extrabotany.common.items.armor.shadowwarrior.ItemShadowWarriorArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tslat.aoa3.content.item.weapon.sword.CoralstormSword;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber
public class SeaDefenderSet extends SetBonus {


    public SeaDefenderSet(){}

    public static SeaDefenderSet INSTANCE = new SeaDefenderSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public Class<? extends ArmorItem> getArmorClass(){return ItemSeaSerpentArmor.class;}

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event){
        if(event.getSource().getTrueSource() instanceof PlayerEntity){
            PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
            if(attacker.getHeldItemMainhand().getItem() instanceof CoralstormSword){
                if(INSTANCE.isPlayerInWater(attacker) && SetBonus.hasArmor(attacker, INSTANCE)){
                    event.getSource().setDamageIsAbsolute();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){
        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            if(SetBonus.hasArmor(player, INSTANCE)){
                if (!event.player.world.isRemote){
                    if(SeaDefenderSet.INSTANCE.isPlayerInWater(player)){
                        if(player.getPersistentData().contains("rainAmpl")){
                            SeaDefenderSet.INSTANCE.decreaseEffect(player,false);
                        }
                        SeaDefenderSet.INSTANCE.amplifyEffect(player, true);
                    }
                    else{
                        SeaDefenderSet.INSTANCE.decreaseEffect(player,true);

                        if(SeaDefenderSet.INSTANCE.isPlayerInRain(player)){
                            SeaDefenderSet.INSTANCE.amplifyEffect(player, false);
                        }
                        else{
                            SeaDefenderSet.INSTANCE.decreaseEffect(player,false);
                        }
                    }


                }

            }
        }


    }


    private boolean isPlayerInWater(PlayerEntity player) {

        return player.isInWater();
    }
    private boolean isPlayerInRain(PlayerEntity player){
        BlockPos blockpos = player.getEntity().getPosition();
        return player.world.isRainingAt(blockpos) || player.world.isRainingAt(new BlockPos((double)blockpos.getX(), player.getBoundingBox().maxY, (double)blockpos.getZ()));
    }

    private void amplifyEffect(PlayerEntity player, boolean water){
        int amplify = water ? 8 : 4;
        String tag = water ? "waterAmpl" : "rainAmpl";

        if(player.getActivePotionEffect(Effects.STRENGTH) != null){
            int duration = player.getActivePotionEffect(Effects.STRENGTH).getDuration();
            int amplifier = player.getActivePotionEffect(Effects.STRENGTH).getAmplifier();
            if(!player.getPersistentData().contains(tag)){
                player.addPotionEffect(new EffectInstance(Effects.STRENGTH, duration, amplifier+amplify));
                player.getPersistentData().putInt(tag, amplifier+amplify);
            }
            else{
                if(player.getPersistentData().getInt(tag) != amplifier){
                    player.getPersistentData().remove(tag);
                }
            }
        }

    }

    private void decreaseEffect(PlayerEntity player, boolean water){
        int amplify = water ? 8 : 4;
        String tag = water ? "waterAmpl" : "rainAmpl";

        if(player.getPersistentData().contains(tag)){
            player.getPersistentData().remove(tag);
            if(player.getActivePotionEffect(Effects.STRENGTH) != null){
                int duration = player.getActivePotionEffect(Effects.STRENGTH).getDuration();
                int amplifier = player.getActivePotionEffect(Effects.STRENGTH).getAmplifier();
                player.removePotionEffect(Effects.STRENGTH);
                player.addPotionEffect(new EffectInstance(Effects.STRENGTH, duration, amplifier-amplify));
            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemSeaSerpentArmor){
            event.getToolTip().add(new StringTextComponent("lol1"));
            event.getToolTip().add(new StringTextComponent("lol2"));
        }
    }
}
