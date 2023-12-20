package com.Meli4.lumos.common.forge.mixin;

import com.Meli4.lumos.common.event.RedGeodeSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.monster.piglin.PiglinTasks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.world.server.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(PiglinTasks.class)
public class MixinPiglinTasks {

    @Inject(method = "func_234524_k_", at=@At("HEAD"), cancellable = true)
    private static void lumos_onPiglinBarter(PiglinEntity p_234524_0_, CallbackInfoReturnable<List<ItemStack>> ci) {
        Optional<PlayerEntity> optional = p_234524_0_.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER);
        if(optional.isPresent()){
            PlayerEntity player = optional.get();
            if(RedGeodeSet.INSTANCE.hasArmor(player)){
                LootTable loottable = p_234524_0_.world.getServer().getLootTableManager().getLootTableFromLocation(LootTables.PIGLIN_BARTERING);
                List<ItemStack> list = loottable.generate((new LootContext.Builder((ServerWorld)p_234524_0_.world)).withParameter(LootParameters.THIS_ENTITY, p_234524_0_).withRandom(p_234524_0_.world.rand).build(LootParameterSets.BARTER));
                List<ItemStack> toAdd = new ArrayList<>();
                for(ItemStack stack: list){
                    if(stack.isStackable()){
                        stack.setCount(stack.getCount()*2);
                    }
                    else{
                        toAdd.add(stack);
                    }

                }
                list.addAll(toAdd);
                ci.setReturnValue(list);
            }
        }

    }

    @Inject(method = "func_234460_a_", at=@At("HEAD"), cancellable = true)
    private static void lumos_piglinAttack(LivingEntity p_234460_0_, CallbackInfoReturnable<Boolean> ci){
        if(p_234460_0_ instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) p_234460_0_;
            if(RedGeodeSet.INSTANCE.hasArmor(player)){
                ci.setReturnValue(true);
            }
        }
    }
}
