package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.block.BlockLightSource;
import com.Meli4.lumos.common.block.ModBlocks;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import com.hollingsworth.arsnouveau.api.event.ManaRegenCalcEvent;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import vectorwing.farmersdelight.registry.ModEffects;

import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
public class BlueGeodeSet extends SetBonus{

    public String desc1;
    public String desc2;

    public BlueGeodeSet(){};

    public BlueGeodeSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }

    public static BlueGeodeSet INSTANCE = new BlueGeodeSet("lol1", "lol2");

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){
            if(itemstack.getItem() instanceof ArmorItem){
                ArmorItem item = (ArmorItem) itemstack.getItem();
                if(item.getArmorMaterial().equals(ArmorMaterials.BLUE_GEODE)){
                    count++;
                }

            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {

    }

    @SubscribeEvent
    public static void onManaGain(ManaRegenCalcEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(INSTANCE.hasArmor(player)){
            event.setRegen(event.getRegen()*1.25);
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != net.minecraftforge.event.TickEvent.Phase.START || !event.player.world.isRemote) {
            return;
        }
        PlayerEntity player = event.player;
        BlockPos pos = player.getEntity().getPosition();
        World world = player.world;
        if (INSTANCE.hasArmor(player)) {
            if (player.getActivePotionEffect(Effects.HASTE) != null) {
                if(player.getActivePotionEffect(Effects.HASTE).getDuration() <=1){
                    player.removePotionEffect(Effects.HASTE);
                }
            }
            if(BlueGeodeSet.isStoneBelow(player)){

                player.addPotionEffect(new EffectInstance(Effects.HASTE, 200, 1));
            }
            if((world.getBlockState(player.getPosition()).getBlock() == Blocks.AIR) || world.getBlockState(player.getPosition()).getBlock() == Blocks.CAVE_AIR){
                world.setBlockState(player.getPosition(), ModBlocks.BlockLightSource.get().getDefaultState());
                //LogManager.getLogger().info(world.getServer().getWorld(player.getEntityWorld().getDimensionKey()));
            }

        }
    }

    private static boolean isStoneBelow(PlayerEntity player){
        return player.world.getBlockState(player.getPosition().down()) == Blocks.STONE.getDefaultState();
    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event){
        if(!(event.getSource().getTrueSource() instanceof PlayerEntity)){return;}
        PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
        if(!INSTANCE.hasArmor(attacker)){return;}
        LivingEntity entity = event.getEntityLiving();
        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 1));
        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 100, 0));
    }



    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().equals(ArmorMaterials.BLUE_GEODE)){
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
            }

        }
    }
}
