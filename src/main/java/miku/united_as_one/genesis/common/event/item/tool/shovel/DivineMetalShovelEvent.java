package miku.united_as_one.genesis.common.event.item.tool.shovel;

import miku.united_as_one.genesis.common.items.tool.shovel.DivineMetalShovel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DivineMetalShovelEvent {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        // 沙子=玻璃
        if (player.getMainHandItem().getItem() instanceof DivineMetalShovel && player.isShiftKeyDown() && !player.isCreative()) {
            if (state.getBlock() == Blocks.SAND) {
                level.destroyBlock(pos, false);
                Block.popResource(level, pos, new ItemStack(Blocks.GLASS));
                if (level instanceof ServerLevel serverLevel) serverLevel.sendParticles(ParticleTypes.FLAME,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    10, 0.5,  0.5, 0.5, 0.01
                );
            }
        }
    }
}