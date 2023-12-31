package com.Meli4.lumos.common.core.network.message;

import com.Meli4.lumos.common.capability.BonusCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public class UpdateBonusMessage {
    public int cooldown;
    public int duration;
    public boolean mode;

    public UpdateBonusMessage(int cooldown, int duration, boolean mode){
        this.cooldown = cooldown;
        this.duration = duration;
        this.mode = mode;
    }

    public static void encode(UpdateBonusMessage message, PacketBuffer buffer){
        buffer.writeInt(message.cooldown);
        buffer.writeInt(message.duration);
        buffer.writeBoolean(message.mode);
    }

    public static UpdateBonusMessage decode(PacketBuffer buffer){
        return new UpdateBonusMessage(buffer.readInt(), buffer.readInt(), buffer.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                BonusCapability.getBonus(player).ifPresent((bonus) -> {
                    bonus.setCooldown(this.cooldown);
                    bonus.setDuration(this.duration);
                    bonus.setMode(this.mode);
                });
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
