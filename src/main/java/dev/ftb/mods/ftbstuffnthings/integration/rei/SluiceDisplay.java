package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SluiceRecipe;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import net.minecraft.world.item.ItemStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import me.shedaniel.rei.api.common.util.EntryStacks;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SluiceDisplay extends BasicDisplay {
    private static final Comparator<ItemWithChance> COMPARATOR = (a, b) -> (int) ((b.chance() * 100) - (a.chance() * 100));

    private final List<ItemWithChance> results;
    private final List<ItemStack> meshStacks;
    private final int fluidAmount;

    public SluiceDisplay(SluiceRecipe recipe) {
        super(buildInputs(recipe), recipe.getResults().stream().sorted(COMPARATOR)
                .map(r -> EntryIngredient.of(EntryStacks.of(r.item()))).toList());
        this.results = recipe.getResults().stream().sorted(COMPARATOR).toList();
        this.meshStacks = recipe.getMeshTypes().stream().map(MeshType::getItemStack).toList();
        this.fluidAmount = recipe.getFluid().map(f -> f.amount()).orElse(0);
    }

    private static List<EntryIngredient> buildInputs(SluiceRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(EntryIngredients.ofIngredient(recipe.getIngredient()));
        recipe.getFluid().ifPresent(f -> inputs.add(EntryIngredient.of(f.getStacks().stream()
                .map(s -> EntryStacks.of(FluidStack.create(s.getFluid(), s.getAmount()))).toList())));
        return inputs;
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    public List<ItemStack> getMeshStacks() {
        return meshStacks;
    }

    public int getFluidAmount() {
        return fluidAmount;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return SluiceCategory.ID;
    }
}
