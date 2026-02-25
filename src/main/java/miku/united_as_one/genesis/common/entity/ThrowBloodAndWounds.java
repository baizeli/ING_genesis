package miku.united_as_one.genesis.common.entity;

import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

public class ThrowBloodAndWounds extends AbstractArrow {
    private int customLifeTime ;
    private int life;
    public ThrowBloodAndWounds(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
        customLifeTime = 20*5;
        life = 0;
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    public void setCustomLifeTime(int ticks) {
        this.customLifeTime = ticks;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && !this.inGround) {
            Vec3 movement = this.getDeltaMovement();

            // 1. 基础核心粒子：深红色粉尘
            this.level().addParticle(
                    ParticleRegistry.BLOOD_DRIP_HANG.get(),
                    this.getX(), this.getY(), this.getZ(),
                    0, 0, 0
            );

            // 2. 螺旋粒子效果
            int particleCount = 2; // 每 tick 生成的螺旋粒子数
            for (int i = 0; i < particleCount; i++) {
                // 随时间变化的旋转角度
                double angle = (this.tickCount + i * 10) * 0.5;
                double radius = 0.3; // 螺旋半径

                // 计算垂直于飞行方向的圆周坐标（简单实现）
                double offsetX = Math.cos(angle) * radius;
                double offsetY = Math.sin(angle) * radius;
                double offsetZ = Math.sin(angle + 1) * radius;

                // 生成红色粉末（DUST）或其他粒子
                this.level().addParticle(
                        ParticleRegistry.BLOOD_DRIP_LAND.get(),
                        this.getX() + offsetX,
                        this.getY() + offsetY,
                        this.getZ() + offsetZ,
                        movement.x * -0.1, 0, movement.z * -0.1 // 稍微向后飘散
                );
            }
        }
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity target) {
        Entity owner = this.getOwner();
        if (owner != null && target.is(owner)) {
            return false;
        }
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        Vec3 motion = this.getDeltaMovement();
        super.onHitEntity(result);
        this.setDeltaMovement(motion);
    }

    @Override
    protected void tickDespawn() {
        ++this.life;
        if (this.life >= this.customLifeTime) {
            this.discard();
        }
    }
}
