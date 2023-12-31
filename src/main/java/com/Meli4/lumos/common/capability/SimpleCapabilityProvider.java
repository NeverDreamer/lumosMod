package com.Meli4.lumos.common.capability;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class SimpleCapabilityProvider<HANDLER> implements ICapabilityProvider {
    protected final Capability<HANDLER> capability;
    protected final Direction facing;
    protected final HANDLER instance;
    protected final LazyOptional<HANDLER> lazyOptional;

    public SimpleCapabilityProvider(Capability<HANDLER> capability, @Nullable Direction facing, @Nullable HANDLER instance) {
        this.capability = capability;
        this.facing = facing;
        this.instance = instance;
        if (this.instance != null) {
            this.lazyOptional = LazyOptional.of(() -> {
                return this.instance;
            });
        } else {
            this.lazyOptional = LazyOptional.empty();
        }

    }

    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return this.getCapability() == null ? LazyOptional.empty() : this.getCapability().orEmpty(capability, this.lazyOptional);
    }

    public final Capability<HANDLER> getCapability() {
        return this.capability;
    }

    @Nullable
    public Direction getFacing() {
        return this.facing;
    }

    @Nullable
    public final HANDLER getInstance() {
        return this.instance;
    }
}
