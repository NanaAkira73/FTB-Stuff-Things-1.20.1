package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class FusingMachineRecipe extends BaseRecipe<FusingMachineRecipe> {
    private final List<Ingredient> inputs;
    private final FluidStack fluidResult;
    private final EnergyRequirement energyRequirement;

    public FusingMachineRecipe(List<Ingredient> inputs, FluidStack fluidResult, EnergyRequirement energyRequirement) {
        super(RecipesRegistry.FUSING_MACHINE_SERIALIZER, RecipesRegistry.FUSING_MACHINE_TYPE);

        this.inputs = inputs;
        this.fluidResult = fluidResult;
        this.energyRequirement = energyRequirement;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public FluidStack getFluidResult() {
        return fluidResult;
    }

    public EnergyRequirement getEnergyComponent() {
        return energyRequirement;
    }

    public boolean test(IItemHandler itemHandler) {
        Set<Ingredient> inputSet = Sets.newIdentityHashSet();
        inputSet.addAll(getInputs());

        int found = 0;
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                Iterator<Ingredient> iter = inputSet.iterator();
                while (iter.hasNext()) {
                    Ingredient ingr = iter.next();
                    if (ingr.test(itemHandler.getStackInSlot(i))) {
                        iter.remove();
                        found++;
                        break;
                    }
                }
                if (found == getInputs().size()) {
                    return true;
                }
            }
        }
        return false;
    }

    public interface IFactory<T extends FusingMachineRecipe> {
        T create(List<Ingredient> inputs, FluidStack fluidResult, EnergyRequirement energyRequirement);
    }

    public static class Serializer<T extends FusingMachineRecipe> implements RecipeSerializer<T> {
        private final Codec<T> codec;
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
            codec = RecordCodecBuilder.create(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("inputs").forGetter(FusingMachineRecipe::getInputs),
                    FluidStack.CODEC.fieldOf("result").forGetter(FusingMachineRecipe::getFluidResult),
                    EnergyRequirement.CODEC.fieldOf("energy").forGetter(FusingMachineRecipe::getEnergyComponent)
            ).apply(builder, factory::create));
        }

        @Override
        public Codec<T> codec() {
            return codec;
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int size = buf.readVarInt();
            List<Ingredient> inputs = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                inputs.add(Ingredient.fromNetwork(buf));
            }
            FluidStack fluidResult = FluidStack.readFromPacket(buf);
            EnergyRequirement energyRequirement = EnergyRequirement.fromNetwork(buf);
            return factory.create(inputs, fluidResult, energyRequirement);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            buf.writeVarInt(recipe.getInputs().size());
            for (Ingredient ingr : recipe.getInputs()) {
                ingr.toNetwork(buf);
            }
            recipe.getFluidResult().writeToPacket(buf);
            EnergyRequirement.toNetwork(buf, recipe.getEnergyComponent());
        }
    }
}