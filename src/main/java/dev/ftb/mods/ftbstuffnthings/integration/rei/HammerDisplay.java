package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.HammerRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class HammerDisplay extends BasicDisplay {
    public HammerDisplay(HammerRecipe recipe) {
        super(
                EntryIngredients.ofIngredient(recipe.getIngredient()),
                recipe.getResults().stream().map(EntryStacks::of).toList()
        );
    }
}
