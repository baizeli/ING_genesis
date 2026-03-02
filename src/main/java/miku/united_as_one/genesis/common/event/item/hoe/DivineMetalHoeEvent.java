package miku.united_as_one.genesis.common.event.item.hoe;

import miku.united_as_one.genesis.common.items.tool.hoe.DivineMetalHoe;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class DivineMetalHoeEvent {
    
    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        if (player.getMainHandItem().getItem() instanceof DivineMetalHoe && !player.isCreative()) {
            List<ItemStack> originalDrops = Block.getDrops(state, (ServerLevel) level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());

            // 小麦=面包
            if (state.getBlock() == Blocks.WHEAT) {
                level.destroyBlock(pos, false);
                for (ItemStack drop : originalDrops) {
                    if (drop.getItem() == Items.WHEAT) {
                        level.addFreshEntity(
                            new ItemEntity(level,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                new ItemStack(Items.BREAD, drop.getCount())
                            )
                        );
                    } else {
                        level.addFreshEntity(
                            new ItemEntity(level,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                drop
                            )
                        );
                    }
                }
            }

            // 马铃薯=烤马铃薯
            if (state.getBlock() == Blocks.POTATOES) {
                level.destroyBlock(pos, false);
                for (ItemStack drop : originalDrops) {
                    if (drop.getItem() == Items.POTATO) {
                        level.addFreshEntity(
                            new ItemEntity(level,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                new ItemStack(Items.BAKED_POTATO, drop.getCount())
                            )
                        );
                    } else {
                        level.addFreshEntity(
                            new ItemEntity(level,
                                pos.getX() + 0.5,
                                pos.getY() + 0.5,
                                pos.getZ() + 0.5,
                                drop
                            )
                        );
                    }
                }
            }
        }
    }
}