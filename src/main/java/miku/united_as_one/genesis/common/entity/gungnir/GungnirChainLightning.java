package miku.united_as_one.genesis.common.entity.gungnir;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.entity.spells.ChainLightning;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import miku.united_as_one.genesis.init.registry.EntityRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

import java.lang.reflect.Field;
import java.util.List;

public class GungnirChainLightning extends ChainLightning {
    static Field allVictimsField;
    static Field lastVictimsField;
    static Field initialVictimField;
    static Field hitsField;

    List<Entity> allVictims;
    List<Entity> lastVictims;

    static {
        try {
            allVictimsField = ChainLightning.class.getDeclaredField("allVictims");
            lastVictimsField = ChainLightning.class.getDeclaredField("lastVictims");
            initialVictimField = ChainLightning.class.getDeclaredField("initialVictim");
            hitsField = ChainLightning.class.getDeclaredField("hits");
        } catch (NoSuchFieldException ignored) {}
    }

    @SuppressWarnings("all")
    public GungnirChainLightning(Level level, Entity owner, Entity initialVictim) {
        super(EntityRegistry.GUNGNIR_CHAIN_LIGHTNING_PROJECTILE.get(), level);
        this.setOwner(owner);
        this.setPos(initialVictim.position());
        try {
            allVictims = (List<Entity>) allVictimsField.get(this);
            lastVictims = (List<Entity>) lastVictimsField.get(this);

            allVictims.add(initialVictim);
            lastVictims.add(initialVictim);

            initialVictimField.set(this, initialVictim);
        } catch (IllegalAccessException ignored) {}
    }

    public GungnirChainLightning(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public void doHurt(Entity victim) {
        try {
            hitsField.set(this, (int)hitsField.get(this) + 1);
        } catch (IllegalAccessException ignored) {}
        DamageSources.applyDamage(victim, this.damage, SpellRegistry.CHAIN_LIGHTNING_SPELL.get().getDamageSource(this, this.getOwner()));
        MagicManager.spawnParticles(this.level, ParticleHelper.ELECTRICITY, victim.getX(), victim.getY() + (double)(victim.getBbHeight() / 2.0F), victim.getZ(), 10, victim.getBbWidth() / 3.0F, victim.getBbHeight() / 3.0F, victim.getBbWidth() / 3.0F, 0.1, false);
        lastVictims.add(victim);
        if (this.getOwner() instanceof LivingEntity livingEntity)
            livingEntity.heal(this.damage * 0.2F);
    }
}
