package dev.ftb.mods.ftbstuffnthings.network;

import com.mojang.datafixers.util.Either;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Received on: CLIENT<br>
 * Sent by server (not more than once every 10 ticks) when items or fluids in the jar have changed.
 *
 * @param jarPos jar blockpos
 * @param resources list of resources to sync; a slot with an item or fluid stack
 */
public record SyncJarContentsPacket(BlockPos jarPos, List<ResourceSlot> resources) {
    public static SyncJarContentsPacket wholeJar(TemperedJarBlockEntity jar) {
        List<ResourceSlot> resources = new ArrayList<>();
        for (int i = 0; i < jar.getInputItemHandler().getSlots(); i++) {
            ItemStack stack = jar.getInputItemHandler().getStackInSlot(i);
            if (!stack.isEmpty()) {
                resources.add(new ResourceSlot(i, Either.left(stack)));
            }
        }
        for (int i = 0; i < jar.getFluidHandler().getTanks(); i++) {
            FluidStack stack = jar.getFluidHandler().getFluidInTank(i);
            if (!stack.isEmpty()) {
                resources.add(new ResourceSlot(i, Either.right(stack)));
            }
        }

        return new SyncJarContentsPacket(jar.getBlockPos(), resources);
    }

    public static SyncJarContentsPacket oneItem(BlockPos pos, int slot, ItemStack stack) {
        return new SyncJarContentsPacket(pos, List.of(new ResourceSlot(slot, Either.left(stack))));
    }

    public static SyncJarContentsPacket oneFluid(BlockPos pos, int slot, FluidStack stack) {
        return new SyncJarContentsPacket(pos, List.of(new ResourceSlot(slot, Either.right(stack))));
    }

    public static void handleData(SyncJarContentsPacket packet) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(packet.jarPos) instanceof TemperedJarBlockEntity jar) {
            jar.syncFromServer(packet.resources);
        }
    }

    public static void encode(SyncJarContentsPacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.jarPos);
        buf.writeVarInt(msg.resources.size());
        for (ResourceSlot slot : msg.resources) {
            ResourceSlot.toNetwork(buf, slot);
        }
    }

    public static SyncJarContentsPacket decode(FriendlyByteBuf buf) {
        BlockPos jarPos = buf.readBlockPos();
        int size = buf.readVarInt();
        List<ResourceSlot> resources = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            resources.add(ResourceSlot.fromNetwork(buf));
        }
        return new SyncJarContentsPacket(jarPos, resources);
    }

    public static void handle(SyncJarContentsPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }

    public record ResourceSlot(int slot, Either<ItemStack,FluidStack> resource) {
        public static ResourceSlot fromNetwork(FriendlyByteBuf buf) {
            int slot = buf.readVarInt();
            boolean isItem = buf.readBoolean();
            Either<ItemStack, FluidStack> resource = isItem ?
                    Either.left(buf.readItem()) :
                    Either.right(FluidStack.readFromPacket(buf));
            return new ResourceSlot(slot, resource);
        }

        public static void toNetwork(FriendlyByteBuf buf, ResourceSlot slot) {
            buf.writeVarInt(slot.slot);
            slot.resource.ifLeft(item -> {
                buf.writeBoolean(true);
                buf.writeItem(item);
            }).ifRight(fluid -> {
                buf.writeBoolean(false);
                fluid.writeToPacket(buf);
            });
        }
    }
}