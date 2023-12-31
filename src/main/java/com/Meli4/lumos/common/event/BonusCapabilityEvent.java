package com.Meli4.lumos.common.event;

import com.Meli4.lumos.LumosMod;
import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.core.network.message.UpdateBonusMessage;
import com.hollingsworth.arsnouveau.common.network.Networking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber
public class BonusCapabilityEvent {

    @SubscribeEvent
    public static void decreaseCD(net.minecraftforge.event.TickEvent.PlayerTickEvent event){
        /*IBonus b = (IBonus) BonusCapability.getBonus(event.player).orElse((IBonus) null);
        if(b != null){
            if (!event.player.world.isRemote){
                LogManager.getLogger().info(b.getCooldown() + " server");
                LogManager.getLogger().info(b.getDuration() + " server");
                LogManager.getLogger().info(b.getMode() + " server");
            }
        }*/




        if (!event.player.world.isRemote)
        {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            {
                PlayerEntity player = event.player;
                /*if(!player.getPersistentData().contains("lumosSetBonus")){
                    player.getPersistentData().putInt("lumosSetBonus", 0);
                    player.getPersistentData().putInt("lumosSetBonusCD", 0);
                }
                else{
                    int tick = player.getPersistentData().getInt("lumosSetBonus");
                    int cd = player.getPersistentData().getInt("lumosSetBonusCD");

                    player.getPersistentData().remove("lumosSetBonus");
                    player.getPersistentData().remove("lumosSetBonusCD");

                    player.getPersistentData().putInt("lumosSetBonus", Math.max(tick-1,0));

                    if(cd != 0){
                        player.getPersistentData().putInt("lumosSetBonusCD", Math.max(cd-1,0));
                        if(cd == 1){
                            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), AMSoundRegistry.VOID_PORTAL_OPEN, SoundCategory.PLAYERS, 1, 1);
                        }
                    }
                    else{
                        player.getPersistentData().putInt("lumosSetBonusCD", 0);
                    }*/
                //}
                IBonus bonus = (IBonus) BonusCapability.getBonus(event.player).orElse((IBonus) null);
                if(bonus != null){
                    bonus.setCooldown(bonus.getCooldown()-1);
                    bonus.setDuration(bonus.getDuration()-1);
                    if(SetBonus.getType(player) == null){
                        bonus.setDuration(0);
                        bonus.setMode(false);
                    }
                    LumosNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new UpdateBonusMessage(bonus.getCooldown(), bonus.getDuration(), bonus.getMode()));
                }
            }
        }


    }

    @SubscribeEvent
    public static void playerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        IBonus bonus = (IBonus) BonusCapability.getBonus(event.getPlayer()).orElse((IBonus) null);
        bonus.setMode(false);
        bonus.setDuration(0);
        syncEvent(event.getPlayer());
    }
    @SubscribeEvent
    public static void playerLoggedIn(PlayerEvent.StartTracking event) {
        syncEvent(event.getPlayer());
    }

    @SubscribeEvent
    public static void playerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        syncEvent(event.getPlayer());
    }

    public static void syncEvent(PlayerEntity player){
        if(player instanceof ServerPlayerEntity){
            BonusCapability.getBonus(player).ifPresent((bonus) -> {
                bonus.setCooldown(bonus.getCooldown());
                bonus.setDuration(bonus.getDuration());
                bonus.setMode(bonus.getMode());
                LumosNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new UpdateBonusMessage(bonus.getCooldown(), bonus.getDuration(), bonus.getMode()));
            });
        }
    }
}
