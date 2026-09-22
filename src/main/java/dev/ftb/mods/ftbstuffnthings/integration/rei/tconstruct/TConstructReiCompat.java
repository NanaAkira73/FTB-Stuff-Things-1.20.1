package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.library.recipe.casting.IDisplayableCastingRecipe;

/**
 * Native REI integration for Tinkers' Construct casting recipes.
 * TConstruct 1.20.1 only ships a JEI plugin, so this adds the equivalent REI categories.
 */
public final class TConstructReiCompat {
    private TConstructReiCompat() {
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new CastingTableCategory());
        registry.add(new CastingBasinCategory());

        Item table = workstationItem(false);
        if (table != null) {
            registry.addWorkstations(CastingTableCategory.ID, EntryStacks.of(table));
        }
        Item basin = workstationItem(true);
        if (basin != null) {
            registry.addWorkstations(CastingBasinCategory.ID, EntryStacks.of(basin));
        }
    }

    public static void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(IDisplayableCastingRecipe.class, CastingDisplay::isCasting, CastingDisplay::new);
    }

    /** The item used as the category icon / workstation (tconstruct:seared_table / tconstruct:seared_basin). */
    public static Item workstationItem(boolean basin) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation("tconstruct", basin ? "seared_basin" : "seared_table"));
    }
}
