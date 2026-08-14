package dev.ftb.mods.ftbstuffnthings.blocks.tube;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

public interface ITubeConnectable {
    static boolean canConnect(Level level, BlockPos pos, Direction face) {
        return level.getCapability(ForgeCapabilities.ITEM_HANDLER, pos, face).isPresent()
                || level.getCapability(ForgeCapabilities.FLUID_HANDLER, pos, face).isPresent()
                || level.getBlockState(pos).getBlock() instanceof ITubeConnectable c && c.isSideTubeConnectable(face)
                || level.getBlockEntity(pos) instanceof ITubeConnectable c1 && c1.isSideTubeConnectable(face);
    }

    boolean isSideTubeConnectable(Direction side);
}
