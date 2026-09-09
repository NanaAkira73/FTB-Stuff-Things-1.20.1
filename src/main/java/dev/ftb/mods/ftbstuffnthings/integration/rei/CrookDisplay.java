package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.Comparator;
import java.util.List;

public class CrookDisplay extends BasicDisplay {
    private static final Comparator<ItemWithChance> COMPARATOR = (a, b) -> (int) ((b.chance() * 100) - (a.chance() * 100));

    private final List<ItemWithChance> results;

    public CrookDisplay(CrookRecipe recipe) {
        super(
                List.of(EntryIngredients.ofIngredient(recipe.getIngredient())),
                recipe.getResults().stream().sorted(COMPARATOR)
                        .map(r -> EntryIngredient.of(EntryStacks.of(r.item()))).toList()
        );
        this.results = recipe.getResults().stream().sorted(COMPARATOR).toList();
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CrookCategory.ID;
    }
}
