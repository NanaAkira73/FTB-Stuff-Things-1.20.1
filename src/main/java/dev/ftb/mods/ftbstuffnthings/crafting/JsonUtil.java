package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Small JSON helpers for parsing recipe ingredients in 1.20.1.
 */
public final class JsonUtil {
    private JsonUtil() {
    }

    public static JsonObject asObject(JsonElement element, String name) {
        if (element == null || !element.isJsonObject()) {
            throw new JsonSyntaxException("Expected object for " + name);
        }
        return element.getAsJsonObject();
    }

    public static ItemStack itemStack(JsonElement element) {
        JsonObject json = asObject(element, "item");
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "id"));
        Item item = BuiltInRegistries.ITEM.get(id);
        if (item == null) {
            throw new JsonSyntaxException("Unknown item '" + id + "'");
        }
        int count = GsonHelper.getAsInt(json, "count", 1);
        ItemStack stack = new ItemStack(item, count);
        if (json.has("tag")) {
            stack.setTag(CraftingHelper.getNBT(json.get("tag")));
        }
        return stack;
    }

    public static FluidStack fluidStack(JsonElement element) {
        JsonObject json = asObject(element, "fluid");
        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(json, "id"));
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
        if (fluid == null) {
            throw new JsonSyntaxException("Unknown fluid '" + id + "'");
        }
        int amount = GsonHelper.getAsInt(json, "amount", 1000);
        FluidStack stack = new FluidStack(fluid, amount);
        if (json.has("tag")) {
            stack.setTag(CraftingHelper.getNBT(json.get("tag")));
        }
        return stack;
    }

    public static Ingredient ingredient(JsonElement element) {
        return Ingredient.fromJson(element);
    }
}
