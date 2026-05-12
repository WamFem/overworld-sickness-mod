package com.ramithemri.overworldsickness.network;

import com.ramithemri.overworldsickness.OverworldSicknessMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(OverworldSicknessMod.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void register() {
        int id = 0;
        INSTANCE.registerMessage(id++, ZombifiedHeartsPacket.class, ZombifiedHeartsPacket::encode, ZombifiedHeartsPacket::decode, ZombifiedHeartsPacket::handle);
    }
}
