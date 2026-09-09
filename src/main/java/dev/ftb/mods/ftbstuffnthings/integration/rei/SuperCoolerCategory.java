package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SuperCoolerCategory extends BaseReiCategory<SuperCoolerDisplay> {
    public static final CategoryIdentifier<SuperCoolerDisplay> ID = CategoryIdentifier.of("ftbstuff", "super_cooler");

    public SuperCoolerCategory() {
        super(ID, Component.translatable("block.ftbstuff.super_cooler"), EntryStacks.of(new ItemStack(ItemsRegistry.SUPER_COOLER.get())));
    }

    @Override
    public int getDisplayHeight() {
        return 28;
    }

    @Override
    public int getDisplayWidth(SuperCoolerDisplay display) {
        return 146;
    }

    @Override
    public List<Widget> setupDisplay(SuperCoolerDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_super_cooler.png", 146, 28, 174, 28));
        widgets.add(textureRegion(bounds, 6, 6, 146, 0, 6, 16, "jei_super_cooler.png", 174, 28));
        widgets.add(textureRegion(bounds, 97, 6, 152, 0, 22, 16, "jei_super_cooler.png", 174, 28));

        var inputs = display.getInputEntries();
        // input 0 = fluid
        if (!inputs.isEmpty()) {
            var fluidInput = inputs.get(0);
            for (var e : fluidInput) {
                e.tooltip(Component.literal(display.getFluidAmount() + " mB"));
            }
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 18, bounds.getMinY() + 6))
                    .entries(fluidInput).markInput().disableBackground());
        }
        // inputs 1..n = items
        for (int i = 1; i < inputs.size(); i++) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 40 + (i - 1) * 18, bounds.getMinY() + 6))
                    .entries(inputs.get(i)).markInput().disableBackground());
        }
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 124, bounds.getMinY() + 6))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());

        int totalEnergy = display.getFePerTick() * display.getTicks();
        widgets.add(scaledText(bounds, 5, 25, "%sFE/t (%sFE)".formatted(display.getFePerTick(), totalEnergy), 0xBEFFFFFF));
        widgets.add(scaledText(bounds, 96, 25, "%s ticks".formatted(display.getTicks()), 0xBEFFFFFF));
        return widgets;
    }
}
