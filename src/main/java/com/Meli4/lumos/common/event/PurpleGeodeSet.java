package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.Meli4.lumos.common.potions.ModPotions;
import com.github.klikli_dev.occultism.registry.OccultismEffects;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import com.hollingsworth.arsnouveau.common.ritual.ScryingRitual;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import org.apache.logging.log4j.LogManager;

import java.util.Map;
import java.util.UUID;

import static com.Meli4.lumos.common.core.network.LumosNetwork.CHANNEL;

@Mod.EventBusSubscriber
public class PurpleGeodeSet extends SetBonus{

    public String desc1;
    public String desc2;

    public PurpleGeodeSet(){};

    public PurpleGeodeSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }

    public static PurpleGeodeSet INSTANCE = new PurpleGeodeSet("lol1", "lol2");

    private static String uuid = "9ae3d87a-497d-47a3-b9b9-99e555d2920e";

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){
            if(itemstack.getItem() instanceof ArmorItem){
                ArmorItem item = (ArmorItem) itemstack.getItem();
                if(item.getArmorMaterial().equals(ArmorMaterials.PURPLE_GEODE)){
                    count++;
                }

            }
        }
        return count == 4;
    }

    @SubscribeEvent
    public static void onAggro(LivingSetAttackTargetEvent event){
        if(event.getTarget() == null){return;}
        if(!(event.getEntityLiving() instanceof EndermanEntity)){return;}
        if(!(event.getTarget() instanceof PlayerEntity)){return;}

        PlayerEntity player = (PlayerEntity) event.getTarget();
        if (!player.world.isRemote){
            if(INSTANCE.hasArmor(player)){
                ((EndermanEntity) event.getEntityLiving()).setAttackTarget(null);
            }
        }


    }

    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

        if (event.phase == net.minecraftforge.event.TickEvent.Phase.START && !event.player.world.isRemote)
        {
            PlayerEntity player = event.player;
            if(INSTANCE.hasArmor(player)){
                if(player.world.getDimensionKey().equals(World.THE_END)){
                    player.addPotionEffect(new EffectInstance(Effects.SLOW_FALLING, 10, 1));
                    player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 10, 3));
                    player.addPotionEffect(new EffectInstance(OccultismEffects.DOUBLE_JUMP.get(), 10, 4 ));
                }
                if(player.world.getBlockState(player.getPosition().down()) == Blocks.END_STONE.getDefaultState()){
                    if(player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(uuid)) == null){
                        INSTANCE.applyAttributesModifiersToEntity(player);
                    }
                }
                else{
                    if(player.getAttribute(Attributes.MOVEMENT_SPEED).getModifier(UUID.fromString(uuid)) != null){
                        INSTANCE.removeAttributesModifiersFromEntity(player);
                    }
                }
                if(player.isInWater() && player.getEntityWorld().getGameTime()%5==0){
                    int i = 0;
                    for(ItemStack stack: player.getArmorInventoryList()){
                        if(i==0){
                            stack.damageItem(1, player, (livingEntity) -> {
                                livingEntity.sendBreakAnimation(EquipmentSlotType.FEET);
                            });
                        }
                        else if(i==1){
                            stack.damageItem(1, player, (livingEntity) -> {
                                livingEntity.sendBreakAnimation(EquipmentSlotType.LEGS);
                            });
                        }
                        else if(i==2){
                            stack.damageItem(1, player, (livingEntity) -> {
                                livingEntity.sendBreakAnimation(EquipmentSlotType.CHEST);
                            });
                        }
                        else if(i==3){
                            stack.damageItem(1, player, (livingEntity) -> {
                                livingEntity.sendBreakAnimation(EquipmentSlotType.HEAD);
                            });
                        }

                        i++;

                    }
                }
            }
        }
    }

    public void removeAttributesModifiersFromEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.removeModifier(UUID.fromString(uuid));
    }

    public void applyAttributesModifiersToEntity(LivingEntity livingEntity) {
        ModifiableAttributeInstance attribute = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        attribute.applyPersistentModifier(new AttributeModifier(UUID.fromString(uuid), "purpleGeodeSpeed", attribute.getBaseValue()*0.8, AttributeModifier.Operation.ADDITION));
    }

    @SubscribeEvent
    public static void onRegen(LivingHealEvent event){
        BlockPos pos = event.getEntity().getEntity().getPosition();
        World world = event.getEntity().world;
        if(!world.isRemote){
            event.setAmount(event.getAmount() * 1.2f);
        }
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {
        player.addPotionEffect(new EffectInstance(ModPotions.CRYSTALLIZED.get(), 100, 0));
        player.getPersistentData().remove("lumosSetBonusCD");
        player.getPersistentData().putInt("lumosSetBonusCD", 6100);
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().equals(ArmorMaterials.PURPLE_GEODE)){
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
            }

        }
    }
}
