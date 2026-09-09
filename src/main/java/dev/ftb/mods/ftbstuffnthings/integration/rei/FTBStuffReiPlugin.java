package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.DripperRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.HammerRecipe;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;

@me.shedaniel.rei.forge.REIPluginClient
public class FTBStuffReiPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HammerCategory());
        registry.add(new CrookCategory());
        registry.add(new DripperCategory());

        for (var item : ItemsRegistry.ALL_HAMMERS) {
            registry.addWorkstations(HammerCategory.ID, EntryStacks.of(item.get()));
        }
        for (var block : BlocksRegistry.ALL_AUTO_HAMMERS) {
            registry.addWorkstations(HammerCategory.ID, EntryStacks.of(block.get()));
        }
        registry.addWorkstations(CrookCategory.ID, EntryStacks.of(ItemsRegistry.CROOK.get()));
        registry.addWorkstations(DripperCategory.ID, EntryStacks.of(ItemsRegistry.DRIPPER.get()));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(HammerRecipe.class, HammerDisplay::new);
        registry.registerFiller(CrookRecipe.class, CrookDisplay::new);
        registry.registerFiller(DripperRecipe.class, DripperDisplay::new);
    }
}
