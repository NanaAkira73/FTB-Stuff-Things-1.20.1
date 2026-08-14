package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(FTBStuffNThings.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void init() {
        CHANNEL.registerMessage(packetId++, SyncJarContentsPacket.class, SyncJarContentsPacket::encode, SyncJarContentsPacket::decode, SyncJarContentsPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncJarRecipePacket.class, SyncJarRecipePacket::encode, SyncJarRecipePacket::decode, SyncJarRecipePacket::handle);
        CHANNEL.registerMessage(packetId++, SyncDisplayFluidPacket.class, SyncDisplayFluidPacket::encode, SyncDisplayFluidPacket::decode, SyncDisplayFluidPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncDisplayItemPacket.class, SyncDisplayItemPacket::encode, SyncDisplayItemPacket::decode, SyncDisplayItemPacket::handle);
        CHANNEL.registerMessage(packetId++, SendSluiceStartPacket.class, SendSluiceStartPacket::encode, SendSluiceStartPacket::decode, SendSluiceStartPacket::handle);
        CHANNEL.registerMessage(packetId++, SyncLootSummaryPacket.class, SyncLootSummaryPacket::encode, SyncLootSummaryPacket::decode, SyncLootSummaryPacket::handle);
        CHANNEL.registerMessage(packetId++, ToggleJarCraftingPacket.class, ToggleJarCraftingPacket::encode, ToggleJarCraftingPacket::decode, ToggleJarCraftingPacket::handle);
    }
}