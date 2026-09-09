package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class TemperedJarCategory extends BaseReiCategory<TemperedJarDisplay> {
    public static final CategoryIdentifier<TemperedJarDisplay> ID = CategoryIdentifier.of("ftbstuff", "jar");

    public TemperedJarCategory() {
        super(ID, Component.translatable(BlocksRegistry.TEMPERED_JAR.get().getDescriptionId()), EntryStacks.of(ItemsRegistry.TEMPERED_JAR.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 18;
    }

    @Override
    public int getDisplayWidth(TemperedJarDisplay display) {
        return 150;
    }

    @Override
    public List<Widget> setupDisplay(TemperedJarDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_tempered_jar.png", 150, 18, 256, 32));

        int nFluidsIn = display.getInputFluidCount();
        var inputs = display.getInputEntries();
        for (int i = 0; i < nFluidsIn; i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 1 + i * 20, bounds.getMinY() + 1))
                    .entries(inputs.get(i)).markInput().disableBackground());
            widgets.add(fluidAmountText(bounds, 1 + i * 20, 1, display.getInputFluidAmounts().get(i)));
        }
        for (int i = 0; i < inputs.size() - nFluidsIn; i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 1 + (i + nFluidsIn) * 20, bounds.getMinY() + 1))
                    .entries(inputs.get(nFluidsIn + i)).markInput().disableBackground());
        }

        String time = String.format("%.1f", display.getTime() / 20f);
        Widget tempWidget = temperatureIcon(bounds, 67, 1, display.getTemperature());
        widgets.add(Widgets.withTooltip(Widgets.withBounds(tempWidget, new Rectangle(bounds.getMinX() + 67, bounds.getMinY() + 1, 16, 16)),
                Component.translatable("ftbstuff.processing_time", time)));

        int nFluidsOut = display.getOutputFluidCount();
        var outputs = display.getOutputEntries();
        for (int i = 0; i < nFluidsOut; i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 93 + i * 20, bounds.getMinY() + 1))
                    .entries(outputs.get(i)).markOutput().disableBackground());
            widgets.add(fluidAmountText(bounds, 93 + i * 20, 1, display.getOutputFluidAmounts().get(i)));
        }
        for (int i = 0; i < outputs.size() - nFluidsOut; i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 93 + (i + nFluidsOut) * 20, bounds.getMinY() + 1))
                    .entries(outputs.get(nFluidsOut + i)).markOutput().disableBackground());
        }
        return widgets;
    }
}
