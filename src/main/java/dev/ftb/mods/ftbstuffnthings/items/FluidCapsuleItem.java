package dev.ftb.mods.ftbstuffnthings.items;

import dev.ftb.mods.ftbstuffnthings.util.ItemStackData;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import dev.ftb.mods.ftbstuffnthings.util.MiscUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class FluidCapsuleItem extends Item {
    public FluidCapsuleItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static ItemStack of(FluidStack fluidStack) {
        ItemStack stack = new ItemStack(ItemsRegistry.FLUID_CAPSULE.get());
        ItemStackData.setStoredFluid(stack, fluidStack.copy());
        return stack;
    }

    public static FluidStack getFluid(ItemStack stack) {
        return ItemStackData.getStoredFluid(stack).copy();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, tooltipComponents, tooltipFlag);

        FluidStack content = ItemStackData.getStoredFluid(stack);
        if (!content.isEmpty()) {
            tooltipComponents.add(MiscUtil.makeFluidStackDesc(content.copy()));
        }
    }

    public static class FluidHandler extends FluidHandlerItemStack {
        public FluidHandler(ItemStack container) {
            super(container, FluidType.BUCKET_VOLUME);
        }

        @Override
        public int fill(FluidStack resource, FluidAction doFill) {
            // only allow filling if it's completely empty
            return getFluid().isEmpty() ? super.fill(resource, doFill) : 0;
        }

        @Override
        public ItemStack getContainer() {
            return ItemStack.EMPTY; // container is consumed
        }
    }
}
