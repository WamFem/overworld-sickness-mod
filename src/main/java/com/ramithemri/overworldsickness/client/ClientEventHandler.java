package com.ramithemri.overworldsickness.client;

import com.ramithemri.overworldsickness.OverworldSicknessMod;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = OverworldSicknessMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {
    private static int musicSuppressionTicks = 0;

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (!ZombifiedHeartsManager.isSicknessActive() || event.getOverlay() != VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            ZombifiedHeartsRenderer.renderZombifiedHearts(mc, event.getGuiGraphics());
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            ZombifiedHeartsManager.setSicknessActive(false);
            return;
        }

        if (!ZombifiedHeartsManager.isSicknessActive()) {
            return;
        }

        musicSuppressionTicks++;
        if (musicSuppressionTicks >= 20) {
            mc.getSoundManager().stop(null, SoundSource.MUSIC);
            musicSuppressionTicks = 0;
        }
    }
}
