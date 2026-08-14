package dev.ftb.mods.ftbstuffnthings.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record SizedFluidIngredient(FluidIngredient ingredient, int amount) {

    public static final Codec<SizedFluidIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    FluidIngredient.FLAT_CODEC.fieldOf("ingredient").forGetter(SizedFluidIngredient::ingredient),
                    Codec.INT.fieldOf("amount").forGetter(SizedFluidIngredient::amount)
            ).apply(instance, SizedFluidIngredient::new)
    );

    public static SizedFluidIngredient fromNetwork(FriendlyByteBuf buf) {
        return new SizedFluidIngredient(FluidIngredient.fromNetwork(buf), buf.readVarInt());
    }

    public static void toNetwork(FriendlyByteBuf buf, SizedFluidIngredient ingredient) {
        FluidIngredient.toNetwork(buf, ingredient.ingredient());
        buf.writeVarInt(ingredient.amount());
    }

    public boolean test(FluidStack stack) {
        return ingredient().test(stack) && stack.getAmount() >= amount();
    }

    public List<FluidStack> getStacks() {
        return ingredient().getStacks().stream()
                .map(stack -> stack.copyWithAmount(amount()))
                .toList();
    }

    public static SizedFluidIngredient of(Fluid fluid, int amount) {
        return new SizedFluidIngredient(FluidIngredient.of(fluid, amount), amount);
    }

    public static SizedFluidIngredient of(FluidIngredient fluidIngredient, int amount) {
        return new SizedFluidIngredient(fluidIngredient, amount);
    }
}