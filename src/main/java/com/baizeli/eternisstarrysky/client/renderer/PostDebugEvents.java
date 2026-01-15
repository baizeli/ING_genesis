package com.baizeli.eternisstarrysky.client.renderer;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.client.particles.ModParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.baizeli.eternisstarrysky.Items.ModItems.INFINITY_SWORD;

@Mod.EventBusSubscriber(
        modid = EternisStarrySky.MOD_ID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT
)
public class PostDebugEvents {

    public static final ResourceLocation DISTORT =
            new ResourceLocation(EternisStarrySky.MOD_ID, "shaders/post/distort.json");

    /** 当前是否启用 Distort */
    private static boolean distortEnabled = false;

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickItem event) {
        if (!event.getLevel().isClientSide()) return;
        if (event.getItemStack().getItem() != INFINITY_SWORD.get()) return;

        Minecraft mc = Minecraft.getInstance();

        try {
            if(mc.player.isShiftKeyDown()){
                if (!distortEnabled ) {
                    // === 开启 ===
                    if (mc.gameRenderer.currentEffect() != null) {
                        mc.gameRenderer.shutdownEffect();
                    }

                    mc.gameRenderer.loadEffect(DISTORT);
                    distortEnabled = true;

                    mc.player.displayClientMessage(
                            Component.literal("§dDistort Enabled"),
                            true
                    );

                } else {
                    // === 关闭 ===
                    mc.gameRenderer.shutdownEffect();
                    distortEnabled = false;

                    mc.player.displayClientMessage(
                            Component.literal("§7Distort Disabled"),
                            true
                    );
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            distortEnabled = false;
        }

        Vec3 look = mc.player.getLookAngle(); // 玩家朝向
        Vec3 pos = mc.player.position().add(0, mc.player.getEyeHeight(), 0); // 从眼睛位置发射

        // 在玩家面前的扇形区域发射10个粒子
        for (int i = 0; i < 10; i++) {
            // 计算随机角度偏移，形成扇形分布
            double angleOffset = (i - 4.5) * 0.2; // 使粒子分布在约±90度的扇形区域内
            double cosOffset = Math.cos(angleOffset);
            double sinOffset = Math.sin(angleOffset);
            
            // 根据玩家的朝向计算新的方向向量
            Vec3 horizontalLook = new Vec3(look.x, 0, look.z).normalize();
            Vec3 perpendicular = new Vec3(-horizontalLook.z, 0, horizontalLook.x).normalize(); // 垂直于朝向的水平向量
            
            // 将原始朝向向量按随机角度旋转
            Vec3 rotatedLook = new Vec3(
                look.x * cosOffset + perpendicular.x * sinOffset,
                look.y,
                look.z * cosOffset + perpendicular.z * sinOffset
            ).normalize();
            
            // 计算粒子位置（稍微向前一点的位置）
            Vec3 particlePos = pos.add(rotatedLook.scale(1.0));
            
            // 设置粒子速度方向
            Vec3 direction = new Vec3(rotatedLook.x * 2.0, rotatedLook.y * 2.0, rotatedLook.z * 2.0);
            
            mc.level.addParticle(ModParticles.CRESCENT_BLADE.get(),
                    particlePos.x, particlePos.y, particlePos.z,
                    direction.x, direction.y, direction.z);
        }


    }
}
