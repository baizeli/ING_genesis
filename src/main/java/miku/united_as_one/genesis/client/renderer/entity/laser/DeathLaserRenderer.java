package miku.united_as_one.genesis.client.renderer.entity.laser;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.common.entity.laser.DeathLaserEntity;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.*;

@OnlyIn(Dist.CLIENT)
public class DeathLaserRenderer extends AbstractLaserRenderer<DeathLaserEntity> {
    public DeathLaserRenderer(EntityRendererProvider.Context mgr) {
        super(mgr);
    }

    @SuppressWarnings("removal")
    @Override
    protected ResourceLocation getLaserTexture() {
        return new ResourceLocation(Genesis.MOD_ID, "textures/entity/laser/death_beam.png");
    }
}