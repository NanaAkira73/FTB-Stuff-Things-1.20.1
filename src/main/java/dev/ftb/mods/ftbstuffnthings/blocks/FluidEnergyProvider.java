package dev.ftb.mods.ftbstuffnthings.blocks;


// REMOVED: already imported

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
