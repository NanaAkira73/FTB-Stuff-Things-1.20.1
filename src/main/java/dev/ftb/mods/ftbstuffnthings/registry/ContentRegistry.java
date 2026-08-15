package dev.ftb.mods.ftbstuffnthings.registry;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.blocks.fusingmachine.FusingMachineMenu;
import dev.ftb.mods.ftbstuffnthings.blocks.jar.TemperedJarMenu;
import dev.ftb.mods.ftbstuffnthings.blocks.strainer.WaterStrainerMenu;
import dev.ftb.mods.ftbstuffnthings.blocks.supercooler.SuperCoolerMenu;
import dev.ftb.mods.ftbstuffnthings.crafting.DevEnvironmentCondition;
import dev.ftb.mods.ftbstuffnthings.lootmodifiers.CrookModifier;
import dev.ftb.mods.ftbstuffnthings.lootmodifiers.HammerModifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ContentRegistry {
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FTBStuffNThings.MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register("obb_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("ftbstuff.itemGroup.tab"))
            .icon(() -> new ItemStack(BlocksRegistry.OAK_SLUICE.get()))
            .displayItems((parameters, output) -> {
                for (RegistryObject<Item> entry : ItemsRegistry.ITEMS.getEntries()) {
                    output.accept(new ItemStack(entry.get()));
                }
            }).build());

    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, FTBStuffNThings.MODID);

    public static final RegistryObject<MenuType<TemperedJarMenu>> TEMPERED_JAR_MENU = registerMenu("tempered_jar", TemperedJarMenu::fromNetwork);
    public static final RegistryObject<MenuType<FusingMachineMenu>> FUSING_MACHINE_MENU = registerMenu("fusing_machine", FusingMachineMenu::new);
    public static final RegistryObject<MenuType<SuperCoolerMenu>> SUPER_COOLER_MENU = registerMenu("super_cooler", SuperCoolerMenu::new);
    public static final RegistryObject<MenuType<WaterStrainerMenu>> WATER_STRAINER_MENU = registerMenu("water_strainer", WaterStrainerMenu::new);

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS_REGISTRY
            = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, FTBStuffNThings.MODID);

    public static final Supplier<Codec<? extends IGlobalLootModifier>> HAMMER_LOOT_MODIFIER
            = LOOT_MODIFIERS_REGISTRY.register("hammer_loot_modifier", HammerModifier.CODEC);
    public static final Supplier<Codec<? extends IGlobalLootModifier>> CROOK_LOOT_MODIFIER
            = LOOT_MODIFIERS_REGISTRY.register("crook_loot_modifier", CrookModifier.CODEC);

    public static void init(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
        MENU_TYPES.register(bus);
        LOOT_MODIFIERS_REGISTRY.register(bus);

        CraftingHelper.register(DevEnvironmentCondition.SERIALIZER);
    }

    private static <C extends AbstractContainerMenu> RegistryObject<MenuType<C>> registerMenu(String name, IContainerFactory<C> f) {
        return MENU_TYPES.register(name, () -> IForgeMenuType.create(f));
    }
}
