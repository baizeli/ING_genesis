package miku.united_as_one.genesis.client.model;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import miku.united_as_one.genesis.Genesis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class ThrowBloodAndWoundsModel<T extends Entity> extends EntityModel<T> {
	public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "textures/entity/throw_blood_and_wounds.png");
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Genesis.MOD_ID, "throw_blood_and_wounds"), "main");
	private final ModelPart bone;

	public ThrowBloodAndWoundsModel(ModelPart root) {
		this.bone = root.getChild("bone");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone = partdefinition.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(33, 26).addBox(-1.0F, -7.0F, -0.3F, 2.0F, 8.0F, 1.6F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.0F, -33.625F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition cube_r1 = bone.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(14, 35).addBox(-0.5F, -2.5F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.2F, -25.7F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r2 = bone.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-14.0F, -2.0F, -0.8F, 23.0F, 3.0F, 2.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(-5.0F, -52.5F, 0.5F, 0.0F, 0.0F, -1.0472F));

		PartDefinition cube_r3 = bone.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 6).addBox(-13.0F, -2.0F, -0.8F, 20.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(4.0F, -47.5F, 0.5F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r4 = bone.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 16).addBox(-14.0F, -3.0F, -0.8F, 14.0F, 3.0F, 2.0F, new CubeDeformation(-0.25F)), PartPose.offsetAndRotation(-3.0F, -32.5F, 0.5F, 0.0F, 0.0F, 0.6545F));

		PartDefinition cube_r5 = bone.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(7, 31).addBox(-7.0F, -2.0F, -0.8F, 7.0F, 2.0F, 1.6F, new CubeDeformation(-0.33F)), PartPose.offsetAndRotation(3.0F, -21.5F, 0.5F, 0.0F, 0.0F, 1.0908F));

		PartDefinition cube_r6 = bone.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(7, 26).addBox(-14.0F, -3.0F, -0.8F, 8.0F, 3.0F, 1.6F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(3.0F, -21.5F, 0.5F, 0.0F, 0.0F, 1.0472F));

		PartDefinition cube_r7 = bone.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 12).addBox(-16.0F, -2.0F, -0.8F, 17.0F, 2.0F, 1.6F, new CubeDeformation(-0.32F)), PartPose.offsetAndRotation(0.5F, -10.5F, 0.5F, 0.0F, 0.0F, 1.7453F));

		PartDefinition cube_r8 = bone.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 22).addBox(-4.0F, -26.0F, -0.8F, 2.0F, 26.0F, 1.6F, new CubeDeformation(-0.33F)), PartPose.offsetAndRotation(-3.9F, -15.5F, 0.5F, 0.0F, 0.0F, 0.4363F));

		PartDefinition cube_r9 = bone.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(26, 26).addBox(-4.0F, -12.0F, -0.8F, 2.0F, 12.0F, 1.6F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-2.3F, -13.5F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r10 = bone.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(7, 35).addBox(-4.0F, -8.0F, -0.8F, 2.0F, 8.0F, 1.6F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(-0.3F, -11.5F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r11 = bone.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(7, 22).addBox(-12.0F, -2.0F, -0.8F, 13.0F, 2.0F, 1.6F, new CubeDeformation(-0.3F)), PartPose.offsetAndRotation(0.7F, -8.5F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r12 = bone.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(33, 16).addBox(-1.0F, -3.0F, -0.8F, 2.0F, 4.0F, 1.6F, new CubeDeformation(-0.31F)), PartPose.offsetAndRotation(0.7F, -7.1F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r13 = bone.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(36, 22).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, -6.7F, 0.5F, 0.0F, 0.0F, 0.7854F));

		PartDefinition cube_r14 = bone.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(19, 35).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.7F, 0.5F, 0.0F, 0.0F, 0.7854F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		bone.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}