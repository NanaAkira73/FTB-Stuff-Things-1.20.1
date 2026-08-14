package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.common.base.MoreObjects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftbstuffnthings.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public record ItemWithChance(ItemStack item, double chance) {
	public static final Codec<ItemWithChance> CODEC = RecordCodecBuilder.create(builder -> builder.group(
			ItemStack.CODEC.fieldOf("item").forGetter(ItemWithChance::item),
			Codec.DOUBLE.validate(MiscUtil::validateChanceRange).fieldOf("chance").forGetter(ItemWithChance::chance)
	).apply(builder, ItemWithChance::new));

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

	public ItemWithChance copy(){
		return new ItemWithChance(item.copy(), chance);
	}
}