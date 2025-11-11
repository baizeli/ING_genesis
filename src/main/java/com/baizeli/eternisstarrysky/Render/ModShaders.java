package com.baizeli.eternisstarrysky.Render;

import com.baizeli.eternisstarrysky.CosmicRender.AvaritiaShaders;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4f;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModShaders {

    public static ShaderInstance halo_shader;
    public static ShaderInstance get_halo_shader() {return halo_shader;}
    @Nullable
    private static ShaderInstance colorfulShader;

    @Nullable
    private static ShaderInstance floridShader;
    @Nullable
    private static ShaderInstance heatWaveShader;
    @Nullable
    private static ShaderInstance heatWavePostprocessShader;

    public static ShaderInstance getColorfulShader() {
        return Objects.requireNonNull(colorfulShader, "Colorful shader not registered");
    }

    public static ShaderInstance getFloridShader() {
        return Objects.requireNonNull(floridShader, "Florid shader not registered");
    }
    public static ShaderInstance getHeatWaveShader() {
        return Objects.requireNonNull(heatWaveShader, "HeatWave shader shader not registered");
    }

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException
    {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(EternisStarrySky.MOD_ID, "halo"), DefaultVertexFormat.POSITION_COLOR_NORMAL), shader -> halo_shader = shader);

        ResourceProvider resourceProvider = event.getResourceProvider();
        ModShaderInstance colorful = new ModShaderInstance(
                resourceProvider,
                new ResourceLocation(EternisStarrySky.MODID, "colorful_shader").toString(),
                DefaultVertexFormat.POSITION_COLOR_TEX
        );
        event.registerShader(colorful, shaderInstance -> colorfulShader = shaderInstance);


        ModShaderInstance florid = new ModShaderInstance(
                resourceProvider,
                new ResourceLocation(EternisStarrySky.MODID, "florid_shader").toString(),
                DefaultVertexFormat.POSITION_TEX
        );
        event.registerShader(florid, shaderInstance -> floridShader = shaderInstance);

        ModShaderInstance heat_wave = new ModShaderInstance(
                resourceProvider,
                new ResourceLocation(EternisStarrySky.MODID, "heat_wave").toString(),
                DefaultVertexFormat.POSITION_TEX
        );
        event.registerShader(heat_wave, shaderInstance -> heatWaveShader = shaderInstance);

    }

    public static Minecraft getMinecraft() {return Minecraft.getInstance();}

    public static boolean setTime(ShaderInstance shader)
    {
        shader.safeGetUniform("time").set((float) AvaritiaShaders.renderTime);
        return true;
    }

    public static boolean setTime(ShaderInstance shader, float pk)
    {
        shader.safeGetUniform("time").set(pk);
        return true;
    }

    public static boolean setScreenSize(ShaderInstance shader)
    {
        shader.safeGetUniform("screenSize").set((float) getMinecraft().getWindow().getWidth(), (float) getMinecraft().getWindow().getHeight());
        return true;
    }
}