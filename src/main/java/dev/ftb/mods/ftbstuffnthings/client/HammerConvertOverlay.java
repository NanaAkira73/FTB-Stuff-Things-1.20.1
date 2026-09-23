package dev.ftb.mods.ftbstuffnthings.client;

import dev.ftb.mods.ftbstuffnthings.FTBStuffNThings;
import dev.ftb.mods.ftbstuffnthings.crafting.NoInventory;
import dev.ftb.mods.ftbstuffnthings.crafting.recipe.HammerRecipe;
import dev.ftb.mods.ftbstuffnthings.items.HammerItem;
import dev.ftb.mods.ftbstuffnthings.registry.RecipesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * HUD preview ported from the original FTB StoneBlock Companion HammerOverlay.
 *
 * While a hammer is held in the main hand and the player is looking at a block, a small two-slot
 * widget is drawn above the hotbar: the left slot shows the targeted block, the right slot shows
 * what hammering it produces (cycling every ~60 ticks when there is more than one possible result).
 * The second frame of the sprite sheet is revealed left to right as the block is broken, acting as
 * a mining progress indicator.
 */
public class HammerConvertOverlay implements IGuiOverlay {
    private static final ResourceLocation TEXTURE = FTBStuffNThings.id("textures/hammer_convert.png");

    private static final int WIDGET_WIDTH = 58;
    private static final int WIDGET_HEIGHT = 16;
    private static final int TEXTURE_HEIGHT = 32;

    /** Slot item offsets inside the widget, matching the original overlay. */
    private static final int LEFT_SLOT_X = 3;
    private static final int RIGHT_SLOT_X = 45;
    private static final int SLOT_Y = 3;

    /** The original rendered 10x10 item icons centred in the 16x16 slots. */
    private static final float SLOT_ITEM_SCALE = 10F / 16F;

    private static final long CACHE_LIFETIME_MS = 60_000L;
    private static final Map<Block, List<ItemStack>> CACHE = new HashMap<>();
    private static long cacheBuiltAt = 0L;

    private static float tick = 0F;
    private static int index = 0;

    @Override
    public void render(ForgeGui gui, GuiGraphics graphics, float partialTick, int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        MultiPlayerGameMode gameMode = mc.gameMode;
        Level level = mc.level;

        if (player == null || level == null || gameMode == null) {
            return;
        }
        if (!(player.getMainHandItem().getItem() instanceof HammerItem)) {
            return;
        }

        HitResult pick = player.pick(gameMode.getPickRange(), partialTick, false);
        if (pick.getType() != HitResult.Type.BLOCK || !(pick instanceof BlockHitResult blockHit)) {
            return;
        }

        BlockState state = level.getBlockState(blockHit.getBlockPos());
        List<ItemStack> results = resultsFor(level, state.getBlock());
        if (results.isEmpty()) {
            return;
        }

        int x = (screenWidth / 2) - (WIDGET_WIDTH / 2);
        int y = screenHeight - 80;

        graphics.setColor(1F, 1F, 1F, 1F);
        graphics.blit(TEXTURE, x, y, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, WIDGET_WIDTH, TEXTURE_HEIGHT);

        float progress = gameMode.destroyProgress;
        if (progress > 0F) {
            int progressWidth = 17 + (int) (progress * 23F);
            graphics.blit(TEXTURE, x, y, 0, WIDGET_HEIGHT, progressWidth, WIDGET_HEIGHT, WIDGET_WIDTH, TEXTURE_HEIGHT);
        }

        tick += mc.getDeltaFrameTime();
        if (tick > 60F) {
            tick = 0F;
            index++;
        }
        if (index >= results.size()) {
            index = 0;
        }

        renderScaledItem(graphics, new ItemStack(state.getBlock()), x + LEFT_SLOT_X, y + SLOT_Y);
        renderScaledItem(graphics, results.get(index), x + RIGHT_SLOT_X, y + SLOT_Y);
        graphics.setColor(1F, 1F, 1F, 1F);
    }

    private static void renderScaledItem(GuiGraphics graphics, ItemStack stack, int x, int y) {
        var pose = graphics.pose();
        pose.pushPose();
        pose.translate(x, y, 0F);
        pose.scale(SLOT_ITEM_SCALE, SLOT_ITEM_SCALE, 1F);
        graphics.renderItem(stack, 0, 0);
        pose.popPose();
    }

    /**
     * Collects every hammer recipe result which applies to the given block. Results are cached per
     * block for a minute so the recipe manager is not walked on every single frame.
     */
    private static List<ItemStack> resultsFor(Level level, Block block) {
        long now = System.currentTimeMillis();
        if (now - cacheBuiltAt > CACHE_LIFETIME_MS) {
            CACHE.clear();
            cacheBuiltAt = now;
        }

        List<ItemStack> cached = CACHE.get(block);
        if (cached != null) {
            return cached;
        }

        ItemStack probe = new ItemStack(block);
        Set<Item> drops = new LinkedHashSet<>();
        for (HammerRecipe recipe : level.getRecipeManager().getRecipesFor(RecipesRegistry.HAMMER_TYPE.get(), NoInventory.INSTANCE, level)) {
            if (recipe.getIngredient().test(probe)) {
                for (ItemStack stack : recipe.getResults()) {
                    drops.add(stack.getItem());
                }
            }
        }

        List<ItemStack> list = new ArrayList<>(drops.size());
        for (Item item : drops) {
            list.add(new ItemStack(item));
        }
        CACHE.put(block, list);
        return list;
    }
}
