package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.EnergyRequirement;
import dev.ftb.mods.ftbstuffnthings.crafting.JsonUtil;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
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
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            List<Ingredient> inputs = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "inputs")) {
                inputs.add(Ingredient.fromJson(e));
            }
            SizedFluidIngredient fluidInput = SizedFluidIngredient.fromJson(json.get("fluid"));
            EnergyRequirement energy = EnergyRequirement.fromJson(json.get("energy"));
            ItemStack result = JsonUtil.itemStack(json.get("result"));
            T recipe = factory.create(inputs, fluidInput, energy, result);
            recipe.setId(id);
            return recipe;
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
            T recipe = factory.create(inputs, fluidInput, energyRequirement, result);
            recipe.setId(id);
            return recipe;
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
