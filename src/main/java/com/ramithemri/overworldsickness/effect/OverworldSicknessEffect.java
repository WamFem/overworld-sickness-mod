package com.ramithemri.overworldsickness.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class OverworldSicknessEffect extends MobEffect {
    public OverworldSicknessEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        // This method can be empty since the health reduction is handled in OverworldSicknessHandler
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 100 == 0; // Apply every 5 seconds (100 ticks)
    }
}
