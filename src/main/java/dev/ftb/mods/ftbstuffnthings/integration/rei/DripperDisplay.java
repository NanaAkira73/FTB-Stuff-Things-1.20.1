package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.DripperRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.List;

public class DripperDisplay extends BasicDisplay {
    public DripperDisplay(DripperRecipe recipe) {
        super(buildInputs(recipe), buildOutputs(recipe));
    }

    private static List<EntryStack<?>> buildInputs(DripperRecipe recipe) {
        List<EntryStack<?>> inputs = new ArrayList<>();
        inputs.add(EntryStacks.of(recipe.getFluid()));
        for (var e : recipe.getInputsForDisplay()) {
            e.ifLeft(stack -> inputs.add(EntryStacks.of(stack)));
            e.ifRight(fluid -> inputs.add(EntryStacks.of(fluid)));
        }
        return inputs;
    }

    private static List<EntryStack<?>> buildOutputs(DripperRecipe recipe) {
        List<EntryStack<?>> outputs = new ArrayList<>();
        recipe.getOutputItemOrFluid()
                .ifLeft(stack -> outputs.add(EntryStacks.of(stack)))
                .ifRight(fluid -> outputs.add(EntryStacks.of(fluid)));
        return outputs;
    }
}
