package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.WoodenBasinRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;

public class WoodenBasinDisplay extends BasicDisplay {
    private final float productionChance;
    private final float blockConsumeChance;
    private final int fluidAmount;

    public WoodenBasinDisplay(WoodenBasinRecipe recipe) {
        super(buildInputs(recipe), List.of(EntryIngredient.of(EntryStacks.of(FluidStack.create(recipe.getFluid().getFluid(), recipe.getFluid().getAmount())))));
        this.productionChance = recipe.getProductionChance();
        this.blockConsumeChance = recipe.getBlockConsumeChance();
        this.fluidAmount = recipe.getFluid().getAmount();
    }

    private static List<EntryIngredient> buildInputs(WoodenBasinRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        List<EntryStack> stacks = new ArrayList<>();
        recipe.getInputsForDisplay().forEach(input ->
                input.ifLeft(stack -> stacks.add(EntryStacks.of(stack)))
                        .ifRight(fluid -> stacks.add(EntryStacks.of(FluidStack.create(fluid, 1000)))));
        if (!stacks.isEmpty()) {
            inputs.add(EntryIngredient.of(stacks));
        }
        return inputs;
    }

    public float getProductionChance() {
        return productionChance;
    }

    public float getBlockConsumeChance() {
        return blockConsumeChance;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return WoodenBasinCategory.ID;
    }
}
