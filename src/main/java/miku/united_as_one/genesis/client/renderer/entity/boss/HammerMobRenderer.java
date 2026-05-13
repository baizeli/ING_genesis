package miku.united_as_one.genesis.client.renderer.entity.boss;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.model.entity.boss.HammerMobModel;
import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HammerMobRenderer extends MobRenderer<HammerMob, HammerMobModel<HammerMob>> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "textures/entity/hammer_mob/base.png");

    public HammerMobRenderer(EntityRendererProvider.Context context) {
        super(context, new HammerMobModel<>(context.bakeLayer(HammerMobModel.LAYER_LOCATION)), 0.5F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull HammerMob entity) {
        return TEXTURE;
    }
}