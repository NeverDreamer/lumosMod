package com.Meli4.lumos.common.event;

import com.Meli4.lumos.common.core.network.message.InputMessage;
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
public class SheepSet extends SetBonus{

    public String desc1;
    public String desc2;

    public SheepSet(){};

    public SheepSet(String desc1, String desc2){
        this.desc1 = desc1;
        this.desc2 = desc2;
    }
    @Override
    public boolean hasArmor(PlayerEntity player) {
        int count = 0;
        for(ItemStack itemstack : player.getArmorInventoryList()){
            if(itemstack.getItem() instanceof ArmorItem){
                ArmorItem item = (ArmorItem) itemstack.getItem();
                if(item.getArmorMaterial().getName().equals("sheep")){
                    count++;
                }

            }
        }
        return count == 4;
    }

    public static SheepSet INSTANCE = new SheepSet("lol1", "lol2");
    @Override
    public void doActiveSkill(PlayerEntity player) {
        CompoundNBT nbt = player.getPersistentData();
        player.getPersistentData().remove("lumosSetBonus");
        player.getPersistentData().remove("lumosSetBonusCD");
        player.getPersistentData().putInt("lumosSetBonus", 300);
        player.getPersistentData().putInt("lumosSetBonusCD", 600);
        player.world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_SHEEP_AMBIENT, SoundCategory.PLAYERS, 1, 1);
    }

    @SubscribeEvent
    public static void onActiveTick(TickEvent.PlayerTickEvent event){
        if(event.player.getPersistentData().getInt("lumosSetBonus") > 0) {
            if (event.phase == net.minecraftforge.event.TickEvent.Phase.START)
            {
                PlayerEntity player = event.player;
                if(SheepSet.INSTANCE.hasArmor(player)){
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


    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().getName().equals("sheep")){
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc1));
                event.getToolTip().add(new StringTextComponent(INSTANCE.desc2));
            }

        }
    }
}
