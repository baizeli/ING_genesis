package miku.united_as_one.genesis.common.event;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.FluidRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class FluidCommonEvents {

    @SubscribeEvent
    public static void onCreateFluidSource(BlockEvent.CreateFluidSourceEvent event) {
        if (isCustomFluid(event.getState().getFluidState())) event.setResult(Event.Result.ALLOW);
    }

    @SubscribeEvent
    public static void onBucketFill(FillBucketEvent event) {
        if (event.getLevel().isClientSide() || event.getTarget() == null || event.getTarget().getType() != net.minecraft.world.phys.HitResult.Type.BLOCK) return;
        net.minecraft.world.phys.BlockHitResult hit = (net.minecraft.world.phys.BlockHitResult) event.getTarget();
        if (isCustomFluid(event.getLevel().getFluidState(hit.getBlockPos()))) {
            event.getLevel().playSound(null, hit.getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onBucketEmpty(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;
        var item = event.getItemStack().getItem();
        if (isCustomBucket(item)) {
            event.getLevel().playSound(null, event.getPos(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            FluidState state = player.level().getFluidState(player.blockPosition());
            if (state.getType() == FluidRegistry.BLACKWATER_FLUID.get() || state.getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
                if (!player.hasEffect(MobEffects.WATER_BREATHING) && player.tickCount % 10 == 0) {
                    player.hurt(player.damageSources().magic(), 4.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onNeighborNotify(BlockEvent.NeighborNotifyEvent event) {
    }

    private static boolean isCustomFluid(FluidState state) {
        var t = state.getType();
        return t == FluidRegistry.SOURCE_FLUID.get() || t == FluidRegistry.SOURCE_FLUID.getSource() ||
                t == FluidRegistry.BLACKWATER_FLUID.get() || t == FluidRegistry.BLACKWATER_FLUID.getSource() ||
                t == FluidRegistry.BLOOD_FLUID.get() || t == FluidRegistry.BLOOD_FLUID.getSource();
    }

    private static boolean isCustomBucket(net.minecraft.world.item.Item item) {
        return item == FluidRegistry.SOURCE_FLUID.getBucket().get() ||
                item == FluidRegistry.BLACKWATER_FLUID.getBucket().get() ||
                item == FluidRegistry.BLOOD_FLUID.getBucket().get();
    }
}