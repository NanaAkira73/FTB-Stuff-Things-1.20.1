package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;

public interface SuperCoolerRecipeSchema {
    RecipeKey<InputItem[]> INGREDIENTS = ItemComponents.INPUT.asArray().key("inputs");
    RecipeKey<SizedFluidIngredient> FLUID = KubeJSComponents.SIZED_FLUID_INGREDIENT.key("fluid");
    RecipeKey<EnergyRequirement> ENERGY = KubeJSComponents.ENERGY_REQUIREMENT.key("energy");
    RecipeKey<ItemStack> RESULT = KubeJSComponents.ITEM_STACK.key("result");

    RecipeSchema SCHEMA = new RecipeSchema(RESULT, INGREDIENTS, FLUID, ENERGY);
}
