package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * REI display for the vanilla cauldron "leaf/sapling to water" interaction. This is not a real
 * recipe type, so the displays are built directly from the block registry.
 */
public class CauldronDisplay extends BasicDisplay {
    public CauldronDisplay(List<ItemStack> inputs, FluidStack output) {
        super(buildInputs(inputs), buildOutputs(output));
    }

    private static List<EntryIngredient> buildInputs(List<ItemStack> inputs) {
        List<EntryStack<?>> stacks = new ArrayList<>(inputs.size());
        for (ItemStack stack : inputs) {
            stacks.add(EntryStacks.of(stack));
        }

        List<EntryIngredient> list = new ArrayList<>(2);
        list.add(EntryIngredient.of(stacks));
        list.add(EntryIngredient.of(EntryStacks.of(new ItemStack(Items.CAULDRON))));
        return list;
    }

    private static List<EntryIngredient> buildOutputs(FluidStack output) {
        List<EntryIngredient> list = new ArrayList<>(1);
        list.add(EntryIngredient.of(EntryStacks.of(output)));
        return list;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return CauldronCategory.ID;
    }
}
