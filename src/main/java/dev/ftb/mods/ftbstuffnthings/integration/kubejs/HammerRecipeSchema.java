package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;

public interface HammerRecipeSchema {
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("input");
    RecipeKey<ItemStack[]> RESULTS = KubeJSComponents.ITEM_STACK.asArray().key("results");

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INGREDIENT);
}
