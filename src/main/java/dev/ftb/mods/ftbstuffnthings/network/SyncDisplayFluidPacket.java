package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.blocks.AbstractMachineBlockEntity;
import dev.ftb.mods.ftbstuffnthings.client.ClientUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SyncDisplayFluidPacket(BlockPos pos, FluidStack fluidStack) {
    public static void handleData(SyncDisplayFluidPacket packet) {
        ClientUtil.getBlockEntityAt(packet.pos, AbstractMachineBlockEntity.class)
                .ifPresent(holder -> holder.syncFluidFromServer(packet.fluidStack));
    }

    public static void encode(SyncDisplayFluidPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        boolean present = !msg.fluidStack.isEmpty();
        buf.writeBoolean(present);
        if (present) {
            msg.fluidStack.writeToPacket(buf);
        }
    }

    public static SyncDisplayFluidPacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        FluidStack fluidStack = buf.readBoolean() ? FluidStack.readFromPacket(buf) : FluidStack.EMPTY;
        return new SyncDisplayFluidPacket(pos, fluidStack);
    }

    public static void handle(SyncDisplayFluidPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}