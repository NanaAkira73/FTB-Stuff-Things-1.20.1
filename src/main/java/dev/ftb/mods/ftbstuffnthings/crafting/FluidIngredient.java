package dev.ftb.mods.ftbstuffnthings.crafting;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public class FluidIngredient {
    private final FluidStack fluidStack;

    private FluidIngredient(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    public boolean test(FluidStack stack) {
        return FluidStack.isSameFluidSameComponents(fluidStack, stack);
    }

    public List<FluidStack> getStacks() {
        return List.of(fluidStack.copy());
    }

    public static FluidIngredient of(Fluid fluid, int amount) {
        return new FluidIngredient(new FluidStack(fluid, amount));
    }

    public static FluidIngredient of(FluidStack fluidStack) {
        return new FluidIngredient(fluidStack);
    }

    public static final Codec<FluidIngredient> FLAT_CODEC = FluidStack.CODEC.xmap(
            FluidIngredient::new,
            fi -> fi.fluidStack
    );

    public static FluidIngredient fromNetwork(FriendlyByteBuf buf) {
        return new FluidIngredient(FluidStack.readFromPacket(buf));
    }

    public static void toNetwork(FriendlyByteBuf buf, FluidIngredient ingredient) {
        ingredient.fluidStack.writeToPacket(buf);
    }
}