package miku.united_as_one.genesis.handlers.item;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.items.tool.axe.VioletAxe;
import miku.united_as_one.genesis.contents.items.tool.hoe.VioletHoe;
import miku.united_as_one.genesis.contents.items.tool.pickaxe.VioletPickaxe;
import miku.united_as_one.genesis.contents.items.tool.shovel.VioletShovel;
import miku.united_as_one.genesis.contents.items.weapon.sword.VioletSword;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletParticleEvents {

    /**
     * 关键修正：虽然 BreakEvent 只触发一次，但在连锁逻辑中，
     * 如果你使用的是 level.destroyBlock(pos, true, player)，
     * 某些版本会触发销毁后的某些事件。
     * 如果依然只有一个，我们需要利用一个“标记”或直接在底层拦截。
     */
    @SubscribeEvent
    public static void onAnyBlockDestroyed(BlockEvent.BreakEvent event) {
        // 依然保留这个用于玩家手动破坏的那一下
        handleParticle(event);
    }

    /**
     * 针对连锁逻辑的补救措施：
     * 因为你不愿意改动其他类，我们在这里监听“方块被破坏后”的通知。
     * 注意：这个事件在 1.20.1 中非常灵敏。
     */
    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
        // 这是一个变通方案：连锁破坏通常会引起频繁的邻居更新
        // 如果检测到当前玩家手里拿着紫极工具，就在更新点产生粒子
        if (event.getLevel() instanceof ServerLevel serverLevel) {
            // 获取最近的玩家（连锁发起者）
            Player player = serverLevel.getNearestPlayer(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), 5.0D, false);
            if (player != null && isVioletTool(player.getMainHandItem().getItem())) {
                spawnParticles(serverLevel, event.getPos());
            }
        }
    }

    private static void handleParticle(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && isVioletTool(player.getMainHandItem().getItem())) {
            spawnParticles(event.getLevel(), event.getPos());
        }
    }

    private static boolean isVioletTool(Item item) {
        return item instanceof VioletSword || item instanceof VioletAxe ||
                item instanceof VioletPickaxe || item instanceof VioletShovel || item instanceof VioletHoe;
    }

    public static void spawnParticles(LevelAccessor level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.PORTAL,
                    pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D,
                    6, 0.2, 0.2, 0.2, 0.05);
        }
    }
}