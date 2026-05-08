package miku.united_as_one.genesis.handlers;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockToolEvents {

    @SubscribeEvent
    public static void onAxeStrip(BlockEvent.BlockToolModificationEvent event) {
        if (!ToolActions.AXE_STRIP.equals(event.getToolAction())) {
            return;
        }

        BlockState state = event.getState();
        Block strippedBlock = getStrippedBlock(state.getBlock());
        if (strippedBlock != null && state.hasProperty(RotatedPillarBlock.AXIS)) {
            event.setFinalState(strippedBlock.defaultBlockState()
                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)));
        }
    }

    private static Block getStrippedBlock(Block block) {
        if (block == BlockRegistry.SWAY_LOG.getBase().get()) {
            return BlockRegistry.SWAY_LOG.getStrippedLog().orElseThrow().get();
        }
        if (block == BlockRegistry.SWAY_LOG.getWood().orElseThrow().get()) {
            return BlockRegistry.SWAY_LOG.getStrippedWood().orElseThrow().get();
        }
        if (block == BlockRegistry.QUIETNESS_LOG.getBase().get()) {
            return BlockRegistry.QUIETNESS_LOG.getStrippedLog().orElseThrow().get();
        }
        if (block == BlockRegistry.QUIETNESS_LOG.getWood().orElseThrow().get()) {
            return BlockRegistry.QUIETNESS_LOG.getStrippedWood().orElseThrow().get();
        }
        return null;
    }
}
