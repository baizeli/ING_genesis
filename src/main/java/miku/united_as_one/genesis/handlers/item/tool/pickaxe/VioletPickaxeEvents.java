package miku.united_as_one.genesis.handlers.item.pickaxe;


import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.items.tool.pickaxe.VioletPickaxe;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletPickaxeEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();

        // 判定：如果是紫极镐且当前方块是矿石
        if (stack.getItem() instanceof VioletPickaxe && isOre(event.getState())) {
            // 执行连锁逻辑
            performChainMining((Level) event.getLevel(), event.getPos(), player, event.getState().getBlock());
        }
    }

    /**
     * 判定是否为矿石（支持原版标签及 Forge 通用矿石标签）
     */
    private static boolean isOre(BlockState state) {
        return state.is(Tags.Blocks.ORES) || state.is(BlockTags.COAL_ORES) ||
                state.is(BlockTags.IRON_ORES) || state.is(BlockTags.GOLD_ORES) ||
                state.is(BlockTags.DIAMOND_ORES) || state.is(BlockTags.REDSTONE_ORES) ||
                state.is(BlockTags.LAPIS_ORES) || state.is(BlockTags.EMERALD_ORES) ||
                state.is(BlockTags.COPPER_ORES);
    }

    /**
     * 连锁挖掘核心逻辑 (广度优先搜索)
     */
    private static void performChainMining(Level level, BlockPos startPos, Player player, Block targetBlock) {
        Queue<BlockPos> queue = new ArrayDeque<>();
        HashSet<BlockPos> visited = new HashSet<>();

        queue.add(startPos);
        visited.add(startPos);

        int count = 0;
        int maxBlocks = 64; // 连锁上限，保护服务器性能

        while (!queue.isEmpty() && count < maxBlocks) {
            BlockPos current = queue.poll();

            // 排除起始点（起始点由 BreakEvent 正常处理）
            if (!current.equals(startPos)) {
                BlockState state = level.getBlockState(current);
                if (state.is(targetBlock)) {
                    // 破坏方块并掉落物品
                    level.destroyBlock(current, true, player);
                    count++;
                } else {
                    continue;
                }
            }

            // 检查周围 26 个方向 (3x3x3 范围)
            for (BlockPos neighbor : BlockPos.betweenClosed(current.offset(-1, -1, -1), current.offset(1, 1, 1))) {
                BlockPos immutablePos = neighbor.immutable();
                if (!visited.contains(immutablePos)) {
                    visited.add(immutablePos);
                    queue.add(immutablePos);
                }
            }
        }
    }
}