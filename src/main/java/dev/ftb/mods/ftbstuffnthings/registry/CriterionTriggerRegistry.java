package dev.ftb.mods.ftbstuffnthings.registry;

import dev.ftb.mods.ftbstuffnthings.advancements.CustomTrigger;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraftforge.eventbus.api.IEventBus;

public class CriterionTriggerRegistry {
    public static final CustomTrigger FTBSTUFF_ROOT = new CustomTrigger("root");
    public static final CustomTrigger SUPERCHARGED = new CustomTrigger("supercharged");

    public static void init(IEventBus modEventBus) {
        CriteriaTriggers.register(FTBSTUFF_ROOT);
        CriteriaTriggers.register(SUPERCHARGED);
    }
}
