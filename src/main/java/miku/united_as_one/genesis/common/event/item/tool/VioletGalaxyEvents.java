package miku.united_as_one.genesis.common.event.item.tool;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.items.tool.VioletGalaxyingotTool;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletGalaxyEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        // 1. 剑右键格挡减伤
        if (event.getEntity() instanceof Player player && player.isUsingItem()) {
            if (player.getUseItem().getItem() instanceof VioletGalaxyingotTool.Sword) {
                event.setAmount(event.getAmount() * 0.5F);
            }
        }

        // 2. 3x3 扩散攻击修复：通过距离判定
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            if (weapon.getItem() instanceof VioletGalaxyingotTool.Sword) {
                LivingEntity victim = event.getEntity();
                Level level = victim.level();
                // 找到受害者周围 3.0 距离内的敌人
                AABB area = victim.getBoundingBox().inflate(1.5D, 1.0D, 1.5D);
                level.getEntitiesOfClass(LivingEntity.class, area).forEach(target -> {
                    if (target != player && target != victim) {
                        // 扩散伤害设置为原伤害的 80%，避免无限循环
                        target.hurt(level.damageSources().playerAttack(player), event.getAmount() * 0.8F);
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        // 1. 斧头一键砍树
        if (stack.getItem() instanceof VioletGalaxyingotTool.Axe) {
            if (event.getState().is(BlockTags.LOGS)) {
                breakTree(level, pos, player);
            }
        }

        // 2. 铲子 5x5 自适应方向挖掘
        if (stack.getItem() instanceof VioletGalaxyingotTool.Shovel && player.isShiftKeyDown()) {
            // 获取玩家看向的方块面
            net.minecraft.world.phys.HitResult hitResult = player.pick(5.0D, 0.0F, false);
            if (hitResult instanceof net.minecraft.world.phys.BlockHitResult blockHit) {
                net.minecraft.core.Direction face = blockHit.getDirection();

                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i == 0 && j == 0) continue;

                        BlockPos target;
                        // 根据朝向计算 5x5 平面
                        if (face.getAxis() == net.minecraft.core.Direction.Axis.Y) {
                            // 挖地板或天花板 (X-Z平面)
                            target = pos.offset(i, 0, j);
                        } else if (face.getAxis() == net.minecraft.core.Direction.Axis.X) {
                            // 挖东西向的墙 (Y-Z平面)
                            target = pos.offset(0, i, j);
                        } else {
                            // 挖南北向的墙 (X-Y平面)
                            target = pos.offset(i, j, 0);
                        }

                        BlockState state = level.getBlockState(target);
                        // 增加硬度判断，防止破坏基岩或空气
                        if (stack.isCorrectToolForDrops(state) && state.getDestroySpeed(level, target) >= 0) {
                            level.destroyBlock(target, true, player);
                        }
                    }
                }
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