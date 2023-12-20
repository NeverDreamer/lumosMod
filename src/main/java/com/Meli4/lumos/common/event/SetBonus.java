package com.Meli4.lumos.common.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public abstract class SetBonus {
    protected String desc1;
    protected String desc2;

    public SetBonus(){
    }
    public SetBonus(String desc1, String desc2){

        this.desc1 = desc1;
        this.desc2 = desc2;
    }

    public abstract boolean hasArmor(PlayerEntity player) ;

    public abstract void doActiveSkill(PlayerEntity player);

    public static SetBonus getType(PlayerEntity player) {
        if(MageSet.INSTANCE.hasArmor(player)){
            return new MageSet();
        } else if (GoblinSet.INSTANCE.hasArmor(player)) {
            return new GoblinSet();
        }
        else if (ShadowSet.INSTANCE.hasArmor(player)){
            return new ShadowSet();
        }
        else if (SheepSet.INSTANCE.hasArmor(player)){
            return new SheepSet();
        } else if (RedGeodeSet.INSTANCE.hasArmor(player)) {
            return new RedGeodeSet();
        } else if (PurpleGeodeSet.INSTANCE.hasArmor(player)) {
            return new PurpleGeodeSet();
        } else if (DeathSet.INSTANCE.hasArmor(player)) {
            return new DeathSet();
        }
        return null;
    }




}
