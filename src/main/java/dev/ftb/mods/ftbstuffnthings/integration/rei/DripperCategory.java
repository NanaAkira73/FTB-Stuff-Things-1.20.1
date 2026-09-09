package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
        widgets.add(jeiBackground(bounds, "jei_dripper.png", 91, 30, 128, 64));

        // output (right)
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 68, bounds.getMinY() + 7))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());

        // input block(s) (middle)
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 23, bounds.getMinY() + 7))
                .entries(display.getInputEntries().get(1)).markInput().disableBackground());

        // input fluid (left), with amount label overlaid like the original JEI FluidAmountDrawable
        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 3, bounds.getMinY() + 7))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());

        final int amount = display.getFluidAmount();
        final String txt = amount >= 1000 ? amount / 1000.0 + "B" : amount + "mB";
        widgets.add(Widgets.createDrawableWidget((graphics, mouseX, mouseY, delta) -> {
            Font font = Minecraft.getInstance().font;
            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(bounds.getMinX() + 3 + 16 - font.width(txt) / 2f, bounds.getMinY() + 7 + 16 - font.lineHeight / 2f, 0);
            pose.scale(.5F, .5F, .5F);
            graphics.drawString(font, txt, 0, 0, 0xFFFFFFFF);
            pose.popPose();
        }));

        return widgets;
    }
}
