package com.baizeli.eternisstarrysky.client.particles;

import com.baizeli.eternisstarrysky.EternisStarrySky;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;

public class StardustTrailParticle extends TrailParticle {
    private static final ResourceLocation TEXTURE =
            ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/particle/stardust_trail.png");

    public StardustTrailParticle(ClientLevel world, double x, double y, double z,
                                 double xSpeed, double ySpeed, double zSpeed,
                                 float red, float green, float blue, float alpha,
                                 float trailWidth, float trailHeight, int lifetime,
                                 int maxSamples, int sampleStep) {
        super(world, x, y, z, xSpeed, ySpeed, zSpeed,
                red, green, blue, alpha, trailWidth, trailHeight,
                lifetime, maxSamples, sampleStep);
    }

    @Override
    protected ResourceLocation getTexture() {
        return TEXTURE;
    }

    @Override
    protected void applyPhysics() {
        
        this.xd *= 0.95D;
        this.yd *= 0.95D;
        this.zd *= 0.95D;
        this.yd -= (double) this.gravity;
    }

    @Override
    protected void updateTrailAlpha() {
        
        float ageRatio = (float) this.age / (float) this.lifetime;
        this.trailAlpha = 1.0F - ageRatio * ageRatio; 
    }

    
    public static class StardustProvider extends TrailParticle.Provider {
        public StardustProvider(SpriteSet sprites) {
            super(sprites);
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new StardustTrailParticle(world, x, y, z, xSpeed, ySpeed, zSpeed,
                    1.0F, 1.0F, 1.0F, 0.8F, 0.5F, 0.5F, 40, 8, 1);
        }
    }
}