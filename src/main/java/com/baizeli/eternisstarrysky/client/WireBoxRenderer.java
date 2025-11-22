package com.baizeli.eternisstarrysky.client;

import com.baizeli.eternisstarrysky.util.EntityData;
import com.baizeli.eternisstarrysky.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WireBoxRenderer {
    public static HashMap<Entity, EntityData> entitiesForRenderWireBoxRenderer = new HashMap<>();
    public static final HashMap<Entity, Float> entityRotationMap = new HashMap<>();
    public static final HashMap<Entity, Direction.Axis> entityAxisMap = new HashMap<>();
    private static final Random random = new Random();

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();

        if (entitiesForRenderWireBoxRenderer.containsKey(entity)) {
            long expireTime = entitiesForRenderWireBoxRenderer.get(entity).time;

            Level level = entity.level();
            if (level.getGameTime() >= expireTime) {   // 时间到了
                entitiesForRenderWireBoxRenderer.remove(entity);
                entityRotationMap.remove(entity);
                entityAxisMap.remove(entity);
                return;
            }
            PoseStack stack = event.getPoseStack();

            AABB box = entity.getBoundingBox();

            double cx = (box.minX + box.maxX) / 2D - entity.getX();
            double cy = (box.minY + box.maxY) / 2D - entity.getY();
            double cz = (box.minZ + box.maxZ) / 2D - entity.getZ();

            float side = (float) (box.maxX - box.minX) * 2.5f;

            // 获取或生成随机角度
            float angle = entityRotationMap.computeIfAbsent(entity, e -> random.nextFloat() * 360F);
            Direction.Axis axis = entityAxisMap.computeIfAbsent(entity, e -> {
                Direction.Axis picked;
                do {
                    picked = Direction.Axis.values()[random.nextInt(Direction.Axis.values().length)];
                } while (picked == Direction.Axis.Z);   // 抽到 Z 就重来
                return picked;
            });

            Axis rotationAxis = switch (axis) {
                case X -> Axis.XP;
                case Y -> Axis.YP;
                default -> throw new IllegalStateException("??? axis " + axis);
            };

            stack.pushPose();
            stack.translate(cx, cy, cz);
            RenderUtils.renderWireCube(stack, event.getMultiBufferSource(), side / 2F, angle, rotationAxis, 220, 20, 60, 0, 0, 0);
            stack.popPose();
        }
    }
}