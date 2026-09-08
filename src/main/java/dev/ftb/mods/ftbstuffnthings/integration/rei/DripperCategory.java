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
        // 第一个是流体，其余是输入方块
        if (!inputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 3, bounds.getMinY() + 7)).entries(List.of(inputs.get(0))).markInput());
        }
        for (int i = 1; i < inputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 23, bounds.getMinY() + 7)).entries(List.of(inputs.get(i))).markInput());
        }
        var outputs = display.getOutputEntries();
        if (!outputs.isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 68, bounds.getMinY() + 7)).entries(outputs).markOutput());
        }
        return widgets;
    }
}
