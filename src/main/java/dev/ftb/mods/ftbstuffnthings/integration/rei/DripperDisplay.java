package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.DripperRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;

public class DripperDisplay extends BasicDisplay {
    private final double chance;
    private final boolean consumeFluidOnFail;
    private final int fluidAmount;

    public DripperDisplay(DripperRecipe recipe) {
        super(buildInputs(recipe), buildOutputs(recipe));
        this.chance = recipe.getChance();
        this.consumeFluidOnFail = recipe.consumeFluidOnFail();
        this.fluidAmount = recipe.getFluid().getAmount();
    }

    public double getChance() {
        return chance;
    }

    public boolean consumeFluidOnFail() {
        return consumeFluidOnFail;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    private static List<EntryIngredient> buildInputs(DripperRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredient.of(EntryStacks.of(FluidStack.create(recipe.getFluid().getFluid(), recipe.getFluid().getAmount()))));
        for (var e : recipe.getInputsForDisplay()) {
            e.ifLeft(stack -> inputs.add(EntryIngredient.of(EntryStacks.of(stack))));
            e.ifRight(fluid -> inputs.add(EntryIngredient.of(EntryStacks.of(FluidStack.create(fluid, 1000)))));
        }
        return inputs;
    }

    private static List<EntryIngredient> buildOutputs(DripperRecipe recipe) {
        List<EntryIngredient> outputs = new ArrayList<>();
        recipe.getOutputItemOrFluid()
                .ifLeft(stack -> outputs.add(EntryIngredient.of(EntryStacks.of(stack))))
                .ifRight(fluid -> outputs.add(EntryIngredient.of(EntryStacks.of(FluidStack.create(fluid, 1000)))));
        return outputs;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return DripperCategory.ID;
    }
}
