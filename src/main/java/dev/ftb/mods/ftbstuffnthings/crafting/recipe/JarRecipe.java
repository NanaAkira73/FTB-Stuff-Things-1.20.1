package dev.ftb.mods.ftbstuffnthings.crafting.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Either;
import dev.ftb.mods.ftbstuffnthings.crafting.JsonUtil;
import dev.ftb.mods.ftbstuffnthings.crafting.NoInventory;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedFluidIngredient;
import dev.ftb.mods.ftbstuffnthings.crafting.SizedIngredient;
import dev.ftb.mods.ftbstuffnthings.integration.stages.StageHelper;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import dev.ftb.mods.ftbstuffnthings.temperature.Temperature;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class JarRecipe implements Recipe<NoInventory>, Comparable<JarRecipe> {
    private final Temperature temperature;
    private final int time;
    private final List<SizedIngredient> inputItems;
    private final List<SizedFluidIngredient> inputFluids;
    private final List<ItemStack> outputItems;
    private final List<FluidStack> outputFluids;
    private final boolean canRepeat;
    private final String stage;
    private final Lazy<String> filterText = Lazy.of(this::buildFilterText);
    private ResourceLocation id;

    public JarRecipe(List<SizedIngredient> inputItems, List<SizedFluidIngredient> inputFluids,
                     List<ItemStack> outputItems, List<FluidStack> outputFluids,
                     Temperature temperature, int time, boolean canRepeat, String stage)
    {
        this.inputItems = inputItems;
        this.inputFluids = inputFluids;
        this.outputItems = outputItems;
        this.outputFluids = outputFluids;
        this.temperature = temperature;
        this.time = time;
        this.canRepeat = canRepeat;
        this.stage = stage;
    }

    @Override
    public boolean matches(NoInventory inv, Level world) {
        return true;
    }

    @Override
    public ItemStack assemble(NoInventory noInventory, RegistryAccess provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess provider) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public void setId(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipesRegistry.TEMPERED_JAR_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipesRegistry.TEMPERED_JAR_TYPE.get();
    }

    public Temperature getTemperature() {
        return temperature;
    }

    public int getTime() {
        return time;
    }

    public List<SizedIngredient> getInputItems() {
        return inputItems;
    }

    public List<SizedFluidIngredient> getInputFluids() {
        return inputFluids;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public List<FluidStack> getOutputFluids() {
        return outputFluids;
    }

    public boolean canRepeat() {
        return canRepeat;
    }

    public String getStage() {
        return stage;
    }

    public boolean isAvailableFor(Player player) {
        return stage.isEmpty() || StageHelper.hasStage(player, stage);
    }

    public boolean hasItems() {
        return !inputItems.isEmpty() || !outputItems.isEmpty();
    }

    public boolean hasFluids() {
        return !inputFluids.isEmpty() || !outputFluids.isEmpty();
    }

    public String getFilterText() {
        return filterText.get();
    }

    private String buildFilterText() {
        LinkedHashSet<String> set = new LinkedHashSet<>();

        for (ItemStack stack : outputItems) {
            set.add(stack.getHoverName().getString().trim().toLowerCase());
        }

        for (FluidStack stack : outputFluids) {
            set.add(stack.getDisplayName().getString().trim().toLowerCase());
        }

        for (SizedIngredient ingredient : inputItems) {
            for (ItemStack stack : ingredient.ingredient().getItems()) {
                set.add(stack.getHoverName().getString().trim().toLowerCase());
            }
        }

        for (SizedFluidIngredient ingredient : inputFluids) {
            for (FluidStack stack : ingredient.ingredient().getStacks()) {
                set.add(stack.getDisplayName().getString().trim().toLowerCase());
            }
        }

        return String.join(" ", set);
    }

    public List<Either<SizedFluidIngredient,SizedIngredient>> allInputs() {
        List<Either<SizedFluidIngredient,SizedIngredient>> res = new ArrayList<>();
        inputFluids.forEach(f -> res.add(Either.left(f)));
        inputItems.forEach(i -> res.add(Either.right(i)));
        return res;
    }

    /**
     * Test if the given item and fluids match this recipe, optionally taking item/fluid amounts into consideration.
     *
     * @param jarTemperature the current jar temperature
     * @param jarItems       the items to test
     * @param jarFluids      the fluids to test
     * @param checkAmounts   true to check ingredient amounts too, false to just check for the right items/fluids
     * @return true if the recipe matches, false otherwise
     */
    public boolean test(Temperature jarTemperature, IItemHandler jarItems, IFluidHandler jarFluids, boolean checkAmounts) {
        if (jarTemperature != getTemperature()) {
            return false;
        }

        int matched = 0;
        for (SizedIngredient inputItem : inputItems) {
            for (int i = 0; i < jarItems.getSlots(); i++) {
                ItemStack toTest = jarItems.getStackInSlot(i);
                if (checkAmounts ? inputItem.test(toTest) : inputItem.ingredient().test(toTest)) {
                    matched++;
                    break;
                }
            }
        }
        if (matched != inputItems.size()) return false;

        matched = 0;
        for (SizedFluidIngredient inputFluid : inputFluids) {
            for (int i = 0; i < jarFluids.getTanks(); i++) {
                FluidStack toTest = jarFluids.getFluidInTank(i);
                if (checkAmounts ? inputFluid.test(toTest) : inputFluid.ingredient().test(toTest)) {
                    matched++;
                    break;
                }
            }
        }
        return matched == inputFluids.size();
    }

    public int inputIngredientCount() {
        return inputFluids.size() + inputItems.size();
    }

    @Override
    public int compareTo(@NotNull JarRecipe o) {
        int c = getTemperature().compareTo(o.getTemperature());
        if (c != 0) return c;

        c = Integer.compare(o.inputIngredientCount(), inputIngredientCount());
        if (c != 0) return c;

        c = Integer.compare(
                o.getInputItems().stream().mapToInt(SizedIngredient::count).sum(),
                getInputItems().stream().mapToInt(SizedIngredient::count).sum()
        );
        if (c != 0) return c;

        return Integer.compare(
                o.getInputFluids().stream().mapToInt(SizedFluidIngredient::amount).sum(),
                getInputFluids().stream().mapToInt(SizedFluidIngredient::amount).sum()
        );
    }

    public interface IFactory<T extends JarRecipe> {
        T create(List<SizedIngredient> inputItems, List<SizedFluidIngredient> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids, Temperature temperature, int time, boolean canRepeat, String stage);
    }

    public static class Serializer<T extends JarRecipe> implements RecipeSerializer<T> {
        private final IFactory<T> factory;

        public Serializer(IFactory<T> factory) {
            this.factory = factory;
        }

        @Override
        public T fromJson(ResourceLocation id, JsonObject json) {
            List<SizedIngredient> inputItems = new ArrayList<>();
            if (json.has("input_items")) {
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "input_items")) {
                    inputItems.add(SizedIngredient.fromJson(e));
                }
            }

            List<SizedFluidIngredient> inputFluids = new ArrayList<>();
            if (json.has("input_fluids")) {
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "input_fluids")) {
                    inputFluids.add(SizedFluidIngredient.fromJson(e));
                }
            }

            List<ItemStack> outputItems = new ArrayList<>();
            if (json.has("output_items")) {
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "output_items")) {
                    outputItems.add(JsonUtil.itemStack(e));
                }
            }

            List<FluidStack> outputFluids = new ArrayList<>();
            if (json.has("output_fluids")) {
                for (JsonElement e : GsonHelper.getAsJsonArray(json, "output_fluids")) {
                    outputFluids.add(JsonUtil.fluidStack(e));
                }
            }

            Temperature temperature = Temperature.byName(GsonHelper.getAsString(json, "temperature", "normal"));
            int time = GsonHelper.getAsInt(json, "time", 200);
            boolean canRepeat = GsonHelper.getAsBoolean(json, "can_repeat", true);
            String stage = GsonHelper.getAsString(json, "stage", "");

            T recipe = factory.create(inputItems, inputFluids, outputItems, outputFluids, temperature, time, canRepeat, stage);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public T fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            int inputItemsSize = buf.readVarInt();
            List<SizedIngredient> inputItems = new ArrayList<>();
            for (int i = 0; i < inputItemsSize; i++) {
                inputItems.add(SizedIngredient.fromNetwork(buf));
            }

            int inputFluidsSize = buf.readVarInt();
            List<SizedFluidIngredient> inputFluids = new ArrayList<>();
            for (int i = 0; i < inputFluidsSize; i++) {
                inputFluids.add(SizedFluidIngredient.fromNetwork(buf));
            }

            int outputItemsSize = buf.readVarInt();
            List<ItemStack> outputItems = new ArrayList<>();
            for (int i = 0; i < outputItemsSize; i++) {
                outputItems.add(buf.readItem());
            }

            int outputFluidsSize = buf.readVarInt();
            List<FluidStack> outputFluids = new ArrayList<>();
            for (int i = 0; i < outputFluidsSize; i++) {
                outputFluids.add(FluidStack.readFromPacket(buf));
            }

            Temperature temperature = SizedIngredient.readEnum(buf, Temperature.class);
            int time = buf.readVarInt();
            boolean canRepeat = buf.readBoolean();
            String stage = buf.readUtf();

            T recipe = factory.create(inputItems, inputFluids, outputItems, outputFluids, temperature, time, canRepeat, stage);
            recipe.setId(id);
            return recipe;
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, T recipe) {
            buf.writeVarInt(recipe.getInputItems().size());
            for (SizedIngredient item : recipe.getInputItems()) {
                SizedIngredient.toNetwork(buf, item);
            }

            buf.writeVarInt(recipe.getInputFluids().size());
            for (SizedFluidIngredient fluid : recipe.getInputFluids()) {
                SizedFluidIngredient.toNetwork(buf, fluid);
            }

            buf.writeVarInt(recipe.getOutputItems().size());
            for (ItemStack stack : recipe.getOutputItems()) {
                buf.writeItem(stack);
            }

            buf.writeVarInt(recipe.getOutputFluids().size());
            for (FluidStack stack : recipe.getOutputFluids()) {
                stack.writeToPacket(buf);
            }

            SizedIngredient.writeEnum(buf, recipe.getTemperature());
            buf.writeVarInt(recipe.getTime());
            buf.writeBoolean(recipe.canRepeat());
            buf.writeUtf(recipe.getStage());
        }
    }
}
