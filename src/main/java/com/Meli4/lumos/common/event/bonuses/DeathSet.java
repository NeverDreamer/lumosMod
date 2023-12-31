package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.Meli4.lumos.common.event.ToggleSetBonus;
import com.Meli4.lumos.common.potions.ModPotions;
import com.ma.api.sound.SFX;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.malum.common.events.SpiritHandlingEvents;
import com.sammy.malum.common.items.tools.spirittools.ScytheItem;
import elucent.eidolon.Registry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import quek.undergarden.registry.UGSoundEvents;

@Mod.EventBusSubscriber
public class DeathSet extends ToggleSetBonus {


    private static float fogAmount = 0.0F;
    private static float fogDecay = 0.01F;
    public static int takeOffTimer = 0;
    public static float[] fogColor = new float[]{1.0F, 1.0F, 1.0F};

    public DeathSet(){}

    public static DeathSet INSTANCE = new DeathSet();

    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "forbidden_arcanus:mortem";}

    @Override
    public void doActiveSkill(PlayerEntity player) {
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}

        if(bonus.getMode()){
            player.addPotionEffect(new EffectInstance(Registry.CHILLED_EFFECT.get(), 1200, 0));
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 1200, 4));
            player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 1200, 0));
            toggleMode(player);
        }
        else{
            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SFX.Event.Ritual.IRON_BELL, SoundCategory.PLAYERS, 1, 1);
            player.addPotionEffect(new EffectInstance(ModPotions.SOUL_WITHERING.get(), 279, 0));
            toggleMode(player);
        }
        /*else if(nbt.getInt("lumosSetBonusCD") <= 0){
            if(!getMode(player)){
                player.getPersistentData().remove("lumosSetBonus");
                player.getPersistentData().remove("lumosSetBonusCD");
                player.getPersistentData().putInt("lumosSetBonus", 12000);
                takeOffTimer = 12000;
                player.getPersistentData().putInt("lumosSetBonusCD", 36000);

                player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SFX.Event.Ritual.IRON_BELL, SoundCategory.PLAYERS, 1, 1);
                player.addPotionEffect(new EffectInstance(ModPotions.SOUL_WITHERING.get(), 279, 0));

                toggleMode(player);
            }

        }*/



    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingKill(LivingDeathEvent event){
        if(!(event.getSource().getTrueSource() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}
        if(!SetBonus.hasArmor(player, INSTANCE) || !bonus.getMode()){return;}
        SpiritHandlingEvents.onEntityKill(event);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus==null){return;}
        if(!SetBonus.hasArmor(player, INSTANCE) || !bonus.getMode()){return;}

        event.setAmount(event.getAmount()*1.35f);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
            if(SetBonus.hasArmor(player, INSTANCE)){
                if (!event.player.world.isRemote)
                {
                    if(bonus==null){return;}
                    if(!bonus.getMode()){
                        player.addPotionEffect(new EffectInstance(ModPotions.DEATH_MARK.get(), 10, 0));
                        if(player.getActivePotionEffect(ModPotions.SOUL_WITHERING.get()) != null){
                            player.removePotionEffect(ModPotions.SOUL_WITHERING.get());
                        }
                    }
                    else{
                        if(player.world.getGameTime() % 100 == 0){
                            player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), UGSoundEvents.UNDERGARDEN_PORTAL_AMBIENT.get(), SoundCategory.PLAYERS, 1, 1);
                        }
                        AxisAlignedBB aabb = new AxisAlignedBB(player.getPosition()).grow(10,10,10);
                        for(LivingEntity livingEntity: player.world.getEntitiesWithinAABB(LivingEntity.class, aabb)){
                            if(livingEntity.equals(player)){return;}
                            livingEntity.addPotionEffect(new EffectInstance(com.meteor.extrabotany.common.potions.ModPotions.timelock, 10, 0));
                        }
                    }

                }

            }
        }

    }
    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        if (!Minecraft.getInstance().isGamePaused()) {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                AxisAlignedBB aabb = new AxisAlignedBB(player.getPosition()).grow(10,10,10);
                IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);

                World world = Minecraft.getInstance().world;
                if(bonus!=null){
                    if (SetBonus.hasArmor(player, INSTANCE) && bonus.getMode()) {
                        fogAmount = 0.8F;
                    }
                } else if(!world.getEntitiesWithinAABB(LivingEntity.class, aabb).isEmpty()){
                    for(LivingEntity livingEntity: world.getEntitiesWithinAABB(LivingEntity.class, aabb)){
                        if(livingEntity instanceof PlayerEntity){
                            if (SetBonus.hasArmor((PlayerEntity) livingEntity, INSTANCE)) {
                                if(livingEntity.getActivePotionEffect(ModPotions.DEATH_MARK.get()) == null){
                                    fogAmount = 0.8F;
                                }
                            }
                        }
                    }
                }
                else {
                    fogDecay = 0.01F;
                }

                if (fogAmount > 0.0F) {
                    fogAmount -= fogDecay;
                }

            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        IBonus bonus = (IBonus) BonusCapability.getBonus(player).orElse((IBonus) null);
        if(bonus == null){return;}
        if(!bonus.getMode() || !SetBonus.hasArmor(player, INSTANCE) || !(player.getHeldItemMainhand().getItem() instanceof ScytheItem) || player.getCooledAttackStrength(0) != 1F || !(event.getTarget() instanceof LivingEntity)){return;}

        event.getTarget().attackEntityFrom(DamageSource.OUT_OF_WORLD, ((LivingEntity) event.getTarget()).getMaxHealth()*0.1F);
        event.setCanceled(true);
    }


    @SubscribeEvent
    public static void onFogRenderEvent(EntityViewRenderEvent.RenderFogEvent event) {
        if (fogAmount > 0.0F) {
            float f1 = MathHelper.lerp(Math.min(1.0F, fogAmount), event.getFarPlaneDistance(), 5.0F);
            float f2 = 0.0F;
            float f3 = f1 * 0.8F;
            RenderSystem.fogStart(f2);
            RenderSystem.fogEnd(f3);
            RenderSystem.fogMode(GlStateManager.FogMode.LINEAR);
            RenderSystem.setupNvFogDistance();
        }

    }

    @SubscribeEvent
    public static void onFogColor(EntityViewRenderEvent.FogColors event) {
        if (fogAmount > 0.0F) {
            event.setRed(fogColor[0]);
            event.setGreen(fogColor[1]);
            event.setBlue(fogColor[2]);
        }

    }

    /*@SubscribeEvent
    public static void onDeath(LivingDeathEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        if(event.getEntityLiving().getEntityWorld().isRemote){
            takeOffTimer=0;
        }
    }*/
    @Override
    public int getCooldown() {
        return 36000;
    }

    /*private static void toggleMode(PlayerEntity player){
        CompoundNBT nbt = player.getPersistentData();

        if(!nbt.contains(nbtName)){
            nbt.putBoolean(nbtName, true);
        }
        else{
            if(nbt.getBoolean(nbtName)){
                nbt.remove(nbtName);
                nbt.putBoolean(nbtName, false);
            }
            else{
                nbt.remove(nbtName);
                nbt.putBoolean(nbtName, true);
            }
        }
    }*/

    /*public static boolean getMode(PlayerEntity player){
        if(!INSTANCE.hasArmor(player)){return false;}
        CompoundNBT nbt = player.getPersistentData();
        if(!nbt.contains(nbtName)){
            return false;
        }
        else{
            return nbt.getBoolean(nbtName);
        }
    }*/

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().equals("forbidden_arcanus:mortem")){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }
        }
    }
}
