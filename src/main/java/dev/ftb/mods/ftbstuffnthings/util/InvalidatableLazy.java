package dev.ftb.mods.ftbstuffnthings.util;

import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * A Lazy whose cached value can be invalidated and recomputed on next access.
 * Forge's 1.20.1 Lazy has no invalidate() method, unlike newer versions.
 */
public class InvalidatableLazy<T> {
    private final Supplier<T> supplier;
    private Lazy<T> lazy;

    public InvalidatableLazy(Supplier<T> supplier) {
        this.supplier = supplier;
        this.lazy = Lazy.of(supplier);
    }

    public T get() {
        return lazy.get();
    }

    public void invalidate() {
        lazy = Lazy.of(supplier);
    }
}
