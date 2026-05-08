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
import miku.united_as_one.genesis.config.Configuration;
import org.joml.Matrix4f;

public class GenesisOutlineRenderer {
    private static final int EFFECT_COUNT = 3;
    private static final TextureTarget[] maskBuffers = new TextureTarget[EFFECT_COUNT];
    private static final boolean[] hasCapturedWorld = new boolean[EFFECT_COUNT];
    private static final int[] EFFECT_LOOKUP = new int[3];

    private static TextureTarget worldBlurA, worldBlurB;
    private static TextureTarget guiBlurA, guiBlurB;
    private static boolean isCapturingWorld = false;
    private static boolean isCapturingGui = false;
    private static GenesisEffect activeGuiEffect = null;
    private static Minecraft cachedMinecraft;

    private static final Matrix4f IDENTITY_MATRIX = new Matrix4f();
    private static final Matrix4f SAVED_PROJ_CACHE = new Matrix4f();

    static {
        EFFECT_LOOKUP[GenesisEffect.BLACK_RED.ordinal()] = 0;
        EFFECT_LOOKUP[GenesisEffect.BLUE_WHITE.ordinal()] = 1;
        EFFECT_LOOKUP[GenesisEffect.RAINBOW.ordinal()] = 2;
        IDENTITY_MATRIX.identity();
    }

    private static int getEffectIndex(GenesisEffect effect) {
        return EFFECT_LOOKUP[effect.ordinal()];
    }

    private static boolean isEffectEnabled(int effectIdx) {
        if (effectIdx == 0) return Configuration.enableRed;
        if (effectIdx == 1) return Configuration.enableBlue;
        return Configuration.enableRainbow;
    }

    private static float getEffectWidth(int effectIdx) {
        if (effectIdx == 0) return Configuration.widthRed;
        if (effectIdx == 1) return Configuration.widthBlue;
        return Configuration.widthRainbow;
    }

    private static Minecraft getMc() {
        if (cachedMinecraft == null) cachedMinecraft = Minecraft.getInstance();
        return cachedMinecraft;
    }

    private static TextureTarget getOrCreateMaskBuffer(int idx) {
        Minecraft mc = getMc();
        RenderTarget main = mc.getMainRenderTarget();
        TextureTarget buf = maskBuffers[idx];
        if (buf == null) {
            buf = new TextureTarget(main.width, main.height, true, Minecraft.ON_OSX);
            buf.setFilterMode(9729);
            maskBuffers[idx] = buf;
        } else if (buf.width != main.width || buf.height != main.height) {
            buf.resize(main.width, main.height, Minecraft.ON_OSX);
        }
        return buf;
    }

    public static void beginWorldPass() {
        for (int i = 0; i < EFFECT_COUNT; i++) hasCapturedWorld[i] = false;
    }

