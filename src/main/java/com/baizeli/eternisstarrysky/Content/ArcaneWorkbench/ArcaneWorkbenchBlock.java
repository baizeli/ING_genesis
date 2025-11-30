package com.baizeli.eternisstarrysky.Content.ArcaneWorkbench;

import com.baizeli.eternisstarrysky.Content.Workbenchs.ModBlockEntities;
import com.baizeli.eternisstarrysky.Content.Workbenchs.ModMenuTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import static com.baizeli.eternisstarrysky.Util.i18nUtil.translatableContainerName;

public class ArcaneWorkbenchBlock extends Block implements EntityBlock {

    public ArcaneWorkbenchBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ArcaneWorkbenchBlockEntity(ModBlockEntities.ARCANE_WORKBENCH.get(), blockPos, blockState);
    }

    @Override
    public String getDescriptionId() {
        return super.getDescriptionId();
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof ArcaneWorkbenchBlockEntity) {
                MenuProvider menuProvider = new SimpleMenuProvider(
                        (containerId, playerInventory, playerEntity) ->
                                new ArcaneWorkbenchMenu(
                                        ModMenuTypes.ARCANE_WORKBENCH_MENU.get(),
                                        containerId,
                                        playerInventory,
                                        (ArcaneWorkbenchBlockEntity) blockEntity),
                        translatableContainerName("arcane_workbench")
                );
                NetworkHooks.openScreen((net.minecraft.server.level.ServerPlayer) player, menuProvider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}