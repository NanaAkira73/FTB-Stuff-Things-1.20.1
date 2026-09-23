package dev.ftb.mods.ftbstuffnthings.cauldron;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Ported from the original FTB StoneBlock Companion ToolsMain.
 *
 * Using a leaf or sapling block on a cauldron fills it with water, one level per item: an empty
 * cauldron becomes a water cauldron, and a partially filled water cauldron gains one level until
 * it is full. This gives the player a renewable water source, exactly as in StoneBlock 3.
 */
public class CauldronWaterInteractions {
    /** Used on an empty cauldron: creates a level 1 water cauldron. */
    public static final CauldronInteraction FILL_FROM_LEAVES = (state, level, pos, player, hand, stack) -> {
        if (!level.isClientSide) {
            consume(level, pos, player, stack);
            level.setBlockAndUpdate(pos, Blocks.WATER_CAULDRON.defaultBlockState());
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    };

    /** Used on a water cauldron: adds one level, unless it is already full. */
    public static final CauldronInteraction ADD_WATER_FROM_LEAVES = (state, level, pos, player, hand, stack) -> {
        if (!level.isClientSide && state.getValue(LayeredCauldronBlock.LEVEL) != LayeredCauldronBlock.MAX_FILL_LEVEL) {
            consume(level, pos, player, stack);
            level.setBlockAndUpdate(pos, state.cycle(LayeredCauldronBlock.LEVEL));
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    };

    private static void consume(Level level, BlockPos pos, Player player, ItemStack stack) {
        Item item = stack.getItem();

        if (!player.getAbilities().instabuild) {
            stack.shrink(1);
        }

        player.awardStat(Stats.USE_CAULDRON);
        player.awardStat(Stats.ITEM_USED.get(item));
        level.playSound(null, pos, SoundEvents.BOTTLE_EMPTY, SoundSource.BLOCKS, 1.0F, 1.0F);
        level.gameEvent(null, GameEvent.FLUID_PLACE, pos);
    }

    /** Registers the interaction for every leaf and sapling block in the game. */
    public static void setup() {
        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof LeavesBlock || block instanceof SaplingBlock) {
                Item item = block.asItem();

                if (item != Items.AIR) {
                    CauldronInteraction.EMPTY.put(item, FILL_FROM_LEAVES);
                    CauldronInteraction.WATER.put(item, ADD_WATER_FROM_LEAVES);
                }
            }
        }
    }
}
