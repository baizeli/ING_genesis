package miku.united_as_one.genesis.client.render.luminous;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.world.item.ItemStack;
import miku.united_as_one.genesis.client.render.ModShaders;
import miku.united_as_one.genesis.init.config.Configuration;
import org.joml.Matrix4f;

public class GenesisOutlineRenderer {
    private static final int EFFECT_COUNT = 3;
    private static final TextureTarget[] maskBuffers = new TextureTarget[EFFECT_COUNT];
    private static final boolean[] hasCapturedWorld = new boolean[EFFECT_COUNT];

    private static TextureTarget worldBlurA, worldBlurB;
    private static TextureTarget guiBlurA, guiBlurB;

    private static boolean isCapturingWorld = false;
    private static boolean isCapturingGui = false;
    private static GenesisEffect activeGuiEffect = null;

    private static int getEffectIndex(GenesisEffect effect) {
        if (effect == GenesisEffect.BLACK_RED) return 0;
        if (effect == GenesisEffect.BLUE_WHITE) return 1;
        return 2;
    }

    private static boolean isEffectEnabled(GenesisEffect effect) {
        if (effect == GenesisEffect.BLACK_RED) return Configuration.ENABLE_RED.get();
        if (effect == GenesisEffect.BLUE_WHITE) return Configuration.ENABLE_BLUE.get();
        return Configuration.ENABLE_RAINBOW.get();
    }

    private static float getEffectWidth(int effectIdx) {
        if (effectIdx == 0) return Configuration.WIDTH_RED.get().floatValue();
        if (effectIdx == 1) return Configuration.WIDTH_BLUE.get().floatValue();
        return Configuration.WIDTH_RAINBOW.get().floatValue();
    }

    private static TextureTarget getOrCreateMaskBuffer(int idx) {
        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();
        if (maskBuffers[idx] == null) {
            maskBuffers[idx] = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
            maskBuffers[idx].setFilterMode(9729);
        } else if (maskBuffers[idx].width != main.width || maskBuffers[idx].height != main.height) {
            maskBuffers[idx].resize(main.width, main.height, Minecraft.ON_OSX);
        }
        return maskBuffers[idx];
    }

    public static void beginWorldPass() {
        for (int i = 0; i < EFFECT_COUNT; i++) hasCapturedWorld[i] = false;
    }

    public static void startWorldCapture(ItemStack stack, GenesisEffect effect) {
        if (!isEffectEnabled(effect)) return;
        int idx = getEffectIndex(effect);
        TextureTarget mb = getOrCreateMaskBuffer(idx);

        if (!hasCapturedWorld[idx]) {
            mb.copyDepthFrom(Minecraft.getInstance().getMainRenderTarget());
            mb.bindWrite(false);
            RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
            RenderSystem.clear(org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);
            hasCapturedWorld[idx] = true;
        } else {
            mb.bindWrite(false);
        }
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        isCapturingWorld = true;
    }

    public static void stopWorldCapture() {
        if (!isCapturingWorld) return;
        Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
        isCapturingWorld = false;
    }

