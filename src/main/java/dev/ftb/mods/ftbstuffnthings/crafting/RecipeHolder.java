package dev.ftb.mods.ftbstuffnthings.crafting;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Simple wrapper for a Recipe and its ResourceLocation ID.
 * In 1.20.1, the vanilla RecipeHolder class doesn't exist (it was added in 1.20.2),
 * so we provide our own equivalent.
 */
public record RecipeHolder<T extends Recipe<?>>(ResourceLocation id, T value) {
}