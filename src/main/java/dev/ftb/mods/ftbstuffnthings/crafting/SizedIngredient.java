package dev.ftb.mods.ftbstuffnthings.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
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

    public static final StreamCodec<ByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, SizedIngredient::ingredient,
            ByteBufCodecs.VAR_INT, SizedIngredient::count,
            SizedIngredient::new
    );

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
     * Helper to create a StreamCodec for enum serialization, replacing NeoForgeStreamCodecs.enumCodec.
     */
    public static <E extends Enum<E>> StreamCodec<ByteBuf, E> enumStreamCodec(Class<E> enumClass) {
        return StreamCodec.of(
                (buf, val) -> buf.writeEnum(val),
                buf -> buf.readEnum(enumClass)
        );
    }
}