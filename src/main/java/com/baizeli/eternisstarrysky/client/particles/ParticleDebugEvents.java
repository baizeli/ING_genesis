package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.baizeli.eternisstarrysky.entity.ModEntities;
import com.baizeli.eternisstarrysky.entity.NyanCat;
import com.baizeli.eternisstarrysky.render.ModShaderInstance;
import com.baizeli.eternisstarrysky.render.ModShaders;
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
import static com.baizeli.eternisstarrysky.items.ModItems.INFINITY_SWORD;
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
                spawnNyanCat(serverLevel, entity, lookVec);
            }
        }
    }

    private static void spawnNyanCat(ServerLevel serverLevel, Player player, Vec3 lookVec) {
        NyanCat nyanCat = new NyanCat(ModEntities.NYAN_CAT.get(), player, serverLevel);
        nyanCat.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        nyanCat.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5F, 1.0F);
        serverLevel.addFreshEntity(nyanCat);
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

    private static void magicCircle(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ModParticles.MAGIC_CIRCLE.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0);
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
}