package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.JsonUtil;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedIngredient;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.item.OutputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Custom KubeJS 6 recipe components that serialize to the same JSON shape that
 * the FTB Stuff & Things recipe serializers expect on Forge 1.20.1.
 */
public final class KubeJSComponents {
    private static final Gson GSON = new Gson();

    private KubeJSComponents() {
    }

    public static final RecipeComponent<ItemStack> ITEM_STACK = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return ItemStack.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, ItemStack stack) {
            JsonObject json = new JsonObject();
            json.addProperty("id", BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
            json.addProperty("count", stack.getCount());
            if (stack.hasTag()) {
                JsonElement tag = net.minecraft.nbt.CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag())
                        .result().orElse(null);
                if (tag != null) {
                    json.add("tag", tag);
                }
            }
            return json;
        }

        @Override
        public ItemStack read(RecipeJS recipe, Object from) {
            if (from instanceof ItemStack stack) {
                return stack;
            }
            if (from instanceof InputItem input && input.ingredient.getItems().length > 0) {
                return input.ingredient.getItems()[0];
            }
            if (from instanceof OutputItem output) {
                return output.item;
            }
            if (from instanceof CharSequence s) {
                return JsonUtil.itemStack(parseItemOrFluid(s.toString(), "id", "count"));
            }
            return JsonUtil.itemStack(toJsonElement(from));
        }
    };

    public static final RecipeComponent<FluidStack> FLUID_STACK = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return FluidStack.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, FluidStack stack) {
            JsonObject json = new JsonObject();
            json.addProperty("id", ForgeRegistries.FLUIDS.getKey(stack.getFluid()).toString());
            json.addProperty("amount", stack.getAmount());
            if (stack.hasTag()) {
                JsonElement tag = net.minecraft.nbt.CompoundTag.CODEC.encodeStart(JsonOps.INSTANCE, stack.getTag())
                        .result().orElse(null);
                if (tag != null) {
                    json.add("tag", tag);
                }
            }
            return json;
        }

        @Override
        public FluidStack read(RecipeJS recipe, Object from) {
            if (from instanceof FluidStack stack) {
                return stack;
            }
            if (from instanceof CharSequence s) {
                return JsonUtil.fluidStack(parseItemOrFluid(s.toString(), "id", "amount"));
            }
            return JsonUtil.fluidStack(toJsonElement(from));
        }
    };

    public static final RecipeComponent<ItemWithChance> ITEM_WITH_CHANCE = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return ItemWithChance.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, ItemWithChance value) {
            JsonObject json = new JsonObject();
            json.addProperty("chance", value.chance());
            json.add("item", ITEM_STACK.write(recipe, value.item()));
            return json;
        }

        @Override
        public ItemWithChance read(RecipeJS recipe, Object from) {
            return ItemWithChance.fromJson(toJsonElement(from));
        }
    };

    public static final RecipeComponent<EnergyRequirement> ENERGY_REQUIREMENT = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return EnergyRequirement.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, EnergyRequirement value) {
            JsonObject json = new JsonObject();
            json.addProperty("fe_per_tick", value.fePerTick());
            json.addProperty("ticks_to_process", value.ticksToProcess());
            return json;
        }

        @Override
        public EnergyRequirement read(RecipeJS recipe, Object from) {
            return EnergyRequirement.fromJson(toJsonElement(from));
        }
    };

    public static final RecipeComponent<SizedIngredient> SIZED_INGREDIENT = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return SizedIngredient.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, SizedIngredient value) {
            JsonObject json = new JsonObject();
            json.add("ingredient", value.ingredient().toJson());
            json.addProperty("count", value.count());
            return json;
        }

        @Override
        public SizedIngredient read(RecipeJS recipe, Object from) {
            return SizedIngredient.fromJson(toJsonElement(from));
        }
    };

    public static final RecipeComponent<SizedFluidIngredient> SIZED_FLUID_INGREDIENT = new RecipeComponent<>() {
        @Override
        public Class<?> componentClass() {
            return SizedFluidIngredient.class;
        }

        @Override
        public JsonElement write(RecipeJS recipe, SizedFluidIngredient value) {
            JsonObject json = new JsonObject();
            json.add("ingredient", FLUID_STACK.write(recipe, value.ingredient().getFluidStack()));
            json.addProperty("amount", value.amount());
            return json;
        }

        @Override
        public SizedFluidIngredient read(RecipeJS recipe, Object from) {
            return SizedFluidIngredient.fromJson(toJsonElement(from));
        }
    };

    private static JsonElement toJsonElement(Object from) {
        if (from instanceof JsonElement element) {
            return element;
        }
        return GSON.toJsonTree(from);
    }

    /**
     * Parses a simple "namespace:path [amount]" string into a JSON object such as
     * {"id": "namespace:path", "count": amount} for items or fluids.
     */
    private static JsonObject parseItemOrFluid(String s, String idKey, String amountKey) {
        String[] parts = s.trim().split("\\s+");
        ResourceLocation id = new ResourceLocation(parts[0]);
        int amount = 1;
        if (parts.length > 1) {
            try {
                amount = Integer.parseInt(parts[parts.length - 1]);
            } catch (NumberFormatException ignored) {
                amount = 1;
            }
        }
        JsonObject json = new JsonObject();
        json.addProperty(idKey, id.toString());
        json.addProperty(amountKey, amount);
        return json;
    }
}
