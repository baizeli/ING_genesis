package miku.united_as_one.genesis.common.entity.test;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BaiZeLiEntity extends PathfinderMob {

    private static final String CRYPTO_KEY = "BaiZe_Absolute_Lock";

    private String bai_ze_li = encrypt("25");
    private final String hyw = encrypt("25");

    private boolean _1211 = false;

    public BaiZeLiEntity(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
        this.setCustomName(Component.literal("测试实体"));
        this.setCustomNameVisible(true);
        this.setPersistenceRequired();

        super.setHealth(25.0f);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ARMOR, 0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    private static String encrypt(String value) {
        byte[] txt = value.getBytes(StandardCharsets.UTF_8);
        byte[] key = CRYPTO_KEY.getBytes(StandardCharsets.UTF_8);
        byte[] res = new byte[txt.length];
        for (int i = 0; i < txt.length; i++) res[i] = (byte) (txt[i] ^ key[i % key.length]);
        return Base64.getEncoder().encodeToString(res);
    }

    private static String decrypt(String base64) {
        try {
            byte[] txt = Base64.getDecoder().decode(base64);
            byte[] key = CRYPTO_KEY.getBytes(StandardCharsets.UTF_8);
            byte[] res = new byte[txt.length];
            for (int i = 0; i < txt.length; i++) res[i] = (byte) (txt[i] ^ key[i % key.length]);
            return new String(res, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "ERROR";
        }
    }

    public boolean check_SB_Death() {
        return "sb".equals(decrypt(this.bai_ze_li));
    }

    public void damageBaiZeLi(String damageAmount, DamageSource source) {
        if (this.level().isClientSide || check_SB_Death()) return;

        String oldEncryptedHealth = this.bai_ze_li;
        String decryptedHealth = decrypt(this.bai_ze_li);
        String decryptedMaxHealth = decrypt(this.hyw);

        try {
            if ("ERROR".equals(decryptedHealth) || "ERROR".equals(decryptedMaxHealth)) {
                throw new IllegalStateException("Corrupted");
            }

            BigDecimal currentHealth = new BigDecimal(decryptedHealth);
            BigDecimal maxHealth = new BigDecimal(decryptedMaxHealth);
            BigDecimal incomingDamage = new BigDecimal(damageAmount);

            // 最高只能扣2.5
            BigDecimal maxAllowedDamage = maxHealth.multiply(new BigDecimal("0.1"));
            BigDecimal actualDamage = incomingDamage.compareTo(maxAllowedDamage) > 0
                    ? maxAllowedDamage : incomingDamage;

            BigDecimal newHealth = currentHealth.subtract(actualDamage);

            if (newHealth.compareTo(BigDecimal.ZERO) <= 0) {
                this.bai_ze_li = encrypt("sb");
                this._1211 = true;
                this.die(source);
            } else {
                this.bai_ze_li = encrypt(newHealth.toPlainString());
            }

        } catch (Exception e) {
            this.bai_ze_li = encrypt("25"); // 篡改防御：回满
        }

        if (!oldEncryptedHealth.equals(this.bai_ze_li)) {
            syncHealthToClients();
        }
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level().isClientSide) return false;
        if (check_SB_Death() && this._1211) return super.hurt(source, amount);

        if (source == this.damageSources().fellOutOfWorld() || source == this.damageSources().genericKill()) {
            return false;
        }

        damageBaiZeLi(String.valueOf(amount), source);

        if (!check_SB_Death()) {
            this.level().broadcastEntityEvent(this, (byte) 2);
        }

        return true;
    }

    @Override
    public void setHealth(float health) {
        if (check_SB_Death() && this._1211) {
            super.setHealth(0.0f);
        } else {
            super.setHealth(25.0f);
        }
    }
    @Override
    public void tick() {
        if (!check_SB_Death() && !this._1211) {
            this.dead = false;
            this.removalReason = null;
            if (this.getY() < -64.0D) {
                this.setDeltaMovement(0, 0, 0);
                this.setPos(this.getX(), 150.0D, this.getZ());
                this.fallDistance = 0.0F;
            }
            if (this.getHealth() != 25.0f) {
                this.setHealth(25.0f);
            }
            String plainHealth = decrypt(this.bai_ze_li);
            try {
                if (!"sb".equals(plainHealth)) {
                    BigDecimal val = new BigDecimal(plainHealth);
                    if (val.compareTo(BigDecimal.ZERO) <= 0) {
                        this.bai_ze_li = encrypt("25");
                    }
                }
            } catch (Exception e) {
                this.bai_ze_li = encrypt("25");
            }

            if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
                if (serverLevel.getEntity(this.getId()) == null) {
                    serverLevel.addFreshEntity(this);
                }
            }
        }

        super.tick();
    }
    @Override
    public void remove(RemovalReason reason) {
        if (!check_SB_Death() || !this._1211) {
            if (reason != RemovalReason.UNLOADED_TO_CHUNK && reason != RemovalReason.UNLOADED_WITH_PLAYER) {
                this.removalReason = null;
                return;
            }
        }
        super.remove(reason);
    }

    @Override
    public void die(DamageSource damageSource) {
        if (check_SB_Death() && this._1211) {
            super.die(damageSource);
        }
    }

    @Override
    public void kill() {
        if (check_SB_Death() && this._1211) {
            super.kill();
        }
    }

    @Override
    public boolean isDeadOrDying() {
        return check_SB_Death() && this._1211;
    }

    @Override
    public boolean isAlive() {
        return !check_SB_Death() && super.isAlive();
    }

    // ==========================================
    // 6. 防位移与防传送 (JZYY 强制坐标锁)
    // ==========================================
    @Override
    public void teleportTo(double x, double y, double z) {
        // 免疫一切传送法杖或指令传送
    }

    @Override
    public void setPos(double x, double y, double z) {
        // 防止被强行设定到极远或极低导致崩溃
        if (Double.isNaN(x) || Double.isNaN(y) || Double.isNaN(z)) return;
        if (y >= -64 && y <= 320 && Math.abs(x) < 30000000 && Math.abs(z) < 30000000) {
            super.setPos(x, y, z);
        }
    }

    @Override
    public Entity changeDimension(ServerLevel destination) {
        return this; // 免疫跨维度传送（丢进末地门、地狱门无效）
    }

    // ==========================================
    // 7. 实体常驻声明
    // ==========================================
    @Override public boolean isAlwaysTicking() { return true; }
    @Override public boolean shouldBeSaved() { return true; }
    @Override public boolean isPersistenceRequired() { return true; }
    @Override public void checkDespawn() { }

    // ==========================================
    // 8. 数据保存与客户端同步
    // ==========================================
    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putString("bai_ze_li_crypto", this.bai_ze_li);
        compound.putBoolean("DeathField_1211", this._1211);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("bai_ze_li_crypto")) {
            this.bai_ze_li = compound.getString("bai_ze_li_crypto");
        }
        if (compound.contains("DeathField_1211")) {
            this._1211 = compound.getBoolean("DeathField_1211");
        }
    }

    public void receiveStringHealthUpdate(String encryptedHealth) {
        if (this.level().isClientSide) {
            this.bai_ze_li = encryptedHealth;
        }
    }

    public void syncHealthToClients() {
        if (!this.level().isClientSide && this.level() instanceof ServerLevel) {
        }
    }

    public float getCustomLifePercent() {
        if (check_SB_Death()) return 0.0f;
        try {
            BigDecimal current = new BigDecimal(decrypt(this.bai_ze_li));
            return current.floatValue() / 25.0f;
        } catch (Exception e) {
            return 1.0f;
        }
    }

    @Override
    public float getHealth() {
        return 25.0f;
    }
}