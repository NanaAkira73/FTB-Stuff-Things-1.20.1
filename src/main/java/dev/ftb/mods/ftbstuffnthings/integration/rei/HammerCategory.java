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

public class HammerCategory extends BaseReiCategory<HammerDisplay> {
    public static final CategoryIdentifier<HammerDisplay> ID = CategoryIdentifier.of("ftbstuff", "hammer");

    public HammerCategory() {
        super(ID, Component.translatable("item.ftbstuff.stone_hammer"), EntryStacks.of(ItemsRegistry.STONE_HAMMER.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 62;
    }

    @Override
    public int getDisplayWidth(HammerDisplay display) {
        return 156;
    }

    @Override
    public List<Widget> setupDisplay(HammerDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_hammer.png", 156, 62, 180, 62));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5, bounds.getMinY() + 5))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());
        var outputs = display.getOutputEntries();
        for (int i = 0; i < outputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 28 + (i % 7) * 18, bounds.getMinY() + 5 + (i / 7) * 18))
                    .entries(outputs.get(i)).markOutput().disableBackground());
        }
        return widgets;
    }
}
