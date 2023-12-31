package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.core.network.message.UpdateBonusMessage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.PacketDistributor;

public abstract class ToggleSetBonus extends ActiveSetBonus {

    public abstract int getCooldown();

    public void toggleMode(PlayerEntity player){
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}
        if(!bonus.getMode()){
            if(bonus.getCooldown() == 0){
                bonus.setMode(true);
            }
        }
        else{
            bonus.setMode(false);
            bonus.setCooldown(getCooldown());
        }
        //LumosNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new UpdateBonusMessage(bonus.getCooldown(), bonus.getDuration(), bonus.getMode()));
        BonusCapabilityEvent.syncEvent(player);
    }
}
