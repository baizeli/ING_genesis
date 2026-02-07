package miku.united_as_one.genesis.common.spell.celestial_source;

import miku.united_as_one.genesis.init.registry.EntityRegistry;
import miku.united_as_one.genesis.common.entity.NyanCat;
import miku.united_as_one.genesis.Genesis;
import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.damage.SpellDamageSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class NyanCatJetSpell extends AbstractSpell {
    private final ResourceLocation spellId = ResourceLocation.fromNamespaceAndPath(Genesis.MOD_ID, "nyan_cat_jet");
    private final DefaultConfig defaultConfig;



    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.damage",
                Utils.stringTruncation(this.getDamage(spellLevel, caster), 2)));
    }

    public NyanCatJetSpell() {
        this.defaultConfig = (new DefaultConfig())
                .setMinRarity(SpellRarity.COMMON)
                .setSchoolResource(SchoolRegistry.FIRE_RESOURCE) 
                .setMaxLevel(10)
                .setCooldownSeconds(12.0)
                .build();
        this.manaCostPerLevel = 1;
        this.baseSpellPower = 0;
        this.spellPowerPerLevel = 1;
        this.castTime = 100;
        this.baseManaCost = 5;
    }

    public CastType getCastType() {
        return CastType.CONTINUOUS;
    }


    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    public Optional<SoundEvent> getCastFinishSound() {
        return Optional.of(SoundEvents.CAT_AMBIENT);
    }

    public void onCast(Level world, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (world.isClientSide()) {
            super.onCast(world, spellLevel, entity, castSource, playerMagicData);
            return;
        }

        ServerLevel serverLevel = (ServerLevel) world;


        spawnNyanCat(serverLevel, entity, spellLevel);


        super.onCast(world, spellLevel, entity, castSource, playerMagicData);
    }

    private void spawnNyanCat(ServerLevel serverLevel, LivingEntity player, int spellLevel) {
        Vec3 lookVec = player.getLookAngle();
        NyanCat nyanCat = new NyanCat(EntityRegistry.NYAN_CAT.get(), player, serverLevel);
        Vec3 spawnPos = player.position()
                .add(0.0, player.getEyeHeight() * 0.7, 0.0)
                .add(lookVec.scale(0.1));
        nyanCat.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
        nyanCat.setBaseDamage(getDamage(spellLevel, player));
        float speed = 1.5F; 
        float inaccuracy = 1.0F;
        speed += spellLevel * 0.1F;
        nyanCat.shoot(lookVec.x, lookVec.y, lookVec.z, speed, inaccuracy);
        serverLevel.addFreshEntity(nyanCat);
    }

    public float getDamage(int spellLevel, LivingEntity caster) {
        return 1.0F + this.getSpellPower(spellLevel, caster) * 0.75F;
    }

    public SpellDamageSource getDamageSource(@Nullable Entity projectile, Entity attacker) {
        
        return super.getDamageSource(projectile, attacker);
        
        
    }


    public boolean shouldAIStopCasting(int spellLevel, Mob mob, LivingEntity target) {
        return mob.distanceToSqr(target) > 120.0;
    }
}