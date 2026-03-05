package miku.united_as_one.genesis.client.renderer.entity.laser;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.laser.DeathLaserEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class DeathLaserRenderer extends AbstractLaserRenderer<DeathLaserEntity> {
    @SuppressWarnings("removal")
    private static final ResourceLocation TEXTURE = new ResourceLocation(Genesis.MOD_ID, "textures/entity/laser_beam.png");

    public DeathLaserRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @Override
    protected ResourceLocation getLaserTexture() {
        return TEXTURE;
    }
}