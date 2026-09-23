package dev.ftb.mods.ftbstuffnthings.integration.rei;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

/**
 * REI category mirroring the original FTB StoneBlock Companion JEI cauldron category, using the
 * mod's own cauldron.png background: input item -&gt; cauldron -&gt; output fluid.
 */
public class CauldronCategory extends BaseReiCategory<CauldronDisplay> {
    public static final CategoryIdentifier<CauldronDisplay> ID = CategoryIdentifier.of("ftbstuff", "cauldron");

    public CauldronCategory() {
        super(ID, Component.translatable("block.minecraft.cauldron"), EntryStacks.of(Items.CAULDRON));
    }

    @Override
    public int getDisplayHeight() {
        return 30;
    }

    @Override
    public int getDisplayWidth(CauldronDisplay display) {
        return 112;
    }

    @Override
    public List<Widget> setupDisplay(CauldronDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(jeiBackground(bounds, "cauldron.png", 112, 30, 128, 64));

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 3, bounds.getMinY() + 7))
                .entries(display.getInputEntries().get(0)).markInput().disableBackground());

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 48, bounds.getMinY() + 7))
                .entries(display.getInputEntries().get(1)).markInput().disableBackground());

        widgets.add(Widgets.createSlot(new Point(bounds.getMinX() + 93, bounds.getMinY() + 7))
                .entries(display.getOutputEntries().get(0)).markOutput().disableBackground());

        return widgets;
    }
}
