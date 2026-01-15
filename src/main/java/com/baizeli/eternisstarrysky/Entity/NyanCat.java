package com.baizeli.eternisstarrysky.Entity;

import com.baizeli.eternisstarrysky.client.particles.ModParticles;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Arrays;

public class NyanCat extends AbstractArrow {
    private int bounceCount = 0;
    private static final int MAX_BOUNCES = 8;
    public final Vec3[] trailPositions = new Vec3[64];
    public int trailPointer = -1;

    public NyanCat(EntityType<NyanCat> entityType, Level level) {
        super(entityType, level);
        setupNyanCatProperties();
    }

    public NyanCat(EntityType<? extends AbstractArrow> entityType, double x, double y, double z, Level level) {
        super(entityType, x, y, z, level);
        setupNyanCatProperties();
    }

    public NyanCat(EntityType<? extends AbstractArrow> entityType, LivingEntity shooter, Level level) {
        super(entityType, shooter, level);
        setupNyanCatProperties();
    }

    private void setupNyanCatProperties() {

        this.setPierceLevel((byte) 255);

        this.setBaseDamage(4.0);

        this.setSoundEvent(SoundEvents.CAT_AMBIENT);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {


        Entity entity = result.getEntity();
        Entity owner = this.getOwner();

        if (!this.level().isClientSide && entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;


            float damage = (float) this.getBaseDamage();
            if (this.isCritArrow()) {
                damage += this.random.nextInt((int) (damage / 2) + 2);
            }


            if (owner == null) {
                livingEntity.hurt(this.damageSources().arrow(this, this), damage);
            } else {
                livingEntity.hurt(this.damageSources().arrow(this, owner), damage);
                if (owner instanceof LivingEntity) {
                    ((LivingEntity) owner).setLastHurtMob(livingEntity);
                }
            }
        }



        this.playSound(this.getHitGroundSoundEvent(), 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {

        if (bounceCount < MAX_BOUNCES) {
            bounceCount++;


            Vec3 currentMotion = this.getDeltaMovement();


            switch (result.getDirection()) {
                case UP:
                case DOWN:

                    this.setDeltaMovement(currentMotion.x, -currentMotion.y * 0.8, currentMotion.z);
                    break;
                case NORTH:
                case SOUTH:

                    this.setDeltaMovement(currentMotion.x, currentMotion.y * 0.8, -currentMotion.z * 0.8);
                    break;
                case EAST:
                case WEST:

                    this.setDeltaMovement(-currentMotion.x * 0.8, currentMotion.y * 0.8, currentMotion.z);
                    break;
            }


            this.setDeltaMovement(
                    this.getDeltaMovement().x + (this.random.nextDouble() - 0.5) * 0.1,
                    this.getDeltaMovement().y + (this.random.nextDouble() - 0.5) * 0.1,
                    this.getDeltaMovement().z + (this.random.nextDouble() - 0.5) * 0.1
            );


            this.playSound(this.getHitGroundSoundEvent(), 0.8F, 1.0F + (this.random.nextFloat() - 0.5F) * 0.2F);


            this.inGround = false;
            this.inGroundTime = 0;
        } else {

            this.discard();
        }
    }

    @Override
    protected ItemStack getPickupItem() {

        return null;
    }

    @Override
    public void tick() {
        super.tick();
        recordTrailPosition();
        {


            Vec3 velocity = this.getDeltaMovement().normalize(); // 获取当前速度方向
            
            // 计算垂直于速度方向的两个向量
            Vec3 perpendicular1, perpendicular2;
            if (Math.abs(velocity.y) < 0.9) {
                // 如果不是接近垂直向上/下的方向，使用Y轴作为参考
                perpendicular1 = new Vec3(0, 1, 0).cross(velocity).normalize();
            } else {
                // 如果接近垂直方向，使用X轴作为参考
                perpendicular1 = new Vec3(1, 0, 0).cross(velocity).normalize();
            }
            perpendicular2 = velocity.cross(perpendicular1).normalize();
            
            // 在垂直于速度的方向上生成一圈粒子
            int particleCount = 8; // 圆周上的粒子数
            for (int i = 0; i < particleCount; i++) {
                double angle = 2.0 * Math.PI * i / particleCount;
                double offsetX = Math.cos(angle) * 0.5; // 半径为0.5的圆
                double offsetZ = Math.sin(angle) * 0.5;
                
                // 计算相对于速度方向的偏移
                Vec3 offset = perpendicular1.scale(offsetX).add(perpendicular2.scale(offsetZ));
                
                Vec3 particlePos = new Vec3(this.getX(), this.getY(), this.getZ()).add(offset);
                
                // 粒子速度方向垂直于速度方向向外扩散
                Vec3 direction = offset.normalize().scale(0.5);
                
                this.level().addParticle(ModParticles.CRESCENT_BLADE.get(),
                        particlePos.x, particlePos.y, particlePos.z,
                        direction.x, direction.y, direction.z);

            }


        }


        if (this.tickCount > 600) {
            this.discard();
        }
    }

    private void recordTrailPosition() {
        Vec3 currentPos = new Vec3(this.getX(), this.getY(), this.getZ());
        
        if (this.trailPointer == -1) {

            Arrays.fill(this.trailPositions, currentPos);
            this.trailPointer = 0;
            return;
        }
        
        if (++this.trailPointer == this.trailPositions.length) {
            this.trailPointer = 0;
        }
        
        this.trailPositions[this.trailPointer] = currentPos;
    }

    @Override
    protected boolean tryPickup(net.minecraft.world.entity.player.Player player) {

        return false;
    }


    public int getBounceCount() {
        return bounceCount;
    }
}