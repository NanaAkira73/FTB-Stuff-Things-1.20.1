package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface CrookRecipeSchema {
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("input");
    RecipeKey<ItemWithChance[]> RESULTS = KubeJSComponents.ITEM_WITH_CHANCE.asArray().key("results");
    RecipeKey<Integer> MAX = NumberComponent.INT.key("max").optional(0);
    RecipeKey<Boolean> REPLACE_DROPS = BooleanComponent.BOOLEAN.key("replace_drops").optional(true);

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INGREDIENT, MAX, REPLACE_DROPS);
}
