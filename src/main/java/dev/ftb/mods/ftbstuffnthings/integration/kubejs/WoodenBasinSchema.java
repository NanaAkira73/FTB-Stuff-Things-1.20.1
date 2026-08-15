package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraftforge.fluids.FluidStack;

public interface WoodenBasinSchema {
    RecipeKey<FluidStack> OUTPUT_FLUID = KubeJSComponents.FLUID_STACK.key("fluid");
    RecipeKey<String> BLOCK_STATE_STRING = StringComponent.ANY.key("input");
    RecipeKey<Float> PRODUCE_CHANCE = NumberComponent.FLOAT.key("chance").optional(1f);
    RecipeKey<Float> CONSUME_CHANCE = NumberComponent.FLOAT.key("block_consume_chance").optional(1f);
    RecipeKey<Boolean> DROP_ITEMS = BooleanComponent.BOOLEAN.key("drop_items").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT_FLUID, BLOCK_STATE_STRING, PRODUCE_CHANCE, CONSUME_CHANCE, DROP_ITEMS);
}
