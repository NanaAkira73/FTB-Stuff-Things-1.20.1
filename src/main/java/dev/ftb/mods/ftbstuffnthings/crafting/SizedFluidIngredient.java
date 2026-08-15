package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public record SizedFluidIngredient(FluidIngredient ingredient, int amount) {

    public static SizedFluidIngredient fromNetwork(FriendlyByteBuf buf) {
        return new SizedFluidIngredient(FluidIngredient.fromNetwork(buf), buf.readVarInt());
    }

    public static void toNetwork(FriendlyByteBuf buf, SizedFluidIngredient ingredient) {
        FluidIngredient.toNetwork(buf, ingredient.ingredient());
        buf.writeVarInt(ingredient.amount());
    }

    public static SizedFluidIngredient fromJson(JsonElement element) {
        JsonObject json = JsonUtil.asObject(element, "sized_fluid_ingredient");
        FluidIngredient ingr = FluidIngredient.fromJson(json.get("ingredient"));
        int amt = GsonHelper.getAsInt(json, "amount");
        return new SizedFluidIngredient(ingr, amt);
    }

    public boolean test(FluidStack stack) {
        return ingredient().test(stack) && stack.getAmount() >= amount();
    }

    public List<FluidStack> getStacks() {
        return ingredient().getStacks().stream()
                .map(stack -> {
                    FluidStack copy = stack.copy();
                    copy.setAmount(amount());
                    return copy;
                })
                .toList();
    }

    public static SizedFluidIngredient of(Fluid fluid, int amount) {
        return new SizedFluidIngredient(FluidIngredient.of(fluid, amount), amount);
    }

    public static SizedFluidIngredient of(FluidIngredient fluidIngredient, int amount) {
        return new SizedFluidIngredient(fluidIngredient, amount);
    }
}
