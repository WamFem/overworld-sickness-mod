package com.ramithemri.overworldsickness.sound;

import com.ramithemri.overworldsickness.OverworldSicknessMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OverworldSicknessMod.MOD_ID);

    public static final RegistryObject<SoundEvent> SICKNESS_SOUND = SOUND_EVENTS.register("sickness_sound",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(OverworldSicknessMod.MOD_ID, "sickness_sound")));

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
