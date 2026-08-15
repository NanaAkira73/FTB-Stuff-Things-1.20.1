package dev.ftb.mods.ftbstuffnthings.integration.kubejs;

import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedIngredient;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.StringComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface JarRecipeSchema {
    RecipeKey<ItemStack[]> OUTPUT_ITEMS = KubeJSComponents.ITEM_STACK.asArray().key("output_items");
    RecipeKey<FluidStack[]> OUTPUT_FLUIDS = KubeJSComponents.FLUID_STACK.asArray().key("output_fluids");
    RecipeKey<SizedIngredient[]> INPUT_ITEMS = KubeJSComponents.SIZED_INGREDIENT.asArray().key("input_items").optional(new SizedIngredient[0]);
    RecipeKey<SizedFluidIngredient[]> INPUT_FLUIDS = KubeJSComponents.SIZED_FLUID_INGREDIENT.asArray().key("input_fluids").optional(new SizedFluidIngredient[0]);
    RecipeKey<Temperature> TEMPERATURE = new EnumComponent<>(Temperature.class, Temperature::getSerializedName, (cls, name) -> Temperature.byName(name)).key("temperature").optional(Temperature.NORMAL);
    RecipeKey<Integer> TIME = NumberComponent.INT.key("time").optional(200);
    RecipeKey<Boolean> CAN_REPEAT = BooleanComponent.BOOLEAN.key("can_repeat").optional(true);
    RecipeKey<String> STAGE = StringComponent.ANY.key("stage").optional("");

    RecipeSchema SCHEMA = new RecipeSchema(OUTPUT_ITEMS, OUTPUT_FLUIDS, INPUT_ITEMS, INPUT_FLUIDS, TEMPERATURE, TIME, CAN_REPEAT, STAGE);
}
