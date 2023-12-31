package com.Meli4.lumos.common.capability;

import com.hollingsworth.arsnouveau.common.capability.SimpleCapabilityProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

public class PersistentCapabilityProvider<HANDLER> extends SimpleCapabilityProvider<HANDLER> implements INBTSerializable<INBT> {
    public PersistentCapabilityProvider(Capability<HANDLER> capability, @Nullable Direction facing) {
        this(capability, facing, capability.getDefaultInstance());
    }

    public PersistentCapabilityProvider(Capability<HANDLER> capability, @Nullable Direction facing, @Nullable HANDLER instance) {
        super(capability, facing, instance);
    }

    @Nullable
    public INBT serializeNBT() {
        HANDLER instance = this.getInstance();
        if (instance == null) {
            return null;
        } else {
            return (INBT)(this.getCapability() == null ? new CompoundNBT() : this.getCapability().writeNBT(instance, this.getFacing()));
        }
    }

    public void deserializeNBT(INBT nbt) {
        HANDLER instance = this.getInstance();
        if (instance != null) {
            this.getCapability().readNBT(instance, this.getFacing(), nbt);
        }
    }
}
