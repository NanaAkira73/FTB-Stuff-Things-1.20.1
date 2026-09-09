package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SuperCoolerRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;

public class SuperCoolerDisplay extends BasicDisplay {
    private final int fePerTick;
    private final int ticks;
    private final int fluidAmount;

    public SuperCoolerDisplay(SuperCoolerRecipe recipe) {
        super(buildInputs(recipe), List.of(EntryIngredient.of(EntryStacks.of(recipe.getResult()))));
        this.fePerTick = recipe.getEnergyComponent().fePerTick();
        this.ticks = recipe.getEnergyComponent().ticksToProcess();
        this.fluidAmount = recipe.getFluidInput().amount();
    }

    private static List<EntryIngredient> buildInputs(SuperCoolerRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        // fluid is always input index 0
        inputs.add(EntryIngredient.of(recipe.getFluidInput().getStacks().stream()
                .map(s -> EntryStacks.of(FluidStack.create(s.getFluid(), s.getAmount()))).toList()));
        recipe.getInputs().forEach(i -> inputs.add(EntryIngredients.ofIngredient(i)));
        return inputs;
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
        return SuperCoolerCategory.ID;
    }
}
