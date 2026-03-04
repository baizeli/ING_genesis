package miku.united_as_one.genesis.common.event.item.axe;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.event.item.VioletParticleEvents;
import miku.united_as_one.genesis.common.items.tool.axe.VioletAxe;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletAxeEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();

        if (stack.getItem() instanceof VioletAxe) {
            if (event.getState().is(BlockTags.LOGS)) {
                breakTree((Level) event.getLevel(), event.getPos(), player);
            }
        }
    }

    private static void breakTree(Level level, BlockPos pos, Player player) {
        HashSet<BlockPos> logs = new HashSet<>();
        findLogs(level, pos, logs);
        for (BlockPos logPos : logs) {
            level.destroyBlock(logPos, true, player);
        }
    }

    private static void findLogs(Level level, BlockPos pos, HashSet<BlockPos> found) {
        if (found.size() > 128 || found.contains(pos)) return;
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.LOGS)) {
            found.add(pos);
            for (BlockPos neighbor : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                findLogs(level, neighbor.immutable(), found);
            }
        }
    }
}