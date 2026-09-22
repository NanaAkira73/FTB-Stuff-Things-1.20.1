package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;

public class CastingTableCategory extends AbstractCastingCategory {
    public static final CategoryIdentifier<CastingDisplay> ID = CategoryIdentifier.of("tconstruct", "casting_table");

    public CastingTableCategory() {
        super(ID, Component.translatable("jei.tconstruct.casting.table"), false);
    }
}
