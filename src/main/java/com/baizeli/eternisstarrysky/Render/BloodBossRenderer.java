package com.baizeli.eternisstarrysky.Render;

import com.baizeli.eternisstarrysky.Entity.BloodBoss;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloodBossRenderer extends LivingEntityRenderer<BloodBoss, PlayerModel<BloodBoss>>
{
	public static final ResourceLocation TEXTURE = ResourceLocation.parse("minecraft:entity/zombie/zombie.png");

	public BloodBossRenderer(EntityRendererProvider.Context context)
	{
		super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
	}

	@Override
	protected boolean shouldShowName(BloodBoss p_114504_)
	{
		return true;
	}

	@Override
	public ResourceLocation getTextureLocation(BloodBoss p_114482_)
	{
		return TEXTURE;
	}

	@Override
	public void render(BloodBoss p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_)
	{
		super.render(p_115308_, p_115309_, p_115310_, p_115311_, p_115312_, p_115313_);
	}
}
