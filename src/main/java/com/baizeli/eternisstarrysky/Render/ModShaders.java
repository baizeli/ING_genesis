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
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector4f;

import java.io.IOException;

@Mod.EventBusSubscriber(modid = EternisStarrySky.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModShaders {

    public static ShaderInstance halo_shader;
    public static ShaderInstance get_halo_shader() {return halo_shader;}

    @SubscribeEvent
    public static void registerShaders(RegisterShadersEvent event) throws IOException
    {
        event.registerShader(new ShaderInstance(event.getResourceProvider(), new ResourceLocation(EternisStarrySky.MOD_ID, "halo"), DefaultVertexFormat.POSITION_COLOR_NORMAL), shader -> halo_shader = shader);
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