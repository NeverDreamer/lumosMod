package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.Meli4.lumos.common.potions.ModPotions;
import com.github.alexthe666.iceandfire.item.ItemDeathwormArmor;
import com.github.alexthe666.iceandfire.item.ItemTrollArmor;
import com.ma.ManaAndArtifice;
import com.ma.Registries;
import com.ma.api.sound.SFX;
import com.ma.effects.EffectInit;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sammy.malum.common.events.SpiritHandlingEvents;
import com.sammy.malum.common.items.tools.spirittools.ScytheItem;
import elucent.eidolon.Registry;
import javafx.scene.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;
import quek.undergarden.registry.UGSoundEvents;
import shadows.apotheosis.ApotheosisObjects;

import java.util.Random;

import static com.Meli4.lumos.common.core.network.LumosNetwork.CHANNEL;

@Mod.EventBusSubscriber
public class DeathSet extends SetBonus {
    public String desc1;
    public String desc2;

    private static String nbtName = "deathSetMode";

    private static float fogAmount = 0.0F;
    private static float fogDecay = 0.01F;
    public static int takeOffTimer = 0;
    public static float[] fogColor = new float[]{1.0F, 1.0F, 1.0F};

    public DeathSet(){}

    public DeathSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    public static DeathSet INSTANCE = new DeathSet("lol1", "lol2");

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ArmorItem){
                if(((ArmorItem) itemstack.getItem()).getArmorMaterial().getName().equals("forbidden_arcanus:mortem")){
                    count++;
                }
            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {
        CompoundNBT nbt = player.getPersistentData();

        if(getMode(player)){
            player.addPotionEffect(new EffectInstance(Registry.CHILLED_EFFECT.get(), 1200, 0));
            player.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 1200, 4));
            player.addPotionEffect(new EffectInstance(Effects.NAUSEA, 1200, 0));
            toggleMode(player);
        }
        else if(nbt.getInt("lumosSetBonusCD") <= 0){
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

        }



    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingKill(LivingDeathEvent event){
        if(!(event.getSource().getTrueSource() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getSource().getTrueSource();
        if(!INSTANCE.hasArmor(player) || !getMode(player)){return;}
        SpiritHandlingEvents.onEntityKill(event);
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(!INSTANCE.hasArmor(player) || !getMode(player)){return;}

        event.setAmount(event.getAmount()*1.35f);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
        {
            PlayerEntity player = event.player;
            if(INSTANCE.hasArmor(player)){
                if (!event.player.world.isRemote)
                {
                    if(!getMode(player)){
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
                World world = Minecraft.getInstance().world;
                if (INSTANCE.hasArmor(player) && player.getActivePotionEffect(ModPotions.DEATH_MARK.get()) == null) {
                    fogAmount = 0.8F;
                } else if(!world.getEntitiesWithinAABB(LivingEntity.class, aabb).isEmpty()){
                    for(LivingEntity livingEntity: world.getEntitiesWithinAABB(LivingEntity.class, aabb)){
                        if(livingEntity instanceof PlayerEntity){
                            if (INSTANCE.hasArmor((PlayerEntity) livingEntity)) {
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
                if(takeOffTimer > 0){
                    takeOffTimer-=1;
                }

            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){return;}
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(!getMode(player) || !INSTANCE.hasArmor(player) || !(player.getHeldItemMainhand().getItem() instanceof ScytheItem) || player.getCooledAttackStrength(0) != 1F || !(event.getTarget() instanceof LivingEntity)){return;}

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

    private static void toggleMode(PlayerEntity player){
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
    }

    public static boolean getMode(PlayerEntity player){
        if(!INSTANCE.hasArmor(player)){return false;}
        CompoundNBT nbt = player.getPersistentData();
        if(!nbt.contains(nbtName)){
            return false;
        }
        else{
            return nbt.getBoolean(nbtName);
        }
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            if(((ArmorItem) event.getItemStack().getItem()).getArmorMaterial().getName().equals("forbidden_arcanus:mortem")){
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
            }
        }
    }
}
