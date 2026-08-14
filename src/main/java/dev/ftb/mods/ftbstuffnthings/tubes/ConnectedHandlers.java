package dev.ftb.mods.ftbstuffnthings.tubes;

import dev.ftb.mods.ftbstuffnthings.crafting.recipe.JarRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedIngredient;
// REMOVED: already imported
import net.minecraftforge.fluids.capability.IFluidHandler;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.List;

public record ConnectedHandlers(List<CapabilityCache> itemHandlers,
                                List<CapabilityCache> fluidHandlers) {
    public static ConnectedHandlers create() {
        return new ConnectedHandlers(new ArrayList<>(), new ArrayList<>());
    }

    public record CapabilityCache(BlockPos pos, Direction dir) {}

    public void checkAndAddHandlers(ServerLevel level, BlockPos pos, Direction dir) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be != null && be.getCapability(ForgeCapabilities.ITEM_HANDLER, dir).orElse(null) != null) {
            itemHandlers.add(new CapabilityCache(pos, dir));
        }
        if (be != null && be.getCapability(ForgeCapabilities.FLUID_HANDLER, dir).orElse(null) != null) {
            fluidHandlers.add(new CapabilityCache(pos, dir));
        }
    }

    public ExtractionContext findIngredients(Level level, JarRecipe recipe) {
        ExtractionContext context = new ExtractionContext();

        for (var input : recipe.allInputs()) {
            input.ifLeft(fluid -> findFluid(level, fluid, context))
                    .ifRight(item -> findItem(level, item, context));
            if (context.isInsufficient()) {
                break;
            }
        }

        return context;
    }

    public boolean distributeOutputs(BlockEntity jar, JarRecipe recipe) {
        List<ItemStack> excessItems = new ArrayList<>();
        Level level = jar.getLevel();

        for (ItemStack stack : recipe.getOutputItems()) {
            int remaining = stack.getCount();
            for (var handler : itemHandlers) {
                BlockEntity be = level.getBlockEntity(handler.pos());
                IItemHandler itemHandler = be != null ? be.getCapability(ForgeCapabilities.ITEM_HANDLER, handler.dir()).orElse(null) : null;
                if (itemHandler != null) {
                    ItemStack excess = ItemHandlerHelper.insertItem(itemHandler, stack.copy(), false);
                    remaining -= stack.getCount() - excess.getCount();
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
            if (remaining > 0) {
                excessItems.add(stack.copy());
            }
        }

        return false;
    }

    private void findFluid(Level level, SizedFluidIngredient ingredient, ExtractionContext context) {
        int remaining = ingredient.amount();

        for (var fluidCache : fluidHandlers) {
            BlockEntity be = level.getBlockEntity(fluidCache.pos());
            IFluidHandler handler = be != null ? be.getCapability(ForgeCapabilities.FLUID_HANDLER, fluidCache.dir()).orElse(null) : null;
            if (handler != null) {
                FluidStack stack = handler.drain(remaining, IFluidHandler.FluidAction.SIMULATE);
                if (ingredient.ingredient().test(stack)) {
                    remaining -= stack.getAmount();
                    context.addFluidSource(handler, stack);
                    if (remaining <= 0) {
                        break;
                    }
                }
            }
        }
        if (remaining > 0) {
            context.markInsufficient();
        }
    }

    private void findItem(Level level, SizedIngredient ingredient, ExtractionContext context) {
        int remaining = ingredient.count();

        for (var itemCache : itemHandlers) {
            BlockEntity be = level.getBlockEntity(itemCache.pos());
            IItemHandler handler = be != null ? be.getCapability(ForgeCapabilities.ITEM_HANDLER, itemCache.dir()).orElse(null) : null;
            if (handler != null) {
                for (int i = 0; i < handler.getSlots(); i++) {
                    ItemStack stack = handler.extractItem(i, remaining, true);
                    if (ingredient.ingredient().test(stack)) {
                        remaining -= stack.getCount();
                        context.addItemSource(handler, stack, i);
                        if (remaining <= 0) {
                            break;
                        }
                    }
                }
            }
        }

        if (remaining > 0) {
            context.markInsufficient();
        }
    }

    public static class ExtractionContext {
        private boolean insufficient;
        private final List<FluidSource> fluidSources = new ArrayList<>();
        private final List<ItemSource> itemSources = new ArrayList<>();

        public ExtractionContext() {
            this.insufficient = false;
        }

        public boolean isInsufficient() {
            return insufficient;
        }

        public void markInsufficient() {
            insufficient = true;
        }

        public void addFluidSource(IFluidHandler handler, FluidStack fluidStack) {
            fluidSources.add(new FluidSource(handler, fluidStack));
        }

        public void addItemSource(IItemHandler handler, ItemStack itemStack, int slot) {
            itemSources.add(new ItemSource(handler, itemStack, slot));
        }

        public boolean apply() {
            // actually extract the resource from the handlers - should always succeed!
            // - as long as this context is used on the same tick that it was created

            return true;
        }
    }

    private record FluidSource(IFluidHandler handler, FluidStack fluidStack) {
    }

    private record ItemSource(IItemHandler handler, ItemStack itemStack, int slot) {
    }
}
