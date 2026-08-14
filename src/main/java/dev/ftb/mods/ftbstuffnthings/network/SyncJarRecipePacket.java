package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Received on: CLIENT<br>
 * Sent by server to update the GUI when the current recipe changes.
 *
 * @param pos jar blockpos
 * @param recipeId the new recipe ID
 */
public record SyncJarRecipePacket(BlockPos pos, Optional<ResourceLocation> recipeId) {
    public static void handleData(SyncJarRecipePacket packet) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(packet.pos) instanceof TemperedJarBlockEntity jar) {
            jar.setCurrentRecipeId(packet.recipeId.orElse(null));
        }
    }

    public static void encode(SyncJarRecipePacket msg, FriendlyByteBuf buf) {
        buf.writeBlockPos(msg.pos);
        buf.writeBoolean(msg.recipeId.isPresent());
        msg.recipeId.ifPresent(buf::writeResourceLocation);
    }

    public static SyncJarRecipePacket decode(FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        Optional<ResourceLocation> recipeId = buf.readBoolean() ?
                Optional.of(buf.readResourceLocation()) : Optional.empty();
        return new SyncJarRecipePacket(pos, recipeId);
    }

    public static void handle(SyncJarRecipePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}