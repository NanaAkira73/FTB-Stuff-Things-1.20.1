package dev.ftb.mods.ftbstuffnthings.crafting;

import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstuffnthings.Config;
import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;
import net.minecraftforge.fml.loading.FMLLoader;

public enum DevEnvironmentCondition implements ICondition {
    INSTANCE;

    public static final IConditionSerializer<DevEnvironmentCondition> SERIALIZER = new IConditionSerializer<>() {
        @Override
        public void write(JsonObject json, DevEnvironmentCondition value) {
        }

        @Override
        public DevEnvironmentCondition read(JsonObject json) {
            return INSTANCE;
        }

        @Override
        public ResourceLocation getID() {
            return INSTANCE.getID();
        }
    };

    @Override
    public ResourceLocation getID() {
        return FTBStuffNThings.id("dev_environment");
    }

    @Override
    public boolean test(IContext context) {
        return Config.INCLUDE_DEV_RECIPES.get() || !FMLLoader.isProduction();
    }
}
