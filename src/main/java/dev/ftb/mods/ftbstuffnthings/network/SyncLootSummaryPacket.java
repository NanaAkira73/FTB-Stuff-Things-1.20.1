package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.util.lootsummary.LootSummaryCollection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncLootSummaryPacket(LootSummaryCollection summary) {
    public static void handleData(SyncLootSummaryPacket packet) {
        LootSummaryCollection.syncFromServer(packet.summary);
    }

    public static void encode(SyncLootSummaryPacket msg, FriendlyByteBuf buf) {
        LootSummaryCollection.toNetwork(buf, msg.summary);
    }

    public static SyncLootSummaryPacket decode(FriendlyByteBuf buf) {
        return new SyncLootSummaryPacket(LootSummaryCollection.fromNetwork(buf));
    }

    public static void handle(SyncLootSummaryPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}