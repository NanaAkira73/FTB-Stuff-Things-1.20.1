package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Received on: CLIENT<br>
 * Sent by server when processing a block starts to sync the processing time (needed by the BER)
 *
 * @param pos sluice blockpos
 * @param processingTime recipe processing time, in ticks
 */
public record SendSluiceStartPacket(BlockPos pos, int processingTime) {
    public static void handleData(SendSluiceStartPacket packet) {
        ClientUtil.getBlockEntityAt(packet.pos, SluiceBlockEntity.class)
                .ifPresent(holder -> holder.syncProcessingTimeFromServer(packet.processingTime));
    }

    public static void encode(SendSluiceStartPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeVarInt(msg.processingTime);
    }

    public static SendSluiceStartPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        int processingTime = buf.readVarInt();
        return new SendSluiceStartPacket(pos, processingTime);
    }

    public static void handle(SendSluiceStartPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}