package dev.ftb.mods.ftbstuffnthings.integration.rei;

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

public class FusingMachineCategory extends BaseReiCategory<FusingMachineDisplay> {
    public static final CategoryIdentifier<FusingMachineDisplay> ID = CategoryIdentifier.of("ftbstuff", "fusing_machine");

    public FusingMachineCategory() {
        super(ID, Component.translatable("block.ftbstuff.fusing_machine"), EntryStacks.of(ItemsRegistry.FUSING_MACHINE.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 28;
    }

    @Override
    public int getDisplayWidth(FusingMachineDisplay display) {
        return 106;
    }

    @Override
    public List<Widget> setupDisplay(FusingMachineDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_fusing_machine.png", 106, 28, 134, 28));
        widgets.add(textureRegion(bounds, 6, 6, 106, 0, 6, 16, "jei_fusing_machine.png", 134, 28));
        widgets.add(textureRegion(bounds, 57, 6, 112, 0, 22, 16, "jei_fusing_machine.png", 134, 28));

        var inputs = display.getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 18 + i * 18, bounds.getMinY() + 6))
                    .entries(inputs.get(i)).markInput().disableBackground());
        }
        var output = display.getOutputEntries().get(0);
        for (var e : output) {
            e.tooltip(Component.literal(display.getFluidAmount() + " mB"));
        }
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 84, bounds.getMinY() + 6))
                .entries(output).markOutput().disableBackground());

        int totalEnergy = display.getFePerTick() * display.getTicks();
        widgets.add(scaledText(bounds, 5, 25, "%sFE/t (%sFE)".formatted(display.getFePerTick(), totalEnergy), 0x404040));
        widgets.add(scaledText(bounds, 83, 25, "%s ticks".formatted(display.getTicks()), 0x404040));
        return widgets;
    }
}
