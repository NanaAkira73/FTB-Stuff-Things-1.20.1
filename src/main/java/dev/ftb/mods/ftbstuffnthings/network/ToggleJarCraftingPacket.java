package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

/**
 * Received on: SERVER<br>
 * Sent by client when Mix/Stop button is pressed on the jar GUI
 */
public enum ToggleJarCraftingPacket implements CustomPacketPayload {
    INSTANCE;

    public static final Type<ToggleJarCraftingPacket> TYPE = new Type<>(FTBStuffNThings.id("start_jar_crafting"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ToggleJarCraftingPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void sendToServer() {
        NetworkHandler.CHANNEL.sendToServer(INSTANCE);
    }

    public static void encode(ToggleJarCraftingPacket msg, FriendlyByteBuf buf) {
        STREAM_CODEC.encode(buf, msg);
    }

    public static ToggleJarCraftingPacket decode(FriendlyByteBuf buf) {
        return STREAM_CODEC.decode(buf);
    }

    public static void handle(ToggleJarCraftingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            var player = ctx.get().getSender();
            if (player != null && player.containerMenu instanceof TemperedJarMenu menu) {
                menu.getJar().toggleCrafting();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
