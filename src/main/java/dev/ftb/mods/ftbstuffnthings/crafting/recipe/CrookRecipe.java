package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbstuffnthings.crafting.BaseRecipe;
import dev.ftb.mods.ftbstuffnthings.crafting.ItemWithChance;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.*;

public class CrookRecipe extends BaseRecipe<CrookRecipe> {
    private final Ingredient ingredient;
    private final List<ItemWithChance> results;
    private final int max;
    private final boolean replaceDrops;

    public CrookRecipe(Ingredient ingredient, List<ItemWithChance> results, int max, boolean replaceDrops) {
        super(RecipesRegistry.CROOK_SERIALIZER, RecipesRegistry.CROOK_TYPE);

        this.ingredient = ingredient;
        this.results = results;
        this.max = max;
        this.replaceDrops = replaceDrops;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<ItemWithChance> getResults() {
        return results;
    }

    public int getMax() {
        return max;
    }

    public boolean replaceDrops() {
        return replaceDrops;
    }

    public interface IFactory<T extends CrookRecipe> {
        T create(Ingredient ingredient, List<ItemWithChance> results, int max, boolean clearDefaultDrops);
    }

    public static class Serializer<T extends CrookRecipe> implements RecipeSerializer<T> {
        private final Codec<T> codec;
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
            codec = RecordCodecBuilder.create(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(CrookRecipe::getIngredient),
                    ItemWithChance.CODEC.listOf().fieldOf("results").forGetter(CrookRecipe::getResults),
                    Codec.INT.optionalFieldOf("max", 0).forGetter(CrookRecipe::getMax),
                    Codec.BOOL.optionalFieldOf("replace_drops", true).forGetter(CrookRecipe::replaceDrops)
            ).apply(builder, factory::create));
        }

        @Override
        public Codec<T> codec() {
            return codec;
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            Ingredient ingredient = Ingredient.fromNetwork(buf);
            int size = buf.readVarInt();
            List<ItemWithChance> results = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                results.add(ItemWithChance.fromNetwork(buf));
            }
            int max = buf.readVarInt();
            boolean replaceDrops = buf.readBoolean();
            return factory.create(ingredient, results, max, replaceDrops);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            recipe.getIngredient().toNetwork(buf);
            buf.writeVarInt(recipe.getResults().size());
            for (ItemWithChance item : recipe.getResults()) {
                ItemWithChance.toNetwork(buf, item);
            }
            buf.writeVarInt(recipe.getMax());
            buf.writeBoolean(recipe.replaceDrops());
        }
    }

    public record CrookDrops(List<ItemWithChance> items, int max, boolean replaceDrops) {
    }
}