package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.JarRecipe;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;

public class TemperedJarDisplay extends BasicDisplay {
    private final Temperature temperature;
    private final int time;
    private final int inputFluidCount;
    private final int outputFluidCount;
    private final List<Integer> inputFluidAmounts;
    private final List<Integer> outputFluidAmounts;

    public TemperedJarDisplay(JarRecipe recipe) {
        super(buildInputs(recipe), buildOutputs(recipe));
        this.temperature = recipe.getTemperature();
        this.time = recipe.getTime();
        this.inputFluidCount = recipe.getInputFluids().size();
        this.outputFluidCount = recipe.getOutputFluids().size();
        this.inputFluidAmounts = recipe.getInputFluids().stream().map(f -> f.amount()).toList();
        this.outputFluidAmounts = recipe.getOutputFluids().stream().map(f -> f.getAmount()).toList();
    }

    private static List<EntryIngredient> buildInputs(JarRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        recipe.getInputFluids().forEach(f -> inputs.add(EntryIngredient.of(f.getStacks().stream()
                .map(s -> EntryStacks.of(FluidStack.create(s.getFluid(), s.getAmount()))).toList())));
        recipe.getInputItems().forEach(i -> inputs.add(EntryIngredient.of(i.getItems().stream()
                .map(EntryStacks::of).toList())));
        return inputs;
    }

    private static List<EntryIngredient> buildOutputs(JarRecipe recipe) {
        List<EntryIngredient> outputs = new ArrayList<>();
        recipe.getOutputFluids().forEach(f -> outputs.add(EntryIngredient.of(EntryStacks.of(FluidStack.create(f.getFluid(), f.getAmount())))));
        recipe.getOutputItems().forEach(i -> outputs.add(EntryIngredient.of(EntryStacks.of(i))));
        return outputs;
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public int getTime() {
        return time;
    }

    public int getInputFluidCount() {
        return inputFluidCount;
    }

    public int getOutputFluidCount() {
        return outputFluidCount;
    }

    public List<Integer> getInputFluidAmounts() {
        return inputFluidAmounts;
    }

    public List<Integer> getOutputFluidAmounts() {
        return outputFluidAmounts;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TemperedJarCategory.ID;
    }
}
