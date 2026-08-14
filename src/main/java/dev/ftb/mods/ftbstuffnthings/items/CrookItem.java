package dev.ftb.mods.ftbstuffnthings.items;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.ToolAction;

public class CrookItem extends DiggerItem {
    public CrookItem(Tier tier, Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility) {
        return ItemAbilities.DEFAULT_SHOVEL_ACTIONS.contains(itemAbility)
                || ItemAbilities.DEFAULT_HOE_ACTIONS.contains(itemAbility);
    }
}
