package dev.ftb.mods.ftbstuffnthings.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.Arrays;
import java.util.List;

public record SizedIngredient(Ingredient ingredient, int count) {

    public static final Codec<SizedIngredient> FLAT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
                    Codec.INT.fieldOf("count").forGetter(SizedIngredient::count)
            ).apply(instance, SizedIngredient::new)
    );

    public static SizedIngredient fromNetwork(FriendlyByteBuf buf) {
        return new SizedIngredient(Ingredient.fromNetwork(buf), buf.readVarInt());
    }

    public static void toNetwork(FriendlyByteBuf buf, SizedIngredient ingredient) {
        ingredient.ingredient().toNetwork(buf);
        buf.writeVarInt(ingredient.count());
    }

    public boolean test(ItemStack stack) {
        return ingredient().test(stack) && stack.getCount() >= count();
    }

    public List<ItemStack> getItems() {
        return Arrays.stream(ingredient().getItems())
                .map(stack -> stack.copyWithCount(count()))
                .toList();
    }

    public static SizedIngredient of(Ingredient ingredient, int count) {
        return new SizedIngredient(ingredient, count);
    }

    public static SizedIngredient of(TagKey<Item> tag, int count) {
        return new SizedIngredient(Ingredient.of(tag), count);
    }

    public static SizedIngredient of(ItemLike item, int count) {
        return new SizedIngredient(Ingredient.of(item), count);
    }

    /**
     * Helper to write an enum value to a FriendlyByteBuf, replacing NeoForgeStreamCodecs.enumCodec.
     */
    public static <E extends Enum<E>> void writeEnum(FriendlyByteBuf buf, E value) {
        buf.writeEnum(value);
    }

    /**
     * Helper to read an enum value from a FriendlyByteBuf, replacing NeoForgeStreamCodecs.enumCodec.
     */
    public static <E extends Enum<E>> E readEnum(FriendlyByteBuf buf, Class<E> enumClass) {
        return buf.readEnum(enumClass);
    }
}