package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class SluiceCategory extends BaseReiCategory<SluiceDisplay> {
    public static final CategoryIdentifier<SluiceDisplay> ID = CategoryIdentifier.of("ftbstuff", "sluice");

    public SluiceCategory() {
        super(ID, Component.translatable("ftbstuff.sluice"), EntryStacks.of(ItemsRegistry.OAK_SLUICE.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 78;
    }

    @Override
    public int getDisplayWidth(SluiceDisplay display) {
        return 156;
    }

    @Override
    public List<Widget> setupDisplay(SluiceDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_sluice.png", 156, 78, 180, 78));

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5, bounds.getMinY() + 5))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5, bounds.getMinY() + 24))
                .entries(EntryIngredient.of(display.getMeshStacks().stream().map(EntryStacks::of).toList()))
                .markInput().disableBackground());

        if (display.getInputEntries().size() > 1) {
            var fluidInput = display.getInputEntries().get(1);
            for (var e : fluidInput) {
                e.tooltip(Component.translatable("ftbstuff.fluid_usage",
                        Component.literal(display.getFluidAmount() + "").withStyle(ChatFormatting.YELLOW)).withStyle(ChatFormatting.GRAY));
            }
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5, bounds.getMinY() + 42))
                    .entries(fluidInput).markInput().disableBackground());
        }

        List<ItemWithChance> outputs = display.getResults();
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % 7;
            int row = i / 7;
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 28 + col * 18, bounds.getMinY() + 5 + row * 24))
                    .entries(display.getOutputEntries().get(i)).markOutput().disableBackground());
            widgets.add(scaledCenteredText(bounds, 36 + col * 18, 23.5f + row * 24, Math.round(outputs.get(i).chance() * 100) + "%", 0xFFFFFF));
        }
        return widgets;
    }
}
