package miku.united_as_one.genesis.common.entity.test;

import miku.united_as_one.genesis.common.entity.test.data.BaiZeLiData;
import miku.united_as_one.genesis.init.registry.BaiZeEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;

public class BaiZeLiEntity extends PathfinderMob implements IBaiZeLi {

    private BaiZeLiData data;
    private BaiZeLiBehavior behavior;
    private boolean initialized = false;

    public BaiZeLiEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);

        this.setCustomName(Component.literal("白泽利"));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();
    }

    public BaiZeLiEntity(PlayMessages.SpawnEntity spawn, Level level) {
        this((EntityType<? extends PathfinderMob>) BaiZeEntities.BAI_ZE.get(), level);
    }

    // ✅ 延迟初始化（关键修复）
    private void ensureInit() {
        if (!initialized) {
            this.data = new BaiZeLiData();
            this.behavior = new BaiZeLiBehavior(this, data);
            initialized = true;
        }
    }

    // ===== 属性 =====
    public static AttributeSupplier.Builder createAttributes() {
        return PathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    // ===== Tick =====
    @Override
    public void tick() {
        ensureInit();

        if (behavior != null) {
            behavior.tick();
        }

        super.tick();
    }

    // ===== 受伤 =====
    @Override
    public boolean hurt(DamageSource source, float amount) {
        ensureInit();

        return behavior != null && behavior.handleHurt(source, amount);
    }

    // ===== 禁止原版血量系统 =====
    @Override public void setHealth(float health) {}
    @Override public void heal(float amount) {}
    @Override public void die(DamageSource source) {}
    @Override public void kill() {}
    @Override public boolean isAlive() { return true; }
    @Override public boolean isDeadOrDying() { return false; }
    @Override public boolean addEffect(net.minecraft.world.effect.MobEffectInstance effect) { return false; }

    // ===== 移除控制（只允许真死）=====
    @Override
    public void remove(RemovalReason reason) {
        if (data == null || data.isRealDead()) {
            super.remove(reason);
        }
    }

    // ===== 假血同步（你原有逻辑）=====
    public void receiveStringHealthUpdate(String encryptedHp) {
        ensureInit();

        if (this.data != null) {
            try {
                float hp = Float.parseFloat(BaiZeLiData.decrypt(encryptedHp, this));
                this.data.setRealHealth(this, hp);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // ===== NBT =====
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        ensureInit();
        if (data != null) data.saveToNBT(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        ensureInit();
        if (data != null) data.loadFromNBT(tag);
    }

    // ===== 接口实现（给 HiddenClass 用）=====
    @Override
    public void tickLogic() {
        ensureInit();
        if (behavior != null) behavior.tick();
    }

    @Override
    public boolean handleHurt(float damage) {
        ensureInit();
        return behavior != null && behavior.handleHurt(null, damage);
    }

    @Override
    public boolean isRealDead() {
        ensureInit();
        return data != null && data.isRealDead();
    }
}