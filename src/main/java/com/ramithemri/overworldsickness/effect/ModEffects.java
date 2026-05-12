package com.ramithemri.overworldsickness.effect;

import com.ramithemri.overworldsickness.OverworldSicknessMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, OverworldSicknessMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

    // Register the custom effect here
    public static final RegistryObject<MobEffect> OVERWORLD_SICKNESS = EFFECTS.register("overworld_sickness",
            () -> new OverworldSicknessEffect(MobEffectCategory.HARMFUL, 0x5A6C81));

}
