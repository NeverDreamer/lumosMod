package com.Meli4.lumos.common.capability;

import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.core.network.message.UpdateBonusMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

public class BonusCapability {

    @CapabilityInject(IBonus.class)
    public static final Capability<IBonus> BONUS_CAPABILITY = null;
    public static final Direction DEFAULT_FACING = null;
    public static final ResourceLocation ID = new ResourceLocation("lumos", "bonus");

    public static void register() {
        CapabilityManager.INSTANCE.register(IBonus.class, new Capability.IStorage<IBonus>() {
            @Nullable
            public INBT writeNBT(Capability<IBonus> capability, IBonus instance, Direction side) {
                CompoundNBT tag = new CompoundNBT();
                tag.putInt("cooldown", instance.getCooldown());
                tag.putInt("duration", instance.getDuration());
                tag.putBoolean("mode", instance.getMode());
                return tag;
            }

            public void readNBT(Capability<IBonus> capability, IBonus instance, Direction side, INBT nbt) {
                if (nbt instanceof CompoundNBT) {
                    CompoundNBT tag = (CompoundNBT)nbt;
                    instance.setCooldown(tag.getInt("cooldown"));
                    instance.setDuration(tag.getInt("duration"));
                    instance.setMode(tag.getBoolean("mode"));
                }
            }
        }, () -> {
            return new Bonus((LivingEntity)null);
        });
        System.out.println("Registered BonusCapability");
    }

    public static LazyOptional<IBonus> getBonus(LivingEntity entity) {
        return entity.getCapability(BONUS_CAPABILITY, DEFAULT_FACING);
    }

    public static ICapabilityProvider createProvider(IBonus bonus) {
        return new PersistentCapabilityProvider<>(BONUS_CAPABILITY, DEFAULT_FACING, bonus);
    }

    @Mod.EventBusSubscriber
    public static class EventHandler {
        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof PlayerEntity) {
                Bonus bonus = new Bonus((LivingEntity)event.getObject());
                event.addCapability(BonusCapability.ID, BonusCapability.createProvider(bonus));
            }

        }

        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            BonusCapability.getBonus(event.getOriginal()).ifPresent((oldBonus) -> {
                BonusCapability.getBonus(event.getPlayer()).ifPresent((newBonus) -> {
                    newBonus.setCooldown(oldBonus.getCooldown());
                    newBonus.setDuration(oldBonus.getDuration());
                    newBonus.setMode(oldBonus.getMode());
                    LumosNetwork.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)event.getEntity()), new UpdateBonusMessage(newBonus.getCooldown(), newBonus.getDuration(), newBonus.getMode()));
                });
            });
        }
    }
}
