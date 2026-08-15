package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.items.MeshType;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.fluids.FluidStack;

import java.util.*;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SluiceRecipe extends BaseRecipe<SluiceRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private final int maxResults;
    private final Optional<SizedFluidIngredient> fluid;
    private final float processingTimeMultiplier;
    private final Set<MeshType> meshTypes;

    public SluiceRecipe(Ingredient ingredient, List<ItemWithChance> results, int maxResults, Optional<SizedFluidIngredient> fluid, float processingTimeMultiplier, List<MeshType> meshTypes) {
        super(RecipesRegistry.SLUICE_SERIALIZER, RecipesRegistry.SLUICE_TYPE);

        this.ingredient = ingredient;
        this.results = results;
        this.maxResults = maxResults;
        this.fluid = fluid;
        this.processingTimeMultiplier = processingTimeMultiplier;
        this.meshTypes = EnumSet.copyOf(meshTypes);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public Optional<SizedFluidIngredient> getFluid() {
        return fluid;
    }

    public boolean testFluid(FluidStack toCheck, boolean checkAmount, double fluidModifier) {
        return fluid.map(ingr -> {
            if (checkAmount) {
                FluidStack copy = toCheck.copy();
                copy.setAmount((int) (toCheck.getAmount() / fluidModifier));
                return ingr.test(copy);
            }
            return ingr.ingredient().test(toCheck);
        }).orElse(true);
    }

    public boolean testFluid(FluidStack toCheck, boolean checkAmount) {
        return testFluid(toCheck, checkAmount, 1.0);
    }

    public float getProcessingTimeMultiplier() {
        return processingTimeMultiplier;
    }

    public Set<MeshType> getMeshTypes() {
        return Collections.unmodifiableSet(meshTypes);
    }

    public List<MeshType> getMeshTypesAsList() {
        return List.copyOf(meshTypes);
    }

    public interface IFactory<T extends SluiceRecipe> {
        T create(Ingredient ingredient, List<ItemWithChance> results, int maxResults, Optional<SizedFluidIngredient> fluid, float processingTimeMultiplier, List<MeshType> meshTypes);
    }

    public static class Serializer<T extends SluiceRecipe> implements RecipeSerializer<T> {
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            List<ItemWithChance> results = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "results")) {
                results.add(ItemWithChance.fromJson(e));
            }
            int maxResults = GsonHelper.getAsInt(json, "max_results", 4);
            Optional<SizedFluidIngredient> fluid = json.has("fluid") && !json.get("fluid").isJsonNull()
                    ? Optional.of(SizedFluidIngredient.fromJson(json.get("fluid")))
                    : Optional.empty();
            float processingTimeMultiplier = GsonHelper.getAsFloat(json, "processing_time_multiplier", 1F);
            List<MeshType> meshTypes = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "mesh_types")) {
                meshTypes.add(MeshType.fromJson(e));
            }
            T recipe = factory.create(ingredient, results, maxResults, fluid, processingTimeMultiplier, meshTypes);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int size = buf.readVarInt();
            List<ItemWithChance> results = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                results.add(ItemWithChance.fromNetwork(buf));
            }
            int maxResults = buf.readVarInt();
            Optional<SizedFluidIngredient> fluid = buf.readBoolean() ? Optional.of(SizedFluidIngredient.fromNetwork(buf)) : Optional.empty();
            float processingTimeMultiplier = buf.readFloat();
            int meshSize = buf.readVarInt();
            List<MeshType> meshTypes = new ArrayList<>();
            for (int i = 0; i < meshSize; i++) {
                meshTypes.add(MeshType.fromNetwork(buf));
            }
            T recipe = factory.create(ingredient, results, maxResults, fluid, processingTimeMultiplier, meshTypes);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            recipe.getIngredient().toNetwork(buf);
            buf.writeVarInt(recipe.getResults().size());
            for (ItemWithChance item : recipe.getResults()) {
                ItemWithChance.toNetwork(buf, item);
            }
            buf.writeVarInt(recipe.getMaxResults());
            buf.writeBoolean(recipe.getFluid().isPresent());
            recipe.getFluid().ifPresent(fluid -> SizedFluidIngredient.toNetwork(buf, fluid));
            buf.writeFloat(recipe.getProcessingTimeMultiplier());
            buf.writeVarInt(recipe.getMeshTypesAsList().size());
            for (MeshType type : recipe.getMeshTypesAsList()) {
                MeshType.toNetwork(buf, type);
            }
        }
    }
}
