package miku.united_as_one.genesis.client.model.entity.boss;

// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.client.animation.entity.boss.HammerMobAnimation;
import miku.united_as_one.genesis.contents.entity.boss.HammerMob;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HammerMobModel<T extends HammerMob> extends HierarchicalModel<T> {
    @SuppressWarnings("removal")
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Genesis.MOD_ID, "hammer_mob"), "main");

    private final ModelPart root;
	private final ModelPart allbody;
	private final ModelPart DownBody;
	private final ModelPart fabric;
	private final ModelPart Bfabric;
	private final ModelPart UpperBody;
	private final ModelPart Rfabric;
	private final ModelPart Lfabric;
	private final ModelPart head;
	private final ModelPart hat;
	private final ModelPart ring;
	private final ModelPart hole;
	private final ModelPart LeftArm;
	private final ModelPart LeftUpperArm;
	private final ModelPart LeftForeArm;
	private final ModelPart LeftHand;
	private final ModelPart RightArm;
	private final ModelPart RightForeArm;
	private final ModelPart RightHand;
	private final ModelPart weapon;
	private final ModelPart RightUpperArm;
	private final ModelPart LeftLeg;
	private final ModelPart Leftleg2;
	private final ModelPart RightLeg;
	private final ModelPart Rightleg2;

	public HammerMobModel(ModelPart root) {
		this.root = root.getChild("root");
		this.allbody = this.root.getChild("allbody");
		this.DownBody = this.allbody.getChild("DownBody");
		this.fabric = this.DownBody.getChild("fabric");
		this.Bfabric = this.DownBody.getChild("Bfabric");
		this.UpperBody = this.DownBody.getChild("UpperBody");
		this.Rfabric = this.UpperBody.getChild("Rfabric");
		this.Lfabric = this.UpperBody.getChild("Lfabric");
		this.head = this.UpperBody.getChild("head");
		this.hat = this.head.getChild("hat");
		this.ring = this.UpperBody.getChild("ring");
		this.hole = this.ring.getChild("hole");
		this.LeftArm = this.UpperBody.getChild("LeftArm");
		this.LeftUpperArm = this.LeftArm.getChild("LeftUpperArm");
		this.LeftForeArm = this.LeftArm.getChild("LeftForeArm");
		this.LeftHand = this.LeftForeArm.getChild("LeftHand");
		this.RightArm = this.UpperBody.getChild("RightArm");
		this.RightForeArm = this.RightArm.getChild("RightForeArm");
		this.RightHand = this.RightForeArm.getChild("RightHand");
		this.weapon = this.RightHand.getChild("weapon");
		this.RightUpperArm = this.RightArm.getChild("RightUpperArm");
		this.LeftLeg = this.allbody.getChild("LeftLeg");
		this.Leftleg2 = this.LeftLeg.getChild("Leftleg2");
		this.RightLeg = this.allbody.getChild("RightLeg");
		this.Rightleg2 = this.RightLeg.getChild("Rightleg2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 27.0F, 0.0F));

		PartDefinition allbody = root.addOrReplaceChild("allbody", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition DownBody = allbody.addOrReplaceChild("DownBody", CubeListBuilder.create().texOffs(90, 226).addBox(-6.5F, -8.0F, -5.0F, 12.0F, 8.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -27.0F, 1.5F));

		PartDefinition fabric = DownBody.addOrReplaceChild("fabric", CubeListBuilder.create(), PartPose.offset(-0.5F, -2.75F, -3.25F));

		PartDefinition DownBody_r1 = fabric.addOrReplaceChild("DownBody_r1", CubeListBuilder.create().texOffs(135, 226).addBox(-4.5F, 0.0F, -2.75F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 2.0F, 1.0F, -0.1309F, 0.0F, 0.0F));

		PartDefinition Bfabric = DownBody.addOrReplaceChild("Bfabric", CubeListBuilder.create(), PartPose.offset(-0.5F, -1.5F, 4.75F));

		PartDefinition DownBody_r2 = Bfabric.addOrReplaceChild("DownBody_r2", CubeListBuilder.create().texOffs(135, 226).addBox(-4.5F, 0.0F, -2.75F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.01F)), PartPose.offsetAndRotation(0.0F, 0.0F, 3.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition UpperBody = DownBody.addOrReplaceChild("UpperBody", CubeListBuilder.create().texOffs(146, 178).addBox(-9.0F, -6.0F, -7.5F, 18.0F, 8.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, -10.0F, 0.5F));

		PartDefinition Rfabric = UpperBody.addOrReplaceChild("Rfabric", CubeListBuilder.create(), PartPose.offset(-7.0F, 2.25F, 0.25F));

		PartDefinition UpperBody_r1 = Rfabric.addOrReplaceChild("UpperBody_r1", CubeListBuilder.create().texOffs(135, 226).addBox(-4.5F, 0.0F, -2.75F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(3.0F, 1.0F, 0.0F, 0.0F, 1.5708F, 0.3491F));

		PartDefinition Lfabric = UpperBody.addOrReplaceChild("Lfabric", CubeListBuilder.create(), PartPose.offset(7.0F, 2.25F, 0.25F));

		PartDefinition UpperBody_r2 = Lfabric.addOrReplaceChild("UpperBody_r2", CubeListBuilder.create().texOffs(135, 226).addBox(-4.5F, 0.0F, -2.75F, 9.0F, 10.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(-3.0F, 1.0F, 0.0F, 0.0F, -1.5708F, -0.3491F));

		PartDefinition head = UpperBody.addOrReplaceChild("head", CubeListBuilder.create().texOffs(119, 245).addBox(-4.0F, -2.0519F, -4.8279F, 8.0F, 7.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(239, 13).addBox(-4.0F, -3.0519F, -4.5779F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.52F))
		.texOffs(152, 252).addBox(1.0F, 0.9481F, -5.8279F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 254).addBox(-5.0F, 0.9481F, -5.8279F, 4.0F, 4.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(239, 0).addBox(-5.0F, -4.3019F, -5.8279F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offset(0.0F, -10.9481F, 0.8279F));

		PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(241, 245).addBox(-2.5F, -4.0F, 0.0F, 5.0F, 5.0F, 10.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, -4.8019F, 1.1721F, -0.7854F, 0.0F, 0.0F));

		PartDefinition hat = head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, -0.0519F, 2.6721F));

		PartDefinition head_r2 = hat.addOrReplaceChild("head_r2", CubeListBuilder.create().texOffs(231, 124).addBox(-5.5F, -3.3449F, -10.072F, 11.0F, 4.0F, 10.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition ring = UpperBody.addOrReplaceChild("ring", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -1.5F, -2.0F, 0.5236F, 0.0F, 0.0F));

		PartDefinition hole = ring.addOrReplaceChild("hole", CubeListBuilder.create().texOffs(211, 178).addBox(-15.0F, -1.5F, 11.0F, 26.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(117, 0).addBox(11.0F, -1.5F, -11.0F, 4.0F, 3.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(178, 0).addBox(-15.0F, -1.5F, -15.0F, 4.0F, 3.0F, 26.0F, new CubeDeformation(0.0F))
		.texOffs(211, 186).addBox(-11.0F, -1.5F, -15.0F, 26.0F, 3.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(0, 0).addBox(-14.5F, -2.0F, -14.5F, 29.0F, 1.0F, 29.0F, new CubeDeformation(-0.2F)), PartPose.offsetAndRotation(0.0F, -6.0F, 5.0F, 0.0F, -0.7854F, 0.0F));

		PartDefinition LeftArm = UpperBody.addOrReplaceChild("LeftArm", CubeListBuilder.create(), PartPose.offsetAndRotation(14.0F, -9.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition LeftUpperArm = LeftArm.addOrReplaceChild("LeftUpperArm", CubeListBuilder.create().texOffs(182, 115).addBox(2.5F, -8.0F, -6.5F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(163, 201).addBox(2.5F, -4.0F, -8.0F, 8.0F, 8.0F, 15.0F, new CubeDeformation(0.1F))
		.texOffs(0, 179).addBox(0.5F, 4.0F, -8.5F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(217, 140).addBox(0.5F, -10.0F, -8.5F, 2.0F, 14.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.2182F));

		PartDefinition LeftForeArm = LeftArm.addOrReplaceChild("LeftForeArm", CubeListBuilder.create().texOffs(49, 222).addBox(-7.5F, 0.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(10.0F, 4.0F, 0.0F));

		PartDefinition LeftHand = LeftForeArm.addOrReplaceChild("LeftHand", CubeListBuilder.create().texOffs(65, 191).addBox(-7.5F, -3.0F, -6.5F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(182, 49).addBox(-7.5F, -5.0F, -6.5F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.0F, 13.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

		PartDefinition RightArm = UpperBody.addOrReplaceChild("RightArm", CubeListBuilder.create(), PartPose.offsetAndRotation(-14.0F, -9.0F, 0.0F, -1.5713F, -0.0114F, 0.0865F));

		PartDefinition RightForeArm = RightArm.addOrReplaceChild("RightForeArm", CubeListBuilder.create().texOffs(163, 225).addBox(-2.5F, 0.0F, -5.5F, 10.0F, 16.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-10.0F, 4.0F, 0.0F, 0.1309F, 0.0F, 0.0F));

		PartDefinition RightHand = RightForeArm.addOrReplaceChild("RightHand", CubeListBuilder.create().texOffs(0, 198).addBox(-4.5F, -3.0F, -6.5F, 12.0F, 18.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(182, 82).addBox(-4.5F, -5.0F, -6.5F, 12.0F, 20.0F, 12.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(1.0F, 13.0F, 0.0F, -1.1781F, 0.0F, 0.0F));

		PartDefinition weapon = RightHand.addOrReplaceChild("weapon", CubeListBuilder.create().texOffs(146, 142).addBox(-1.5F, -1.5F, -18.625F, 3.0F, 3.0F, 32.0F, new CubeDeformation(0.0F))
		.texOffs(146, 142).addBox(-1.5F, -1.5F, 2.375F, 3.0F, 3.0F, 32.0F, new CubeDeformation(0.01F))
		.texOffs(114, 191).addBox(-2.0F, -2.0F, 34.375F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
		.texOffs(131, 191).addBox(-2.0F, -2.0F, -38.625F, 4.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(73, 142).addBox(-9.0F, -15.0F, -36.375F, 18.0F, 30.0F, 18.0F, new CubeDeformation(0.0F))
		.texOffs(0, 129).addBox(-9.0F, -15.5F, -36.375F, 18.0F, 31.0F, 18.0F, new CubeDeformation(0.3F))
		.texOffs(85, 31).addBox(-12.0F, 13.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.5F))
		.texOffs(85, 68).addBox(-12.0F, 13.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.3F))
		.texOffs(85, 105).addBox(-12.0F, 13.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.01F))
		.texOffs(85, 105).addBox(-12.0F, -25.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.01F))
		.texOffs(85, 68).addBox(-12.0F, -25.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.3F))
		.texOffs(85, 31).addBox(-12.0F, -25.0F, -39.375F, 24.0F, 12.0F, 24.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(3.5F, 10.25F, -1.125F, 0.0873F, 0.0F, 0.0F));

		PartDefinition RightUpperArm = RightArm.addOrReplaceChild("RightUpperArm", CubeListBuilder.create().texOffs(114, 201).addBox(-14.5F, -8.0F, -6.5F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F))
		.texOffs(210, 201).addBox(-10.5F, -4.0F, -8.0F, 8.0F, 8.0F, 15.0F, new CubeDeformation(0.1F))
		.texOffs(182, 30).addBox(-16.5F, 4.0F, -8.5F, 16.0F, 2.0F, 16.0F, new CubeDeformation(0.0F))
		.texOffs(204, 225).addBox(-2.5F, -10.0F, -8.5F, 2.0F, 14.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.2182F));

		PartDefinition LeftLeg = allbody.addOrReplaceChild("LeftLeg", CubeListBuilder.create(), PartPose.offset(3.0F, -28.0F, 0.5F));

		PartDefinition LeftLeg_r1 = LeftLeg.addOrReplaceChild("LeftLeg_r1", CubeListBuilder.create().texOffs(247, 30).addBox(-6.227F, 0.266F, -0.8372F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(3.25F, -2.3F, -1.0F, -0.1739F, -0.0151F, -0.2605F));

		PartDefinition LeftLeg_r2 = LeftLeg.addOrReplaceChild("LeftLeg_r2", CubeListBuilder.create().texOffs(241, 225).addBox(-2.0F, -12.0F, -3.0F, 7.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.5F, 12.0F, -1.0F, -0.1745F, 0.0F, -0.1745F));

		PartDefinition Leftleg2 = LeftLeg.addOrReplaceChild("Leftleg2", CubeListBuilder.create().texOffs(0, 229).addBox(-4.0F, -2.0F, -3.75F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(231, 49).addBox(-4.0F, -3.0F, -3.75F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(3.0F, 11.0F, -0.5F));

		PartDefinition RightLeg = allbody.addOrReplaceChild("RightLeg", CubeListBuilder.create(), PartPose.offset(-3.0F, -28.0F, 0.5F));

		PartDefinition RightLeg_r1 = RightLeg.addOrReplaceChild("RightLeg_r1", CubeListBuilder.create().texOffs(33, 249).addBox(-0.773F, 0.266F, -0.8372F, 7.0F, 8.0F, 7.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(-3.25F, -2.3F, -1.0F, -0.1739F, 0.0151F, 0.2605F));

		PartDefinition RightLeg_r2 = RightLeg.addOrReplaceChild("RightLeg_r2", CubeListBuilder.create().texOffs(90, 245).addBox(-5.0F, -12.0F, -3.0F, 7.0F, 12.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.5F, 12.0F, -1.0F, -0.1745F, 0.0F, 0.1745F));

		PartDefinition Rightleg2 = RightLeg.addOrReplaceChild("Rightleg2", CubeListBuilder.create().texOffs(231, 74).addBox(-4.0F, -2.0F, -3.75F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(231, 99).addBox(-4.0F, -3.0F, -3.75F, 8.0F, 16.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(-3.0F, 11.0F, -0.5F));

		return LayerDefinition.create(meshdefinition, 512, 512);
	}

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.awaken, HammerMobAnimation.AWAKEN, ageInTicks);
        this.animate(entity.idle, HammerMobAnimation.IDLE, ageInTicks);
        this.animateWalk(HammerMobAnimation.WALKING, limbSwing, limbSwingAmount, 2.0F, 2.5F);
        this.animate(entity.sprinting, HammerMobAnimation.SPRINTING, ageInTicks);
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }

    @Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}