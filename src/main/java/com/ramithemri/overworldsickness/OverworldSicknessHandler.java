package com.ramithemri.overworldsickness;

import com.ramithemri.overworldsickness.effect.ModEffects;
import com.ramithemri.overworldsickness.network.ModMessages;
import com.ramithemri.overworldsickness.network.ZombifiedHeartsPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.network.NetworkDirection;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = OverworldSicknessMod.MOD_ID, bus = Bus.FORGE)
public class OverworldSicknessHandler {
    private static final long SICKNESS_DURATION = 2 * 60 * 60 * 1000L; // 2 hours in milliseconds
    private static final long ZOMBIFY_INTERVAL = 5 * 1000L; // 5 seconds in milliseconds
    private static final int MAX_ZOMBIFIED_HEARTS = 10;
    private static final float NORMAL_HEALTH = 20.0F;
    private static final float MIN_SURVIVABLE_HEALTH = 2.0F;
    private static final Random rand = new Random();

    // Store the sickness start time per player
    private static final Map<ServerPlayer, Long> playerSicknessStartTimes = new HashMap<>();
    private static final Map<ServerPlayer, Boolean> playerSoundPlayed = new HashMap<>();
    private static final Map<ServerPlayer, Integer> playerRestoredHearts = new HashMap<>();
    private static final Map<ServerPlayer, Integer> playerLastSyncedHearts = new HashMap<>();
    private static final Map<ServerPlayer, Integer> playerLastAppliedHearts = new HashMap<>();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.player.level().isClientSide || !(event.player instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.player;
        long currentTime = System.currentTimeMillis();

        // Get or initialize the sickness start time for the player
        playerSicknessStartTimes.putIfAbsent(player, currentTime + (rand.nextInt(21) + 10) * 1000L); // Random time between 10 to 30 seconds
        long sicknessStartTime = playerSicknessStartTimes.get(player);

        // Check if it's time to apply the sickness
        if (currentTime >= sicknessStartTime) {
            if (!player.hasEffect(ModEffects.OVERWORLD_SICKNESS.get())) {
                player.addEffect(new MobEffectInstance(ModEffects.OVERWORLD_SICKNESS.get(), (int) (SICKNESS_DURATION / 50), 0, false, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, (int) (SICKNESS_DURATION / 50), 0, false, false, true));
                OverworldSicknessMod.LOGGER.info("Overworld Sickness effect started for player " + player.getName().getString() + ".");
            }

            // Stop the current Minecraft music and play the custom sound
            boolean shouldStartSicknessMusic = !playerSoundPlayed.getOrDefault(player, false);
            if (shouldStartSicknessMusic) {
                playerSoundPlayed.put(player, true);
            }

            // Calculate the elapsed time since the effect started
            int zombifiedHearts = getZombifiedHearts(player, currentTime, sicknessStartTime);
            applyZombifiedHeartDamage(player, zombifiedHearts);
            syncZombifiedHearts(player, zombifiedHearts, shouldStartSicknessMusic);

            if (zombifiedHearts >= MAX_ZOMBIFIED_HEARTS && player.isAlive()) {
                player.hurt(player.damageSources().genericKill(), Float.MAX_VALUE);
            }
        }
    }

    @SubscribeEvent
    public static void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ItemStack usedItem = event.getItem();
        if (!usedItem.is(Items.GOLDEN_APPLE) && !usedItem.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        Long sicknessStartTime = playerSicknessStartTimes.get(player);
        if (sicknessStartTime == null || currentTime < sicknessStartTime) {
            return;
        }

        int currentHearts = getZombifiedHearts(player, currentTime, sicknessStartTime);
        if (currentHearts <= 0) {
            return;
        }

        playerRestoredHearts.merge(player, 2, Integer::sum);
        int updatedHearts = getZombifiedHearts(player, currentTime, sicknessStartTime);
        applyZombifiedHeartDamage(player, updatedHearts);
        syncZombifiedHearts(player, updatedHearts, false);
        OverworldSicknessMod.LOGGER.info("Golden apple restored 2 hearts for " + player.getName().getString() + ".");
    }

    private static int getZombifiedHearts(ServerPlayer player, long currentTime, long sicknessStartTime) {
        long elapsedTime = currentTime - sicknessStartTime;
        int rawZombifiedHearts = (int) (elapsedTime / ZOMBIFY_INTERVAL);
        int restoredHearts = playerRestoredHearts.getOrDefault(player, 0);
        return Math.max(0, Math.min(rawZombifiedHearts - restoredHearts, MAX_ZOMBIFIED_HEARTS));
    }

    private static void applyZombifiedHeartDamage(ServerPlayer player, int zombifiedHearts) {
        int previousHearts = playerLastAppliedHearts.getOrDefault(player, 0);
        if (previousHearts == zombifiedHearts) {
            return;
        }

        if (zombifiedHearts > previousHearts) {
            float targetHealth = Math.max(MIN_SURVIVABLE_HEALTH, NORMAL_HEALTH - zombifiedHearts * 2.0F);
            if (player.getHealth() > targetHealth) {
                player.hurt(player.damageSources().magic(), 0.1F);
                player.setHealth(targetHealth);
            }
        } else {
            player.heal((previousHearts - zombifiedHearts) * 2.0F);
        }

        playerLastAppliedHearts.put(player, zombifiedHearts);
    }

    private static void syncZombifiedHearts(ServerPlayer player, int zombifiedHearts, boolean startSicknessMusic) {
        Integer lastSyncedHearts = playerLastSyncedHearts.get(player);
        if (!startSicknessMusic && lastSyncedHearts != null && lastSyncedHearts == zombifiedHearts) {
            return;
        }

        playerLastSyncedHearts.put(player, zombifiedHearts);
        OverworldSicknessMod.LOGGER.info("Player " + player.getName().getString() + " has " + zombifiedHearts + " zombified hearts.");
        ModMessages.INSTANCE.sendTo(new ZombifiedHeartsPacket(zombifiedHearts, startSicknessMusic), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
