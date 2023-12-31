package com.Meli4.lumos.common.event.bonuses;

import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.event.PressSetBonus;
import com.Meli4.lumos.common.event.SetBonus;
import com.github.alexthe666.iceandfire.item.ItemSeaSerpentArmor;
import com.github.wolfshotz.wyrmroost.items.base.ArmorMaterials;
import com.hollingsworth.arsnouveau.api.util.DropDistribution;
import com.hollingsworth.arsnouveau.client.ClientInfo;
import com.hollingsworth.arsnouveau.client.particle.GlowParticleData;
import com.hollingsworth.arsnouveau.client.particle.ParticleColor;
import com.hollingsworth.arsnouveau.client.particle.ParticleUtil;
import com.hollingsworth.arsnouveau.common.potions.ModPotions;
import com.hollingsworth.arsnouveau.common.ritual.ScryingRitual;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
public class RedGeodeSet extends PressSetBonus {

    public RedGeodeSet(){};

    public static RedGeodeSet INSTANCE = new RedGeodeSet();

    public static List<BlockPos> scryingPositions = new ArrayList();


    public static SetBonus getInstance(){return INSTANCE;}

    public IArmorMaterial getMaterial(){return ArmorMaterials.RED_GEODE;}

/*    @SubscribeEvent
    public static void playerLoginEvent(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.getEntity().world.isRemote && INSTANCE.hasArmor(event.getPlayer())) {
            CompoundNBT tag = event.getEntity().getPersistentData().getCompound(PlayerEntity.PERSISTED_NBT_TAG);
            Networking.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntity()), new PacketGetPersistentData(tag));
        }
    }*/

    @SubscribeEvent
    public static void playerTickEvent(net.minecraftforge.event.TickEvent.PlayerTickEvent event) {
       if (event.side == LogicalSide.CLIENT && event.phase == TickEvent.Phase.END && SetBonus.hasArmor(event.player, INSTANCE) && ClientInfo.ticksInGame % 30 == 0) {

            List<BlockPos> scryingPos = new ArrayList<>();
            CompoundNBT tag = ClientInfo.persistentData;
            if (!tag.contains("an_scrying"))
                return;

            PlayerEntity playerEntity = event.player;
            World world = playerEntity.world;
            for (BlockPos p : BlockPos.getProximitySortedBoxPositionsIterator(playerEntity.getEntity().getPosition(), 20,120,20)) {
                if (p.getY() > world.getHeight() && world.getBlockState(p).isAir(world, p)) {
                    continue;
                }

                if (scryingPos.size() >= 50)
                    break;

                if (world.getBlockState(p).getBlock().equals(Blocks.NETHER_GOLD_ORE)) {
                    scryingPos.add(new BlockPos(p));
                }
            }
            RedGeodeSet.scryingPositions = scryingPos;
        }
        if(event.side.isServer()){
            PlayerEntity player = event.player;
            if(SetBonus.hasArmor(player, INSTANCE)){
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 10, 0));
            }


        }
    }


    @SubscribeEvent
    public static void onRenderWorldLast(RenderWorldLastEvent event) {
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (playerEntity != null && playerEntity.getActivePotionEffect(ModPotions.SCRYING_EFFECT) != null) {
            Vector3d vector3d = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
            ClientWorld world = Minecraft.getInstance().world;
            double xView = vector3d.x;
            double yView = vector3d.y;
            double zView = vector3d.z;
            if (!Minecraft.getInstance().isGamePaused()) {
                ParticleColor color;
                BlockPos renderPos;
                for(Iterator var10 = RedGeodeSet.scryingPositions.iterator(); var10.hasNext(); world.addParticle(GlowParticleData.createData(color, true), (double)renderPos.getX() + 0.5 + ParticleUtil.inRange(-0.1, 0.1), (double)renderPos.getY() + 0.2 + ParticleUtil.inRange(-0.1, 0.1), (double)renderPos.getZ() + 0.5 + ParticleUtil.inRange(-0.1, 0.1), 0.0, 0.029999999329447746, 0.0)) {
                    BlockPos p = (BlockPos)var10.next();
                    color = new ParticleColor(DropDistribution.rand.nextInt(255), DropDistribution.rand.nextInt(255), DropDistribution.rand.nextInt(255));
                    renderPos = new BlockPos(p);
                    if (Math.abs(yView - (double)p.getY()) >= 30.0) {
                        renderPos = new BlockPos((double)p.getX(), (double)p.getY() > yView ? yView + 20.0 : yView - 20.0, (double)p.getZ());
                        color = new ParticleColor(DropDistribution.rand.nextInt(30), DropDistribution.rand.nextInt(255), DropDistribution.rand.nextInt(50));
                    }

                    if (Math.abs(yView - (double)p.getY()) >= 60.0) {
                        renderPos = new BlockPos((double)p.getX(), (double)p.getY() > yView ? yView + 20.0 : yView - 20.0, (double)p.getZ());
                        color = new ParticleColor(DropDistribution.rand.nextInt(50), DropDistribution.rand.nextInt(50), DropDistribution.rand.nextInt(255));
                    }
                }

            }
        }
    }

    @Override
    public int getCooldown() {
        return 7200;
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public void onPress(PlayerEntity player){
        super.onPress(player);
        ScryingRitual.grantScrying((ServerPlayerEntity)player, new ItemStack(Blocks.NETHER_GOLD_ORE), 1200);
        ScryingRitual.grantScrying((ServerPlayerEntity)player, new ItemStack(Blocks.ANCIENT_DEBRIS), 1200);
    }

    @SubscribeEvent
    public static void modifyToolTip(ItemTooltipEvent event){
        if(event.getItemStack().getItem() instanceof ArmorItem){
            ArmorItem item = (ArmorItem) event.getItemStack().getItem();
            if(item.getArmorMaterial().equals(ArmorMaterials.RED_GEODE)){
                event.getToolTip().add(new StringTextComponent("lol1"));
                event.getToolTip().add(new StringTextComponent("lol2"));
            }

        }
    }
}
