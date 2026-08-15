package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record EnergyRequirement(int fePerTick, int ticksToProcess) {

    public static EnergyRequirement fromJson(JsonElement element) {
        JsonObject json = JsonUtil.asObject(element, "energy");
        int fe = GsonHelper.getAsInt(json, "fe_per_tick");
        int ticks = GsonHelper.getAsInt(json, "ticks_to_process");
        return new EnergyRequirement(fe, ticks);
    }

    public static EnergyRequirement fromNetwork(FriendlyByteBuf buf) {
        return new EnergyRequirement(buf.readInt(), buf.readVarInt());
    }

    public static void toNetwork(FriendlyByteBuf buf, EnergyRequirement req) {
        buf.writeInt(req.fePerTick());
        buf.writeVarInt(req.ticksToProcess());
    }
}
