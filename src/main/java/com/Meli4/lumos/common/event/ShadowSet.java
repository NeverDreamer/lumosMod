package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.ma.brewing.BrewingInit;
import com.ma.brewing.ManaPotion;
import com.ma.effects.EffectInit;
import com.meteor.extrabotany.common.items.ItemShadowKatana;
import com.meteor.extrabotany.common.items.armor.shadowwarrior.ItemShadowWarriorArmor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import shadows.apotheosis.ApotheosisObjects;

import static com.Meli4.lumos.common.core.network.LumosNetwork.CHANNEL;


@Mod.EventBusSubscriber
public class ShadowSet extends SetBonus {

    public String desc1;
    public String desc2;

    public ShadowSet(){};

    public ShadowSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    public static ShadowSet INSTANCE = new ShadowSet("lol1", "lol2");

    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){

            if(itemstack.getItem() instanceof ItemShadowWarriorArmor){
                count++;
            }
        }
        return count == 4;
    }

    @Override
    public void doActiveSkill(PlayerEntity player) {
        CompoundNBT nbt = player.getPersistentData();
        player.getPersistentData().remove("lumosSetBonus");
        player.getPersistentData().remove("lumosSetBonusCD");
        player.getPersistentData().putInt("lumosSetBonus", 140);
        player.getPersistentData().putInt("lumosSetBonusCD", 1940);
    }

    @SubscribeEvent
    public static void onAttack(LivingHurtEvent event){

        if(!(event.getSource().getTrueSource() instanceof PlayerEntity)){return;}
        PlayerEntity attacker = (PlayerEntity) event.getSource().getTrueSource();
        if(!ShadowSet.INSTANCE.hasArmor(attacker)){return;}
        LivingEntity entity = event.getEntityLiving();

        //if(!isInsideOfSector(Vector3.fromEntityCenter(attacker), Vector3.fromEntityCenter(entity), -entity.rotationYaw, 4d)){

        Vector3d targetVec = entity.getLookVec();
        Vector3d attackVec = attacker.getLookVec();
        if(Math.signum(targetVec.x)==Math.signum(attackVec.x) && Math.signum(targetVec.z)==Math.signum(attackVec.z)){
            if(entity.hurtResistantTime == 20){
                entity.hurtResistantTime = 0;
                entity.attackEntityFrom(DamageSource.GENERIC, 3);
            }
        }

        if(attacker.getHeldItemMainhand().getItem() instanceof ItemShadowKatana){
            entity.addPotionEffect(new EffectInstance(ModPotions.HEX_EFFECT, 60, 0));
        }
        if(attacker.getPersistentData().getInt("lumosSetBonus") > 0){
            entity.addPotionEffect(new EffectInstance(Effects.WITHER, 60, 2));
            entity.addPotionEffect(new EffectInstance(ApotheosisObjects.SUNDERING, 60, 2));
            AxisAlignedBB aabb = new AxisAlignedBB(entity.getPosition()).grow(2,2,2);
            for(LivingEntity livingEntity: entity.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class, aabb)){
                if(!(livingEntity instanceof PlayerEntity)){
                    livingEntity.addPotionEffect(new EffectInstance(ApotheosisObjects.BLEEDING, 60*20, 3));
                }
            }
        }
    }



    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent event){

            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            {
                PlayerEntity player = event.player;
                if(ShadowSet.INSTANCE.hasArmor(player)){
                    if (!event.player.world.isRemote)
                    {

                        if(player.getPersistentData().getInt("lumosSetBonus") > 0) {
                            player.addPotionEffect(new EffectInstance(Effects.SPEED, 10, 1));
                            player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 10, 3));
                            if(player.getActivePotionEffect(EffectInit.TRUE_INVISIBILITY.get()) == null){
                                player.addPotionEffect(new EffectInstance(EffectInit.TRUE_INVISIBILITY.get(), 10, 0));
                            }
                            ServerPlayerEntity finalPlayer = (ServerPlayerEntity) player;
                            //CHANNEL.send(PacketDistributor.PLAYER.with(() -> finalPlayer), new InputMessage(1));
                            CHANNEL.send(PacketDistributor.ALL.noArg(), new InputMessage(1));
                        }
                    }

                }
            }
    }

    @SubscribeEvent
    public static void onAggro(LivingSetAttackTargetEvent event){
        if(event.getTarget() == null){return;}
        if(!(event.getEntityLiving() instanceof MobEntity)){return;}
        if(!(event.getTarget() instanceof PlayerEntity)){return;}

        PlayerEntity player = (PlayerEntity) event.getTarget();
        if (!player.world.isRemote){
            if(ShadowSet.INSTANCE.hasArmor(player)){
                if(player.getPersistentData().getInt("lumosSetBonus") > 0){
                    ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
/*                    try{
                        ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, event.getEntityLiving(), null, "field_70696_bz");
                    } catch (Exception e) {

                    }
                    try{
                        ObfuscationReflectionHelper.setPrivateValue(LivingEntity.class, event.getEntityLiving(), null, "attackTarget");
                    } catch (Exception e) {

                    }*/


                }
            }
        }


    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ItemShadowWarriorArmor){
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
            event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
        }
    }
}
