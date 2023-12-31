package com.Meli4.lumos.common.event;

import com.Meli4.lumos.LumosMod;
import com.Meli4.lumos.common.event.bonuses.ManaSteelSet;
import com.google.common.reflect.ClassPath;
import com.meteor.extrabotany.common.items.armor.shadowwarrior.ItemShadowWarriorArmor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.util.Set;

@Mod.EventBusSubscriber
public abstract class SetBonus {
    //protected String desc1;
    //protected String desc2;

    //protected static SetBonus INSTANCE;

    public SetBonus(){
    }
    /*public SetBonus(String desc1, String desc2){

        this.desc1 = desc1;
        this.desc2 = desc2;
    }*/

    //public abstract boolean hasArmor(PlayerEntity player) ;



    public int getCount(){return 4;}

    public static SetBonus getInstance(){return null;}

    public Class<? extends ArmorItem> getArmorClass(){return null;}

    public Class<? extends ArmorItem> getHelmClass(){return null;}

    public String getMaterialName(){return null;}

    public IArmorMaterial getMaterial(){return null;}



    public static SetBonus getType(PlayerEntity player) {
        /*if(MageSet.INSTANCE.hasArmor(player)){
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
        return null;*/


        Set<Class<? extends SetBonus>> classes = LumosMod.reflections.getSubTypesOf(SetBonus.class);
        for(Class<? extends SetBonus> clas: classes){
            try {
                if(hasArmor(player, clas.newInstance())){
                    return clas.newInstance();
                };
            } /*catch (InstantiationException e) {
            } catch (IllegalAccessException e) {
            }*/
            catch(Exception e){
            }
        }

        return null;

    }

    public static boolean hasArmor(PlayerEntity player, SetBonus setBonus){
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){
            if(itemstack.getItem() instanceof ArmorItem){
                if(itemstack.getItem().getClass().equals(setBonus.getArmorClass()) || itemstack.getItem().getClass().equals(setBonus.getHelmClass())){
                    count++;
                }

                else if(((ArmorItem) itemstack.getItem()).getArmorMaterial() != null){
                    if(setBonus.getMaterialName() != null){
                        //endswith just because the abyss exists
                        if(((ArmorItem) itemstack.getItem()).getArmorMaterial().getName().startsWith(setBonus.getMaterialName()) || ((ArmorItem) itemstack.getItem()).getArmorMaterial().getName().endsWith(setBonus.getMaterialName())){
                            count++;
                        }
                    }
                    else if(((ArmorItem) itemstack.getItem()).getArmorMaterial().equals(setBonus.getMaterial())){
                        count++;
                    }
                }


            }
        }
        return count == setBonus.getCount();
    }



}
