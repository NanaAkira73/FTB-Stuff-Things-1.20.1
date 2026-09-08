package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class CrookDisplay extends BasicDisplay {
    public CrookDisplay(CrookRecipe recipe) {
        super(
                List.of(EntryIngredients.ofIngredient(recipe.getIngredient())),
                recipe.getResults().stream().map(r -> EntryIngredient.of(EntryStacks.of(r.item()))).toList()
        );
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CrookCategory.ID;
    }
}
