package dev.ftb.mods.ftbstuffnthings.integration.rei;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.CrookRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.DripperRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.FusingMachineRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.HammerRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.JarRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SluiceRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.SuperCoolerRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.TemperatureSourceRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.WoodenBasinRecipe;
import dev.ftb.mods.ftbstuffnthings.registry.BlocksRegistry;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import dev.architectury.fluid.FluidStack;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@me.shedaniel.rei.forge.REIPluginClient
public class FTBStuffReiPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new HammerCategory());
        registry.add(new CrookCategory());
        registry.add(new DripperCategory());
        registry.add(new FusingMachineCategory());
        registry.add(new SluiceCategory());
        registry.add(new SuperCoolerCategory());
        registry.add(new TemperatureSourceCategory());
        registry.add(new TemperedJarCategory());
        registry.add(new WoodenBasinCategory());
        registry.add(new CauldronCategory());

        for (var item : ItemsRegistry.ALL_HAMMERS) {
            registry.addWorkstations(HammerCategory.ID, EntryStacks.of(item.get()));
        }
        for (var block : BlocksRegistry.ALL_AUTO_HAMMERS) {
            registry.addWorkstations(HammerCategory.ID, EntryStacks.of(block.get()));
        }
        registry.addWorkstations(CrookCategory.ID, EntryStacks.of(ItemsRegistry.CROOK.get()));
        registry.addWorkstations(DripperCategory.ID, EntryStacks.of(ItemsRegistry.DRIPPER.get()));
        registry.addWorkstations(FusingMachineCategory.ID, EntryStacks.of(ItemsRegistry.FUSING_MACHINE.get()));
        registry.addWorkstations(SuperCoolerCategory.ID, EntryStacks.of(ItemsRegistry.SUPER_COOLER.get()));
        registry.addWorkstations(WoodenBasinCategory.ID, EntryStacks.of(ItemsRegistry.WOODEN_BASIN.get()));
        registry.addWorkstations(TemperedJarCategory.ID, EntryStacks.of(ItemsRegistry.TEMPERED_JAR.get()));
        for (var block : BlocksRegistry.ALL_SLUICES) {
            registry.addWorkstations(SluiceCategory.ID, EntryStacks.of(block.get()));
        }
        registry.addWorkstations(TemperatureSourceCategory.ID, EntryStacks.of(ItemsRegistry.CREATIVE_HOT_TEMPERATURE_SOURCE.get()));
        registry.addWorkstations(TemperatureSourceCategory.ID, EntryStacks.of(ItemsRegistry.CREATIVE_SUPERHEATED_TEMPERATURE_SOURCE.get()));
        registry.addWorkstations(TemperatureSourceCategory.ID, EntryStacks.of(ItemsRegistry.CREATIVE_CHILLED_TEMPERATURE_SOURCE.get()));
        registry.addWorkstations(CauldronCategory.ID, EntryStacks.of(Items.CAULDRON));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        registry.registerFiller(HammerRecipe.class, HammerDisplay::new);
        registry.registerFiller(CrookRecipe.class, CrookDisplay::new);
        registry.registerFiller(DripperRecipe.class, DripperDisplay::new);
        registry.registerFiller(FusingMachineRecipe.class, FusingMachineDisplay::new);
        registry.registerFiller(SluiceRecipe.class, SluiceDisplay::new);
        registry.registerFiller(SuperCoolerRecipe.class, SuperCoolerDisplay::new);
        registry.registerFiller(TemperatureSourceRecipe.class, TemperatureSourceDisplay::new);
        registry.registerFiller(JarRecipe.class, TemperedJarDisplay::new);
        registry.registerFiller(WoodenBasinRecipe.class, WoodenBasinDisplay::new);

        // The cauldron is not a real recipe type: build its displays from the block registry,
        // exactly like the original JEI plugin did.
        List<ItemStack> leaves = new ArrayList<>();
        List<ItemStack> saplings = new ArrayList<>();

        for (Block block : ForgeRegistries.BLOCKS) {
            if (block instanceof LeavesBlock) {
                Item item = block.asItem();
                if (item != Items.AIR) {
                    leaves.add(item.getDefaultInstance());
                }
            } else if (block instanceof SaplingBlock) {
                Item item = block.asItem();
                if (item != Items.AIR) {
                    saplings.add(item.getDefaultInstance());
                }
            }
        }

        registry.add(new CauldronDisplay(leaves, FluidStack.create(Fluids.WATER, 333)));
        registry.add(new CauldronDisplay(saplings, FluidStack.create(Fluids.WATER, 333)));
    }
}
