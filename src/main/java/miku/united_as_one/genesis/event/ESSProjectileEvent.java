package miku.united_as_one.genesis.event;

import miku.united_as_one.genesis.Genesis;
import miku.united_as_one.genesis.Items.curios.rune_plus.EnderRunePlus;
import miku.united_as_one.genesis.Items.curios.rune_plus.LightningRunePlus;
import miku.united_as_one.genesis.util.ModCurios;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import io.redspace.ironsspellbooks.entity.spells.EchoingStrikeEntity;
import io.redspace.ironsspellbooks.entity.spells.lightning_lance.LightningLanceProjectile;
import io.redspace.ironsspellbooks.entity.spells.magic_arrow.MagicArrowProjectile;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.particle.ZapParticleOption;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector3f;

@Mod.EventBusSubscriber(modid = Genesis.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ESSProjectileEvent {
    @SubscribeEvent
    public static void lightningLanceProjectile(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        Entity target = projectile;
        switch (event.getRayTraceResult().getType()) {
            case MISS,BLOCK -> {
                BlockHitResult traceResult = (BlockHitResult) event.getRayTraceResult();
                BlockPos blockPos = traceResult.getBlockPos();
                target.setPos(blockPos.getX(), blockPos.getY() + 1, blockPos.getZ());
            }
            case ENTITY -> {
                EntityHitResult traceResult = (EntityHitResult) event.getRayTraceResult();
                target = traceResult.getEntity();
            }
        }
        if(projectile instanceof LightningLanceProjectile lanceProjectile) {
            Entity owner = lanceProjectile.getOwner();
            if(owner instanceof ServerPlayer player) {
                if(ModCurios.hasCurios(player, LightningRunePlus::test)) {
                    summonShockwave(projectile.level, 4.0f, lanceProjectile.getDamage() * 0.75f, target, owner, lanceProjectile);
                }
            }
        }
        if(projectile instanceof MagicArrowProjectile magicProjectile) {
            Entity owner = magicProjectile.getOwner();
            if(owner instanceof ServerPlayer player && target instanceof LivingEntity) {
                if(ModCurios.hasCurios(player, EnderRunePlus::test)) {
                    EchoingStrikeEntity echo = new EchoingStrikeEntity(player.level, player, magicProjectile.getDamage(), 2.0F);
                    echo.setTracking(target);
                    echo.setPos(target.getBoundingBox().getCenter().subtract(0.0F, echo.getBbHeight() * 0.5F, 0.0F));
                    echo.tickCount = 10;
                    player.level.addFreshEntity(echo);
                }
            }
        }
    }

    private static void summonShockwave(Level level, float radius, float damage, Entity target, Entity owner, Projectile projectile) {
        Vector3f edge = new Vector3f(0.7F, 1.0F, 1.0F);
        Vector3f center = new Vector3f(1.0F, 1.0F, 1.0F);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(edge, radius * 1.02F), target.getX(), target.getY() + (double)0.15F, target.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(edge, radius * 0.98F), target.getX(), target.getY() + (double)0.15F, target.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(center, radius), target.getX(), target.getY() + (double)0.165F, target.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F, true);
        MagicManager.spawnParticles(level, new BlastwaveParticleOptions(center, radius), target.getX(), target.getY() + (double)0.135F, target.getZ(), 1, 0.0F, 0.0F, 0.0F, 0.0F, true);
        MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, target.getX(), target.getY() + (double)1.0F, target.getZ(), 80, 0.25F, 0.25F, 0.25F, 0.7F + radius * 0.1F, false);
        Vec3 start = target.getBoundingBox().getCenter();
        LightningBolt dummyLightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
        dummyLightningBolt.setDamage(0.0F);
        dummyLightningBolt.setVisualOnly(true);
        level.getEntities(target, target.getBoundingBox().inflate(radius, radius, radius), (e) -> !DamageSources.isFriendlyFireBetween(e, target) && Utils.hasLineOfSight(level, target, e, true)).forEach((e) -> {
            if (e instanceof LivingEntity livingEntity) {
                if (canHit(e) && livingEntity.distanceToSqr(target) < (double)(radius * radius)) {
                    Vec3 dest = livingEntity.getBoundingBox().getCenter();
                    ((ServerLevel)level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRICITY, livingEntity.getX(), livingEntity.getY() + (double)(livingEntity.getBbHeight() / 2.0F), livingEntity.getZ(), 10, livingEntity.getBbWidth() / 3.0F, livingEntity.getBbHeight() / 3.0F, livingEntity.getBbWidth() / 3.0F, 0.1, false);
                    DamageSources.applyDamage(livingEntity, damage, SpellDamageSource.source(projectile, owner, SpellRegistry.SHOCKWAVE_SPELL.get()));
                }
            }
        });

        for(int i = 0; i < 7; ++i) {
            Vec3 dest = start.add(Utils.getRandomVec3(1.0F).multiply(4.0F, 2.5F, 4.0F).add(0.0F, 4.0F, 0.0F));
            ((ServerLevel)level).sendParticles(new ZapParticleOption(dest), start.x, start.y, start.z, 1, 0.0F, 0.0F, 0.0F, 0.0F);
        }
    }

    private static boolean canHit(Entity target) {
        return target.isAlive() && target.isPickable() && !target.isSpectator();
    }
}
