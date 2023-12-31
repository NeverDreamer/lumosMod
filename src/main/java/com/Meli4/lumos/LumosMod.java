package com.Meli4.lumos;

import com.Meli4.lumos.common.block.BlockLightSource;
import com.Meli4.lumos.common.block.ModBlocks;
import com.Meli4.lumos.common.capability.BonusCapability;
import com.Meli4.lumos.common.capability.IBonus;
import com.Meli4.lumos.common.core.init.KeybindsInit;
import com.Meli4.lumos.common.core.network.LumosNetwork;
import com.Meli4.lumos.common.event.BonusCapabilityEvent;
import com.Meli4.lumos.common.potions.ModPotions;
import com.Meli4.lumos.common.tileentity.TileEntityInit;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import software.bernie.shadowed.eliotlash.mclib.math.functions.classic.Abs;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(LumosMod.MOD_ID)
public class LumosMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "lumos";
    public static Reflections reflections = null;

    public LumosMod() {

        reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage("com.Meli4.lumos.common.event.bonuses")));
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntityInit.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModPotions.POTIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code

        BonusCapability.register();
        LumosNetwork.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        KeybindsInit.register(event);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
/*           blockRegistryEvent.getRegistry().register(new BlockLightSource(AbstractBlock.Properties.create(Material.AIR).doesNotBlockMovement().setAir().setLightLevel((state) -> {
                return 15;
            })));*/
        }
        @SubscribeEvent
        public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
            CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
            commandDispatcher.register(Commands.literal("removeCooldown")
                    .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                    .executes(
                    (commandSource) -> {
                        IBonus bonus = (IBonus) BonusCapability.getBonus(commandSource.getSource().asPlayer()).orElse((IBonus) null);
                        bonus.setCooldown(0);
                        BonusCapabilityEvent.syncEvent(commandSource.getSource().asPlayer());
                        return 0;
                    }));
        }
    }
}
