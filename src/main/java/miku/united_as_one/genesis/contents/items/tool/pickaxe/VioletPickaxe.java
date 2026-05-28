package miku.united_as_one.genesis.contents.items.tool.pickaxe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class VioletPickaxe extends PickaxeItem {

    public VioletPickaxe(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, (int) -tier.getAttackDamageBonus(), 0.0F, properties);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, Player player) {
        Level level = player.level();

        if (player.isCrouching() && !level.isClientSide) {
            // 使用射线检测确定挖掘的面
            var hitResult = player.pick(5.0D, 0.0F, false);
            if (hitResult instanceof net.minecraft.world.phys.BlockHitResult blockHit) {
                Direction face = blockHit.getDirection();
                this.breakThreeByThree(level, pos, face, player, itemstack);
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    private void breakThreeByThree(Level level, BlockPos center, Direction face, Player player, ItemStack stack) {
        boolean anyBroken = false;

        for (int h = -1; h <= 1; h++) {
            for (int v = -1; v <= 1; v++) {
                if (h == 0 && v == 0) continue;

                BlockPos targetPos = getRelativePos(center, face, h, v);
                BlockState state = level.getBlockState(targetPos);

                if (state.getDestroySpeed(level, targetPos) != -1.0F && this.isCorrectToolForDrops(stack, state)) {
                    BlockEntity blockEntity = level.getBlockEntity(targetPos);
                    Block.dropResources(state, level, targetPos, blockEntity, player, stack);
                    level.removeBlock(targetPos, false);
                    stack.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(player.getUsedItemHand()));
                    anyBroken = true;
                }
            }
        }

        if (anyBroken) {
            level.playSound(null, center, SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.united_as_one.violet_pickaxe.line1"));
    }

    private BlockPos getRelativePos(BlockPos origin, Direction face, int h, int v) {
        return switch (face) {
            case DOWN, UP -> origin.offset(h, 0, v);
            case NORTH, SOUTH -> origin.offset(h, v, 0);
            case WEST, EAST -> origin.offset(0, v, h);
        };
    }
}
