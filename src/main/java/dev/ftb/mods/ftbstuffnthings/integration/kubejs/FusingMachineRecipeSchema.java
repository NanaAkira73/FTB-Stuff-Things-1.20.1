package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraftforge.fluids.FluidStack;

public interface FusingMachineRecipeSchema {
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT.asArray().key("inputs");
    RecipeKey<FluidStack> RESULT = KubeJSComponents.FLUID_STACK.key("result");
    RecipeKey<EnergyRequirement> ENERGY = KubeJSComponents.ENERGY_REQUIREMENT.key("energy");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, ENERGY);
}
