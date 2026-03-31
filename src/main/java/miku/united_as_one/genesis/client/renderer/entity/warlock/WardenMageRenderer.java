package miku.united_as_one.genesis.client.renderer.entity.warlock;

import miku.united_as_one.genesis.common.entity.warlock.WardenMageEntity;
import miku.united_as_one.genesis.client.model.warlock.WardenMageModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WardenMageRenderer extends MobRenderer<WardenMageEntity, WardenMageModel<WardenMageEntity>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("iron_spells_genesis", "textures/entity/warden_mancer.png");

    public WardenMageRenderer(EntityRendererProvider.Context context) {
        super(context, new WardenMageModel<>(context.bakeLayer(WardenMageModel.LAYER_LOCATION)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(WardenMageEntity entity) {
        return TEXTURE;
    }
}