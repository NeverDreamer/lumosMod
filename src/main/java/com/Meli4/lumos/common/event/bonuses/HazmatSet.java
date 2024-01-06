    package com.Meli4.lumos.common.event.bonuses;

    import com.Meli4.lumos.common.event.SetBonus;
    import net.minecraft.entity.AreaEffectCloudEntity;
    import net.minecraft.entity.boss.dragon.EnderDragonEntity;
    import net.minecraft.entity.player.PlayerEntity;
    import net.minecraft.item.ArmorItem;
    import net.minecraft.particles.ParticleTypes;
    import net.minecraft.potion.Effects;
    import net.minecraft.util.DamageSource;
    import net.minecraft.util.text.StringTextComponent;
    import net.minecraftforge.event.TickEvent;
    import net.minecraftforge.event.entity.living.LivingHurtEvent;
    import net.minecraftforge.event.entity.player.ItemTooltipEvent;
    import net.minecraftforge.eventbus.api.SubscribeEvent;
    import net.minecraftforge.fml.common.Mod;
    import net.tslat.aoa3.content.item.armour.HazmatArmour;
    import org.apache.logging.log4j.LogManager;


    @Mod.EventBusSubscriber
    public class HazmatSet extends SetBonus {

        public HazmatSet(){};

        public static HazmatSet INSTANCE = new HazmatSet();

        public static SetBonus getInstance(){return INSTANCE;}

        public Class<? extends ArmorItem> getArmorClass(){return HazmatArmour.class;}

        @SubscribeEvent
        public static void onLivingHurt(LivingHurtEvent event){
            if(event.getEntity() instanceof PlayerEntity){
                PlayerEntity player = (PlayerEntity) event.getEntity();

                if (SetBonus.hasArmor(player, INSTANCE)) {
                    if(event.getSource().getImmediateSource() instanceof AreaEffectCloudEntity){
                        AreaEffectCloudEntity entity = (AreaEffectCloudEntity) event.getSource().getImmediateSource();
                        if(entity.getParticleData().equals(ParticleTypes.DRAGON_BREATH)){
                            event.setAmount(0);
                            event.setCanceled(true);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void onActiveTick(TickEvent.PlayerTickEvent event) {
            if (event.player == null) {
                return;
            }
            PlayerEntity player = event.player;
            if (SetBonus.hasArmor(player, INSTANCE)) {
                if (event.phase == net.minecraftforge.event.TickEvent.Phase.START) {
                    if (player.getActivePotionEffect(Effects.POISON) != null) {
                        player.removePotionEffect(Effects.POISON);
                    }
                    if (player.getActivePotionEffect(Effects.SLOWNESS) != null) {
                        player.removePotionEffect(Effects.SLOWNESS);
                    }

                    if (player.getActivePotionEffect(Effects.WITHER) != null) {
                        player.removePotionEffect(Effects.WITHER);
                    }
                }
            }
        }

        @SubscribeEvent
        public static void modifyToolTip(ItemTooltipEvent event){
            if(event.getItemStack().getItem() instanceof HazmatArmour){
                event.getToolTip().add(new StringTextComponent("lol11"));
                event.getToolTip().add(new StringTextComponent("lol22"));
            }
        }
    }