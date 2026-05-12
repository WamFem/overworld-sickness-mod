package com.ramithemri.overworldsickness.Item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
public class CuringItem {
    private final boolean isNotchApple;

    public CuringItem(Properties properties, boolean isNotchApple) {
        super();
        this.isNotchApple = isNotchApple;
    }
/*
    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        if (!world.isClientSide) {
            if (isNotchApple) {
                entity.removeEffect(ModEffects.OVERWORLD_SICKNESS.get());
            } else {
                MobEffectInstance effectInstance = entity.getEffect(ModEffects.OVERWORLD_SICKNESS.get());
                if (effectInstance != null) {
                    int duration = effectInstance.getDuration();
                    entity.removeEffect(ModEffects.OVERWORLD_SICKNESS.get());
                    entity.addEffect(new MobEffectInstance(ModEffects.OVERWORLD_SICKNESS.get(), Math.max(duration - 2400, 0)));
                }
            }
        }
        return super.finishUsingItem(stack, world, entity);
    }
*/
}
