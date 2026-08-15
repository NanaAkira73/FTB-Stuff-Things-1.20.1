package dev.ftb.mods.ftbstuffnthings.blocks;

import java.util.List;

/**
 * Implement this on blocks which have data that needs to be serialized onto the dropped item. This is for the
 * purpose of block loot table data generation; the declared NBT keys for each block must also be handled via
 * saveAdditional() and load() in the corresponding block entity.
 */
@FunctionalInterface
public interface SerializableComponentsProvider {
    void addSerializableComponents(List<String> list);
}
