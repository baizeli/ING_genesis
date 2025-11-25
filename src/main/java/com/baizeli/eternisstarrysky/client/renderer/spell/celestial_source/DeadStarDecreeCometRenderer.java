package com.baizeli.eternisstarrysky.client.renderer.spell.celestial_source;

import com.baizeli.eternisstarrysky.Entity.spells.celestial_source.DeadStarDecreeComet;
import com.baizeli.eternisstarrysky.EternisStarrySky;
import io.redspace.ironsspellbooks.entity.spells.fireball.FireballRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Projectile;

public class DeadStarDecreeCometRenderer extends FireballRenderer {
    private final static ResourceLocation BASE_TEXTURE = ResourceLocation.fromNamespaceAndPath(
        EternisStarrySky.MOD_ID, "textures/entity/comet/dead_star_decree_comet.png"
    );
    private final static ResourceLocation FIRE_TEXTURES[] = {
        ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/entity/comet/dead_star_decree_fire_1.png"),
        ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/entity/comet/dead_star_decree_fire_2.png"),
        ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/entity/comet/dead_star_decree_fire_3.png"),
        ResourceLocation.fromNamespaceAndPath(EternisStarrySky.MOD_ID, "textures/entity/comet/dead_star_decree_fire_4.png")
    };

    public DeadStarDecreeCometRenderer(EntityRendererProvider.Context context, float scale) {
        super(context, scale);
    }

    @Override
    public ResourceLocation getTextureLocation(Projectile entity) {
        return BASE_TEXTURE;
    }

    public ResourceLocation getFireTextureLocation(Projectile entity) {
        if (entity instanceof DeadStarDecreeComet comet) {
            int frame = (comet.tickCount / 2) % FIRE_TEXTURES.length;
            return FIRE_TEXTURES[frame];
        }
        return FIRE_TEXTURES[0];
    }
}