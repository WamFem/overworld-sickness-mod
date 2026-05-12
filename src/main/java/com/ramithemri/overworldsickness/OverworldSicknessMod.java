package com.ramithemri.overworldsickness;

import com.ramithemri.overworldsickness.sound.ModSounds;
import net.minecraft.world.item.CreativeModeTabs;
import com.mojang.logging.LogUtils;
import com.ramithemri.overworldsickness.Item.ModItems;
import com.ramithemri.overworldsickness.client.ClientEventHandler;
import com.ramithemri.overworldsickness.effect.ModEffects;
import com.ramithemri.overworldsickness.network.ModMessages;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(OverworldSicknessMod.MOD_ID)
public class OverworldSicknessMod {
    // Development command from the project root:
    // .\gradlew.bat runClient

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "overworldsickness";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();



    public OverworldSicknessMod()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        //IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        ModItems.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new OverworldSicknessHandler());
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);

        ModMessages.register(); // Register network messages
        ModSounds.register(modEventBus); // Register sounds


        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO from server starting");

    }

    private void setup(final FMLCommonSetupEvent event) {
        // Some preinit code
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // Some client setup code
    }



    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
        @SubscribeEvent
        public static void buildCreativeModeTabContents(BuildCreativeModeTabContentsEvent event) {
            if (event.getTabKey() == CreativeModeTabs.COMBAT) {
                event.accept(ModItems.ZIRCON.get());
            }
        }
    }

}
