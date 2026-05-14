package miku.united_as_one.genesis.client.renderer.entity.spell.chaos;

import miku.united_as_one.genesis.api.render.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class WireBoxRenderer {
    public static Map<UUID, Long> entitiesForRenderWireBoxRenderer = new ConcurrentHashMap<>();
    public static Map<UUID, Float>  entityRotationMap = new ConcurrentHashMap<>();
    public static Map<UUID, Direction.Axis> entityAxisMap = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onRenderLiving(RenderLivingEvent.Post<?, ?> event) {
        LivingEntity entity = event.getEntity();
        UUID uuid = entity.getUUID();

        Long data = entitiesForRenderWireBoxRenderer.get(uuid);
        if (data == null) return;

        Level level = entity.level();
        if (level.getGameTime() >= data || !Objects.requireNonNull(level.getEntities().get(uuid)).isAlive()) {
            entitiesForRenderWireBoxRenderer.remove(uuid);
            entityRotationMap.remove(uuid);
            entityAxisMap.remove(uuid);
            return;
        }

        PoseStack stack = event.getPoseStack();
        AABB box = entity.getBoundingBox();

        double cx = (box.minX + box.maxX) / 2D - entity.getX();
        double cy = (box.minY + box.maxY) / 2D - entity.getY();
        double cz = (box.minZ + box.maxZ) / 2D - entity.getZ();

        float side = (float) (box.maxX - box.minX) * 2.5f;

        Float angle = entityRotationMap.get(uuid);
        if (angle == null) {
            angle = java.util.concurrent.ThreadLocalRandom.current().nextFloat() * 360F;
            entityRotationMap.put(uuid, angle);
        }

        Direction.Axis axis = entityAxisMap.get(uuid);
        if (axis == null) {
            Direction.Axis[] axes = Direction.Axis.values();
            do {
                axis = axes[java.util.concurrent.ThreadLocalRandom.current().nextInt(axes.length)];
            } while (axis == Direction.Axis.Z);
            entityAxisMap.put(uuid, axis);
        }

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