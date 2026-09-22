package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import dev.ftb.mods.ftbstuffnthings.integration.rei.BaseReiCategory;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared REI category for Tinkers' Construct casting (table & basin).
 * Layout and texture are ported from TConstruct's own JEI casting category
 * (tconstruct:textures/gui/jei/casting.png, 117x54).
 */
public abstract class AbstractCastingCategory extends BaseReiCategory<CastingDisplay> {
    private static final ResourceLocation CASTING_TEX = new ResourceLocation("tconstruct", "textures/gui/jei/casting.png");
    private static final int TEX = 256;

    private final boolean basin;

    protected AbstractCastingCategory(CategoryIdentifier<CastingDisplay> id, Component title, boolean basin) {
        super(id, title, EntryStacks.of(TConstructReiCompat.workstationItem(basin)));
        this.basin = basin;
    }

    @Override
    public int getDisplayHeight() {
        return 54;
    }

    @Override
    public int getDisplayWidth(CastingDisplay display) {
        return 117;
    }

    @Override
    public List<Widget> setupDisplay(CastingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(tex(bounds, 0, 0, 0, 0, 117, 54));

        var fluid = display.getInputEntries().get(0);

        // output item slot (93, 18)
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 93, bounds.getMinY() + 18))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());

        // cast slot (38, 19) — only when the recipe has a cast
        if (display.hasCast() && display.getInputEntries().size() > 1) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 38, bounds.getMinY() + 19))
                    .entries(display.getInputEntries().get(1)).markInput().disableBackground());
        }

        // fluid tank (3, 3) rendered 32x32 with the tank overlay on top
        Slot tank = Widgets.createSlot(new Point(bounds.getMinX() + 3, bounds.getMinY() + 3))
                .entries(fluid).markInput().disableBackground();
        tank.getBounds().setSize(32, 32);
        widgets.add(tank);
        widgets.add(tex(bounds, 3, 3, 133, 0, 32, 32));

        // faucet fluid indicator (43, 8)
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 43, bounds.getMinY() + 8))
                .entries(fluid).disableBackground());

        // casting block icon (38, 35)
        widgets.add(tex(bounds, 38, 35, 117, basin ? 16 : 0, 16, 16));

        // progress arrow (58, 18)
        widgets.add(tex(bounds, 58, 18, 117, 32, 24, 17));

        // cast kept / consumed indicator (63, 39)
        if (display.hasCast()) {
            widgets.add(tex(bounds, 63, 39, 141, display.consumed() ? 32 : 43, 13, 11));
        }

        // cooling time text (centred inside the 89-wide area starting at x=28, y=2)
        widgets.add(scaledCenteredText(bounds, 72.5f, 2f,
                Component.translatable("jei.tconstruct.time", display.coolingTime() / 20).getString(), 0xFF808080));

        return widgets;
    }

    private static Widget tex(Rectangle bounds, int x, int y, int u, int v, int w, int h) {
        return Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) ->
                graphics.blit(CASTING_TEX, bounds.getMinX() + x, bounds.getMinY() + y, u, v, w, h, TEX, TEX));
    }
}
