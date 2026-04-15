package miku.united_as_one.genesis.common.entity.test.data;

import miku.united_as_one.genesis.common.network.ModPacketHandler;
import miku.united_as_one.genesis.common.network.SyncBaiZeHealthPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.PacketDistributor;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BaiZeLiData {

    private static final String CRYPTO_KEY = "BaiZe_Absolute_Lock";

    private String bai_ze_li = encrypt("50", null); // 真血
    private final String hyw = encrypt("50", null); // 最大血
    private float fake_health = 50f;

    private boolean downed = false;
    private boolean realDead = false;

    public BaiZeLiData() {}

    // 加密/解密
    private static String sign(String data, String key) {
        return Integer.toHexString((data + key).hashCode());
    }

    private static String encrypt(String value, Entity e) {
        try {
            String key = CRYPTO_KEY + (e != null ? e.getUUID() : "default");
            String sig = sign(value, key);
            String raw = "nb:" + value + ":" + sig + ":sb";

            byte[] step1 = Base64.getEncoder().encode(raw.getBytes(StandardCharsets.UTF_8));
            byte[] k = key.getBytes(StandardCharsets.UTF_8);
            byte[] step2 = new byte[step1.length];

            for (int i = 0; i < step1.length; i++) {
                step2[i] = (byte) (step1[i] ^ k[i % k.length]);
            }

            return Base64.getEncoder().encodeToString(step2);

        } catch (Exception e2) {
            return "50";
        }
    }

    public static String decrypt(String base64, Entity e) {
        try {
            String key = CRYPTO_KEY + (e != null ? e.getUUID() : "default");
            byte[] step1 = Base64.getDecoder().decode(base64);
            byte[] k = key.getBytes(StandardCharsets.UTF_8);
            byte[] step2 = new byte[step1.length];

            for (int i = 0; i < step1.length; i++) {
                step2[i] = (byte) (step1[i] ^ k[i % k.length]);
            }

            byte[] step3 = Base64.getDecoder().decode(step2);
            String raw = new String(step3, StandardCharsets.UTF_8);

            if (!raw.startsWith("nb:") || !raw.endsWith(":sb")) return "50";
            String[] parts = raw.substring(3, raw.length() - 3).split(":");
            if (parts.length != 2) return "50";
            if (!sign(parts[0], key).equals(parts[1])) return "50";
            return parts[0];

        } catch (Exception ignored) {}

        return "50";
    }

    // Getter / Setter
    public float getRealHealth(Entity e) { return new BigDecimal(decrypt(bai_ze_li, e)).floatValue(); }
    public float getMaxHealth() { return new BigDecimal(hyw).floatValue(); }
    public float getFakeHealth() { return fake_health; }
    public void setFakeHealth(float value) { this.fake_health = value; }
    public boolean isDowned() { return downed; }
    public void setDowned(boolean value) { this.downed = value; }
    public boolean isRealDead() { return realDead; }
    public void setRealDead(boolean value) { this.realDead = value; }
    public void setRealHealth(Entity e, float value) { this.bai_ze_li = encrypt(String.valueOf(Math.max(value,0)), e); }

    // NBT
    public void saveToNBT(CompoundTag tag) {
        tag.putString("hp", bai_ze_li);
        tag.putFloat("fake", fake_health);
        tag.putBoolean("downed", downed);
        tag.putBoolean("dead", realDead);
    }
    public void loadFromNBT(CompoundTag tag) {
        bai_ze_li = tag.getString("hp");
        fake_health = tag.getFloat("fake");
        downed = tag.getBoolean("downed");
        realDead = tag.getBoolean("dead");
    }

    public void receiveHealthUpdate(String healthString) {
        this.bai_ze_li = healthString;
        try {
            this.fake_health = new BigDecimal(decrypt(this.bai_ze_li, null)).floatValue();
        } catch (Exception e) {
            this.fake_health = getMaxHealth();
        }
    }

    public void syncHealth(Entity entity) {
        if (!entity.level().isClientSide) {
            ModPacketHandler.INSTANCE.send(
                    PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new SyncBaiZeHealthPacket(entity.getId(), bai_ze_li)
            );
        }
    }
}