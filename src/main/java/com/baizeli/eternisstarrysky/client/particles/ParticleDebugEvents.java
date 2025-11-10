package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.baizeli.eternisstarrysky.Items.ModItems.INFINITY_SWORD;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ParticleDebugEvents {


    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem() == INFINITY_SWORD.get()) {
            Player entity = event.getEntity();
            Level level = event.getLevel();
            if (level instanceof ServerLevel serverLevel) {
                Vec3 lookVec = entity.getLookAngle();
                cubeTest(serverLevel, entity, lookVec);
                cubeTest(serverLevel, entity, lookVec);
                testTest(serverLevel, entity, lookVec);
                testTestA(serverLevel, entity, lookVec);
                testTestb(serverLevel, entity, lookVec);
            }
        }
    }

    private static void cubeTest(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.CUBE.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTest(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.TEST.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTestA(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.TESTA.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTestb(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.TESTB.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }


}


