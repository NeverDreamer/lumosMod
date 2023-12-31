package com.Meli4.lumos.common.capability;

import net.minecraft.entity.LivingEntity;

import javax.annotation.Nullable;

public class Bonus implements IBonus{

    private final LivingEntity livingEntity;

    private int cooldown;

    private int duration;

    private boolean mode;

    public Bonus(@Nullable LivingEntity livingEntity) {
        this.livingEntity = livingEntity;
    }

    @Override
    public int getCooldown() {
        return cooldown;
    }

    @Override
    public int setCooldown(int ticks) {
        cooldown = Math.max(ticks, 0);
        return cooldown;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public int setDuration(int ticks) {
        duration = Math.max(ticks, 0);
        return duration;
    }

    @Override
    public boolean getMode() {
        return mode;
    }

    @Override
    public boolean setMode(boolean mode1) {
        mode = mode1;
        return mode;
    }
}
