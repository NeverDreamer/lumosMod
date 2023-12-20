package com.Meli4.lumos.common.potions;

import elucent.eidolon.entity.ai.GoToPositionGoal;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import shadows.apotheosis.ApotheosisObjects;

import java.util.List;
import java.util.stream.Collectors;

public class DeathMarkEffect extends Effect {
    protected DeathMarkEffect() {
        super(EffectType.BENEFICIAL, 11119017);
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    @Override
    public void performEffect(LivingEntity entity, int p_76394_2_) {
        super.performEffect(entity, p_76394_2_);
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = ((PlayerEntity)entity);
            AxisAlignedBB aabb = new AxisAlignedBB(entity.getPosition()).grow(4,4,4);
            for(AnimalEntity animalEntity: entity.getEntityWorld().getEntitiesWithinAABB(AnimalEntity.class, aabb)){
                Vector3d vec = new Vector3d((-player.getPosX()+animalEntity.getPosX()),0,(-player.getPosZ()+animalEntity.getPosZ()));
                BlockPos playerPos = player.getPosition();
                boolean hasGoal = animalEntity.goalSelector.getRunningGoals()
                        .filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                        .count() > 0;
                List<Goal> goals = animalEntity.goalSelector.getRunningGoals().filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                        .collect(Collectors.toList());
                for (Goal g : goals) animalEntity.goalSelector.removeGoal(g);
                //}
                vec = vec.scale(5/vec.length());
                BlockPos target = new BlockPos(vec.x + playerPos.getX(),vec.y + playerPos.getY(),vec.z + playerPos.getZ());
                animalEntity.goalSelector.addGoal(1, new GoToPositionGoal(animalEntity, target, 2f));

            }

        }

    }
}
