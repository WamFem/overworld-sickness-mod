package com.ramithemri.overworldsickness.network;

import com.ramithemri.overworldsickness.client.ZombifiedHeartsManager;
import com.ramithemri.overworldsickness.sound.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ZombifiedHeartsPacket {
    private final int zombifiedHearts;
    private final boolean startSicknessMusic;

    public ZombifiedHeartsPacket(int zombifiedHearts, boolean startSicknessMusic) {
        this.zombifiedHearts = zombifiedHearts;
        this.startSicknessMusic = startSicknessMusic;
    }

    public static void encode(ZombifiedHeartsPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.zombifiedHearts);
        buf.writeBoolean(msg.startSicknessMusic);
    }

    public static ZombifiedHeartsPacket decode(FriendlyByteBuf buf) {
        return new ZombifiedHeartsPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(ZombifiedHeartsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ZombifiedHeartsManager.setZombifiedHearts(msg.zombifiedHearts);
            if (msg.startSicknessMusic) {
                ZombifiedHeartsManager.setSicknessActive(true);
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.getSoundManager().stop(null, SoundSource.MUSIC);
                minecraft.getSoundManager().play(SimpleSoundInstance.forAmbientAddition(ModSounds.SICKNESS_SOUND.get()));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
