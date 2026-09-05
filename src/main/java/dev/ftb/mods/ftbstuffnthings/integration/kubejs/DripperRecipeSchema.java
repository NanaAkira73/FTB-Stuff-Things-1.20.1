package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraftforge.fluids.FluidStack;

public interface DripperRecipeSchema {
    RecipeKey<String> INPUT = StringComponent.NON_EMPTY.key("input");
    RecipeKey<String> OUTPUT = StringComponent.NON_EMPTY.key("output");
    RecipeKey<FluidStack> FLUID = KubeJSComponents.FLUID_STACK.key("fluid");
    RecipeKey<Double> CHANCE = NumberComponent.DOUBLE.key("chance").optional(1.0);
    RecipeKey<Boolean> CONSUME_FLUID_ON_FAIL = BooleanComponent.BOOLEAN.key("consume_fluid_on_fail").optional(false);

    RecipeSchema SCHEMA = new RecipeSchema(INPUT, OUTPUT, FLUID, CHANCE, CONSUME_FLUID_ON_FAIL);
}
