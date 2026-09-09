package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TemperatureSourceCategory extends BaseReiCategory<TemperatureSourceDisplay> {
    public static final CategoryIdentifier<TemperatureSourceDisplay> ID = CategoryIdentifier.of("ftbstuff", "temperature_source");

    public TemperatureSourceCategory() {
        super(ID, Component.translatable("ftbstuff.temperature_source"),
                (graphics, bounds, mouseX, mouseY, delta) -> graphics.blit(Temperature.HOT.getTexture(), bounds.getMinX(), bounds.getMinY(), 0, 0, 16, 16, 16, 16));
    }

    @Override
    public int getDisplayHeight() {
        return 30;
    }

    @Override
    public int getDisplayWidth(TemperatureSourceDisplay display) {
        return 71;
    }

    @Override
    public List<Widget> setupDisplay(TemperatureSourceDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_temperature_source.png", 71, 30, 128, 64));

        Widget tempWidget = temperatureIcon(bounds, 48, 7, display.getTemperature());
        widgets.add(Widgets.withTooltip(Widgets.withBounds(tempWidget, new Rectangle(bounds.getMinX() + 48, bounds.getMinY() + 7, 16, 16)),
                Component.translatable("ftbstuff.efficiency", Component.literal((int) (display.getEfficiency() * 100) + "%"))));

        if (!display.getDisplayStack().isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 3, bounds.getMinY() + 7))
                    .entries(display.getInputEntries().get(0)).markInput().disableBackground());
        }
        return widgets;
    }
}
