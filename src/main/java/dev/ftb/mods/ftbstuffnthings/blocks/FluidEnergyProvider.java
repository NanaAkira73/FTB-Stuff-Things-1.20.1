package dev.ftb.mods.ftbstuffnthings.blocks;


import net.minecraftforge.fluids.FluidStack;

public interface FluidEnergyProvider {
    int getEnergy();
    int getMaxEnergy();

    FluidStack getFluid();

    void setFluid(FluidStack fluid);

    int getMaxFluid();

    void setEnergy(int energy);

    void setProgress(int progress);

    void setMaxProgress(int maxProgress);
}
