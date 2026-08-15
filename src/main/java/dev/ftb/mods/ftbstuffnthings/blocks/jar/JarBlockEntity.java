package dev.ftb.mods.ftbstuffnthings.blocks.jar;

import dev.ftb.mods.ftbstuffnthings.blocks.tube.ITubeConnectable;
import dev.ftb.mods.ftbstuffnthings.registry.BlockEntitiesRegistry;
import dev.ftb.mods.ftbstuffnthings.util.ItemStackData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class JarBlockEntity extends BlockEntity implements ITubeConnectable {
    private final FluidTank tank = new JarFluidTank();

    public JarBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntitiesRegistry.JAR.get(), blockPos, blockState);
    }

    @Override
    public boolean isSideTubeConnectable(Direction side) {
        return side == Direction.UP;
    }


    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", tank.writeToNBT(new CompoundTag()));
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.readFromNBT(tag.getCompound("Tank"));
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public IFluidHandler getFluidHandler() {
        return tank;
    }

    public FluidTank getTank() {
        return tank;
    }

    public int getComparatorSignal() {
        return tank.getFluidAmount() * 15 / tank.getCapacity();
    }

    public void onRightClick(Player player, InteractionHand hand, ItemStack item) {
        FluidUtil.interactWithFluidHandler(player, hand, tank);

        if (!level.isClientSide()) {
            if (tank.isEmpty()) {
                player.displayClientMessage(Component.translatable("ftblibrary.empty"), true);
            } else {
                player.displayClientMessage(Component.translatable("ftblibrary.mb", tank.getFluidAmount(), tank.getFluid().getDisplayName()), true);
            }
        }
    }

    private class JarFluidTank extends FluidTank {
        public JarFluidTank() {
            super(8000);
        }

        @Override
        protected void onContentsChanged() {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_ALL_IMMEDIATE);
        }
    }
}
