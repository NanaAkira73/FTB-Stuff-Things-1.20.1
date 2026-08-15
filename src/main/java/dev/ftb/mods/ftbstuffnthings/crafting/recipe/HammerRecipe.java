package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.JsonUtil;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;
import java.util.List;

public class HammerRecipe extends BaseRecipe<HammerRecipe> {
    private final Ingredient ingredient;
    private final List<ItemStack> results;

    public HammerRecipe(Ingredient ingredient, List<ItemStack> results) {
        super(RecipesRegistry.HAMMER_SERIALIZER, RecipesRegistry.HAMMER_TYPE);

        this.ingredient = ingredient;
        this.results = results;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemStack> getResults() {
        return results;
    }

    public interface IFactory<T extends HammerRecipe> {
        T create(Ingredient ingredient, List<ItemStack> results);
    }

    public static class Serializer<T extends HammerRecipe> implements RecipeSerializer<T> {
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            List<ItemStack> results = new ArrayList<>();
            for (JsonElement e : GsonHelper.getAsJsonArray(json, "results")) {
                results.add(JsonUtil.itemStack(e));
            }
            T recipe = factory.create(ingredient, results);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int size = buf.readVarInt();
            List<ItemStack> results = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                results.add(buf.readItem());
            }
            T recipe = factory.create(ingredient, results);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            recipe.getIngredient().toNetwork(buf);
            buf.writeVarInt(recipe.getResults().size());
            for (ItemStack stack : recipe.getResults()) {
                buf.writeItem(stack);
            }
        }
    }
}
