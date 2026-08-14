package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.sluice.SluiceBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
/**
 * Received on: CLIENT<br>
 * Sent by server when processing a block starts to sync the processing time (needed by the BER)
 *
 * @param pos sluice blockpos
 * @param processingTime recipe processing time, in ticks
 */
public record SendSluiceStartPacket(BlockPos pos, int processingTime) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendSluiceStartPacket> TYPE = new CustomPacketPayload.Type<>(FTBStuffNThings.id("send_sluice_start"));

    public static final StreamCodec<FriendlyByteBuf, SendSluiceStartPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SendSluiceStartPacket::pos,
            ByteBufCodecs.VAR_INT, SendSluiceStartPacket::processingTime,
            SendSluiceStartPacket::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SendSluiceStartPacket packet) {
        ClientUtil.getBlockEntityAt(packet.pos, SluiceBlockEntity.class)
                .ifPresent(holder -> holder.syncProcessingTimeFromServer(packet.processingTime));
    }

    public static void encode(SendSluiceStartPacket msg, FriendlyByteBuf buf) {
        STREAM_CODEC.encode(buf, msg);
    }

    public static SendSluiceStartPacket decode(FriendlyByteBuf buf) {
        return STREAM_CODEC.decode(buf);
    }

    public static void handle(SendSluiceStartPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}
