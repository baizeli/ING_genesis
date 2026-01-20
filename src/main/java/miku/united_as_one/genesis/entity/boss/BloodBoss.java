package miku.united_as_one.genesis.entity.boss;

import io.redspace.ironsspellbooks.api.network.IClientEventEntity;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.entity.mobs.IAnimatedAttacker;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Optional;

public class BloodBoss extends AbstractSpellCastingMob implements Enemy, IAnimatedAttacker, IEntityAdditionalSpawnData, IClientEventEntity {
    int spawnTimer;

    public BloodBoss(EntityType<? extends AbstractSpellCastingMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
            .add(Attributes.MAX_HEALTH, 950)
            .add(Attributes.MOVEMENT_SPEED, 0.42)
            .add(Attributes.ATTACK_DAMAGE, 10)
            .add(Attributes.ARMOR, 20);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return GeckoLibUtil.createInstanceCache(this);
    }

    @Override
    public void handleClientEvent(byte b) {

    }

    @Override
    public void playAnimation(String s) {

    }

    @Override
    public void writeSpawnData(FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeInt(this.spawnTimer);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf friendlyByteBuf) {
        this.spawnTimer = friendlyByteBuf.readInt();
    }

    @Override
    public boolean isCasting() {
        return super.isCasting(); // 或你的自定义逻辑
    }




}