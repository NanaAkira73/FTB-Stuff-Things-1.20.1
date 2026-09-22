package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import net.minecraftforge.fml.ModList;

/**
 * REI plugin entry point for Tinkers' Construct. Soft dependency: all TConstruct
 * referencing code lives in {@link TConstructReiCompat}, which is only touched when
 * TConstruct is actually loaded.
 */
@me.shedaniel.rei.forge.REIPluginClient
public class TConstructReiPlugin implements REIClientPlugin {
    private static boolean available() {
        return ModList.get().isLoaded("tconstruct");
    }

    @Override
    public void registerCategories(CategoryRegistry registry) {
        if (!available()) {
            return;
        }
        TConstructReiCompat.registerCategories(registry);
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        if (!available()) {
            return;
        }
        TConstructReiCompat.registerDisplays(registry);
    }
}
