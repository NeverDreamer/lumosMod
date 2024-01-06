package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.event.SetBonus;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import shadows.apotheosis.ApotheosisObjects;

@Mod.EventBusSubscriber
public class ObsidianSet extends SetBonus {

        public ObsidianSet() {
        }

        public static ObsidianSet INSTANCE = new ObsidianSet();

        public static SetBonus getInstance() {
            return INSTANCE;
        }

        public String getMaterialName() {
            return "forbidden_arcanus:obsidian";
        }


        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event) {

            if (event.getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getEntity();
                if(event.getSource().getImmediateSource() instanceof LivingEntity) {
                    LivingEntity entity = (LivingEntity) event.getSource().getImmediateSource();
                    if (SetBonus.hasArmor(player, INSTANCE)) {
                        entity.addPotionEffect(new EffectInstance(ApotheosisObjects.BLEEDING, entity instanceof PlayerEntity ? 100 : 200, entity instanceof PlayerEntity ? 2 : 4));
                    }
                }
            }
        }


        @SubscribeEvent
        public static void modifyToolTip(ItemTooltipEvent event) {
            if (event.getItemStack().getItem() instanceof ArmorItem) {
                if (((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().equals("forbidden_arcanus:obsidian")) {
                    event.getToolTip().add(new StringTextComponent("lol1"));
                    event.getToolTip().add(new StringTextComponent("lol2"));
                }
            }
        }
    }


