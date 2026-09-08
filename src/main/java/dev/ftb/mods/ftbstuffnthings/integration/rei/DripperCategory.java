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

public class DripperCategory extends BaseReiCategory<DripperDisplay> {
    public static final CategoryIdentifier<DripperDisplay> ID = CategoryIdentifier.of("ftbstuff", "dripper");

    public DripperCategory() {
        super(ID, Component.translatable("block.ftbstuff.dripper"), EntryStacks.of(ItemsRegistry.DRIPPER.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 30;
    }

    @Override
    public int getDisplayWidth(DripperDisplay display) {
        return 91;
    }

    @Override
    public List<Widget> setupDisplay(DripperDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        var inputs = display.getInputEntries();
        // 0 = fluid, 1+ = input blocks
        for (int i = 0; i < inputs.size(); i++) {
            int x = i == 0 ? bounds.getMinX() + 3 : bounds.getMinX() + 23;
            widgets.add(Widgets.createSlot(new Point(x, bounds.getMinY() + 7)).entries(inputs.get(i)).markInput());
        }
        var outputs = display.getOutputEntries();
        for (var output : outputs) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 68, bounds.getMinY() + 7)).entries(output).markOutput());
        }
        return widgets;
    }
}
