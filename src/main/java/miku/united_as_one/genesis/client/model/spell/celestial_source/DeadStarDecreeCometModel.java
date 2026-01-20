package miku.united_as_one.genesis.client.model.spell.celestial_source;

import miku.united_as_one.genesis.EternisStarrySky;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.*;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class DeadStarDecreeCometModel<T extends Entity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
		new ResourceLocation(EternisStarrySky.MOD_ID, "dead_star_decree_comet"), "main"
	);

	private final ModelPart dead_star_decree_comet;

	public DeadStarDecreeCometModel(ModelPart root) {
		this.dead_star_decree_comet = root.getChild("dead_star_decree_comet");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("dead_star_decree_comet",
			CubeListBuilder.create()
				.texOffs(0, 80)
				.addBox(-16.0F, -32.0F, -16.0F, 32.0F, 32.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0)
				.addBox(-20.0F, -40.0F, -20.0F, 40.0F, 40.0F, 40.0F, new CubeDeformation(0.0F)), 
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, (float)Math.PI, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		dead_star_decree_comet.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}