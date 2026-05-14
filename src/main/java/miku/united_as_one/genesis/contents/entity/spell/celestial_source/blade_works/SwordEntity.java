package miku.united_as_one.genesis.contents.entity.spell.celestial_source.blade_works;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SwordEntity extends LivingEntity {
    @Nullable
    private ItemStack storedSword = ItemStack.EMPTY;
    @Nullable
    private LivingEntity maker;
    private int age = 0;

    public SwordEntity(EntityType<SwordEntity> entityType, Level level) {
        super(entityType, level);
        init(null);
    }

    public SwordEntity(EntityType<SwordEntity> entityType, Level level, double x, double y, double z, LivingEntity maker) {
        super(entityType, level);
        setPos(x, y, z);
        init(maker);
    }

    private void init(LivingEntity maker) {
        this.noPhysics = true;
        if (maker != null) {
            ItemStack sword = maker.getMainHandItem().copy();
            setStoredSword(sword);
        }
        this.maker = maker;
    }

    @Nullable
    public ItemStack getStoredSword() {
        return storedSword;
    }

    public void setStoredSword(ItemStack sword) {
        this.storedSword = sword == null ? ItemStack.EMPTY : sword.copy();
    }

    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        if (this.storedSword != null) {
            CompoundTag swordTag = new CompoundTag();
            this.storedSword.save(swordTag);
            compound.put("StoredSword", swordTag);
        }
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        if (compound.contains("StoredSword", 10)) { // 10 = CompoundTag
            this.storedSword = ItemStack.of(compound.getCompound("StoredSword"));
        }
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return ClientboundAddSwordEntityPacket.create(this);
    }

    @Override
    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket packet) {
        super.recreateFromPacket(packet);
        if (packet instanceof ClientboundAddSwordEntityPacket ext) {
            this.readAdditionalSaveData(ext.extra);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return AttributeSupplier.builder()
                .add(Attributes.MAX_HEALTH, 1)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.ARMOR_TOUGHNESS, 0)
                .add(ForgeMod.SWIM_SPEED.get(), 0)
                .add(ForgeMod.NAMETAG_DISTANCE.get(), 0)
                .add(ForgeMod.ENTITY_GRAVITY.get(), 0)
                .add(ForgeMod.STEP_HEIGHT_ADDITION.get(), 0)
                .add(Attributes.FOLLOW_RANGE, 0)
                .add(Attributes.ATTACK_KNOCKBACK, 0);
    }

    @Override
    public void tick() {
        super.tick();
        ++age;

        if (this.age > 600) {
            this.stopRiding();
            this.levelCallback.onRemove(Entity.RemovalReason.DISCARDED);
        }

        List<LivingEntity> nearby = this.level().getEntitiesOfClass(
                LivingEntity.class,
                this.getBoundingBox().inflate(15.0D),
                e -> e != this && e.isAlive() && !(e instanceof SwordEntity) && !(e instanceof MagicCircle) && e != this.maker
        );

        LivingEntity target = this.level().getNearestEntity(
                nearby,
                TargetingConditions.forNonCombat()
                        .range(15.0D)
                        .selector(e -> e.isAlive() && !(e instanceof SwordEntity) && !(e instanceof MagicCircle) && e != this.maker),
                this,
                this.getX(),
                this.getY(),
                this.getZ()
        );

        if (target != null) {
            this.setPos(target.position());
        }
    }

    @Override
    public void rideTick() {
    }

    @Override
    public void tickDeath() {
    }

    @Override
    public void tickEffects() {
    }

    @Override
    public void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
    }

    @Override
    public float tickHeadTurn(float yRot, float animStep) {
        return 0;
    }

    @Override
    public boolean isAlive() {
        return true;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.singleton(ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {
    }

    @Override
    public boolean isInLava() {
        return false;
    }

    @Override
    public boolean isFreezing() {
        return false;
    }

    @Override
    public boolean isFree(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public void setHealth(float health) {
        this.entityData.set(DATA_HEALTH_ID, 1F);
    }

    @Override
    public float getHealth() {
        return 1;
    }

    @Override
    public boolean isAlwaysTicking() {
        return true;
    }

    @Override
    public boolean isInWater() {
        return false;
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public boolean isInWaterOrRain() {
        return false;
    }

    @Override
    public boolean isPassenger() {
        return false;
    }

    @Override
    public boolean isSwimming() {
        return false;
    }

    @Override
    public boolean isUnderWater() {
        return false;
    }

    @Override
    public boolean isInvisible() {
        return false;//
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isFullyFrozen() {
        return false;
    }

    @Override
    public boolean isFlapping() {
        return false;
    }

    @Override
    public boolean isDeadOrDying() {
        return false;
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isShiftKeyDown() {
        return false;
    }

    @Override
    public boolean isEffectiveAi() {
        return false;
    }

    @Override
    public boolean isUsingItem() {
        return false;
    }

    @Override
    public boolean isInWaterOrBubble() {
        return false;
    }

    @Override
    public boolean isInWaterRainOrBubble() {
        return false;
    }

    @Override
    public boolean isAutoSpinAttack() {
        return false;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canCollideWith(@NotNull Entity entity) {
        return false;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public void die(@NotNull DamageSource damageSource) {
    }

    @Override
    public void dropAllDeathLoot(@NotNull DamageSource damageSource) {
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    @Override
    public boolean canAddPassenger(@NotNull Entity passenger) {
        return false;
    }

    @Override
    protected boolean canRide(@NotNull Entity vehicle) {
        return false;
    }

    @Override
    public boolean canUpdate() {
        return true;
    }

    @Override
    public boolean canBeAffected(@NotNull MobEffectInstance effectInstance) {
        return false;
    }

    @Override
    public boolean canTakeItem(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean canBeSeenByAnyone() {
        return true;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity livingentity, @NotNull TargetingConditions condition) {
        return false;
    }

    @Override
    public boolean canAttack(@NotNull LivingEntity target) {
        return false;
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canSprint() {
        return false;
    }

    @Override
    public boolean canAttackType(@NotNull EntityType<?> entityType) {
        return false;
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    @Override
    public boolean canEnterPose(@NotNull Pose pose) {
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean canDisableShield() {
        return false;
    }

    @Override
    public boolean addEffect(@NotNull MobEffectInstance effectInstance, @Nullable Entity entity) {
        return false;
    }

    @Override
    public boolean addEffect(@NotNull MobEffectInstance effectInstance) {
        return false;
    }

    @Override
    public void addPassenger(@NotNull Entity passenger) {
    }

    @Override
    public boolean addTag(@NotNull String tag) {
        return false;
    }

    @Override
    public void setId(int id) {
    }

    @Override
    public void setAirSupply(int air) {
    }

    @Override
    public void setSwimming(boolean swimming) {
    }

    @Override
    public void setUUID(@NotNull UUID uniqueId) {
    }

    @Override
    public void setPose(@NotNull Pose pose) {
    }

    @Override
    public void setInvisible(boolean invisible) {
    }

    @Override
    public void setIsInPowderSnow(boolean isInPowderSnow) {
    }

    @Override
    public void setCustomName(@Nullable Component name) {
    }

    @Override
    public void setCustomNameVisible(boolean alwaysRenderNameTag) {
    }

    @Override
    public void setTicksFrozen(int ticksFrozen) {
    }

    @Override
    public void setOnGround(boolean onGround) {
    }

    @Override
    public void setNoGravity(boolean noGravity) {
    }

    @Override
    public void setYRot(float yRot) {
    }

    @Override
    public void setXRot(float xRot) {
    }

    @Override
    public void setYBodyRot(float offset) {
    }

    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.LEFT;
    }
    @Override
    public void setYHeadRot(float rotation) {
    }

    @Override
    public void remove(@NotNull Entity.RemovalReason reason) {
    }

    @Override
    public void setRemoved(@NotNull Entity.RemovalReason reason) {
    }

    @Override
    public int getAirSupply() {
        return 1;
    }

    @Override
    public @NotNull Component getTypeName() {
        return Component.empty();
    }

    @Override
    public @NotNull Component getName() {
        return Component.empty();
    }

//    @Override
//    public @NotNull AABB getBoundingBoxForCulling() {
//        return DEFAULT_AABB;
//    }
//
//    @Override
//    public @NotNull AABB getBoundingBoxForPose(@NotNull Pose pose) {
//        return DEFAULT_AABB;
//    }
//
//    @Override
//    public @NotNull AABB getLocalBoundsForPose(@NotNull Pose pose) {
//        return DEFAULT_AABB;
//    }
//
//    @Override
//    public @NotNull AABB getBoundingBox() {
//        return DEFAULT_AABB;
//    }

    @Override
    public float getEyeHeight(@NotNull Pose pose) {
        return Float.MIN_VALUE;
    }

    @Override
    public float getEyeHeight() {
        return Float.MIN_VALUE;
    }

    @Override
    public void move(@NotNull MoverType type, @NotNull Vec3 pos) {
    }

    @Override
    public void moveTo(@NotNull Vec3 vec) {
    }

    @Override
    public void moveTo(double x, double y, double z) {
    }

    @Override
    public void moveTo(@NotNull BlockPos pos, float yRot, float xRot) {
    }

    @Override
    public void moveTo(double x, double y, double z, float yRot, float xRot) {
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return true;
    }

    @Override
    public void push(@NotNull Entity entity) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    public boolean mayInteract(@NotNull Level level, @NotNull BlockPos pos) {
        return false;
    }

    @Override
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 vec, @NotNull InteractionHand hand) {
        return InteractionResult.PASS;
    }

    @Override
    public boolean isEyeInFluidType(FluidType type) {
        return false;
    }

    public static class ClientboundAddSwordEntityPacket extends ClientboundAddEntityPacket {
        private final CompoundTag extra;

        public ClientboundAddSwordEntityPacket(SwordEntity e) {
            super(e);
            this.extra = new CompoundTag();
            e.addAdditionalSaveData(extra);
        }

        public ClientboundAddSwordEntityPacket(FriendlyByteBuf buf) {
            super(buf);
            this.extra = buf.readNbt();
        }

        @Override
        public void write(@NotNull FriendlyByteBuf buf) {
            super.write(buf);
            buf.writeNbt(extra);
        }

        public static Packet<ClientGamePacketListener> create(SwordEntity e) {
            return new ClientboundAddSwordEntityPacket(e);
        }
    }
}
