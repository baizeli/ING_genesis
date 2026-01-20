package miku.united_as_one.genesis.render.entity;

import miku.united_as_one.genesis.Entity.CustomArrowEntity;
import miku.united_as_one.genesis.EternisStarrySky;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CustomArrowRenderer extends ArrowRenderer<CustomArrowEntity> {

    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/arrow.png");
    public CustomArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public ResourceLocation getTextureLocation(CustomArrowEntity arrow) {
        return TEXTURE;
    }
}
