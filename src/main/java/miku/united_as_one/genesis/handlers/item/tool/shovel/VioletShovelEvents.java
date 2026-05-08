package miku.united_as_one.genesis.handlers.item.shovel;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.contents.items.tool.shovel.VioletShovel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MODID)
public class VioletShovelEvents {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = player.getMainHandItem();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();

        // 仅在蹲下时触发 5x5 范围挖掘
        if (stack.getItem() instanceof VioletShovel && player.isShiftKeyDown()) {
            HitResult hitResult = player.pick(5.0D, 0.0F, false);
            if (hitResult instanceof BlockHitResult blockHit) {
                Direction face = blockHit.getDirection();

                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i == 0 && j == 0) continue;

                        BlockPos target;
                        if (face.getAxis() == Direction.Axis.Y) {
                            target = pos.offset(i, 0, j);
                        } else if (face.getAxis() == Direction.Axis.X) {
                            target = pos.offset(0, i, j);
                        } else {
                            target = pos.offset(i, j, 0);
                        }

                        BlockState state = level.getBlockState(target);
                        if (stack.isCorrectToolForDrops(state) && state.getDestroySpeed(level, target) >= 0) {
                            level.destroyBlock(target, true, player);
                        }
                    }
                }
            }
        }
    }
}