package miku.united_as_one.genesis.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.Random;

public class BaiZeLiSlashEffect {

    private static final float RADIUS = 1.0f;
    private static final int LIFETIME = 15; // 剑光寿命 15 帧 (0.75秒)

    private int age = 0;
    private final Vec3 center;
    private final float yaw;
    private final float pitch;
    private final float slashTilt;
    private final boolean leftToRight;

    private boolean finished = false;
    private static final Random RANDOM = new Random();

    public BaiZeLiSlashEffect(Vec3 center, float yaw, float pitch) {
        this.center = center;
        this.yaw = yaw;
        this.pitch = pitch;

        this.leftToRight = RANDOM.nextBoolean();
        float baseTilt = 15.0f + RANDOM.nextFloat() * 45.0f;
        this.slashTilt = leftToRight ? -baseTilt : baseTilt;
    }

    public void tick() {
        age++;
        if (age >= LIFETIME) finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, float partialTick) {
        if (finished) return;

        float exactAge = age + partialTick;
        float progress = exactAge / (float) LIFETIME;
        if (progress >= 1.0f) return;
        float alpha = 1.0f - (progress * progress);
        if (alpha <= 0.01f) return;
        float cleaveProgress = Math.min(1.2f, (exactAge / 6.0f) * 1.2f);

        ModShaders.setTime(ModShaders.getRibbonShader(), exactAge);
        VertexConsumer vc = buffer.getBuffer(ModRenderType.ribbon);
        Vec3 camPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        poseStack.pushPose();

        poseStack.translate(
                center.x - camPos.x,
                center.y - camPos.y,
                center.z - camPos.z
        );

        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));

        poseStack.mulPose(Axis.ZP.rotationDegrees(slashTilt));

        poseStack.scale(6.0f, 0.6f, 1.0f);

        Matrix4f matrix = poseStack.last().pose();
        float r = RADIUS;

        float rColor = cleaveProgress;
        float gColor = leftToRight ? 1.0f : 0.0f;

        vc.vertex(matrix, -r, -r, 0).color(rColor, gColor, 1.0f, alpha).uv(0, 1).endVertex();
        vc.vertex(matrix,  r, -r, 0).color(rColor, gColor, 1.0f, alpha).uv(1, 1).endVertex();
        vc.vertex(matrix,  r,  r, 0).color(rColor, gColor, 1.0f, alpha).uv(1, 0).endVertex();
        vc.vertex(matrix, -r,  r, 0).color(rColor, gColor, 1.0f, alpha).uv(0, 0).endVertex();

        poseStack.popPose();
    }
}