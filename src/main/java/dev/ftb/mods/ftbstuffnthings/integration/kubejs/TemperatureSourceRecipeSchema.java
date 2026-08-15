package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;

public interface TemperatureSourceRecipeSchema {
    RecipeKey<String> BLOCKSTATE_STR = StringComponent.ANY.key("blockstate");
    RecipeKey<Temperature> TEMPERATURE = new EnumComponent<>(Temperature.class, Temperature::getSerializedName, (cls, name) -> Temperature.byName(name)).key("temperature").optional(Temperature.NORMAL);
    RecipeKey<Double> EFFICIENCY = NumberComponent.DOUBLE.key("efficiency").optional(1.0);
    RecipeKey<ItemStack> DISPLAY_STACK = KubeJSComponents.ITEM_STACK.key("display_item").optional(ItemStack.EMPTY);
    RecipeKey<Boolean> HIDE_FROM_JEI = BooleanComponent.BOOLEAN.key("hide_from_jei").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(BLOCKSTATE_STR, TEMPERATURE, EFFICIENCY, DISPLAY_STACK, HIDE_FROM_JEI);
}
