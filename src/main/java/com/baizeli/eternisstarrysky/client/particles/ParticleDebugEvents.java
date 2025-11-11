package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.Render.ModShaderInstance;
import com.baizeli.eternisstarrysky.Render.ModShaders;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;

import static com.baizeli.eternisstarrysky.EternisStarrySky.MODID;
import static com.baizeli.eternisstarrysky.Items.ModItems.INFINITY_SWORD;
import static com.mojang.blaze3d.platform.GlConst.GL_COLOR_BUFFER_BIT;
import static com.mojang.blaze3d.platform.GlConst.GL_NEAREST;

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

    private static TextureTarget heatWaveRenderTarget;
    static ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/effect/heat_wave.png");
    private static final RenderType SHADER_RENDER_TYPE = RenderType.create(
            "shader_test",
            DefaultVertexFormat.POSITION_TEX,
            VertexFormat.Mode.QUADS,
            256,
            false,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> ModShaders.getHeatWaveShader()))
                    .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setTransparencyState(createHeatWaveTransparency())
                    .setCullState(RenderStateShard.NO_CULL)
                    .createCompositeState(false)
    );
    private static final RenderType SHADER_RENDER_TYPE2 = RenderType.create(
            "shader_render_type2",           
            DefaultVertexFormat.POSITION_TEX_COLOR_NORMAL,        
            VertexFormat.Mode.TRIANGLES,         
            256,                                 
            false,                               
            true,                                
            RenderType.CompositeState.builder()  
                    .setShaderState(new RenderStateShard.ShaderStateShard(ModShaders::getHeatWaveShader))
                    .setTextureState(new RenderStateShard.TextureStateShard(TEXTURE, false, false))
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)  
                    .setCullState(RenderStateShard.NO_CULL)          
                    .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)     
                    .setWriteMaskState(RenderStateShard.COLOR_DEPTH_WRITE)
                    .createCompositeState(false)
    );

    private static RenderStateShard.TransparencyStateShard createOverlayTransparency() {
        return new RenderStateShard.TransparencyStateShard("overlay_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.DST_COLOR,
                    GlStateManager.DestFactor.SRC_COLOR,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE
            );
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
    }
    private static RenderStateShard.TransparencyStateShard createHeatWaveTransparency() {
        return new RenderStateShard.TransparencyStateShard("heat_wave_transparency", () -> {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.SRC_ALPHA,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                    GlStateManager.SourceFactor.ONE,
                    GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
            );
        }, () -> {
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        });
    }


/*

    @SubscribeEvent
    public static void onRenderLivingEvent(RenderLivingEvent event) {
        if(true){
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;


            RenderType renderType = SHADER_RENDER_TYPE2;

            ResourceLocation textureLocation = new ResourceLocation(MODID, "textures/effect/heat_wave.png");
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);

            ModShaderInstance shader = (ModShaderInstance) ModShaders.getHeatWaveShader();
            shader.setTime((player.level().getGameTime() + mc.getFrameTime()) / 10.0f);


            shader.setSampler("Sample0", texture.getId());

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();
            poseStack.translate(0, 2, 3);

            
            renderType.setupRenderState();

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            float size = 2f;
            Matrix4f matrix = poseStack.last().pose();
            buffer.vertex(matrix, -size, -size, 0).uv(0, 0).endVertex();
            buffer.vertex(matrix, -size, size, 0).uv(0, 1).endVertex();
            buffer.vertex(matrix, size, size, 0).uv(1, 1).endVertex();
            buffer.vertex(matrix, size, -size, 0).uv(1, 0).endVertex();

            tesselator.end();
            poseStack.popPose();

            
            renderType.clearRenderState();
        }
        if(false){
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;
            if (player == null) return;

            
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(
                    GlStateManager.SourceFactor.DST_COLOR,    
                    GlStateManager.DestFactor.SRC_COLOR,      
                    GlStateManager.SourceFactor.ONE,          
                    GlStateManager.DestFactor.ZERO            
            );
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);

            ResourceLocation textureLocation = new ResourceLocation(MODID, "textures/effect/heat_wave.png");
            AbstractTexture texture = Minecraft.getInstance().getTextureManager().getTexture(textureLocation);

            ModShaderInstance shader = (ModShaderInstance) ModShaders.getHeatWaveShader();
            shader.setTime((player.level().getGameTime() + mc.getFrameTime()) / 10.0f);
            shader.setSampler("Sample0", texture.getId());

            PoseStack poseStack = event.getPoseStack();
            poseStack.pushPose();

            
            poseStack.translate(0, 0, -10); 

            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            
            float size = 20f; 
            Matrix4f matrix = poseStack.last().pose();
            buffer.vertex(matrix, -size, -size, 0).uv(0, 0).endVertex();
            buffer.vertex(matrix, -size, size, 0).uv(0, 1).endVertex();
            buffer.vertex(matrix, size, size, 0).uv(1, 1).endVertex();
            buffer.vertex(matrix, size, -size, 0).uv(1, 0).endVertex();

            tesselator.end();
            poseStack.popPose();

            
            RenderSystem.depthMask(true);
            RenderSystem.enableDepthTest();
            RenderSystem.disableBlend();
            RenderSystem.defaultBlendFunc();
        }
    }
*/

}


