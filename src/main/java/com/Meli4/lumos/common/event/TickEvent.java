package com.Meli4.lumos.common.event;

import com.github.alexthe666.alexsmobs.misc.AMSoundRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;

@Mod.EventBusSubscriber
public class TickEvent {

    @SubscribeEvent
    public static void decreaseCD(net.minecraftforge.event.TickEvent.PlayerTickEvent event){

        if (!event.player.world.isRemote)
        {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            {
                PlayerEntity player = event.player;
                if(!player.getPersistentData().contains("lumosSetBonus")){
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
                    }



                }
            }
        }


    }
}
