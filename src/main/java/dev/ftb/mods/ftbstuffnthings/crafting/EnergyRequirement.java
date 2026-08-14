package dev.ftb.mods.ftbstuffnthings.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;

public record EnergyRequirement(int fePerTick, int ticksToProcess) {
    public static final Codec<EnergyRequirement> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("fe_per_tick").forGetter(EnergyRequirement::fePerTick),
            ExtraCodecs.POSITIVE_INT.fieldOf("ticks_to_process").forGetter(EnergyRequirement::ticksToProcess)
    ).apply(builder, EnergyRequirement::new));

    public static EnergyRequirement fromNetwork(FriendlyByteBuf buf) {
        return new EnergyRequirement(buf.readInt(), buf.readVarInt());
    }

    public static void toNetwork(FriendlyByteBuf buf, EnergyRequirement req) {
        buf.writeInt(req.fePerTick());
        buf.writeVarInt(req.ticksToProcess());
    }
}