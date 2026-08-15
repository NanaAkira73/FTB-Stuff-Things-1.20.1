package dev.ftb.mods.ftbstuffnthings.blocks.tube;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public interface ITubeConnectable {
    static boolean canConnect(Level level, BlockPos pos, Direction face) {
        BlockEntity be = level.getBlockEntity(pos);
        return be != null && (be.getCapability(ForgeCapabilities.ITEM_HANDLER, face).isPresent()
                || be.getCapability(ForgeCapabilities.FLUID_HANDLER, face).isPresent())
                || level.getBlockState(pos).getBlock() instanceof ITubeConnectable c && c.isSideTubeConnectable(face)
                || be instanceof ITubeConnectable c1 && c1.isSideTubeConnectable(face);
    }

    boolean isSideTubeConnectable(Direction side);
}