    public static void startWorldCapture(ItemStack stack, GenesisEffect effect) {
        int idx = getEffectIndex(effect);
        if (!isEffectEnabled(idx)) return;
        TextureTarget mb = getOrCreateMaskBuffer(idx);

        if (!hasCapturedWorld[idx]) {
            mb.copyDepthFrom(getMc().getMainRenderTarget());
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
        getMc().getMainRenderTarget().bindWrite(false);
        isCapturingWorld = false;
    }

    public static void flushWorldPass() {
        ShaderInstance outlineShader = ModShaders.getGenesisOutline();
        ShaderInstance bloomShader = ModShaders.getGenesisBloom();
        ShaderInstance blurShader = ModShaders.getGenesisBloomBlur();

        if (outlineShader == null || bloomShader == null || blurShader == null) {
            beginWorldPass(); return;
        }

        Minecraft mc = getMc();
        RenderTarget main = mc.getMainRenderTarget();
        boolean anyToRender = false;
        for (boolean b : hasCapturedWorld) if (b) { anyToRender = true; break; }
        if (!anyToRender) return;

        SAVED_PROJ_CACHE.set(RenderSystem.getProjectionMatrix());
        IDENTITY_MATRIX.identity();
        RenderSystem.setProjectionMatrix(IDENTITY_MATRIX, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        com.mojang.blaze3d.vertex.PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose(); modelViewStack.setIdentity(); RenderSystem.applyModelViewMatrix();

        main.bindWrite(true);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest(); RenderSystem.depthMask(false);

        for (int i = 0; i < EFFECT_COUNT; i++) {
            if (!hasCapturedWorld[i]) continue;
            TextureTarget maskBuf = maskBuffers[i];
            int colorTexId = maskBuf.getColorTextureId();
            int depthTexId = maskBuf.getDepthTextureId();

            RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, colorTexId);
            drawQuad();

            RenderSystem.setShader(() -> outlineShader);
            outlineShader.setSampler("DiffuseSampler", colorTexId);
            outlineShader.setSampler("DepthSampler", depthTexId);
            applyGlobalUniforms(outlineShader, i, main, false);
            drawQuad();

            runBloomPass(mc, main, i, false, bloomShader, blurShader);
        }

        modelViewStack.popPose(); RenderSystem.applyModelViewMatrix();
        RenderSystem.setProjectionMatrix(SAVED_PROJ_CACHE, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        RenderSystem.enableDepthTest(); RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        RenderSystem.bindTexture(0);

        beginWorldPass();
    }

    public static void startGuiCapture(ItemStack stack, GenesisEffect effect) {
        if (!isEffectEnabled(getEffectIndex(effect))) return;
        TextureTarget mb = getOrCreateMaskBuffer(0);
        mb.bindWrite(false);
        RenderSystem.clearColor(0.0F, 0.0F, 0.0F, 0.0F);
        RenderSystem.clear(org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);
        RenderSystem.disableDepthTest(); RenderSystem.depthMask(false);
        isCapturingGui = true; activeGuiEffect = effect;
    }

    public static void flushGuiCapture() {
        if (!isCapturingGui || activeGuiEffect == null) return;
        isCapturingGui = false;

        ShaderInstance outlineShader = ModShaders.getGenesisOutline();
        ShaderInstance bloomShader = ModShaders.getGenesisBloom();
        ShaderInstance blurShader = ModShaders.getGenesisBloomBlur();

        if (outlineShader == null || bloomShader == null || blurShader == null) {
            activeGuiEffect = null; getMc().getMainRenderTarget().bindWrite(false); return;
        }

        Minecraft mc = getMc(); RenderTarget main = mc.getMainRenderTarget();
        int effectIdx = getEffectIndex(activeGuiEffect);
        TextureTarget mb = maskBuffers[0];

        SAVED_PROJ_CACHE.set(RenderSystem.getProjectionMatrix());
        IDENTITY_MATRIX.identity();
        RenderSystem.setProjectionMatrix(IDENTITY_MATRIX, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        com.mojang.blaze3d.vertex.PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose(); modelViewStack.setIdentity(); RenderSystem.applyModelViewMatrix();

        main.bindWrite(true);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest(); RenderSystem.depthMask(false);

        int colorTexId = mb.getColorTextureId();
        int depthTexId = mb.getDepthTextureId();

        RenderSystem.setShader(net.minecraft.client.renderer.GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, colorTexId);
        drawQuad();

        RenderSystem.setShader(() -> outlineShader);
        outlineShader.setSampler("DiffuseSampler", colorTexId);
        outlineShader.setSampler("DepthSampler", depthTexId);
        applyGlobalUniforms(outlineShader, effectIdx, main, true);
        drawQuad();

        runBloomPass(mc, main, effectIdx, true, bloomShader, blurShader);

        modelViewStack.popPose(); RenderSystem.applyModelViewMatrix();
        RenderSystem.setProjectionMatrix(SAVED_PROJ_CACHE, com.mojang.blaze3d.vertex.VertexSorting.ORTHOGRAPHIC_Z);

        RenderSystem.enableDepthTest(); RenderSystem.depthMask(true);
        RenderSystem.colorMask(true, true, true, true);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();
        RenderSystem.activeTexture(org.lwjgl.opengl.GL13.GL_TEXTURE0);
        RenderSystem.bindTexture(0);

        activeGuiEffect = null;
    }

    private static void runBloomPass(Minecraft mc, RenderTarget main, int effectIdx, boolean isGui,
                                     ShaderInstance bloomShader, ShaderInstance blurShader) {
        if (blurShader == null || bloomShader == null) return;
        int bw = Math.max(64, main.width / (isGui ? 4 : 2));
        int bh = Math.max(64, main.height / (isGui ? 4 : 2));

        TextureTarget activeBlurA = isGui ? guiBlurA : worldBlurA;
        TextureTarget activeBlurB = isGui ? guiBlurB : worldBlurB;

        activeBlurA = syncBuffer(activeBlurA, bw, bh);
        activeBlurB = syncBuffer(activeBlurB, bw, bh);

        if (isGui) { guiBlurA = activeBlurA; guiBlurB = activeBlurB; }
        else { worldBlurA = activeBlurA; worldBlurB = activeBlurB; }

        RenderSystem.disableBlend();
        if (blurShader.getUniform("ScreenSize") != null) blurShader.getUniform("ScreenSize").set((float) bw, (float) bh);

        float radius = isGui ? 0.6F : 2.5F;
        int passes = isGui ? 1 : 4;
        int srcTex = maskBuffers[isGui ? 0 : effectIdx].getColorTextureId();
        int blurResult = runBlur(blurShader, srcTex, activeBlurA, activeBlurB, radius / passes, passes);

        main.bindWrite(true);
        RenderSystem.enableBlend(); RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(() -> bloomShader);
        bloomShader.setSampler("DiffuseSampler", srcTex);
        bloomShader.setSampler("NearBlurSampler", srcTex);
        bloomShader.setSampler("FarBlurSampler", blurResult);
        applyGlobalUniforms(bloomShader, effectIdx, main, isGui);

        if (bloomShader.getUniform("BloomStrength") != null) bloomShader.getUniform("BloomStrength").set(0.0F);
        if (bloomShader.getUniform("BloomRadius") != null) bloomShader.getUniform("BloomRadius").set(radius);
        drawQuad();
    }

    private static int runBlur(ShaderInstance shader, int tex, TextureTarget bufA, TextureTarget bufB, float radius, int passes) {
        int current = tex;
        for (int i = 0; i < passes; i++) {
            if (shader.getUniform("BlurRadius") != null) shader.getUniform("BlurRadius").set(radius);
            bufA.bindWrite(true);
            shader.setSampler("DiffuseSampler", current);
            if (shader.getUniform("BlurDirection") != null) shader.getUniform("BlurDirection").set(1.0F, 0.0F);
            drawQuad();

            bufB.bindWrite(true);
            shader.setSampler("DiffuseSampler", bufA.getColorTextureId());
            if (shader.getUniform("BlurDirection") != null) shader.getUniform("BlurDirection").set(0.0F, 1.0F);
            drawQuad();
            current = bufB.getColorTextureId();
        }
        return current;
    }

    private static TextureTarget syncBuffer(TextureTarget buffer, int w, int h) {
        if (buffer == null) {
            TextureTarget newBuf = new TextureTarget(w, h, false, Minecraft.ON_OSX);
            newBuf.setFilterMode(9729); return newBuf;
        } else if (buffer.width != w || buffer.height != h) {
            buffer.resize(w, h, Minecraft.ON_OSX); buffer.setFilterMode(9729);
        }
        return buffer;
    }

    private static void applyGlobalUniforms(ShaderInstance shader, int effectIdx, RenderTarget target, boolean isGui) {
        if (shader.getUniform("ScreenSize") != null) shader.getUniform("ScreenSize").set((float) target.width, (float) target.height);
        if (shader.getUniform("Time") != null) shader.getUniform("Time").set((float) (System.currentTimeMillis() % 24000L) / 1000.0F);

        switch (effectIdx) {
            case 0:
                setVec4(shader, "OutlineColor", 0.8F, 0.05F, 0.05F);
                setVec4(shader, "SecondaryColor", 0.02F, 0.02F, 0.02F);
                if (shader.getUniform("ColorMode") != null) shader.getUniform("ColorMode").set(1.0F);
                break;
            case 1:
                setVec4(shader, "OutlineColor", 0.1F, 0.5F, 1.0F);
                setVec4(shader, "SecondaryColor", 0.7F, 0.9F, 1.0F);
                if (shader.getUniform("ColorMode") != null) shader.getUniform("ColorMode").set(1.0F);
                break;
            default:
                setVec4(shader, "OutlineColor", 1.0F, 1.0F, 1.0F);
                if (shader.getUniform("ColorMode") != null) shader.getUniform("ColorMode").set(2.0F);
        }

        if (shader.getUniform("OutlineWidth") != null) shader.getUniform("OutlineWidth").set(getEffectWidth(effectIdx));
        if (shader.getUniform("DistanceScale") != null) shader.getUniform("DistanceScale").set(1.0F);
        if (shader.getUniform("Opacity") != null) shader.getUniform("Opacity").set(0.95F);
    }

    private static void setVec4(ShaderInstance shader, String uniform, float r, float g, float b) {
        if (shader.getUniform(uniform) != null) shader.getUniform(uniform).set(r, g, b, 1.0F);
    }

    private static void drawQuad() {
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(-1.0D, -1.0D, 0.0D).uv(0.0F, 0.0F).endVertex();
        builder.vertex(1.0D, -1.0D, 0.0D).uv(1.0F, 0.0F).endVertex();
        builder.vertex(1.0D, 1.0D, 0.0D).uv(1.0F, 1.0F).endVertex();
        builder.vertex(-1.0D, 1.0D, 0.0D).uv(0.0F, 1.0F).endVertex();
        BufferUploader.drawWithShader(builder.end());
    }
}