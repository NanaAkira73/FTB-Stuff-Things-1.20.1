package dev.ftb.mods.ftbstuffnthings.integration.rei;

import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.network.chat.Component;

public abstract class BaseReiCategory<T extends Display> implements DisplayCategory<T> {
    protected final CategoryIdentifier<T> id;
    protected final Component title;
    protected final Renderer icon;

    protected BaseReiCategory(CategoryIdentifier<T> id, Component title, Renderer icon) {
        this.id = id;
        this.title = title;
        this.icon = icon;
    }

    @Override
    public CategoryIdentifier<? extends T> getCategoryIdentifier() {
        return id;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public Renderer getIcon() {
        return icon;
    }
}
