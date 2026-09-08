package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

public class CrookDisplay extends BasicDisplay {
    public CrookDisplay(CrookRecipe recipe) {
        super(
                EntryIngredients.ofIngredient(recipe.getIngredient()),
                recipe.getResults().stream().map(r -> EntryStacks.of(r.item())).toList()
        );
    }
}