    public static void flushWorldPass() {
        if (ModShaders.getGenesisOutline() == null || ModShaders.getGenesisBloom() == null || ModShaders.getGenesisBloomBlur() == null) {
            beginWorldPass();
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();
        ShaderInstance shader = ModShaders.getGenesisOutline();

        boolean anyToRender = false;
        for (boolean b : hasCapturedWorld) if (b) anyToRender = true;
        if (!anyToRender) return;

        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        Matrix4f identity = new Matrix4f();
        RenderSystem.setProjectionMatrix(identity, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        com.mojang.blaze3d.vertex.PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.setIdentity();
        RenderSystem.applyModelViewMatrix();

        main.bindWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        for (int i = 0; i < EFFECT_COUNT; i++) {
            if (!hasCapturedWorld[i]) continue;

            RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, maskBuffers[i].getColorTextureId());
            drawQuad();

            shader.setSampler("DiffuseSampler", maskBuffers[i].getColorTextureId());
            shader.setSampler("DepthSampler", maskBuffers[i].getDepthTextureId());
            applyGlobalUniforms(shader, i, main, false);
            drawShaderQuad(shader);

            runBloomPass(mc, main, i, false);
        }

        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setProjectionMatrix(savedProj, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);

        beginWorldPass();
    }

    public static void startGuiCapture(ItemStack stack, GenesisEffect effect) {
        if (!isEffectEnabled(effect)) return;
        TextureTarget mb = getOrCreateMaskBuffer(0);
        mb.bindWrite(false);
        RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        RenderSystem.clear(org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        isCapturingGui = true;
        activeGuiEffect = effect;
    }

    public static void flushGuiCapture() {
        if (!isCapturingGui || activeGuiEffect == null) return;
        isCapturingGui = false;

        if (ModShaders.getGenesisOutline() == null || ModShaders.getGenesisBloom() == null || ModShaders.getGenesisBloomBlur() == null) {
            activeGuiEffect = null;
            Minecraft.getInstance().getMainRenderTarget().bindWrite(false);
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        RenderTarget main = mc.getMainRenderTarget();
        ShaderInstance outlineShader = ModShaders.getGenesisOutline();

        Matrix4f savedProj = new Matrix4f(RenderSystem.getProjectionMatrix());
        Matrix4f identity = new Matrix4f();
        RenderSystem.setProjectionMatrix(identity, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        com.mojang.blaze3d.vertex.PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.setIdentity();
        RenderSystem.applyModelViewMatrix();

        main.bindWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        TextureTarget mb = maskBuffers[0];
        int effectIdx = getEffectIndex(activeGuiEffect);

        RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, mb.getColorTextureId());
        drawQuad();

        outlineShader.setSampler("DiffuseSampler", mb.getColorTextureId());
        outlineShader.setSampler("DepthSampler", mb.getDepthTextureId());
        applyGlobalUniforms(outlineShader, effectIdx, main, true);
        drawShaderQuad(outlineShader);

        runBloomPass(mc, main, effectIdx, true);

        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
        RenderSystem.setProjectionMatrix(savedProj, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);

        activeGuiEffect = null;
    }

    private static void runBloomPass(Minecraft mc, RenderTarget main, int effectIdx, boolean isGui) {
        ShaderInstance blurShader = ModShaders.getGenesisBloomBlur();
        ShaderInstance bloomShader = ModShaders.getGenesisBloom();
        if (blurShader == null || bloomShader == null) return;

        int bw = Math.max(64, main.width / (isGui ? 4 : 2));
        int bh = Math.max(64, main.height / (isGui ? 4 : 2));

        TextureTarget activeBlurA = isGui ? guiBlurA : worldBlurA;
        TextureTarget activeBlurB = isGui ? guiBlurB : worldBlurB;

        activeBlurA = syncBuffer(activeBlurA, bw, bh);
        activeBlurB = syncBuffer(activeBlurB, bw, bh);

        if (isGui) {
            guiBlurA = activeBlurA;
            guiBlurB = activeBlurB;
        } else {
            worldBlurA = activeBlurA;
            worldBlurB = activeBlurB;
        }

        RenderSystem.disableBlend();
        if (blurShader.getUniform("ScreenSize") != null) blurShader.getUniform("ScreenSize").set((float) bw, (float) bh);

        // 移除配置项后，写死一个相对平滑合理的发光大小
        float radius = isGui ? 0.6F : 2.5F;
        int passes = 4;

        int nTex = maskBuffers[isGui ? 0 : effectIdx].getColorTextureId();
        int fTex = runBlur(blurShader, maskBuffers[isGui ? 0 : effectIdx].getColorTextureId(), activeBlurA, activeBlurB, radius / passes, passes);

        main.bindWrite(true);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        bloomShader.setSampler("DiffuseSampler", maskBuffers[isGui ? 0 : effectIdx].getColorTextureId());
        bloomShader.setSampler("NearBlurSampler", nTex);
        bloomShader.setSampler("FarBlurSampler", fTex);
        applyGlobalUniforms(bloomShader, effectIdx, main, isGui);

        if (bloomShader.getUniform("BloomStrength") != null) bloomShader.getUniform("BloomStrength").set(0.0F);
        if (bloomShader.getUniform("BloomRadius") != null) bloomShader.getUniform("BloomRadius").set(radius);

        drawShaderQuad(bloomShader);
    }

    private static int runBlur(ShaderInstance s, int tex, TextureTarget a, TextureTarget b, float r, int p) {
        int cur = tex;
        for (int i = 0; i < p; i++) {
            if (s.getUniform("BlurRadius") != null) s.getUniform("BlurRadius").set(r);
            a.bindWrite(true);
            s.setSampler("DiffuseSampler", cur);
            if (s.getUniform("BlurDirection") != null) s.getUniform("BlurDirection").set(1.0F, 0.0F);
            drawShaderQuad(s);
            b.bindWrite(true);
            s.setSampler("DiffuseSampler", a.getColorTextureId());
            if (s.getUniform("BlurDirection") != null) s.getUniform("BlurDirection").set(0.0F, 1.0F);
            drawShaderQuad(s);
            cur = b.getColorTextureId();
        }
        return cur;
    }

    private static TextureTarget syncBuffer(TextureTarget t, int w, int h) {
        if (t == null) {
            TextureTarget nt = new TextureTarget(w, h, false, Minecraft.ON_OSX);
            nt.setFilterMode(9729);
            return nt;
        } else if (t.width != w || t.height != h) {
            t.resize(w, h, Minecraft.ON_OSX);
            t.setFilterMode(9729);
        }
        return t;
    }

    private static void applyGlobalUniforms(ShaderInstance s, int effectIdx, RenderTarget t, boolean isGui) {
        if (s.getUniform("ScreenSize") != null) s.getUniform("ScreenSize").set((float) t.width, (float) t.height);
        if (s.getUniform("Time") != null) s.getUniform("Time").set((float) (System.currentTimeMillis() % 24000L) / 1000.0F);

        if (effectIdx == 0) { // 黑红
            setVec4(s, "OutlineColor", 0.8F, 0.05F, 0.05F);
            setVec4(s, "SecondaryColor", 0.02F, 0.02F, 0.02F);
            if (s.getUniform("ColorMode") != null) s.getUniform("ColorMode").set(1.0F);
        } else if (effectIdx == 1) {
            setVec4(s, "OutlineColor", 0.1F, 0.5F, 1.0F); // 纯正的亮蓝色
            setVec4(s, "SecondaryColor", 0.7F, 0.9F, 1.0F); // 浅蓝色偏白
            if (s.getUniform("ColorMode") != null) s.getUniform("ColorMode").set(1.0F);
        } else { // 彩色
            setVec4(s, "OutlineColor", 1.0F, 1.0F, 1.0F);
            if (s.getUniform("ColorMode") != null) s.getUniform("ColorMode").set(2.0F);
        }

        float outlineWidth = getEffectWidth(effectIdx);
        if (s.getUniform("OutlineWidth") != null) s.getUniform("OutlineWidth").set(outlineWidth);

        if (s.getUniform("DistanceScale") != null) s.getUniform("DistanceScale").set(1.0F);
        if (s.getUniform("Opacity") != null) s.getUniform("Opacity").set(0.95F);
    }

    private static void setVec4(ShaderInstance s, String n, float r, float g, float b) {
        if (s.getUniform(n) != null) s.getUniform(n).set(r, g, b, 1.0F);
    }

    private static void drawQuad() {
        BufferBuilder b = Tesselator.getInstance().getBuilder();
        b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        b.vertex(-1.0D, -1.0D, 0.0D).uv(0.0F, 0.0F).endVertex();
        b.vertex(1.0D, -1.0D, 0.0D).uv(1.0F, 0.0F).endVertex();
        b.vertex(1.0D, 1.0D, 0.0D).uv(1.0F, 1.0F).endVertex();
        b.vertex(-1.0D, 1.0D, 0.0D).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(b.end());
    }

    private static void drawShaderQuad(ShaderInstance s) {
        RenderSystem.setShader(() -> s);
        drawQuad();
    }
}