package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.common.base.MoreObjects;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;

public record ItemWithChance(ItemStack item, double chance) {

    public static ItemWithChance fromJson(JsonElement element) {
        JsonObject json = JsonUtil.asObject(element, "item_with_chance");
        ItemStack stack = JsonUtil.itemStack(json.get("item"));
        double chance = GsonHelper.getAsDouble(json, "chance");
        return new ItemWithChance(stack, chance);
    }

    public static ItemWithChance fromNetwork(FriendlyByteBuf buf) {
        return new ItemWithChance(buf.readItem(), buf.readDouble());
    }

    public static void toNetwork(FriendlyByteBuf buf, ItemWithChance item) {
        buf.writeItem(item.item());
        buf.writeDouble(item.chance());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("item", item)
            .add("chance", chance)
            .toString();
    }

    public ItemWithChance copy() {
        return new ItemWithChance(item.copy(), chance);
    }
}
