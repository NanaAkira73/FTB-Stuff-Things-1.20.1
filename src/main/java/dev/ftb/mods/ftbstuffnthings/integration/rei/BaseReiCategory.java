package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

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

    /**
     * Draws one of the mod's original JEI background textures (assets/ftbstuff/textures/gui/jei/*.png)
     * at the given bounds, cropping the top-left {@code (w, h)} region out of a {@code (texW, texH)}
     * texture. This mirrors what the JEI categories did, so the REI display reuses the built-in UI.
     */
    protected static Widget jeiBackground(Rectangle bounds, String textureName, int w, int h, int texW, int texH) {
        ResourceLocation tex = FTBStuffNThings.id("textures/gui/jei/" + textureName);
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) ->
                graphics.blit(tex, bounds.getMinX(), bounds.getMinY(), 0, 0, w, h, texW, texH)
        );
    }
}
