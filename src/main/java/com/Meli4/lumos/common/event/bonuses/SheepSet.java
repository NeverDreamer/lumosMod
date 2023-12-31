package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.core.network.message.InputMessage;
import com.Meli4.lumos.common.event.PressSetBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemSilverArmor;
import elucent.eidolon.entity.ai.GoToPositionGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.List;
import java.util.stream.Collectors;

import static com.Meli4.lumos.common.core.network.LumosNetwork.CHANNEL;

@Mod.EventBusSubscriber
public class SheepSet extends PressSetBonus {


    public SheepSet(){};


    public static SetBonus getInstance(){return INSTANCE;}

    public String getMaterialName(){return "sheep";}

    public static SheepSet INSTANCE = new SheepSet();

    @Override
    public int getCooldown() {
        return 600;
    }

    @Override
    public int getDuration() {
        return 300;
    }

    @Override
    public void onPress(PlayerEntity player) {
        super.onPress(player);
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SHEEP_AMBIENT, SoundCategory.PLAYERS, 1, 1);
    }

    @SubscribeEvent
    public static void onActiveTick(TickEvent.PlayerTickEvent event){
        IBonus bonus = (IBonus) BonusCapability.getBonus(event.player).orElse((IBonus) null);
        if(bonus != null){
            if(bonus.getDuration() > 0) {
                if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
                {
                    PlayerEntity player = event.player;
                    if(SetBonus.hasArmor(player, INSTANCE)){
                        if (!event.player.world.isRemote){
                            World world = player.world;
                            BlockPos pos = player.getPosition();
                            CHANNEL.send(PacketDistributor.ALL.noArg(), new InputMessage(2));
                            if (world.getGameTime() % 60 == 0) {
                                List<AnimalEntity> animals = world.getEntitiesWithinAABB(AnimalEntity.class, new AxisAlignedBB(pos).grow(15, 15, 15));
                                for (AnimalEntity a : animals) {
                                    boolean hasGoal = a.goalSelector.getRunningGoals()
                                            .filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                                            .count() > 0;
                                    //if (!hasGoal && world.rand.nextInt(4) == 0) {

                                    //} else if (hasGoal) {
                                    List<Goal> goals = a.goalSelector.getRunningGoals().filter((goal) -> goal.getGoal() instanceof GoToPositionGoal)
                                            .collect(Collectors.toList());
                                    for (Goal g : goals) a.goalSelector.removeGoal(g);
                                    //}
                                    BlockPos target = pos.down();
                                    a.goalSelector.addGoal(1, new GoToPositionGoal(a, target, 1.0));
                                }
                            }
                        }
                    }

                }
            }
        }
    }


    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().getName().equals("sheep")){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol1"));
            }

        }
    }
}
