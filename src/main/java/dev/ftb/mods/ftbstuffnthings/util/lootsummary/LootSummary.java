package dev.ftb.mods.ftbstuffnthings.util.lootsummary;

import com.google.common.collect.ImmutableList;
import dev.ftb.mods.ftbstuffnthings.util.MiscUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LootSummary {
    private final Map<String,List<SummaryEntry>> entryMap;
    private final int hashCode;

    private LootSummary(Map<String,List<SummaryEntry>> entryMap) {
        this.entryMap = Collections.unmodifiableMap(entryMap);
        this.hashCode = entryMap.hashCode();
    }

    public static LootSummary forLootTable(LootTable table, LootParams params) {
        Map<String,List<SummaryEntry>> map = new LinkedHashMap<>();

        LootContext ctx = new LootContext.Builder(params).create(null);

        expandTable(table, ctx, map, 1f);

        Map<String, List<SummaryEntry>> sortedMap = new LinkedHashMap<>();
        map.forEach((pool, entries) -> sortedMap.put(pool, entries.stream().sorted().toList()));
        return new LootSummary(sortedMap);
    }

    private static void expandTable(LootTable table, LootContext ctx, Map<String, List<SummaryEntry>> map, float weightMult) {
        for (LootPool pool : table.pools) {
            ImmutableList.Builder<SummaryEntry> builder = ImmutableList.builder();
            MutableFloat totalWeight = new MutableFloat(0F);
            for (LootPoolEntryContainer entryContainer : pool.entries) {
                if (entryContainer instanceof LootPoolSingletonContainer s) {
                    totalWeight.add(s.weight);
                } else {
                    entryContainer.expand(ctx, entry -> totalWeight.add(entry.getWeight(1f)));
                }
            }
            for (LootPoolEntryContainer entryContainer : pool.entries) {
                if (entryContainer instanceof LootTableReference nested) {
                    expandTable(getNestedLootTable(nested, ctx), ctx, map, weightMult * nested.weight / totalWeight.floatValue());
                } else {
                    entryContainer.expand(ctx, entry -> entry.createItemStack(stack ->
                            builder.add(new SummaryEntry(weightMult * entry.getWeight(1.0f) / totalWeight.floatValue(), stack)), ctx)
                    );
                }
            }
            String tblName = Objects.requireNonNullElse(pool.getName(), String.format("pool:%X", pool.hashCode()));
            map.computeIfAbsent(tblName, k -> new ArrayList<>()).addAll(builder.build());
        }
    }

    private static LootTable getNestedLootTable(LootTableReference nested, LootContext ctx) {
        return ctx.getLevel().getServer().getLootData().getLootTable(nested.name);
    }

    public Map<String,List<SummaryEntry>> entryMap() {
        return entryMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LootSummary that = (LootSummary) o;
        return hashCode == that.hashCode;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public static LootSummary fromNetwork(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, List<SummaryEntry>> entryMap = new LinkedHashMap<>();
        for (int i = 0; i < size; i++) {
            String key = buf.readUtf();
            int listSize = buf.readVarInt();
            List<SummaryEntry> entries = new ArrayList<>();
            for (int j = 0; j < listSize; j++) {
                entries.add(SummaryEntry.fromNetwork(buf));
            }
            entryMap.put(key, entries);
        }
        return new LootSummary(entryMap);
    }

    public static void toNetwork(FriendlyByteBuf buf, LootSummary summary) {
        buf.writeVarInt(summary.entryMap().size());
        summary.entryMap().forEach((key, entries) -> {
            buf.writeUtf(key);
            buf.writeVarInt(entries.size());
            for (SummaryEntry entry : entries) {
                SummaryEntry.toNetwork(buf, entry);
            }
        });
    }

    public record SummaryEntry(float weight, ItemStack stack) implements Comparable<SummaryEntry> {
        public static SummaryEntry fromNetwork(FriendlyByteBuf buf) {
            return new SummaryEntry(buf.readFloat(), buf.readItem());
        }

        public static void toNetwork(FriendlyByteBuf buf, SummaryEntry entry) {
            buf.writeFloat(entry.weight());
            buf.writeItem(entry.stack());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SummaryEntry that = (SummaryEntry) o;
            return weight == that.weight && ItemStack.matches(stack, that.stack);
        }

        @Override
        public int hashCode() {
            return Objects.hash(weight, MiscUtil.hashItemAndComponents(stack), stack.getCount());
        }

        @Override
        public int compareTo(@NotNull LootSummary.SummaryEntry o) {
            return Float.compare(o.weight, weight);
        }
    }
}