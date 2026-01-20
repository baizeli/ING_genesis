package miku.united_as_one.genesis.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class AfterImageData {
    private final Vec3 position;
    private final float yRot;
    private final float xRot;
    private final long createTime;
    private final String playerUUID;
    private int age = 0;
    private ItemStack mainHandItem;
    private ItemStack offHandItem;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private HumanoidModel.ArmPose leftArmPose;
    private HumanoidModel.ArmPose rightArmPose;
    private ModelPoseData poseData;
    private ResourceLocation skinTexture;
    private boolean slimModel;

    public static class ModelPoseData {
        public float headXRot;
        public float headYRot;
        public float headZRot;
        public float bodyXRot;
        public float bodyYRot;
        public float bodyZRot;
        public float leftArmXRot;
        public float leftArmYRot;
        public float leftArmZRot;
        public float rightArmXRot;
        public float rightArmYRot;
        public float rightArmZRot;
        public float leftLegXRot;
        public float leftLegYRot;
        public float leftLegZRot;
        public float rightLegXRot;
        public float rightLegYRot;
        public float rightLegZRot;
        public float leftArmX;
        public float leftArmY;
        public float leftArmZ;
        public float rightArmX;
        public float rightArmY;
        public float rightArmZ;
        public float leftLegX;
        public float leftLegY;
        public float leftLegZ;
        public float rightLegX;
        public float rightLegY;
        public float rightLegZ;
        public float swimAmount;
        public boolean crouching;
        public boolean sleeping;
    }

    public AfterImageData(Player player) {
        this.position = player.position();
        this.yRot = player.getYRot();
        this.xRot = player.getXRot();
        this.createTime = System.currentTimeMillis();
        this.playerUUID = player.getStringUUID();
        this.mainHandItem = player.getMainHandItem().copy();
        this.offHandItem = player.getOffhandItem().copy();
        this.helmet = player.getInventory().getArmor(3).copy();
        this.chestplate = player.getInventory().getArmor(2).copy();
        this.leggings = player.getInventory().getArmor(1).copy();
        this.boots = player.getInventory().getArmor(0).copy();
        
        if (player instanceof AbstractClientPlayer) {
            AbstractClientPlayer clientPlayer = (AbstractClientPlayer) player;
            PlayerRenderer renderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(clientPlayer);
            PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) renderer.getModel();
            this.leftArmPose = model.leftArmPose;
            this.rightArmPose = model.rightArmPose;

            this.skinTexture = clientPlayer.getSkinTextureLocation();
            this.slimModel = false;
        } else {
            this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        }

        this.poseData = copyPlayerPose(player);
    }

    public ItemStack getMainHandItem() {
        return mainHandItem;
    }

    public ItemStack getOffHandItem() {
        return offHandItem;
    }

    public ItemStack getHelmet() {
        return helmet;
    }

    public ItemStack getChestplate() {
        return chestplate;
    }

    public ItemStack getLeggings() {
        return leggings;
    }

    public ItemStack getBoots() {
        return boots;
    }

    public HumanoidModel.ArmPose getLeftArmPose() {
        return leftArmPose;
    }

    public HumanoidModel.ArmPose getRightArmPose() {
        return rightArmPose;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getYRot() {
        return yRot;
    }

    public void tick() {
        this.age++;
    }

    public boolean isExpired() {
        return (this.age >= 40);
    }

    public float getAlpha() {
        return Math.max(0.0F, 0.8F * (1.0F - (float) this.age / 40.0F));
    }

    private ModelPoseData copyPlayerPose(Player player) {
        ModelPoseData pose = new ModelPoseData();
        Minecraft mc = Minecraft.getInstance();
        EntityRenderDispatcher dispatcher = mc.getEntityRenderDispatcher();
        EntityRenderer renderer = dispatcher.getRenderer(player);
        
        if (renderer instanceof PlayerRenderer) {
            PlayerRenderer playerRenderer = (PlayerRenderer) renderer;
            PlayerModel<AbstractClientPlayer> model = (PlayerModel<AbstractClientPlayer>) playerRenderer.getModel();
            ModelPart head = model.head;
            pose.headXRot = head.xRot;
            pose.headYRot = head.yRot;
            pose.headZRot = head.zRot;

            ModelPart body = model.body;
            pose.bodyXRot = body.xRot;
            pose.bodyYRot = body.yRot;
            pose.bodyZRot = body.zRot;

            ModelPart leftArm = model.leftArm;
            pose.leftArmXRot = leftArm.xRot;
            pose.leftArmYRot = leftArm.yRot;
            pose.leftArmZRot = leftArm.zRot;
            pose.leftArmX = leftArm.x;
            pose.leftArmY = leftArm.y;
            pose.leftArmZ = leftArm.z;

            ModelPart rightArm = model.rightArm;
            pose.rightArmXRot = rightArm.xRot;
            pose.rightArmYRot = rightArm.yRot;
            pose.rightArmZRot = rightArm.zRot;
            pose.rightArmX = rightArm.x;
            pose.rightArmY = rightArm.y;
            pose.rightArmZ = rightArm.z;

            ModelPart leftLeg = model.leftLeg;
            pose.leftLegXRot = leftLeg.xRot;
            pose.leftLegYRot = leftLeg.yRot;
            pose.leftLegZRot = leftLeg.zRot;
            pose.leftLegX = leftLeg.x;
            pose.leftLegY = leftLeg.y;
            pose.leftLegZ = leftLeg.z;

            ModelPart rightLeg = model.rightLeg;
            pose.rightLegXRot = rightLeg.xRot;
            pose.rightLegYRot = rightLeg.yRot;
            pose.rightLegZRot = rightLeg.zRot;
            pose.rightLegX = rightLeg.x;
            pose.rightLegY = rightLeg.y;
            pose.rightLegZ = rightLeg.z;

            pose.swimAmount = model.swimAmount;
            pose.crouching = model.crouching;
            pose.sleeping = player.isSleeping();
        }

        return pose;
    }

    public ResourceLocation getSkinTexture() {
        return skinTexture;
    }

    public void applyPoseToModel(PlayerModel<AbstractClientPlayer> model) {
        if (this.poseData == null) {
            return;
        }
        model.head.xRot = this.poseData.headXRot;
        model.head.yRot = this.poseData.headYRot;
        model.head.zRot = this.poseData.headZRot;

        model.body.xRot = this.poseData.bodyXRot;
        model.body.yRot = this.poseData.bodyYRot;
        model.body.zRot = this.poseData.bodyZRot;

        model.leftArm.xRot = this.poseData.leftArmXRot;
        model.leftArm.yRot = this.poseData.leftArmYRot;
        model.leftArm.zRot = this.poseData.leftArmZRot;
        model.leftArm.x = this.poseData.leftArmX;
        model.leftArm.y = this.poseData.leftArmY;
        model.leftArm.z = this.poseData.leftArmZ;

        model.rightArm.xRot = this.poseData.rightArmXRot;
        model.rightArm.yRot = this.poseData.rightArmYRot;
        model.rightArm.zRot = this.poseData.rightArmZRot;
        model.rightArm.x = this.poseData.rightArmX;
        model.rightArm.y = this.poseData.rightArmY;
        model.rightArm.z = this.poseData.rightArmZ;

        model.leftLeg.xRot = this.poseData.leftLegXRot;
        model.leftLeg.yRot = this.poseData.leftLegYRot;
        model.leftLeg.zRot = this.poseData.leftLegZRot;
        model.leftLeg.x = this.poseData.leftLegX;
        model.leftLeg.y = this.poseData.leftLegY;
        model.leftLeg.z = this.poseData.leftLegZ;

        model.rightLeg.xRot = this.poseData.rightLegXRot;
        model.rightLeg.yRot = this.poseData.rightLegYRot;
        model.rightLeg.zRot = this.poseData.rightLegZRot;
        model.rightLeg.x = this.poseData.rightLegX;
        model.rightLeg.y = this.poseData.rightLegY;
        model.rightLeg.z = this.poseData.rightLegZ;

        model.swimAmount = this.poseData.swimAmount;
        model.crouching = this.poseData.crouching;
    }
}