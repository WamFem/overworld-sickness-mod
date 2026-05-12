package com.ramithemri.overworldsickness.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class ZombifiedHeartsRenderer {
    private static final ResourceLocation ZOMBIFIED_HEARTS_LOCATION = new ResourceLocation("overworldsickness", "textures/gui/zombified_heart.png");
    private static final ResourceLocation EMPTY_ZOMBIFIED_HEARTS_LOCATION = new ResourceLocation("overworldsickness", "textures/gui/zombified_heart_empty.png");
    private static final int HEART_WIDTH = 9;
    private static final int HEART_HEIGHT = 9;
    private static final int MAX_HEARTS = 10;

    public static void renderZombifiedHearts(Minecraft mc, GuiGraphics guiGraphics) {
        if (mc.player == null) {
            return;
        }

        int filledHearts = (int) Math.ceil(mc.player.getHealth() / 2.0F);
        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();
        int left = screenWidth / 2 - 91;
        int y = screenHeight - 39;

        RenderSystem.enableBlend();

        for (int i = 0; i < MAX_HEARTS; i++) {
            ResourceLocation texture = i < filledHearts ? ZOMBIFIED_HEARTS_LOCATION : EMPTY_ZOMBIFIED_HEARTS_LOCATION;
            RenderSystem.setShaderTexture(0, texture);
            guiGraphics.blit(texture, left + i * 8, y, 0, 0, HEART_WIDTH, HEART_HEIGHT);
        }

        RenderSystem.disableBlend();
    }
}
