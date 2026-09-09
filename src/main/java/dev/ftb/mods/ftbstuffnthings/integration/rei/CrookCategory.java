package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
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
        widgets.add(jeiBackground(bounds, "jei_crook.png", 156, 78, 180, 78));
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 5, bounds.getMinY() + 5))
                .entries(display.getInputEntries().get(0)).markInput());

        List<ItemWithChance> outputs = display.getResults();
        for (int i = 0; i < outputs.size(); i++) {
            int col = i % 7;
            int row = i / 7;
            widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 28 + col * 18, bounds.getMinY() + 5 + row * 24))
                    .entries(display.getOutputEntries().get(i)).markOutput());

            // chance label, drawn scaled 0.5 under the item — matches the original JEI category
            final float tx = bounds.getMinX() + 36 + col * 18;
            final float ty = bounds.getMinY() + 23.5f + row * 24;
            final String pct = Math.round(outputs.get(i).chance() * 100) + "%";
            widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
                var pose = graphics.pose();
                pose.pushPose();
                pose.translate(tx, ty, 100);
                pose.scale(.5F, .5F, 1F);
                graphics.drawCenteredString(Minecraft.getInstance().font, pct, 0, 0, 0xFFFFFF);
                pose.popPose();
            }));
        }
        return widgets;
    }
}
