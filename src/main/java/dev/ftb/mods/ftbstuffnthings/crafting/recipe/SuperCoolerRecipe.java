package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SuperCoolerRecipe extends BaseRecipe<SuperCoolerRecipe> {
    private final List<Ingredient> inputs;
    private final EnergyRequirement energyRequirement;
    private final SizedFluidIngredient fluidInput;
    private final ItemStack result;

    public SuperCoolerRecipe(List<Ingredient> inputs, SizedFluidIngredient fluidInput, EnergyRequirement energyRequirement, ItemStack result) {
        super(RecipesRegistry.SUPER_COOLER_SERIALIZER, RecipesRegistry.SUPER_COOLER_TYPE);

        this.inputs = inputs;
        this.fluidInput = fluidInput;
        this.energyRequirement = energyRequirement;
        this.result = result;
    }

    public List<Ingredient> getInputs() {
        return inputs;
    }

    public SizedFluidIngredient getFluidInput() {
        return fluidInput;
    }

    public EnergyRequirement getEnergyComponent() {
        return energyRequirement;
    }

    public ItemStack getResult() {
        return result;
    }

    public interface IFactory<T extends SuperCoolerRecipe> {
        T create(List<Ingredient> ingredients, SizedFluidIngredient fluidIngredient, EnergyRequirement energyRequirement, ItemStack result);
    }

    public boolean test(IItemHandler itemHandler, FluidStack fluidStack) {
        // note: just testing for a fluid match, not the amount here
        if (!getFluidInput().ingredient().test(fluidStack)) {
            return false;
        }

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

    public static class Serializer<T extends SuperCoolerRecipe> implements RecipeSerializer<T> {
        private final Codec<T> codec;
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
            codec = RecordCodecBuilder.create(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.listOf().fieldOf("inputs").forGetter(SuperCoolerRecipe::getInputs),
                    SizedFluidIngredient.FLAT_CODEC.fieldOf("fluid").forGetter(SuperCoolerRecipe::getFluidInput),
                    EnergyRequirement.CODEC.fieldOf("energy").forGetter(SuperCoolerRecipe::getEnergyComponent),
                    ItemStack.CODEC.fieldOf("result").forGetter(SuperCoolerRecipe::getResult)
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
            SizedFluidIngredient fluidInput = SizedFluidIngredient.fromNetwork(buf);
            EnergyRequirement energyRequirement = EnergyRequirement.fromNetwork(buf);
            ItemStack result = buf.readItem();
            return factory.create(inputs, fluidInput, energyRequirement, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            buf.writeVarInt(recipe.getInputs().size());
            for (Ingredient ingr : recipe.getInputs()) {
                ingr.toNetwork(buf);
            }
            SizedFluidIngredient.toNetwork(buf, recipe.getFluidInput());
            EnergyRequirement.toNetwork(buf, recipe.getEnergyComponent());
            buf.writeItem(recipe.getResult());
        }
    }
}