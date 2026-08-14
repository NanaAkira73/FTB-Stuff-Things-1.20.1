package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncDisplayItemPacket(BlockPos pos, ItemStack stack) {
    public static SyncDisplayItemPacket forSluice(AbstractMachineBlockEntity machine) {
        return new SyncDisplayItemPacket(machine.getBlockPos(), machine.getItemHandler().getStackInSlot(0).copy());
    }

    public static void handleData(SyncDisplayItemPacket packet) {
        ClientUtil.getBlockEntityAt(packet.pos, AbstractMachineBlockEntity.class)
                .ifPresent(holder -> holder.syncItemFromServer(packet.stack));
    }

    public static void encode(SyncDisplayItemPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeItem(msg.stack);
    }

    public static SyncDisplayItemPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        ItemStack stack = buf.readItem();
        return new SyncDisplayItemPacket(pos, stack);
    }

    public static void handle(SyncDisplayItemPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}