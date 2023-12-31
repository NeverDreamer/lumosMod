package com.Meli4.lumos.common.core.network.message;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.ActiveSetBonus;
import com.Meli4.lumos.common.event.bonuses.DeathSet;
import com.Meli4.lumos.common.event.SetBonus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

public class InputMessage {
    public int index;
    public InputMessage(){
    }

    public InputMessage(int index){
        this.index = index;
    }

    public static void encode(InputMessage message, PacketBuffer buffer){
        buffer.writeInt(message.index);

    }

    public static InputMessage decode(PacketBuffer buffer){
        return new InputMessage(buffer.readInt());
    }

    public static void handle(InputMessage message, Supplier<NetworkEvent.Context> contextSupplier){
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if(message.index == 0){
                ServerPlayerEntity player = context.getSender();
                if (player != null ) {
                    IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
                    if(bonus!=null){
                        if(bonus.getCooldown()==0){
                            if(SetBonus.getType(player) instanceof ActiveSetBonus){
                                ((ActiveSetBonus) SetBonus.getType(player)).doActiveSkill(player);
                            }
                        }
                    }
                }
            }
            if(message.index == 1){
                ClientPlayerEntity player1 = Minecraft.getInstance().player;
                for(int i = 0; i < 15; i++){
                    player1.world.addParticle(ParticleTypes.SMOKE, player1.getPosXRandom(1), player1.getPosYRandom(), player1.getPosZRandom(1), 0, 0, 0);
                }


            }
            if(message.index == 2){
                ClientPlayerEntity player1 = Minecraft.getInstance().player;
                if (player1.world.getGameTime() % 10 == 0) {
                    Random rand = new Random();
                    for(int i = 0; i < 3; i++){
                        double d0 = rand.nextGaussian() * 0.02D;
                        double d1 = rand.nextGaussian() * 0.02D;
                        double d2 = rand.nextGaussian() * 0.02D;
                        player1.world.addParticle(ParticleTypes.HEART, player1.getPosXRandom(1.0D), player1.getPosYRandom() + 0.5D, player1.getPosZRandom(1.0D), d0, d1, d2);
                    }

                }
            }


        });
        context.setPacketHandled(true);
    }
}
