package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface SluiceRecipeSchema {
    RecipeKey<ItemWithChance[]> RESULTS = KubeJSComponents.ITEM_WITH_CHANCE.asArray().key("results");
    RecipeKey<InputItem> INGREDIENT = ItemComponents.INPUT.key("input");
    RecipeKey<SizedFluidIngredient> FLUID = KubeJSComponents.SIZED_FLUID_INGREDIENT.key("fluid");
    RecipeKey<MeshType[]> MESH_TYPES = new EnumComponent<>(MeshType.class, MeshType::getSerializedName, (cls, name) -> MeshType.byName(name)).asArray().key("mesh_types");
    RecipeKey<Integer> MAX_RESULTS = NumberComponent.INT.key("max_results").optional(4);
    RecipeKey<Float> TIME = NumberComponent.FLOAT.key("processing_time_multiplier").optional(1F);

    RecipeSchema SCHEMA = new RecipeSchema(RESULTS, INGREDIENT, FLUID, MESH_TYPES, MAX_RESULTS, TIME);
}
