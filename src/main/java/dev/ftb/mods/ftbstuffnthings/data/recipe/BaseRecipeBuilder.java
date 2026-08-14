package dev.ftb.mods.ftbstuffnthings.data.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.crafting.DevEnvironmentCondition;
import net.minecraft.advancements.Criterion;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class BaseRecipeBuilder<T extends Recipe<?>> implements RecipeBuilder {
    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    abstract protected T buildRecipe();

    @Override
    public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        T recipe = buildRecipe();
        ResourceLocation id1 = new ResourceLocation(id.getNamespace(), recipe.getType() + "/" + id.getPath());
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                // Serialization handled by the recipe itself
            }

            @Override
            public ResourceLocation getId() {
                return id1;
            }

            @Override
            public Recipe<?> getType() {
                return recipe;
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }

    public void saveTest(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        T recipe = buildRecipe();
        ResourceLocation id1 = new ResourceLocation(id.getNamespace(), recipe.getType() + "/dev_test_" + id.getPath());
        consumer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject json) {
                JsonArray conditions = new JsonArray();
                JsonObject conditionObj = new JsonObject();
                conditionObj.addProperty("type", DevEnvironmentCondition.INSTANCE.getID().toString());
                conditions.add(conditionObj);
                json.add("conditions", conditions);
            }

            @Override
            public ResourceLocation getId() {
                return id1;
            }

            @Override
            public Recipe<?> getType() {
                return recipe;
            }

            @Override
            public @Nullable JsonObject serializeAdvancement() {
                return null;
            }

            @Override
            public @Nullable ResourceLocation getAdvancementId() {
                return null;
            }
        });
    }
}