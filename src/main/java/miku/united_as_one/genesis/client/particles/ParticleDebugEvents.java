package miku.united_as_one.genesis.client.particles;

import miku.united_as_one.genesis.registries.entity.EntityRegistry;
import miku.united_as_one.genesis.contents.entity.NyanCat;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.registries.client.ParticleRegistry;
import miku.bai_ze_li.genesis.api.render.shader.GenesisShaders;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static miku.united_as_one.genesis.Genesis.MODID;
import static miku.united_as_one.genesis.registries.item.ItemRegistry.INFINITY_SWORD;

@Mod.EventBusSubscriber(modid = Genesis.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
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
        NyanCat nyanCat = new NyanCat(EntityRegistry.NYAN_CAT.get(), player, serverLevel);
        nyanCat.setPos(player.getX(), player.getY() + player.getEyeHeight(), player.getZ());
        nyanCat.shoot(lookVec.x, lookVec.y, lookVec.z, 1.5F, 1.0F);
        serverLevel.addFreshEntity(nyanCat);
    }

    private static void cubeTest(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ParticleRegistry.CUBE.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTest(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ParticleRegistry.TEST.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTestA(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ParticleRegistry.TESTA.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }
    private static void testTestb(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ParticleRegistry.TESTB.get(),
                entity.position().x, entity.position().y, entity.position().z,
                1,
                lookVec.x, lookVec.y, lookVec.z,
                0.1);
    }

    private static void magicCircle(ServerLevel serverLevel, Player entity, Vec3 lookVec) {
        serverLevel.sendParticles(
                ParticleRegistry.MAGIC_CIRCLE.get(),
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
                    .setShaderState(new RenderStateShard.ShaderStateShard(() -> GenesisShaders.getHeatWaveShader()))
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
                    .setShaderState(new RenderStateShard.ShaderStateShard(GenesisShaders::getHeatWaveShader))
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