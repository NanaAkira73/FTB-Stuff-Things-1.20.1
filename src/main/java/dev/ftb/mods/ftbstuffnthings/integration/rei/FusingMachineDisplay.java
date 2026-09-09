package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.FusingMachineRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class FusingMachineDisplay extends BasicDisplay {
    private final int fePerTick;
    private final int ticks;
    private final int fluidAmount;

    public FusingMachineDisplay(FusingMachineRecipe recipe) {
        super(
                recipe.getInputs().stream().map(EntryIngredients::ofIngredient).toList(),
                List.of(EntryIngredient.of(EntryStacks.of(FluidStack.create(recipe.getFluidResult().getFluid(), recipe.getFluidResult().getAmount()))))
        );
        this.fePerTick = recipe.getEnergyComponent().fePerTick();
        this.ticks = recipe.getEnergyComponent().ticksToProcess();
        this.fluidAmount = recipe.getFluidResult().getAmount();
    }

    public int getFePerTick() {
        return fePerTick;
    }

    public int getTicks() {
        return ticks;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return FusingMachineCategory.ID;
    }
}
