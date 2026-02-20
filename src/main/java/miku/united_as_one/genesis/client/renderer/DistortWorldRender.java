package miku.united_as_one.genesis.client.renderer;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.init.mixin.minecraft.client.renderer.PostChainAccessor;
import miku.united_as_one.genesis.init.mixin.minecraft.client.renderer.ParticleAccessor;
import miku.united_as_one.genesis.init.mixin.minecraft.client.renderer.ParticleEngineAccessor;
import miku.united_as_one.genesis.client.particles.CrescentBladeParticle;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.shaders.Uniform;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class DistortWorldRender {
    public static final ResourceLocation DISTORT =
            new ResourceLocation(Genesis.MOD_ID, "shaders/post/distort.json");
    public static final ResourceLocation VECTOR_DISTORT =
            new ResourceLocation(Genesis.MOD_ID, "shaders/post/vector_distort.json");
    public static final ResourceLocation SPHERE_CHAIN =
            new ResourceLocation(Genesis.MOD_ID, "shaders/post/sphere_chain.json");
    public static PostChain distortChain;
    public static PostChain vectorDistort;
    public static PostChain sphereChain;

    public static void processMyPostChain(float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.cameraEntity == null) return;

        // 1. 处理原本的 Vector Distort
        if (vectorDistort != null) {
            vectorDistort.process(partialTicks);
        }

        /*
        * 不知道为啥使用原版深度图无效
        * 具体表现为深度图为纯白(全1)
        * 可能得使用rendertarget自定义一个深度图复制原版深度
        * */


        // 2. 处理 Sphere Chain
        if (sphereChain != null) {
            // 使用反编译源码中实际存在的 getTempTarget 方法
            RenderTarget swapTarget = sphereChain.getTempTarget("swap");
            if (swapTarget != null) {
                // 将主渲染目标的深度缓冲区拷贝到我们的中间目标
                swapTarget.copyDepthFrom(mc.getMainRenderTarget());
            }

            // 保持尺寸同步
            sphereChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());

            // 注入 Uniform (CameraPos 和 ProjMat)
            Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();

            // 获取摄像机的旋转四元数
            Quaternionf cameraRotation = mc.gameRenderer.getMainCamera().rotation();
            // 将四元数转换为矩阵 (这是 View -> World 的旋转矩阵)
            Matrix4f viewToWorldRotMat = new Matrix4f().rotation(cameraRotation);

            List<PostPass> passes = ((PostChainAccessor) sphereChain).getPasses();
            for (PostPass pass : passes) {
                // 1. 设置相机坐标
                Uniform cameraPosUniform = pass.getEffect().getUniform("CameraPos");
                if (cameraPosUniform != null) {
                    cameraPosUniform.set((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
                }

                // 2. 设置投影矩阵
                Uniform projMatUniform = pass.getEffect().getUniform("ProjMat");
                if (projMatUniform != null) {
                    projMatUniform.set(RenderSystem.getProjectionMatrix());
                }

                // 3. 设置旋转矩阵 (新增)
                Uniform rotMatUniform = pass.getEffect().getUniform("IViewRotMat");
                if (rotMatUniform != null) {
                    rotMatUniform.set(viewToWorldRotMat);
                }
            }

//            sphereChain.process(partialTicks);
        }
    }
    public static void initChain(Minecraft mc) {
        if (distortChain != null) distortChain.close();
        try {
            distortChain = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), DISTORT);
            distortChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (vectorDistort != null) vectorDistort.close();
        try {
            vectorDistort = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), VECTOR_DISTORT);
            vectorDistort.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sphereChain != null) sphereChain.close();
        try {
            sphereChain = new PostChain(mc.getTextureManager(), mc.getResourceManager(), mc.getMainRenderTarget(), SPHERE_CHAIN);
            sphereChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;

        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (player == null) return;

        // 处理DISTORT后处理RT
//        handleDistortRT(event, mc);
        
        // 处理VECTOR_DISTORT后处理RT
        handleVectorDistortRT(event, mc);
    }

    // 提取处理DISTORT后处理RT的方法
    private static void handleDistortRT(RenderLevelStageEvent event, Minecraft mc) {
        // ① 必须有后处理在跑
        PostChain chain = distortChain;
        if (chain == null) return;
        distortChain.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        // ② 拿 PostChain 自己创建的 RT（JSON 里的 "distort"）
        var rt = chain.getTempTarget("iron_spells_genesis_distort");
        if (rt == null) return;

        // ③ 复制主屏幕深度 → 保证遮挡
        rt.copyDepthFrom(mc.getMainRenderTarget());

        // ④ 绑定这个 RT
        rt.bindWrite(true);

        // ⑤ 只清颜色，不清深度
        RenderSystem.clearColor(0, 0, 0, 0);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        // 渲染各种几何体
//        renderGeometries(event, mc);

        // 渲染粒子
//        renderParticles(event.getPoseStack(), mc);
        renderParticlesInVectorBuffer(event.getPoseStack(), mc);

        //  切回主屏幕
        mc.getMainRenderTarget().bindWrite(true);

        //测试rt
//         rt.blitToScreen(
//                mc.getWindow().getWidth(),
//                mc.getWindow().getHeight(),
//                false
//        );
    }

    // 添加处理VECTOR_DISTORT后处理RT的方法
    private static void handleVectorDistortRT(RenderLevelStageEvent event, Minecraft mc) {
        // ① 必须有后处理在跑
        PostChain chain = vectorDistort;
        if (chain == null) return;
        vectorDistort.resize(mc.getWindow().getWidth(), mc.getWindow().getHeight());
        // ② 拿 PostChain 自己创建的 RT（JSON 里的 "vector_distort"）
        var rt = chain.getTempTarget("iron_spells_genesis_vector_buffer");
        if (rt == null) return;

        // ③ 复制主屏幕深度 → 保证遮挡
        rt.copyDepthFrom(mc.getMainRenderTarget());

        // ④ 绑定这个 RT
        rt.bindWrite(true);

        // ⑤ 只清颜色，不清深度
        RenderSystem.clearColor(0, 0, 0, 0);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, false);

        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);

        // 渲染各种几何体
