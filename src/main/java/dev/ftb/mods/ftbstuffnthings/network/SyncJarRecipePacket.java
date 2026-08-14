package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
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
public record SyncJarRecipePacket(BlockPos pos, Optional<ResourceLocation> recipeId) implements CustomPacketPayload {
    public static final Type<SyncJarRecipePacket> TYPE = new Type<>(FTBStuffNThings.id("sync_jar_recipe"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncJarRecipePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, SyncJarRecipePacket::pos,
            ByteBufCodecs.optional(ResourceLocation.STREAM_CODEC), SyncJarRecipePacket::recipeId,
            SyncJarRecipePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleData(SyncJarRecipePacket packet) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.getBlockEntity(packet.pos) instanceof TemperedJarBlockEntity jar) {
            jar.setCurrentRecipeId(packet.recipeId.orElse(null));
        }
    }

    public static void encode(SyncJarRecipePacket msg, FriendlyByteBuf buf) {
        STREAM_CODEC.encode(buf, msg);
    }

    public static SyncJarRecipePacket decode(FriendlyByteBuf buf) {
        return STREAM_CODEC.decode(buf);
    }

    public static void handle(SyncJarRecipePacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> handleData(msg));
        ctx.get().setPacketHandled(true);
    }
}
