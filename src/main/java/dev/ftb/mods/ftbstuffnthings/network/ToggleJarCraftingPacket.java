package dev.ftb.mods.ftbstuffnthings.network;

import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Received on: SERVER<br>
 * Sent by client when Mix/Stop button is pressed on the jar GUI
 */
public enum ToggleJarCraftingPacket {
    INSTANCE;

    public static void sendToServer() {
        NetworkHandler.CHANNEL.sendToServer(INSTANCE);
    }

    public static void encode(ToggleJarCraftingPacket msg, FriendlyByteBuf buf) {
        // no data to encode
    }

    public static ToggleJarCraftingPacket decode(FriendlyByteBuf buf) {
        return INSTANCE;
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