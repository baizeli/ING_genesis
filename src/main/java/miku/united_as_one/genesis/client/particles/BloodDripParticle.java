package miku.united_as_one.genesis.client.particles;

import miku.united_as_one.genesis.init.registry.client.ParticleRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BloodDripParticle extends TextureSheetParticle {

    protected boolean isGlowing;

    protected BloodDripParticle(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;

        // 🩸 血红
        this.setColor(0.75F, 0.05F, 0.05F);
        this.setAlpha(0.95F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return this.isGlowing ? LightTexture.FULL_BRIGHT : super.getLightColor(partialTick);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.preMoveUpdate();

        if (!this.removed) {
            this.yd -= this.gravity;
            this.move(this.xd, this.yd, this.zd);
            this.postMoveUpdate();

            if (!this.removed) {
                this.xd *= 0.98;
                this.yd *= 0.98;
                this.zd *= 0.98;
            }
        }
    }

    protected void preMoveUpdate() {
        if (this.lifetime-- <= 0) {
            this.remove();
        }
    }

    protected void postMoveUpdate() {}

    /* ================= HANG ================= */

    public static class Hang extends BloodDripParticle {

        private final ParticleOptions fallingParticle;

        public Hang(ClientLevel level, double x, double y, double z) {
            super(level, x, y, z);
            this.fallingParticle = ParticleRegistry.BLOOD_DRIP_FALL.get();

            this.gravity *= 0.02F;

            this.lifetime = 35 + (int)(Math.random() * 26);

            this.isGlowing = true;
        }

        @Override
        protected void preMoveUpdate() {
            if (this.lifetime-- <= 0) {
                this.remove();
                this.level.addParticle(
                        this.fallingParticle,
                        this.x, this.y, this.z,
                        this.xd, this.yd, this.zd
                );
            }
        }

        @Override
        protected void postMoveUpdate() {
            this.xd *= 0.02;
            this.yd *= 0.02;
            this.zd *= 0.02;
        }
    }

    /* ================= FALL ================= */

    public static class Fall extends BloodDripParticle {

        private final ParticleOptions landParticle;

        public Fall(ClientLevel level, double x, double y, double z) {
            super(level, x, y, z);
            this.landParticle = ParticleRegistry.BLOOD_DRIP_LAND.get();

            this.gravity = 0.01F;
            this.isGlowing = true;
        }

        @Override
        protected void postMoveUpdate() {
            if (this.onGround) {
                this.remove();
                this.level.addParticle(
                        this.landParticle,
                        this.x, this.y, this.z,
                        0.0, 0.0, 0.0
                );
            }
        }
    }

    /* ================= LAND ================= */

    public static class Land extends BloodDripParticle {

        public Land(ClientLevel level, double x, double y, double z) {
            super(level, x, y, z);
            this.lifetime = (int)(28.0 / (Math.random() * 0.8 + 0.2));
            this.isGlowing = true;
        }
    }

    /* ================= PROVIDERS ================= */

    public static class HangProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public HangProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            BloodDripParticle p = new Hang(level, x, y, z);
            p.pickSprite(this.sprites);
            return p;
        }
    }

    public static class FallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public FallProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            BloodDripParticle p = new Fall(level, x, y, z);
            p.pickSprite(this.sprites);
            return p;
        }
    }

    public static class LandProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public LandProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double dx, double dy, double dz) {
            BloodDripParticle p = new Land(level, x, y, z);
            p.pickSprite(this.sprites);
            return p;
        }
    }
}
