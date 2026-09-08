package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.HammerRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class HammerDisplay extends BasicDisplay {
    public HammerDisplay(HammerRecipe recipe) {
        super(
                List.of(EntryIngredients.ofIngredient(recipe.getIngredient())),
                recipe.getResults().stream().map(s -> EntryIngredient.of(EntryStacks.of(s))).toList()
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return HammerCategory.ID;
    }
}
