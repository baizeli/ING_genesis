package miku.united_as_one.genesis.common.event;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.registry.FluidRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.FluidState;
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
        if (event.getState().getFluidState().getType() == FluidRegistry.SOURCE_FLUID.get() ||
                event.getState().getFluidState().getType() == FluidRegistry.SOURCE_FLUID.getSource() ||
                event.getState().getFluidState().getType() == FluidRegistry.BLACKWATER_FLUID.get() ||
                event.getState().getFluidState().getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
            event.setResult(Event.Result.ALLOW);
        }
    }

    @SubscribeEvent
    public static void onBucketFill(FillBucketEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getTarget() != null && event.getTarget().getType() == net.minecraft.world.phys.HitResult.Type.BLOCK) {
            net.minecraft.world.phys.BlockHitResult blockHit = (net.minecraft.world.phys.BlockHitResult) event.getTarget();
            FluidState state = event.getLevel().getFluidState(blockHit.getBlockPos());
            if (state.getType() == FluidRegistry.SOURCE_FLUID.getSource() || state.getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
                event.getLevel().playSound(null, blockHit.getBlockPos(), SoundEvents.BUCKET_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);
            }
        }
    }

    @SubscribeEvent
    public static void onBucketEmpty(PlayerInteractEvent.RightClickBlock event) {
        if (event.getLevel().isClientSide) return;

        if (event.getItemStack().getItem() == FluidRegistry.SOURCE_FLUID.getBucket().get() ||
                event.getItemStack().getItem() == FluidRegistry.BLACKWATER_FLUID.getBucket().get()) {
            event.getLevel().playSound(null, event.getPos(), SoundEvents.BUCKET_EMPTY, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.player.level().isClientSide) {
            Player player = event.player;
            if (player.isAlive()) {
                FluidState state = player.level().getFluidState(player.blockPosition());
                if (state.getType() == FluidRegistry.BLACKWATER_FLUID.get() || state.getType() == FluidRegistry.BLACKWATER_FLUID.getSource()) {
                    if (!player.hasEffect(MobEffects.WATER_BREATHING)) {
                        if (player.tickCount % 10 == 0) {
                            player.hurt(player.damageSources().magic(), 4.0F);
                        }
                    }
                }
            }
        }
    }
}