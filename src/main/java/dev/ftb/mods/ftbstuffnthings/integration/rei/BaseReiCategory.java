package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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

    /** Draws one of the mod's original JEI background textures, cropping the top-left (w,h) region of a (texW,texH) texture. */
    protected static Widget jeiBackground(Rectangle bounds, String textureName, int w, int h, int texW, int texH) {
        ResourceLocation tex = FTBStuffNThings.id("textures/gui/jei/" + textureName);
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) ->
                graphics.blit(tex, bounds.getMinX(), bounds.getMinY(), 0, 0, w, h, texW, texH)
        );
    }

    /** Draws an arbitrary region of a JEI texture at the given offset (used for power/progress bars). */
    protected static Widget textureRegion(Rectangle bounds, int x, int y, int u, int v, int w, int h, String textureName, int texW, int texH) {
        ResourceLocation tex = FTBStuffNThings.id("textures/gui/jei/" + textureName);
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) ->
                graphics.blit(tex, bounds.getMinX() + x, bounds.getMinY() + y, u, v, w, h, texW, texH)
        );
    }

    /** Draws a temperature icon (16x16 texture) at the given slot position, like JEI's TemperatureRenderer. */
    protected static Widget temperatureIcon(Rectangle bounds, int x, int y, Temperature temperature) {
        ResourceLocation tex = temperature.getTexture();
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) ->
                graphics.blit(tex, bounds.getMinX() + x, bounds.getMinY() + y, 0, 0, 16, 16, 16, 16)
        );
    }

    /** Draws a fluid amount label in the bottom-right of a 16x16 slot, like JEI's FluidAmountDrawable. */
    protected static Widget fluidAmountText(Rectangle bounds, int slotX, int slotY, int amount) {
        final String txt = amount >= 1000 ? amount / 1000.0 + "B" : amount + "mB";
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            Font font = Minecraft.getInstance().font;
            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(bounds.getMinX() + slotX + 16 - font.width(txt) / 2f, bounds.getMinY() + slotY + 16 - font.lineHeight / 2f, 0);
            pose.scale(.5F, .5F, .5F);
            graphics.drawString(font, txt, 0, 0, 0xFFFFFFFF);
            pose.popPose();
        });
    }

    /** Draws a scaled (0.5x) centered text label, like the chance labels JEI draws under output slots. */
    protected static Widget scaledCenteredText(Rectangle bounds, float x, float y, String text, int color) {
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(bounds.getMinX() + x, bounds.getMinY() + y, 100);
            pose.scale(.5F, .5F, 1F);
            graphics.drawCenteredString(Minecraft.getInstance().font, text, 0, 0, color);
            pose.popPose();
        });
    }

    /** Draws a scaled (0.5x) left-aligned text label, like the FE/tick labels JEI draws under machine slots. */
    protected static Widget scaledText(Rectangle bounds, float x, float y, String text, int color) {
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(bounds.getMinX() + x, bounds.getMinY() + y, 100);
            pose.scale(.5F, .5F, 1F);
            graphics.drawString(Minecraft.getInstance().font, text, 0, 0, color);
            pose.popPose();
        });
    }
}
