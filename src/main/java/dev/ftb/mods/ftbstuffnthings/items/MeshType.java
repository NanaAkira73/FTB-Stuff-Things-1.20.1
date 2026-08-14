package dev.ftb.mods.ftbstuffnthings.items;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbstuffnthings.registry.ItemsRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public enum MeshType implements StringRepresentable {
    EMPTY("empty", null, null),

    CLOTH("cloth", ItemsRegistry.CLOTH_MESH, Tags.Items.STRINGS),
    IRON("iron", ItemsRegistry.IRON_MESH, Tags.Items.INGOTS_IRON),
    GOLD("gold", ItemsRegistry.GOLD_MESH, Tags.Items.INGOTS_GOLD),
    DIAMOND("diamond", ItemsRegistry.DIAMOND_MESH, Tags.Items.GEMS_DIAMOND),
    BLAZING("blazing", ItemsRegistry.BLAZING_MESH, Tags.Items.RODS_BLAZE);

    public static final Codec<MeshType> CODEC = StringRepresentable.fromEnum(MeshType::values);

    public static final List<MeshType> NON_EMPTY_VALUES = Arrays.stream(values()).filter(e -> e != EMPTY).toList();

    @Nullable
    private final RegistryObject<MeshItem> meshItem;
    private final String name;
    @Nullable
    private final TagKey<Item> ingredientTag;

    MeshType(String name, @Nullable RegistryObject<MeshItem> meshItem, @Nullable TagKey<Item> ingredientTag) {
        this.name = name;
        this.meshItem = meshItem;
        this.ingredientTag = ingredientTag;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public ItemStack getItemStack() {
        return meshItem == null ? ItemStack.EMPTY : meshItem.toStack();
    }

    public @Nullable TagKey<Item> getIngredientTag() {
        return ingredientTag;
    }

    public static MeshType fromNetwork(FriendlyByteBuf buf) {
        return buf.readEnum(MeshType.class);
    }

    public static void toNetwork(FriendlyByteBuf buf, MeshType type) {
        buf.writeEnum(type);
    }
}