        package com.Meli4.lumos.common.event.bonuses;
        import com.Meli4.lumos.common.event.SetBonus;
        import net.minecraft.entity.player.PlayerEntity;
        import net.minecraft.item.ArmorItem;
        import net.minecraft.potion.EffectInstance;
        import net.minecraft.potion.Effects;
        import net.minecraft.util.text.StringTextComponent;
        import net.minecraftforge.event.entity.living.LivingHurtEvent;
        import net.minecraftforge.event.entity.player.ItemTooltipEvent;
        import net.minecraftforge.eventbus.api.SubscribeEvent;
        import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber
public class IgnistheSet extends SetBonus {
    public IgnistheSet(){};

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "ignisithe_armor";}


    public static IgnistheSet INSTANCE = new IgnistheSet();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) event.getEntity();
            if (SetBonus.hasArmor(player, INSTANCE)) {
                if(event.getSource().damageType.equals("dragon_fire")){
                    event.setAmount(0);
                    event.setCanceled(true);
                    player.getArmorInventoryList().forEach((itemStack)->{itemStack.setDamage(0);} );
                    player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 600, 2));

                }
            }
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().startsWith("ignisithe_armor") || ((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().endsWith("ignisithe_armor")){
                event.getToolTip().add(new StringTextComponent("lol11"));
                event.getToolTip().add(new StringTextComponent("lol22"));
            }
        }
    }
}

