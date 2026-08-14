package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
public record SyncDisplayItemPacket(BlockPos pos, ItemStack stack) implements CustomPacketPayload {
    public static final Type<SyncDisplayItemPacket> TYPE = new Type<>(FTBStuffNThings.id("sync_display_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncDisplayItemPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncDisplayItemPacket::pos,
            ItemStack.OPTIONAL_STREAM_CODEC, SyncDisplayItemPacket::stack,
            SyncDisplayItemPacket::new
    );

    public static SyncDisplayItemPacket forSluice(AbstractMachineBlockEntity machine) {
        return new SyncDisplayItemPacket(machine.getBlockPos(), machine.getItemHandler().getStackInSlot(0).copy());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SyncDisplayItemPacket packet) {
        ClientUtil.getBlockEntityAt(packet.pos, AbstractMachineBlockEntity.class)
                .ifPresent(holder -> holder.syncItemFromServer(packet.stack));
    }

    public static void encode(SyncDisplayItemPacket msg, FriendlyByteBuf buf) {
        STREAM_CODEC.encode(buf, msg);
    }

    public static SyncDisplayItemPacket decode(FriendlyByteBuf buf) {
        return STREAM_CODEC.decode(buf);
    }

    public static void handle(SyncDisplayItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}
