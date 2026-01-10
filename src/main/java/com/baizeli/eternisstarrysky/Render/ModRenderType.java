package com.baizeli.eternisstarrysky.Render;

import com.baizeli.eternisstarrysky.CosmicRender.AvaritiaShaders;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.inventory.InventoryMenu;

public class ModRenderType extends RenderType {
    public ModRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);}

    public static final RenderType cosmic_world = RenderType.create(
            "cosmic_world",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.TRIANGLES,
            2097149,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(() -> AvaritiaShaders.cosmicShader))
                    .setTextureState(new TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setLightmapState(AvaritiaShaders.RenderStateShardAccess.LIGHT_MAP)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(true)
    );

    public static final RenderType halo = RenderType.create(
            "halo",
            DefaultVertexFormat.POSITION_COLOR_NORMAL,
            VertexFormat.Mode.TRIANGLES,
            2097149,
            true,
            false,
            RenderType.CompositeState.builder()
                    .setShaderState(new ShaderStateShard(ModShaders::getHaloShader))
                    .setTextureState(new TextureStateShard(InventoryMenu.BLOCK_ATLAS, false, false))
                    .setDepthTestState(LEQUAL_DEPTH_TEST)
                    .setLightmapState(AvaritiaShaders.RenderStateShardAccess.LIGHT_MAP)
                    .setTransparencyState(NO_TRANSPARENCY)
                    .setCullState(NO_CULL)
                    .setWriteMaskState(COLOR_DEPTH_WRITE)
                    .createCompositeState(true)
    );
}