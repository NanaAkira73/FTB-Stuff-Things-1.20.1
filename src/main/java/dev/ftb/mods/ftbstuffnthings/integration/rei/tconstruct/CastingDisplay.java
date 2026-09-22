package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

import java.util.ArrayList;
import java.util.List;

/**
 * REI display for Tinkers' Construct casting recipes (casting table / casting basin).
 * Mirrors the layout of TConstruct's own JEI category.
 */
public class CastingDisplay extends BasicDisplay {
    private final boolean basin;
    private final boolean hasCast;
    private final boolean consumed;
    private final int coolingTime;

    public CastingDisplay(IDisplayableCastingRecipe recipe) {
        super(buildInputs(recipe), buildOutputs(recipe));
        this.basin = isBasin(recipe);
        this.hasCast = recipe.hasCast();
        this.consumed = recipe.isConsumed();
        this.coolingTime = recipe.getCoolingTime();
    }

    public boolean isBasin() {
        return basin;
    }

    public boolean hasCast() {
        return hasCast;
    }

    public boolean consumed() {
        return consumed;
    }

    public int coolingTime() {
        return coolingTime;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return basin ? CastingBasinCategory.ID : CastingTableCategory.ID;
    }

    private static ResourceLocation typeKey(IDisplayableCastingRecipe recipe) {
        if (recipe instanceof Recipe<?> r) {
            return BuiltInRegistries.RECIPE_TYPE.getKey(r.getType());
        }
        return null;
    }

    static boolean isCasting(IDisplayableCastingRecipe recipe) {
        ResourceLocation k = typeKey(recipe);
        return k != null && "tconstruct".equals(k.getNamespace())
                && ("casting_table".equals(k.getPath()) || "casting_basin".equals(k.getPath()));
    }

    static boolean isBasin(IDisplayableCastingRecipe recipe) {
        ResourceLocation k = typeKey(recipe);
        return k != null && "tconstruct".equals(k.getNamespace()) && "casting_basin".equals(k.getPath());
    }

    private static List<EntryIngredient> buildInputs(IDisplayableCastingRecipe recipe) {
        List<EntryIngredient> inputs = new ArrayList<>();
        inputs.add(fluidIngredient(recipe.getFluids()));
        List<ItemStack> casts = recipe.getCastItems();
        if (recipe.hasCast() && !casts.isEmpty()) {
            List<EntryStack<?>> list = new ArrayList<>();
            for (ItemStack s : casts) {
                if (!s.isEmpty()) {
                    list.add(EntryStacks.of(s));
                }
            }
            inputs.add(EntryIngredient.of(list));
        }
        return inputs;
    }

    private static List<EntryIngredient> buildOutputs(IDisplayableCastingRecipe recipe) {
        List<EntryStack<?>> list = new ArrayList<>();
        for (ItemStack s : recipe.getOutputs()) {
            if (!s.isEmpty()) {
                list.add(EntryStacks.of(s));
            }
        }
        return List.of(EntryIngredient.of(list));
    }

    static EntryIngredient fluidIngredient(List<net.minecraftforge.fluids.FluidStack> fluids) {
        List<EntryStack<?>> list = new ArrayList<>();
        for (net.minecraftforge.fluids.FluidStack fs : fluids) {
            list.add(EntryStacks.of(FluidStack.create(fs.getFluid(), fs.getAmount(), fs.getTag())));
        }
        return EntryIngredient.of(list);
    }
}
