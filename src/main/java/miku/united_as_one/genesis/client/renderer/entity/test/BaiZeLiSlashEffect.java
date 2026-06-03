package miku.united_as_one.genesis.client.renderer.entity.test;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import miku.bai_ze_li.genesis.api.render.shader.GenesisRenderType;
import miku.bai_ze_li.genesis.api.render.shader.GenesisShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class BaiZeLiSlashEffect {

    private static final int SEGMENTS = 32;
    private static final float RADIUS = 3.0f;
    private static final int LIFETIME = 20;

    private int age = 0;
    private final Vec3 center;
    private final float startYaw;
    private boolean finished = false;

    public BaiZeLiSlashEffect(Vec3 center, float entityYaw) {
        this.center = center;
        this.startYaw = entityYaw;
    }

    public void tick() {
        age++;
        if (age >= LIFETIME) {
            finished = true;
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        if (finished) return;

        float progress = age / (float) LIFETIME;
        float alpha = 1.0f - progress * progress;

        if (alpha <= 0.01f) return;

        GenesisShaders.setTime(GenesisShaders.getRibbonShader(), age + partialTick);

        VertexConsumer vc = buffer.getBuffer(GenesisRenderType.ribbon);

        poseStack.pushPose();
        poseStack.translate(center.x, center.y + 1.5, center.z);

        Matrix4f matrix = poseStack.last().pose();
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        float startAngle = startYaw - 90.0f;
        float endAngle = startYaw + 90.0f;
        float angleRange = endAngle - startAngle;

        for (int i = 0; i < SEGMENTS; i++) {
            float t1 = i / (float) SEGMENTS;
            float t2 = (i + 1) / (float) SEGMENTS;

            float angle1 = startAngle + angleRange * t1;
            float angle2 = startAngle + angleRange * t2;

            Vec3 pos1 = calculatePosition(angle1, RADIUS);
            Vec3 pos2 = calculatePosition(angle2, RADIUS);
            Vec3 innerPos1 = calculatePosition(angle1, RADIUS * 0.3f);
            Vec3 innerPos2 = calculatePosition(angle2, RADIUS * 0.3f);

            Vec3 p1 = pos1.subtract(camPos);
            Vec3 p2 = pos2.subtract(camPos);
            Vec3 p3 = innerPos2.subtract(camPos);
            Vec3 p4 = innerPos1.subtract(camPos);

            float u1 = t1;
            float u2 = t2;

            vc.vertex(matrix, (float) p1.x, (float) p1.y, (float) p1.z)
                    .color(1.0F, 1.0F, 1.0F, alpha)
                    .uv(u1, 0.0F)
                    .endVertex();

            vc.vertex(matrix, (float) p2.x, (float) p2.y, (float) p2.z)
                    .color(1.0F, 1.0F, 1.0F, alpha)
                    .uv(u2, 0.0F)
                    .endVertex();

            vc.vertex(matrix, (float) p3.x, (float) p3.y, (float) p3.z)
                    .color(1.0F, 1.0F, 1.0F, alpha)
                    .uv(u2, 1.0F)
                    .endVertex();

            vc.vertex(matrix, (float) p4.x, (float) p4.y, (float) p4.z)
                    .color(1.0F, 1.0F, 1.0F, alpha)
                    .uv(u1, 1.0F)
                    .endVertex();
        }

        poseStack.popPose();
    }

    private Vec3 calculatePosition(float yawDegrees, float radius) {
        float yawRad = yawDegrees * Mth.DEG_TO_RAD;
        float x = -Mth.sin(yawRad) * radius;
        float z = Mth.cos(yawRad) * radius;
        return new Vec3(x, 0, z);
    }
}
