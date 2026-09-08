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

public class CrookCategory extends BaseReiCategory<CrookDisplay> {
    public static final CategoryIdentifier<CrookDisplay> ID = CategoryIdentifier.of("ftbstuff", "crook");

    public CrookCategory() {
        super(ID, Component.translatable("item.ftbstuff.stone_crook"), EntryStacks.of(ItemsRegistry.CROOK.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 78;
    }

    @Override
    public int getDisplayWidth(CrookDisplay display) {
        return 156;
    }

    @Override
    public List<Widget> setupDisplay(CrookDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        var inputs = display.getInputEntries();
        for (int i = 0; i < inputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5 + i * 18, bounds.getMinY() + 5)).entries(inputs.get(i)).markInput());
        }
        var outputs = display.getOutputEntries();
        for (int i = 0; i < outputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 28 + (i % 7) * 18, bounds.getMinY() + 5 + (i / 7) * 24)).entries(outputs.get(i)).markOutput());
        }
        return widgets;
    }
}
