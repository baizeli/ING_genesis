package miku.united_as_one.genesis.Entity.bloodboss;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class BloodBossRenderer extends GeoEntityRenderer<BloodBossEntity> {

	public BloodBossRenderer(EntityRendererProvider.Context context) {
		super(context, new BloodBossModel());

	}

	@Override
	public ResourceLocation getTextureLocation(BloodBossEntity entity) {
		return BloodBossModel.STAGE1_TEXTURE;
	}


}