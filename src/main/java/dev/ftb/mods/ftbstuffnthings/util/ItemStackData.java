package dev.ftb.mods.ftbstuffnthings.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to replace NeoForge data components with NBT-based storage
 * for Forge 1.20.1 compatibility.
 */
public class ItemStackData {
    // Fluid storage
    public static FluidStack getStoredFluid(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Fluid")) {
            return FluidStack.loadFluidStackFromNBT(tag.getCompound("Fluid"));
        }
        return FluidStack.EMPTY;
    }

    public static void setStoredFluid(ItemStack stack, FluidStack fluid) {
        if (fluid.isEmpty()) {
            if (stack.hasTag()) stack.getTag().remove("Fluid");
        } else {
            stack.getOrCreateTag().put("Fluid", fluid.writeToNBT(new CompoundTag()));
        }
    }

    // Energy storage
    public static int getStoredEnergy(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("Energy")) {
            return tag.getInt("Energy");
        }
        return 0;
    }

    public static void setStoredEnergy(ItemStack stack, int energy) {
        if (energy <= 0) {
            if (stack.hasTag()) stack.getTag().remove("Energy");
        } else {
            stack.getOrCreateTag().putInt("Energy", energy);
        }
    }

    // Fluid tanks list storage (for TemperedJar)
    public static List<FluidStack> getFluidTanks(ItemStack stack) {
        List<FluidStack> result = new ArrayList<>();
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("FluidTanks")) {
            ListTag list = tag.getList("FluidTanks", 10);
            for (int i = 0; i < list.size(); i++) {
                result.add(FluidStack.loadFluidStackFromNBT(list.getCompound(i)));
            }
        }
        return result;
    }

    public static void setFluidTanks(ItemStack stack, List<FluidStack> tanks) {
        if (tanks.isEmpty()) {
            if (stack.hasTag()) stack.getTag().remove("FluidTanks");
        } else {
            ListTag list = new ListTag();
            for (FluidStack fluid : tanks) {
                list.add(fluid.writeToNBT(new CompoundTag()));
            }
            stack.getOrCreateTag().put("FluidTanks", list);
        }
    }
}