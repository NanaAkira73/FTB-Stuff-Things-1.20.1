package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WoodenBasinCategory extends BaseReiCategory<WoodenBasinDisplay> {
    public static final CategoryIdentifier<WoodenBasinDisplay> ID = CategoryIdentifier.of("ftbstuff", "wooden_basin");

    public WoodenBasinCategory() {
        super(ID, Component.translatable(BlocksRegistry.WOODEN_BASIN.get().getDescriptionId()), EntryStacks.of(ItemsRegistry.WOODEN_BASIN.get()));
    }

    @Override
    public int getDisplayHeight() {
        return 64;
    }

    @Override
    public int getDisplayWidth(WoodenBasinDisplay display) {
        return 64;
    }

    @Override
    public List<Widget> setupDisplay(WoodenBasinDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "jei_wooden_basin.png", 64, 64, 64, 64));

        if (!display.getInputEntries().isEmpty()) {
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 6, bounds.getMinY() + 25))
                    .entries(display.getInputEntries().get(0)).markInput().disableBackground());
        }

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 43, bounds.getMinY() + 43))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());
        widgets.add(fluidAmountText(bounds, 43, 43, display.getFluidAmount()));

        widgets.add(Widgets.createDrawableWidget((graphics, mx, my, delta) ->
                graphics.renderItem(new ItemStack(ItemsRegistry.WOODEN_BASIN.get()), bounds.getMinX() + 6, bounds.getMinY() + 43)));

        Widget infoWidget = Widgets.createDrawableWidget((graphics, mx, my, delta) ->
                graphics.blit(new ResourceLocation("ftblibrary", "textures/icons/info.png"), bounds.getMinX() + 42, bounds.getMinY() + 5, 0, 0, 16, 16, 16, 16));
        widgets.add(Widgets.withTooltip(Widgets.withBounds(infoWidget, new Rectangle(bounds.getMinX() + 42, bounds.getMinY() + 5, 16, 16)),
                Component.translatable("ftbstuff.jei.wooden_basin_info"),
                Component.empty(),
                Component.translatable("ftbstuff.wooden_basin.produce_chance", (int) (display.getProductionChance() * 100)).withStyle(ChatFormatting.GRAY),
                Component.translatable("ftbstuff.wooden_basin.consume_chance", (int) (display.getBlockConsumeChance() * 100)).withStyle(ChatFormatting.GRAY)));

        return widgets;
    }
}
