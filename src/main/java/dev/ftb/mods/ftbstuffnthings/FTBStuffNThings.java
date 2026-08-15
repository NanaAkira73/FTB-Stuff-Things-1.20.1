package dev.ftb.mods.ftbstuffnthings;

import com.mojang.logging.LogUtils;
import dev.ftb.mods.ftbstuffnthings.client.FTBStuffNThingsClient;
import dev.ftb.mods.ftbstuffnthings.crafting.RecipeCaches;
import dev.ftb.mods.ftbstuffnthings.network.NetworkHandler;
import dev.ftb.mods.ftbstuffnthings.network.SyncLootSummaryPacket;
import dev.ftb.mods.ftbstuffnthings.registry.*;
import dev.ftb.mods.ftbstuffnthings.util.lootsummary.LootSummaryCollection;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Mod(FTBStuffNThings.MODID)
public class FTBStuffNThings {
    public static final String MODID = "ftbstuff";

    public static final Logger LOGGER = LogUtils.getLogger();

    public FTBStuffNThings(IEventBus modEventBus) {
        // Config is registered via SNBTConfig system in 1.20.1 FTB Library
        Config.CONFIG.load(FMLPaths.CONFIGDIR.get().resolve(MODID + ".snbt"));

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> () -> new FTBStuffNThingsClient(modEventBus));

        BlocksRegistry.init(modEventBus);
        ItemsRegistry.init(modEventBus);
        BlockEntitiesRegistry.init(modEventBus);
        RecipesRegistry.init(modEventBus);
        ContentRegistry.init(modEventBus);
        CriterionTriggerRegistry.init(modEventBus);

        NetworkHandler.init();

        MinecraftForge.EVENT_BUS.addListener(this::addReloadListeners);
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerJoin);
    }

    private void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncLootSummaries(serverPlayer);
            CriterionTriggerRegistry.FTBSTUFF_ROOT.trigger(serverPlayer);
        }
    }

    public static void syncLootSummaries(ServerPlayer serverPlayer) {
        LootSummaryCollection lsc = new LootSummaryCollection();

        Config.getStrainerLootTable().ifPresent(lootTableId -> BlocksRegistry.waterStrainers().forEach(b ->
                lsc.addEntry(ResourceKey.create(Registries.BLOCK, b.getId()), lootTableId, makeBlockParams(serverPlayer, b.get().defaultBlockState())))
        );
        BlocksRegistry.BARRELS.forEach(b ->
                lsc.addEntry(b.getKey(), blockLootTable(b), makeBlockParams(serverPlayer, b.get().defaultBlockState()))
        );
        BlocksRegistry.CRATES.forEach(b ->
                lsc.addEntry(b.getKey(), blockLootTable(b), makeBlockParams(serverPlayer, b.get().defaultBlockState()))
        );

        NetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new SyncLootSummaryPacket(lsc));
    }

    private static LootParams makeBlockParams(ServerPlayer serverPlayer, BlockState state) {
        return new LootParams.Builder(serverPlayer.serverLevel())
                .withParameter(LootContextParams.BLOCK_STATE, state)
                .withParameter(LootContextParams.ORIGIN, Vec3.ZERO)
                .withParameter(LootContextParams.TOOL, Items.DIAMOND_PICKAXE.getDefaultInstance())
                .withOptionalParameter(LootContextParams.THIS_ENTITY, serverPlayer)
                .create(LootContextParamSets.BLOCK);
    }

    private static ResourceLocation blockLootTable(RegistryObject<Block> db) {
        return ResourceLocation.fromNamespaceAndPath(db.getId().getNamespace(), "blocks/" + db.getId().getPath());
    }

    private void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new CacheReloadListener());
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static class CacheReloadListener implements PreparableReloadListener {
        @Override
        public CompletableFuture<Void> reload(PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return CompletableFuture.runAsync(RecipeCaches::clearAll, gameExecutor).thenCompose(stage::wait);
        }
    }
}