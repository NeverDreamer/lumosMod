package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.block.ModBlocks;
import com.Meli4.lumos.common.event.SetBonus;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import com.hollingsworth.arsnouveau.api.event.ManaRegenCalcEvent;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
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

@Mod.EventBusSubscriber
public class BlueGeodeSet extends SetBonus {

    public BlueGeodeSet(){};


    public static BlueGeodeSet INSTANCE = new BlueGeodeSet();


    public static SetBonus getInstance(){return INSTANCE;}

    public IArmorMaterial getMaterial(){return ArmorMaterials.BLUE_GEODE;}

    @SubscribeEvent
    public static void onManaGain(ManaRegenCalcEvent event){
        if(!(event.getEntityLiving() instanceof PlayerEntity)){
            return;
        }
        PlayerEntity player = (PlayerEntity) event.getEntityLiving();
        if(SetBonus.hasArmor(player, INSTANCE)){
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
        if (SetBonus.hasArmor(player, INSTANCE)) {
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
        if(!SetBonus.hasArmor(attacker, INSTANCE)){return;}
        LivingEntity entity = event.getEntityLiving();
        entity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 100, 1));
        entity.addPotionEffect(new EffectInstance(Effects.WEAKNESS, 100, 0));
    }



    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().equals(ArmorMaterials.BLUE_GEODE)){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }

        }
    }
}
