package dev.ftb.mods.ftbstuffnthings.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

@FunctionalInterface
public interface IHideableRecipe {
    static <I extends Container, T extends Recipe<I>> boolean shouldShow(T t) {
        return !(t instanceof IHideableRecipe h) || h.shouldShowRecipe();
    }

    boolean shouldShowRecipe();
}
