package dev.ftb.mods.ftbstuffnthings.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class MiscUtil {
    public static NonNullList<ItemStack> getItemsInHandler(IItemHandler handler) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                items.add(stack);
            }
        }
        NonNullList<ItemStack> res = NonNullList.create();
        res.addAll(items);
        return res;
    }

    public static Component makeFluidStackDesc(FluidStack stack) {
        return Component.translatable("ftbstuff.tooltip.fluid", stack.getAmount(), stack.getDisplayName()).withStyle(ChatFormatting.AQUA);
    }

    public static int hashItemAndComponents(ItemStack stack) {
        int h = stack.getItem().hashCode();
        h = 31 * h + (stack.hasTag() ? stack.getTag().hashCode() : 0);
        return h;
    }

    public static int hashFluidAndComponents(FluidStack stack) {
        return stack.hashCode();
    }
}
