
package miku.united_as_one.genesis.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import miku.united_as_one.genesis.client.animation.WSAnims;
import miku.united_as_one.genesis.common.entity.WardenSpellcaster;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class WardenSpellcasterModel<T extends WardenSpellcaster> extends HierarchicalModel<T>
{
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
			new ResourceLocation("modid", "wspellcaster"), "main");
	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart right_ribcage;
	private final ModelPart left_ribcage;
	private final ModelPart head;
	private final ModelPart hood;
	private final ModelPart right_tendril;
	private final ModelPart left_tendril;
	private final ModelPart right_arm;
	private final ModelPart group2;
	private final ModelPart left_arm;
	private final ModelPart group;
	private final ModelPart right_leg;
	private final ModelPart left_leg;

	public WardenSpellcasterModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.right_ribcage = this.body.getChild("right_ribcage");
		this.left_ribcage = this.body.getChild("left_ribcage");
		this.head = this.body.getChild("head");
		this.hood = this.head.getChild("hood");
		this.right_tendril = this.head.getChild("right_tendril");
		this.left_tendril = this.head.getChild("left_tendril");
		this.right_arm = this.body.getChild("right_arm");
		this.group2 = this.right_arm.getChild("group2");
		this.left_arm = this.body.getChild("left_arm");
		this.group = this.left_arm.getChild("group");
		this.right_leg = this.root.getChild("right_leg");
		this.left_leg = this.root.getChild("left_leg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F, new CubeDeformation(0.0F))
		.texOffs(0, 33).addBox(-9.0F, -13.0F, -4.0F, 18.0F, 21.0F, 11.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, -21.0F, 0.0F));

		PartDefinition left_leg_r1 = body.addOrReplaceChild("left_leg_r1", CubeListBuilder.create().texOffs(68, 91).addBox(-3.0F, -5.0F, -5.5F, 6.0F, 10.0F, 11.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(7.0F, 12.0F, 1.5F, 0.0F, 0.0F, -0.2182F));

		PartDefinition right_leg_r1 = body.addOrReplaceChild("right_leg_r1", CubeListBuilder.create().texOffs(33, 91).addBox(-3.0F, -5.0F, -5.5F, 6.0F, 10.0F, 11.0F, new CubeDeformation(0.51F)), PartPose.offsetAndRotation(-7.0F, 12.0F, 1.5F, 0.0F, 0.0F, 0.2182F));

		PartDefinition right_ribcage = body.addOrReplaceChild("right_ribcage", CubeListBuilder.create().texOffs(112, 20).addBox(-2.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-7.0F, -2.0F, -4.0F));

		PartDefinition left_ribcage = body.addOrReplaceChild("left_ribcage", CubeListBuilder.create().texOffs(112, 20).mirror().addBox(-7.0F, -11.0F, -0.1F, 9.0F, 21.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(7.0F, -2.0F, -4.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(59, 0).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(59, 27).addBox(-8.0F, -16.0F, -5.0F, 16.0F, 16.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition hood = head.addOrReplaceChild("hood", CubeListBuilder.create().texOffs(111, 41).addBox(-11.0F, -2.9398F, 0.0918F, 14.0F, 4.0F, 5.0F, new CubeDeformation(0.99F)), PartPose.offsetAndRotation(4.0F, -12.0F, 4.5F, -0.3491F, 0.0F, 0.0F));

		PartDefinition right_tendril = head.addOrReplaceChild("right_tendril", CubeListBuilder.create().texOffs(103, 78).addBox(-16.0F, -11.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(-8.0F, -12.0F, 0.0F));

		PartDefinition left_tendril = head.addOrReplaceChild("left_tendril", CubeListBuilder.create().texOffs(103, 95).addBox(0.0F, -11.0F, 0.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(8.0F, -12.0F, 0.0F));

		PartDefinition right_arm = body.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(59, 54).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(92, 54).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.5F))
		.texOffs(124, 50).addBox(-4.0F, 0.0F, -4.0F, 7.0F, 5.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(-13.0F, -13.0F, 1.0F));

		PartDefinition group2 = right_arm.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(54, 123).addBox(-0.5F, -9.0F, -0.5F, 1.0F, 32.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 131).addBox(-1.0F, 23.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(59, 123).addBox(-1.5F, -12.0F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 28.0F, -7.0F, 1.5708F, 0.0F, 0.0F));

		PartDefinition left_arm = body.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(0, 66).addBox(-4.0F, 0.0F, -4.0F, 8.0F, 28.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 103).addBox(-4.0F, 7.0F, -4.0F, 8.0F, 15.0F, 8.0F, new CubeDeformation(0.5F))
		.texOffs(124, 50).addBox(-3.0F, 0.0F, -4.0F, 7.0F, 5.0F, 8.0F, new CubeDeformation(0.2F)), PartPose.offset(13.0F, -13.0F, 1.0F));

		PartDefinition group = left_arm.addOrReplaceChild("group", CubeListBuilder.create().texOffs(54, 112).addBox(-9.0F, -7.0F, 3.0F, 3.0F, 1.0F, 10.0F, new CubeDeformation(0.1F))
		.texOffs(132, 111).addBox(-10.0F, -7.0F, 3.0F, 1.0F, 7.0F, 10.0F, new CubeDeformation(0.1F))
		.texOffs(102, 111).addBox(-10.2F, -7.2F, 2.8F, 5.4F, 7.4F, 10.4F, new CubeDeformation(0.1F))
		.texOffs(32, 112).addBox(-6.0F, -7.0F, 3.0F, 1.0F, 7.0F, 10.0F, new CubeDeformation(0.1F))
		.texOffs(80, 112).addBox(-9.0F, -6.0F, 4.0F, 3.0F, 5.0F, 8.0F, new CubeDeformation(0.1F)), PartPose.offset(8.0F, 33.0F, -8.0F));

		PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(33, 66).addBox(-3.1F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.9F, -13.0F, 0.0F));

		PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(112, 0).addBox(-2.9F, 0.0F, -3.0F, 6.0F, 13.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(5.9F, -13.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public ModelPart root()
	{
		return root;
	}

	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		this.head.yRot = netHeadYaw * 0.017f;
		this.head.xRot = headPitch * 0.017f;
		this.animateWalk(WSAnims.walk, limbSwing, limbSwingAmount, 2.0F, 2.5F);
		this.animate(entity.idle, WSAnims.idle, ageInTicks);
		this.animate(entity.start, WSAnims.start, ageInTicks);
		this.animate(entity.roar, WSAnims.roar, ageInTicks);
		this.animate(entity.smell, WSAnims.smell, ageInTicks);
		this.animate(entity.skill, WSAnims.skill, ageInTicks);
		this.animate(entity.spell1, WSAnims.spell1, ageInTicks);
		this.animate(entity.spell2, WSAnims.spell2, ageInTicks);
		this.animate(entity.spell3, WSAnims.spell3, ageInTicks);
		this.animate(entity.leave, WSAnims.leave, ageInTicks);
		this.animate(entity.combat, WSAnims.combat, ageInTicks);
	}

	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}