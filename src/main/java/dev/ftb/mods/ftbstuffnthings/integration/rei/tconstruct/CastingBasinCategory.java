package dev.ftb.mods.ftbstuffnthings.integration.rei.tconstruct;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import net.minecraft.network.chat.Component;

public class CastingBasinCategory extends AbstractCastingCategory {
    public static final CategoryIdentifier<CastingDisplay> ID = CategoryIdentifier.of("tconstruct", "casting_basin");

    public CastingBasinCategory() {
        super(ID, Component.translatable("jei.tconstruct.casting.basin"), true);
    }
}
