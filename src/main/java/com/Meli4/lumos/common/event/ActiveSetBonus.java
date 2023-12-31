package com.Meli4.lumos.common.event;

import net.minecraft.entity.player.PlayerEntity;

public abstract class ActiveSetBonus extends SetBonus{

    public abstract void doActiveSkill(PlayerEntity player);
}
