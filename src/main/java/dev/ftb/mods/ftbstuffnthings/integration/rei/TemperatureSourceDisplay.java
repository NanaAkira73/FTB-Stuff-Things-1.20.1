package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import net.minecraft.world.item.ItemStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.List;

public class TemperatureSourceDisplay extends BasicDisplay {
    private final Temperature temperature;
    private final double efficiency;
    private final ItemStack displayStack;

    public TemperatureSourceDisplay(TemperatureSourceRecipe recipe) {
        super(
                recipe.getDisplayStack().isEmpty() ? List.of() : List.of(EntryIngredient.of(EntryStacks.of(recipe.getDisplayStack()))),
                List.of()
        );
        this.temperature = recipe.getTemperature();
        this.efficiency = recipe.getEfficiency();
        this.displayStack = recipe.getDisplayStack();
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public double getEfficiency() {
        return efficiency;
    }

    public ItemStack getDisplayStack() {
        return displayStack;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return TemperatureSourceCategory.ID;
    }
}