//        renderGeometries(event, mc);

        // 渲染粒子
        renderParticlesInVectorBuffer(event.getPoseStack(), mc);

        //  切回主屏幕
        mc.getMainRenderTarget().bindWrite(true);

        //测试rt
//         rt.blitToScreen(
//                mc.getWindow().getWidth(),
//                mc.getWindow().getHeight(),
//                false
//        );
    }

    private static void renderGeometries(RenderLevelStageEvent event, Minecraft mc) {
        var poseStack = event.getPoseStack();
        poseStack.pushPose();

        Vec3 cam = mc.gameRenderer.getMainCamera().getPosition();
        Vec3 pos = mc.player.position().add(mc.player.getLookAngle().scale(5.0));

        poseStack.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);
        poseStack.mulPose(mc.gameRenderer.getMainCamera().rotation());

        renderQuad(poseStack, mc);

        poseStack.popPose();
    }

    private static void renderQuad(PoseStack poseStack, Minecraft mc) {
        Matrix4f mat = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        float s = 5f;
        buf.vertex(mat, -s, -s, 0).color(255, 255, 255, 255).endVertex();
        buf.vertex(mat, s, -s, 0).color(255, 255, 255, 255).endVertex();
        buf.vertex(mat, s, s, 0).color(255, 255, 255, 255).endVertex();
        buf.vertex(mat, -s, s, 0).color(255, 255, 255, 255).endVertex();

        BufferUploader.drawWithShader(buf.end());
    }

    // 将来可以添加其他渲染方法，例如：
    private static void renderParticles(PoseStack eventStack, Minecraft mc) {
        ParticleEngineAccessor accessor = (ParticleEngineAccessor) mc.particleEngine;
        Map<ParticleRenderType, Queue<Particle>> map = accessor.getParticles();

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        // 1. 创建干净的旋转矩阵，确保位移计算基于世界坐标偏移
        PoseStack renderStack = new PoseStack();
        renderStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        renderStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for (Queue<Particle> queue : map.values()) {
            for (Particle p : queue) {
                if (!(p instanceof CrescentBladeParticle cbp)) continue;
                ParticleAccessor cbpAccessor = (ParticleAccessor) cbp;

                // 2. 插值计算位移
                float partialTicks = mc.getFrameTime();
                double x = Mth.lerp(partialTicks, cbpAccessor.getXo(), cbpAccessor.getX());
                double y = Mth.lerp(partialTicks, cbpAccessor.getYo(), cbpAccessor.getY());
                double z = Mth.lerp(partialTicks, cbpAccessor.getZo(), cbpAccessor.getZ());

                float relX = (float) (x - camPos.x);
                float relY = (float) (y - camPos.y);
                float relZ = (float) (z - camPos.z);

                renderStack.pushPose();
                renderStack.translate(relX, relY, relZ);

                // 3. 计算正交基 (Right 和 Up)
                // 拿到标准化的速度方向
                Vec3 dir = new Vec3(cbpAccessor.getXd(), cbpAccessor.getYd(), cbpAccessor.getZd()).normalize();
                Vector3f forward = new Vector3f((float) dir.x, (float) dir.y, (float) dir.z);

                // 计算一个不与前进方向平行的临时向量用于叉乘
                Vector3f temp = new Vector3f(0, 1, 0);
                if (Math.abs(forward.dot(temp)) > 0.99f) temp = new Vector3f(1, 0, 0);

                // 得到相互垂直的 Right 和 Up 向量
                Vector3f right = new Vector3f();
                forward.cross(temp, right).normalize();
                Vector3f up = new Vector3f();
                right.cross(forward, up).normalize();

                // 4. 设置正方形大小
                float s = cbp.radius; // 半长/半宽
                Matrix4f mat = renderStack.last().pose();

                // 5. 颜色与透明度 (恢复使用粒子的动态透明度)
                int r = 255, g = 255, b = 255;
                int a =  255;

                // 6. 顶点构建：构建一个以粒子中心为原点，垂直于 forward 方向的正方形
                // 顶点顺序：左下 -> 右下 -> 右上 -> 左上
                buf.vertex(mat, (-right.x() - up.x()) * s, (-right.y() - up.y()) * s, (-right.z() - up.z()) * s).color(r, g, b, a).endVertex();
                buf.vertex(mat, (right.x() - up.x()) * s, (right.y() - up.y()) * s, (right.z() - up.z()) * s).color(r, g, b, a).endVertex();
                buf.vertex(mat, (right.x() + up.x()) * s, (right.y() + up.y()) * s, (right.z() + up.z()) * s).color(r, g, b, a).endVertex();
                buf.vertex(mat, (-right.x() + up.x()) * s, (-right.y() + up.y()) * s, (-right.z() + up.z()) * s).color(r, g, b, a).endVertex();

                renderStack.popPose();
            }
        }

        BufferUploader.drawWithShader(buf.end());
    }

    private static final ResourceLocation VECTOR_DISTORT_TEX =
            new ResourceLocation("iron_spells_genesis", "textures/misc/vector_distort.png");


    private static void renderParticlesInVectorBuffer(PoseStack poseStack, Minecraft mc) {
        ParticleEngineAccessor accessor = (ParticleEngineAccessor) mc.particleEngine;
        Map<ParticleRenderType, Queue<Particle>> map = accessor.getParticles();

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();

        PoseStack renderStack = new PoseStack();
        renderStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        renderStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));

        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture(0, VECTOR_DISTORT_TEX);

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);

        for (Queue<Particle> queue : map.values()) {
            for (Particle p : queue) {
                if (!(p instanceof CrescentBladeParticle cbp)) continue;
                ParticleAccessor acc = (ParticleAccessor) cbp;

                float partialTicks = mc.getFrameTime();

                double x = Mth.lerp(partialTicks, acc.getXo(), acc.getX());
                double y = Mth.lerp(partialTicks, acc.getYo(), acc.getY());
                double z = Mth.lerp(partialTicks, acc.getZo(), acc.getZ());

                float relX = (float) (x - camPos.x);
                float relY = (float) (y - camPos.y);
                float relZ = (float) (z - camPos.z);

                renderStack.pushPose();
                renderStack.translate(relX, relY, relZ);

                double dx = acc.getXd();
                double dy = acc.getYd();
                double dz = acc.getZd();
                double speedSq = dx * dx + dy * dy + dz * dz;

                Vector3f forward = new Vector3f();

                if (speedSq < 1.0E-6D) {

                    float seed = (float) ((p.hashCode() % 100) / 100.0);
                    forward.set(Mth.sin(seed * 3.1415f), Mth.cos(seed * 3.1415f), 0.5f).normalize();
                } else {
                    float speed = (float) Math.sqrt(speedSq);
                    forward.set((float) (dx / speed), (float) (dy / speed), (float) (dz / speed));
                }

                Vector3f temp = new Vector3f(0, 1, 0);
                if (Math.abs(forward.dot(temp)) > 0.99f) {
                    temp.set(1, 0, 0);
                }

                Vector3f right = new Vector3f();
                forward.cross(temp, right);
                right.normalize();

                Vector3f up = new Vector3f();
                right.cross(forward, up);
                up.normalize();

                float s = cbp.radius;
                Matrix4f mat = renderStack.last().pose();

                int r = 255, g = 255, b = 255, a = 255;

                buf.vertex(mat, (-right.x() - up.x()) * s, (-right.y() - up.y()) * s, (-right.z() - up.z()) * s)
                        .uv(0.0F, 1.0F)
                        .color(r, g, b, a)
                        .endVertex();

                buf.vertex(mat, (right.x() - up.x()) * s, (right.y() - up.y()) * s, (right.z() - up.z()) * s)
                        .uv(1.0F, 1.0F)
                        .color(r, g, b, a)
                        .endVertex();

                buf.vertex(mat, (right.x() + up.x()) * s, (right.y() + up.y()) * s, (right.z() + up.z()) * s)
                        .uv(1.0F, 0.0F)
                        .color(r, g, b, a)
                        .endVertex();

                buf.vertex(mat, (-right.x() + up.x()) * s, (-right.y() + up.y()) * s, (-right.z() + up.z()) * s)
                        .uv(0.0F, 0.0F)
                        .color(r, g, b, a)
                        .endVertex();

                renderStack.popPose();
            }
        }

        BufferUploader.drawWithShader(buf.end());
        RenderSystem.enableCull();
    }


}